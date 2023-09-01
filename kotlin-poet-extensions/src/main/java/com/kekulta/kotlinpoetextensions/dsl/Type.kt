package com.kekulta.kotlinpoetextensions.dsl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import java.lang.reflect.Type
import kotlin.reflect.KClass

internal fun TypeSpec.Builder.addClassInternal(
    fileSpecBuilder: FileSpec.Builder,
    buildScope: TypeSpec.Builder.() -> (Unit)
) {
    buildScope()
    fileSpecBuilder.addType(this.build())
}

internal fun TypeSpec.Builder.addClassInternal(
    typeSpecBuilder: TypeSpec.Builder,
    buildScope: TypeSpec.Builder.() -> (Unit)
) {
    buildScope()
    typeSpecBuilder.addType(this.build())
}

fun TypeSpec.Builder.private() {
    addModifiers(KModifier.PRIVATE)
}

fun TypeSpec.Builder.addProp(
    name: String,
    type: TypeName,
    buildScope: PropertySpec.Builder.() -> (Unit)
) {
    PropertySpec.builder(name = name, type = type).addPropertyInternal(this, buildScope)
}

fun TypeSpec.Builder.addProp(
    name: String,
    type: KClass<*>,
    buildScope: PropertySpec.Builder.() -> (Unit)
) {
    PropertySpec.builder(name = name, type = type).addPropertyInternal(this, buildScope)
}

fun TypeSpec.Builder.addProp(
    name: String,
    type: Type,
    buildScope: PropertySpec.Builder.() -> (Unit)
) {
    PropertySpec.builder(name = name, type = type).addPropertyInternal(this, buildScope)
}

fun TypeSpec.Builder.addFunc(name: String, buildScope: FunSpec.Builder.() -> (Unit)) {
    val spec = FunSpec.builder(name)
    spec.buildScope()
    this.addFunction(spec.build())
}

fun TypeSpec.Builder.addClass(name: ClassName, buildScope: TypeSpec.Builder.() -> (Unit)) {
    TypeSpec.classBuilder(name).addClassInternal(this, buildScope)
}

fun TypeSpec.Builder.addClass(name: String, buildScope: TypeSpec.Builder.() -> (Unit)) {
    TypeSpec.classBuilder(name).addClassInternal(this, buildScope)
}

fun TypeSpec.Builder.addCompanion(buildScope: TypeSpec.Builder.() -> (Unit)) {
    TypeSpec.companionObjectBuilder().addClassInternal(this, buildScope)
}