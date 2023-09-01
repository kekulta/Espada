package com.kekulta.processor.models.injector

import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.ClassName
import com.kekulta.processor.models.injector.Injection

/**
 * Contains all the necessary information to generate Injector class to perform member injections
 *
 * @param containingFiles store files in which [com.kekulta.annotations.Module] annotated classes with used dependencies contained
 * @param injectorClass is the name of this Injector
 * @param recipientClass is the name of the class member injections will be performed on
 * @param injections are the list of member injections to perform
 */
internal data class InjectorBlueprint(
    val containingFiles: List<KSFile>,
    val injectorClass: ClassName,
    val recipientClass: ClassName,
    val injections: List<Injection>,
)