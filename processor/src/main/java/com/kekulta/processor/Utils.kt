package com.kekulta.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ksp.toClassName
import com.kekulta.processor.exceptions.DependencyResolutionException

/**
 * Filter out all the instances where [KSDeclaration.containingFile] is null and return paired instance with its non-nullable [KSFile]
 */
internal fun <T : KSDeclaration> Collection<T>.filterNotNullFiles(): Collection<Pair<T, KSFile>> =
    mapNotNull { clazz ->
        if (clazz.containingFile == null) {
            null
        } else {
            Pair(
                clazz,
                clazz.containingFile!!,
            )
        }
    }

/**
 * Filter out all the instances where [KSDeclaration.containingFile] is null and return paired instance with its non-nullable [KSFile]
 */
internal fun <T : KSDeclaration> Sequence<T>.filterNotNullFiles(): Sequence<Pair<T, KSFile>> =
    mapNotNull { clazz ->
        if (clazz.containingFile == null) {
            null
        } else {
            Pair(
                clazz,
                clazz.containingFile!!,
            )
        }
    }

/**
 * Return all the functions declarations from the [KSDeclaration] except for [Any.hashCode], [Any.equals], [Any.toString] and constructor
 */
internal fun KSClassDeclaration.getCustomFunctions(): Sequence<KSFunctionDeclaration> =
    getAllFunctions().filterNot(KSFunctionDeclaration::isStandard)

/**
 * @return true if function is not [Any.hashCode], [Any.equals], [Any.toString] or constructor
 */
internal fun KSFunctionDeclaration.isStandard(): Boolean =
    simpleName.asString() in standardFunctions

/**
 * Filter out all the instances where [KSDeclaration.parentDeclaration] is null and return paired instance with its not-nullable [KSClassDeclaration]
 */
internal fun <T : KSDeclaration> Collection<T>.filterNotNullClasses(): Collection<Pair<T, KSClassDeclaration>> =
    mapNotNull { clazz ->
        if (clazz.parentDeclaration == null || clazz.parentDeclaration !is KSClassDeclaration) {
            null
        } else {
            Pair(
                clazz,
                clazz.parentDeclaration!! as KSClassDeclaration,
            )
        }
    }

/**
 * Filter out all the instances where [KSDeclaration.parentDeclaration] is null and return paired instance with its non-nullable [KSClassDeclaration]
 */
internal fun <T : KSDeclaration> Sequence<T>.filterNotNullClasses(): Sequence<Pair<T, KSClassDeclaration>> =
    mapNotNull { clazz ->
        if (clazz.parentDeclaration == null || clazz.parentDeclaration !is KSClassDeclaration) {
            null
        } else {
            Pair(
                clazz,
                clazz.parentDeclaration!! as KSClassDeclaration,
            )
        }
    }

/**
 * Filter out all the instances where [KSDeclaration.parentDeclaration] is null and return paired instance with its non-nullable [KSClassDeclaration]
 */
internal fun <T : KSDeclaration> Collection<T>.filterNotNullClassesAndFiles(): Collection<SafeKSDeclaration<T>> =
    asSequence()
        .filterNotNullClasses()
        .map { (clazz, _) -> clazz }
        .filterNotNullFiles()
        .map { (clazz, file) ->
            SafeKSDeclaration(
                clazz = clazz,
                containingFile = file,
                parentClass = clazz.parentDeclaration!! as KSClassDeclaration
            )
        }.toList()

/**
 * Returns the return type of the function or throw [DependencyResolutionException] if error during resolution occurred
 *
 * @return return type of the [KSFunctionDeclaration]
 * @throws DependencyResolutionException if error during type resolving occurred
 */
internal fun KSFunctionDeclaration.returnTypeOrThrow() = returnType?.resolve()?.toClassName()
    ?: throw DependencyResolutionException("Error during resolution of return type!")

/**
 * List of standard functions that I don't want to get from objects
 */
private val standardFunctions = setOf("<init>", "toString", "equals", "hashCode")

/**
 * [KSDeclaration] but with non-nullable properties added
 */
internal data class SafeKSDeclaration<T : KSDeclaration>(
    val clazz: T,
    val containingFile: KSFile,
    val parentClass: KSClassDeclaration,
)