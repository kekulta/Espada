package com.kekulta.espada

import javax.inject.Provider

public class Factory_dep2Provider : Provider<Dep2> {
  private val internalProperty_module: TestModule = com.kekulta.espada.TestModule()

  public override fun `get`(): Dep2 = internalProperty_module.dep2Provider(
    dep = com.kekulta.espada.Singleton_dep3Provider().get(),
  )
}
