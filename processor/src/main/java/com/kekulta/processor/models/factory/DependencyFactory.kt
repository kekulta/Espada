package com.kekulta.processor.models.factory

import com.squareup.kotlinpoet.ClassName

/**
 * Contains all the necessary information to use or validate a factory but not generate it
 *
 * @param providedClass is the class provided by the factory
 * @param factoryClass is the name of the factory
 * @param dependencies is the classes on which this factory depends
 * @param isSingleton if this factory is a singleton factory
 */
internal data class DependencyFactory(
    val providedClass: ClassName,
    val factoryClass: ClassName,
    val dependencies: List<ClassName>,
    val isSingleton: Boolean,
)
