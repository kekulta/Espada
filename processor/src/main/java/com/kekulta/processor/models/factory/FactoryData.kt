package com.kekulta.processor.models.factory

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

/**
 * Data class to store raw information about a factory based on provider function on [com.kekulta.annotations.Module] annotated class
 * One module may contain multiple provider functions and therefore originate multiple [FactoryData] instances
 *
 * @param containingFile store file in which [com.kekulta.annotations.Module] annotated class contained
 * @param moduleDeclaration store declaration of the [com.kekulta.annotations.Module] annotated class
 * @param moduleProviderFunctionDeclaration store declaration of the function that will provide dependency itself
 */
internal data class FactoryData(
    val containingFile: KSFile,
    val moduleDeclaration: KSClassDeclaration,
    val moduleProviderFunctionDeclaration: KSFunctionDeclaration,
)

