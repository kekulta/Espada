package com.kekulta.processor.mappers

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ksp.toClassName
import com.kekulta.processor.container.DependencyContainer
import com.kekulta.processor.exceptions.DependencyResolutionException
import com.kekulta.processor.models.factory.DependencyProvider
import com.kekulta.processor.models.factory.FactoryBlueprint
import com.kekulta.processor.models.factory.FactoryData
import com.kekulta.processor.models.factory.Param
import com.kekulta.processor.returnTypeOrThrow

/**
 * Combine raw [FactoryData] with data from [DependencyContainer] to create [FactoryBlueprint] with all the necessary data to generate factories
 * @throws DependencyResolutionException in case of missing dependency or ksp resolution error
 */
internal class FactoryBlueprintMapper(private val dependencyContainer: DependencyContainer) {

    fun map(factoryDataList: List<FactoryData>): List<FactoryBlueprint> {
        return factoryDataList.map { factoryData ->
            val module = factoryData.moduleDeclaration.toClassName()
            val providedClass = factoryData.moduleProviderFunctionDeclaration.returnTypeOrThrow()
            val providerFunction =
                factoryData.moduleProviderFunctionDeclaration.simpleName.asString()
            val params = resolveParams(factoryData.moduleProviderFunctionDeclaration.parameters)

            val provider = DependencyProvider(
                module = module,
                providedClass = providedClass,
                providerFunction = providerFunction,
                dependencies = params,
            )

            val factoryClass = dependencyContainer[providedClass].factoryClass
            val isSingleton = dependencyContainer[providedClass].isSingleton

            FactoryBlueprint(
                containingFile = factoryData.containingFile,
                factoryClass = factoryClass,
                isSingleton = isSingleton,
                provider = provider,
            )
        }
    }

    private fun resolveParams(params: List<KSValueParameter>): List<Param> {
        return params.map { param ->
            val type = param.type.resolve().toClassName()
            val name = param.name?.asString()
                ?: throw DependencyResolutionException("Params in module's provider functions should be named!")
            val factory = dependencyContainer[type]

            Param(
                name = name,
                type = type,
                factory = factory,
            )
        }
    }
}