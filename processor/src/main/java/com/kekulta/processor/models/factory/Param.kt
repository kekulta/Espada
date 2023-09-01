package com.kekulta.processor.models.factory

import com.squareup.kotlinpoet.ClassName

/**
 * Class contains all the necessary information to resolve parameter of a provider function
 *
 * @param name parameter's name
 * @param type parameter's type
 * @param factory factory that can be used to resolve this parameter
 */
internal data class Param(
    val name: String,
    val type: ClassName,
    val factory: DependencyFactory,
)