package com.kekulta.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.kekulta.processor.generators.FactoryFileGenerator
import com.kekulta.processor.generators.FileGenerator
import com.kekulta.processor.generators.InjectorFileGenerator

/**
 * Provides processor to work with
 */
internal class FactoryProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        environment.logStart()

        val generator = FileGenerator(environment.codeGenerator)

        return FactoryProcessor(
            factoryGenerator = FactoryFileGenerator(generator),
            injectorGenerator = InjectorFileGenerator(generator),
        )
    }

    private fun SymbolProcessorEnvironment.logStart() {
        logger.warn(
            """
             SymbolProcessorEnvironment.create
             kotlinVersion: $kotlinVersion
             apiVersion: $apiVersion
             compilerVersion: $compilerVersion
             platforms: $platforms
             options: $options
        """.trimIndent()
        )
    }
}

