package com.kekulta.kotlinpoetextensions.dsl

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

fun PropertySpec.Builder.private() {
    addModifiers(KModifier.PRIVATE)
}

internal fun PropertySpec.Builder.addPropertyInternal(
    fileSpecBuilder: TypeSpec.Builder,
    buildScope: PropertySpec.Builder.() -> (Unit)
) {
    buildScope()
    fileSpecBuilder.addProperty(build())
}

internal fun PropertySpec.Builder.addPropertyInternal(
    fileSpecBuilder: FileSpec.Builder,
    buildScope: PropertySpec.Builder.() -> (Unit)
) {
    buildScope()
    fileSpecBuilder.addProperty(build())
}