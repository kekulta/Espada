package com.kekulta.processor.generators

import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.kekulta.kotlinpoetextensions.dsl.addClass
import com.kekulta.kotlinpoetextensions.dsl.addCompanion
import com.kekulta.kotlinpoetextensions.dsl.addFunc
import com.kekulta.kotlinpoetextensions.dsl.addProp
import com.kekulta.kotlinpoetextensions.dsl.addStatement
import com.kekulta.kotlinpoetextensions.dsl.buildFileSpec
import com.kekulta.kotlinpoetextensions.dsl.controlFlow
import com.kekulta.kotlinpoetextensions.dsl.overrides
import com.kekulta.kotlinpoetextensions.dsl.private
import com.kekulta.processor.models.factory.DependencyFactory
import com.kekulta.processor.models.factory.DependencyProvider
import com.kekulta.processor.models.factory.FactoryBlueprint
import com.kekulta.processor.models.factory.Param
import javax.inject.Provider

private const val NULLABLE_DEPENDENCY_ERROR = "Dependency can not be null!"
private const val SINGLETON_INSTANCE = "INSTANCE"
private const val NULL = "null"
private const val MODULE_PROPERTY_NAME = "internalProperty_module"
private const val GET_FUN_NAME = "get"
private const val INDENT = "  "
private const val EMPTY = ""

/**
 * Generate Factory class by its blueprint using [FileGenerator]
 */
internal class FactoryFileGenerator(private val generator: FileGenerator) {

    fun generate(blueprint: FactoryBlueprint) {

        val spec = with(blueprint) {
            buildFileSpec(factoryClass) {
                addClass(factoryClass.simpleName) {
                    addSuperinterface(resolveProviderInterface(provider))
                    addProp(name = MODULE_PROPERTY_NAME, type = provider.module) {
                        private()
                        initializer(resolveInitializer(provider))
                    }
                    if (isSingleton) {
                        addSingletonProviderFun(provider)
                    } else {
                        addProviderFun(provider)
                    }
                }
            }
        }

        generator.write(spec, blueprint.containingFile)
    }

    private fun TypeSpec.Builder.addSingletonProviderFun(provider: DependencyProvider) {
        addFunc(GET_FUN_NAME) {
            overrides()
            returns(provider.providedClass)
            controlFlow("if ($SINGLETON_INSTANCE == $NULL)") {
                addStatement {
                    "$SINGLETON_INSTANCE = $MODULE_PROPERTY_NAME.${provider.providerFunction}(${
                        resolveParams(
                            provider.dependencies
                        )
                    })"
                }
            }
            addStatement {
                """return requireNotNull($SINGLETON_INSTANCE) { "$NULLABLE_DEPENDENCY_ERROR" }"""
            }
        }

        addCompanion {
            private()
            addProp(SINGLETON_INSTANCE, provider.providedClass.copy(nullable = true)) {
                mutable(true)
                initializer(NULL)
            }
        }
    }

    private fun TypeSpec.Builder.addProviderFun(provider: DependencyProvider) {
        addFunc(GET_FUN_NAME) {
            overrides()
            returns(provider.providedClass)
            addStatement {
                "return $MODULE_PROPERTY_NAME.${provider.providerFunction}(${
                    resolveParams(
                        provider.dependencies
                    )
                })"
            }
        }
    }

    private fun resolveParams(params: List<Param>): String {
        if (params.isEmpty()) {
            return EMPTY
        }

        return params.joinToString(
            separator = ",\n$INDENT",
            prefix = "\n$INDENT",
            postfix = ",\n"
        ) { param ->
            "${param.name} = ${resolveFactory(param.factory)}"
        }
    }

    private fun resolveProviderInterface(provider: DependencyProvider): ParameterizedTypeName {
        return Provider::class.asClassName().parameterizedBy(listOf(provider.providedClass))
    }

    private fun resolveInitializer(providerFunction: DependencyProvider): String {
        return "${providerFunction.module.canonicalName}()"
    }

    private fun resolveFactory(factory: DependencyFactory): String {
        return "${factory.factoryClass.canonicalName}().get()"
    }
}