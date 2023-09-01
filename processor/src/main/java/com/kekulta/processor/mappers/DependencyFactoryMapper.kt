package com.kekulta.processor.mappers

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.kekulta.processor.models.factory.DependencyFactory
import com.kekulta.processor.models.factory.FactoryData
import com.kekulta.processor.returnTypeOrThrow

private const val FACTORY = "Factory"
private const val SINGLETON = "Singleton"
private const val SINGLETON_ANNOTATION = "javax.inject.Singleton"

/**
 * Map [FactoryData] to [DependencyFactory], it does not validate or check graph in any way
 * @throws com.kekulta.processor.exceptions.DependencyResolutionException if error during type resolution in ksp occurred
 */
internal class DependencyFactoryMapper {
    fun map(factoryDataList: List<FactoryData>): List<DependencyFactory> {
        return factoryDataList.map { factoryData ->
            val isSingleton = factoryData.moduleProviderFunctionDeclaration.annotations
                .map { ksAnnotation ->
                    ksAnnotation.annotationType.resolve().toClassName().canonicalName
                }
                .any { annotationName -> annotationName == SINGLETON_ANNOTATION }

            val factoryTypeName = if (isSingleton) SINGLETON else FACTORY

            DependencyFactory(
                factoryClass = ClassName(
                    packageName = factoryData.moduleDeclaration.packageName.asString(),
                    simpleNames = listOf("${factoryTypeName}_${factoryData.moduleProviderFunctionDeclaration.simpleName.asString()}"),
                ),
                providedClass = factoryData.moduleProviderFunctionDeclaration.returnTypeOrThrow(),
                dependencies = factoryData.moduleProviderFunctionDeclaration.parameters
                    .map { ksValueParameter -> ksValueParameter.type.resolve().toClassName() },
                isSingleton = isSingleton,
            )
        }
    }
}