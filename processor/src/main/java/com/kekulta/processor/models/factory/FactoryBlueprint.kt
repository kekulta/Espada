package com.kekulta.processor.models.factory

import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.ClassName

/**
 * Contains all the necessary information to generate factory class
 *
 * @param containingFile store file in which [com.kekulta.annotations.Module] annotated class contained
 * @param factoryClass is the class name of this factory
 * @param isSingleton if this factory is a singleton factory or not
 * @param provider contains all the necessary information to resolve dependency
 */
internal data class FactoryBlueprint(
    val containingFile: KSFile,
    val factoryClass: ClassName,
    val isSingleton: Boolean,
    val provider: DependencyProvider,
)