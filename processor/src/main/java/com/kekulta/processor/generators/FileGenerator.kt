package com.kekulta.processor.generators

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.FileSpec
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

/**
 * Create or rewrite file by provided [FileSpec] with provided [KSFile]s as its dependencies
 */
internal class FileGenerator(private val generator: CodeGenerator) {
    fun write(spec: FileSpec, file: KSFile) = write(spec, listOf(file))

    fun write(spec: FileSpec, files: List<KSFile>) {
        val dependencies = Dependencies(false, *files.toTypedArray())
        val fos = try {
            generator.createNewFile(
                dependencies = dependencies,
                packageName = spec.packageName,
                fileName = spec.name,
            )
        } catch (e: FileAlreadyExistsException) {
            e.file.outputStream()
        }

        OutputStreamWriter(fos, StandardCharsets.UTF_8).use(spec::writeTo)
    }
}