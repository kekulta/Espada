package com.kekulta.annotations

/**
 * Every function inside annotated class will be used as a *provider function* and factory will be generated
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Module {
    companion object {
        const val NAME = "com.kekulta.annotations.Module"
    }
}