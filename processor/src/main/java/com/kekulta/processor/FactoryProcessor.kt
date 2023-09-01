package com.kekulta.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.validate
import com.kekulta.annotations.Module
import com.kekulta.processor.container.DependencyContainer
import com.kekulta.processor.generators.FactoryFileGenerator
import com.kekulta.processor.generators.InjectorFileGenerator
import com.kekulta.processor.mappers.DependencyFactoryMapper
import com.kekulta.processor.mappers.FactoryBlueprintMapper
import com.kekulta.processor.mappers.InjectorBlueprintMapper
import com.kekulta.processor.models.AnnotatedSymbols
import com.kekulta.processor.models.factory.FactoryData
import com.kekulta.processor.models.injector.InjectionData

private const val INJECT = "javax.inject.Inject"

/**
 * Collects all the classes annotated with [com.kekulta.annotations.Module].
 * Each member function of these classes called provider function.
 * Based on each provider function generated one factory class that implements [javax.inject.Provider] interface.
 * The return type of the provider function is the provided type of the factory class.
 * Params of the provider function resolved with other factories generated based on its provider functions.
 *
 * Collects all the properties annotated with [javax.inject.Inject] and groups them based on their parent(or recipient).
 * For each recipient class Injector class will be generated named *RecipientNameInjector*.
 * Injector class has the function named *inject(recipient: RecipientName)* which will inject its dependencies.
 * Dependencies will be resolves with early generated factories.
 *
 * If graph is incomplete ot has circular dependencies [com.kekulta.processor.exceptions.DependencyResolutionException] will be thrown at the compile time
 *
 * @throws com.kekulta.processor.exceptions.DependencyResolutionException if graph is incorrect
 */

internal class FactoryProcessor(
    private val injectorGenerator: InjectorFileGenerator,
    private val factoryGenerator: FactoryFileGenerator,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {

        val moduleAnnotated = resolver.getAnnotated(Module.NAME)
        val dependencyContainer = generateFactories(moduleAnnotated.valid)

        dependencyContainer.validate()

        val injectAnnotated = resolver.getAnnotated(INJECT)
        generateMemberInjections(injectAnnotated.valid, dependencyContainer)

        return moduleAnnotated.invalid + injectAnnotated.invalid
    }

    private fun Resolver.getAnnotated(annotation: String): AnnotatedSymbols {
        val annotatedSymbols = getSymbolsWithAnnotation(annotation)
            .groupBy { it.validate() }

        return AnnotatedSymbols(
            valid = annotatedSymbols[true].orEmpty(),
            invalid = annotatedSymbols[false].orEmpty(),
        )
    }

    private fun generateFactories(symbols: List<KSAnnotated>): DependencyContainer {
        val markedClassDeclarations = symbols.filterIsInstance<KSClassDeclaration>()

        val factoryData =
            markedClassDeclarations.filterNotNullFiles().flatMap { (annotatedClass, file) ->
                annotatedClass.getCustomFunctions().map { annotatedFunction ->

                    FactoryData(
                        containingFile = file,
                        moduleDeclaration = annotatedClass,
                        moduleProviderFunctionDeclaration = annotatedFunction,
                    )
                }.toList()
            }
        val dependencyFactories = DependencyFactoryMapper().map(factoryData)

        val dependencyContainer = DependencyContainer().apply { put(dependencyFactories) }

        val blueprintMapper = FactoryBlueprintMapper(dependencyContainer)

        blueprintMapper.map(factoryData).forEach { blueprint ->
            factoryGenerator.generate(blueprint)
        }

        return dependencyContainer
    }

    private fun generateMemberInjections(
        symbols: List<KSAnnotated>,
        dependencyContainer: DependencyContainer,
    ) {
        val markedPropertyAnnotations = symbols.filterIsInstance<KSPropertyDeclaration>()

        val injections =
            markedPropertyAnnotations.filterNotNullClassesAndFiles().map { safeDeclaration ->
                InjectionData(
                    containingFile = safeDeclaration.containingFile,
                    recipientDeclaration = safeDeclaration.parentClass,
                    propertyDeclaration = safeDeclaration.clazz,
                )
            }

        val injectorMapper = InjectorBlueprintMapper(dependencyContainer)

        val injectorBlueprints = injectorMapper.map(injections)

        injectorBlueprints.forEach { injectorBlueprint ->
            injectorGenerator.generate(injectorBlueprint)
        }
    }
}

