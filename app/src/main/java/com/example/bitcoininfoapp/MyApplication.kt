package com.example.bitcoininfoapp

import android.app.Application
import com.example.bitcoininfoapp.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    companion object {
        lateinit var app: Application
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        // Adding Koin modules to our application
        startKoin {
            androidContext(this@MyApplication)
            modules(appModules)
        }
    }
}