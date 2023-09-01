package com.kekulta.espada

import javax.inject.Provider

public class Factory_randomName : Provider<Dep5> {
  private val internalProperty_module: TestModule2 = com.kekulta.espada.TestModule2()

  public override fun `get`(): Dep5 = internalProperty_module.randomName(
    dep = com.kekulta.espada.Factory_depProvider().get(),
    dep2 = com.kekulta.espada.Factory_dep2Provider().get(),
    dep3 = com.kekulta.espada.Singleton_dep3Provider().get(),
    dep4 = com.kekulta.espada.Singleton_dep4Provider().get(),
  )
}
