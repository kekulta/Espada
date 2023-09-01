package com.kekulta.processor.mappers

import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.kekulta.processor.container.DependencyContainer
import com.kekulta.processor.exceptions.DependencyResolutionException
import com.kekulta.processor.models.injector.Injection
import com.kekulta.processor.models.injector.InjectionData
import com.kekulta.processor.models.injector.InjectorBlueprint

/**
 * Combine raw [InjectionData] with data from [DependencyContainer] to create [InjectorBlueprint] with all the necessary data to generate injectors
 * @throws DependencyResolutionException in case of missing dependency or ksp resolution error
 */
internal class InjectorBlueprintMapper(private val dependencyContainer: DependencyContainer) {

    fun map(injectionData: List<InjectionData>): List<InjectorBlueprint> {
        val injectionMap =
            injectionData.groupBy { injection ->
                injection.recipientDeclaration.toClassName()
            }

        val injectors = injectionMap.entries.map { (recipientClass, injections) ->
            val files = mutableSetOf<KSFile>()
            val injectorClass = ClassName(
                packageName = recipientClass.packageName,
                simpleNames = listOf("${recipientClass.simpleName}Injector")
            )

            val injectionsMapped = injections.map { injection ->
                files.add(injection.containingFile)
                val type = injection.propertyDeclaration.type.resolve().toClassName()
                Injection(
                    propertyName = injection.propertyDeclaration.simpleName.asString(),
                    factory = dependencyContainer[type]
                )
            }

            InjectorBlueprint(
                containingFiles = files.toList(),
                injectorClass = injectorClass,
                recipientClass = recipientClass,
                injections = injectionsMapped,
            )
        }
        return injectors
    }
}