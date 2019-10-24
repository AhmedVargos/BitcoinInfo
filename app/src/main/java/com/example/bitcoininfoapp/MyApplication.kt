package com.example.bitcoininfoapp

import android.app.Application
import com.example.bitcoininfoapp.di.liveModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Adding Koin modules to our application
        startKoin {
            androidContext(this@MyApplication)
            modules(liveModules)
        }
    }
}