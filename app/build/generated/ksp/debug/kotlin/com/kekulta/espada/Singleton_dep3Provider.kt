package com.kekulta.espada

import javax.inject.Provider

public class Singleton_dep3Provider : Provider<Dep3> {
  private val internalProperty_module: TestModule2 = com.kekulta.espada.TestModule2()

  public override fun `get`(): Dep3 {
    if (INSTANCE == null) {
      INSTANCE = internalProperty_module.dep3Provider(
            dep = com.kekulta.espada.Factory_depProvider().get(),
            dep4 = com.kekulta.espada.Singleton_dep4Provider().get(),
          )
    }
    return requireNotNull(INSTANCE) { "Dependency can not be null!" }
  }

  private companion object {
    public var INSTANCE: Dep3? = null
  }
}
