package com.kekulta.processor.container

import com.squareup.kotlinpoet.ClassName
import com.kekulta.processor.exceptions.DependencyResolutionException
import com.kekulta.processor.models.factory.DependencyFactory

/**
 * Container to store and validate info about all of the dependencies, its factories and dependencies of the factories
 */
internal class DependencyContainer {
    private val container: MutableMap<ClassName, DependencyFactory> = mutableMapOf()

    /**
     * Add [DependencyFactory] to the container
     * @throws DependencyResolutionException if factory with same provided class was added before
     */
    fun put(factory: DependencyFactory) {
        if (container[factory.providedClass] != null) {
            throw DependencyResolutionException("${factory.providedClass.canonicalName} provided more than once!")
        }
        container[factory.providedClass] = factory
    }

    /**
     * Add list of the [DependencyFactory] to the container
     * @throws DependencyResolutionException if factory with same provided class was added before
     */
    fun put(factories: List<DependencyFactory>) {
        factories.forEach { factory ->
            if (container[factory.providedClass] != null) {
                throw DependencyResolutionException("${factory.providedClass.canonicalName} provided more than once!")
            }
            container[factory.providedClass] = factory
        }
    }

    /**
     * Get [DependencyFactory] from the container by its provided class
     * @throws DependencyResolutionException if no factory with this provided class was added
     */
    operator fun get(dependency: ClassName): DependencyFactory {
        return container[dependency]
            ?: throw DependencyResolutionException("No factory for ${dependency.canonicalName}")
    }

    /**
     * Validate dependency graph
     * @throws DependencyResolutionException if there are missing or circular dependency in the graph
     */
    fun validate() {
        val allVisited = mutableSetOf<ClassName>()
        val currentVisited = mutableSetOf<ClassName>()

        fun visit(key: ClassName) {
            if (!currentVisited.add(key)) {
                throw DependencyResolutionException("Circular dependency! $key depends on itself!")
            }
            if (allVisited.add(key)) {
                get(key).dependencies.forEach { dependency ->
                    visit(dependency)
                }
            }
        }

        container.entries.forEach { (key, _) ->
            visit(key)
            currentVisited.clear()
        }
    }
}