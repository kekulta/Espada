package com.kekulta.processor.models.injector

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

/**
 * Data class to store raw information about member injection, a property annotated with [javax.inject.Inject]
 *
 * @param containingFile store file in which [javax.inject.Inject] annotated field contained
 * @param recipientDeclaration store declaration of the parent class
 * @param propertyDeclaration store declaration of property itself
 */
internal data class InjectionData(
    val containingFile: KSFile,
    val recipientDeclaration: KSClassDeclaration,
    val propertyDeclaration: KSPropertyDeclaration,
)

