package com.kekulta.kotlinpoetextensions.dsl

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName

fun FunSpec.Builder.addStatement(statementScope: () -> (String)) {
    addStatement(statementScope.invoke())
}

fun FunSpec.Builder.overrides() {
    addModifiers(KModifier.OVERRIDE)
}

fun FunSpec.Builder.private() {
    addModifiers(KModifier.PRIVATE)
}

fun FunSpec.Builder.addParam(
    name: String,
    type: TypeName,
    builderScope: ParameterSpec.Builder.() -> (Unit) = {}
) {
    val spec = ParameterSpec.builder(name = name, type = type)
    spec.builderScope()
    addParameter(spec.build())
}

fun FunSpec.Builder.controlFlow(controlFlow: String, controlScope: FunSpec.Builder.() -> (Unit)) {
    beginControlFlow(controlFlow)
    controlScope()
    endControlFlow()
}


