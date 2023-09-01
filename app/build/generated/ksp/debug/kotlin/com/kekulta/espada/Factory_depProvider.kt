package com.kekulta.espada

import javax.inject.Provider

public class Factory_depProvider : Provider<Dep> {
  private val internalProperty_module: TestModule = com.kekulta.espada.TestModule()

  public override fun `get`(): Dep = internalProperty_module.depProvider()
}
