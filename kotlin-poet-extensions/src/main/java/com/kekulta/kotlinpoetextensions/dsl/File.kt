package com.kekulta.kotlinpoetextensions.dsl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import java.lang.reflect.Type
import kotlin.reflect.KClass

fun buildFileSpec(
    packageName: String,
    fileName: String,
    buildScope: FileSpec.Builder.() -> (Unit)
): FileSpec {
    val spec = FileSpec.builder(
        packageName = packageName,
        fileName = fileName,
    )
    spec.buildScope()
    return spec.build()
}

fun buildFileSpec(clazz: ClassName, buildScope: FileSpec.Builder.() -> (Unit)): FileSpec {
    val spec = FileSpec.builder(
        packageName = clazz.packageName,
        fileName = clazz.simpleName,
    )
    spec.buildScope()
    return spec.build()
}

fun FileSpec.Builder.addClass(name: String, buildScope: TypeSpec.Builder.() -> (Unit)) {
    TypeSpec.classBuilder(name).addClassInternal(this, buildScope)
}

fun FileSpec.Builder.addClass(name: ClassName, buildScope: TypeSpec.Builder.() -> (Unit)) {
    TypeSpec.classBuilder(name).addClassInternal(this, buildScope)
}

fun FileSpec.Builder.addFunc(name: String, buildScope: FunSpec.Builder.() -> (Unit)) {
    val spec = FunSpec.builder(name)
    spec.buildScope()
    this.addFunction(spec.build())
}

fun FileSpec.Builder.addStatement(statementScope: () -> (String)) {
    addStatement(statementScope.invoke())
}

fun FileSpec.Builder.addProp(
    name: String,
    type: TypeName,
    buildScope: PropertySpec.Builder.() -> (Unit)
) {
    PropertySpec.builder(name = name, type = type).addPropertyInternal(this, buildScope)
}

fun FileSpec.Builder.addProp(
    name: String,
    type: KClass<*>,
    buildScope: PropertySpec.Builder.() -> (Unit)
) {
    PropertySpec.builder(name = name, type = type).addPropertyInternal(this, buildScope)
}

fun FileSpec.Builder.addProp(
    name: String,
    type: Type,
    buildScope: PropertySpec.Builder.() -> (Unit)
) {
    PropertySpec.builder(name = name, type = type).addPropertyInternal(this, buildScope)
}