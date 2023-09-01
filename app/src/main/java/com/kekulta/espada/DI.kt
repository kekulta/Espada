package com.kekulta.espada

import com.kekulta.annotations.Module
import javax.inject.Singleton

/*
 * We have here some mock classes that depends on each other
 */
class Dep()
class Dep2(val dep3: Dep3)
class Dep3(val dep: Dep, val dep4: Dep4)
class Dep4()
class Dep5(val dep: Dep, val dep2: Dep2, val dep3: Dep3, val dep4: Dep4)

/**
 * Describe out graph in two modules, every *provider function* requires some(or zero) dependencies and provides one class
 */
@Module
class TestModule {
    fun depProvider(): Dep = Dep()
    fun dep2Provider(dep: Dep3): Dep2 = Dep2(dep)
}

/**
 * We can mark some *provider function*s as [Singleton] so Singleton Factory will be generated
 */
@Module
class TestModule2 {
    @Singleton
    fun dep3Provider(dep: Dep, dep4: Dep4): Dep3 = Dep3(dep, dep4)
    @Singleton
    fun dep4Provider(): Dep4 = Dep4()
    fun randomName(dep: Dep, dep2: Dep2, dep3: Dep3, dep4: Dep4): Dep5 = Dep5(dep, dep2, dep3, dep4)
}