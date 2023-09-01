package com.kekulta.processor.models.injector

import com.kekulta.processor.models.factory.DependencyFactory

/**
 * Contains information necessary for Injector generated with [InjectorBlueprint] to resolve member injection
 *
 * @param propertyName property which should be injected
 * @param factory factory that can be used to resolve this injection
 */
internal data class Injection(
    val propertyName: String,
    val factory: DependencyFactory,
)