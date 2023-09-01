package com.kekulta.espada

import kotlin.Unit

public class MainActivityInjector {
  private fun MainActivity.injectDep2(): Unit {
    dep2 = com.kekulta.espada.Factory_dep2Provider().get()
  }

  private fun MainActivity.injectDep(): Unit {
    dep = com.kekulta.espada.Factory_depProvider().get()
  }

  public fun inject(internalProperty_recipient: MainActivity): Unit {
    internalProperty_recipient.injectDep2()
    internalProperty_recipient.injectDep()
  }
}
