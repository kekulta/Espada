package com.kekulta.processor.models.factory

import com.squareup.kotlinpoet.ClassName

/**
 * Contains all the necessary information about provider function and its environment
 *
 * @param module is the [com.kekulta.annotations.Module] annotated class name
 * @param providedClass name of the class provided by the provider function of the [module] class
 * @param providerFunction is the name of the provider function of the [module] class
 * @param dependencies are the params that provider function requires with its names, types and factory to get from
 */
internal data class DependencyProvider(
    val module: ClassName,
    val providedClass: ClassName,
    val providerFunction: String,
    val dependencies: List<Param>,
)