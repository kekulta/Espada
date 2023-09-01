package com.kekulta.processor.generators

import com.kekulta.kotlinpoetextensions.dsl.addClass
import com.kekulta.kotlinpoetextensions.dsl.addFunc
import com.kekulta.kotlinpoetextensions.dsl.addStatement
import com.kekulta.kotlinpoetextensions.dsl.buildFileSpec
import com.kekulta.kotlinpoetextensions.dsl.private
import com.kekulta.processor.models.factory.DependencyFactory
import com.kekulta.processor.models.injector.InjectorBlueprint

private const val RECIPIENT_PROPERTY_NAME = "internalProperty_recipient"
private const val INJECTOR_FUN_NAME = "inject"

/**
 * Generate Injector class by its blueprint using [FileGenerator]
 */
internal class InjectorFileGenerator(private val generator: FileGenerator) {
    fun generate(
        injectorBlueprint: InjectorBlueprint,
    ) {

        val spec = with(injectorBlueprint) {
            buildFileSpec(injectorClass) {
                addClass(injectorClass) {
                    injections.forEach { injection ->
                        with(injection) {
                            addFunc(resolveInjectionFuncName(propertyName)) {
                                private()
                                receiver(recipientClass)
                                addStatement {
                                    "$propertyName = ${resolveFactory(factory)}"
                                }
                            }
                        }
                    }
                    addFunc(INJECTOR_FUN_NAME) {
                        addParameter(RECIPIENT_PROPERTY_NAME, recipientClass)
                        injections.forEach { injection ->
                            with(injection) {
                                addStatement {
                                    resolveInjectionFunc(
                                        recipientProperty = RECIPIENT_PROPERTY_NAME,
                                        injectionFuncName = resolveInjectionFuncName(propertyName)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        generator.write(spec, injectorBlueprint.containingFiles)
    }

    private fun resolveInjectionFunc(recipientProperty: String, injectionFuncName: String): String {
        return "$recipientProperty.$injectionFuncName()"
    }

    private fun resolveInjectionFuncName(propertyName: String): String {
        return "inject${propertyName.replaceFirstChar { char -> char.uppercaseChar() }}"
    }

    private fun resolveFactory(factory: DependencyFactory): String {
        return "${factory.factoryClass.canonicalName}().get()"
    }
}