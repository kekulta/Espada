package com.kekulta.espada

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    /**
     * Mark properties to perform member injection on
     */
    @Inject
    lateinit var dep2: Dep2
    @Inject
    lateinit var dep: Dep

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Use generated *Injector* class to inject our dependencies
         */
        MainActivityInjector().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}