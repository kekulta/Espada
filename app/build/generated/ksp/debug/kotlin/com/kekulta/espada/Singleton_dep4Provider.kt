package com.kekulta.espada

import javax.inject.Provider

public class Singleton_dep4Provider : Provider<Dep4> {
  private val internalProperty_module: TestModule2 = com.kekulta.espada.TestModule2()

  public override fun `get`(): Dep4 {
    if (INSTANCE == null) {
      INSTANCE = internalProperty_module.dep4Provider()
    }
    return requireNotNull(INSTANCE) { "Dependency can not be null!" }
  }

  private companion object {
    public var INSTANCE: Dep4? = null
  }
}
