package com.example.demo

import android.app.Application
import com.example.demo.shared.modules.networkModule
import com.example.demo.shared.modules.repositoryModule
import com.example.demo.shared.modules.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DemoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DemoApplication)
            modules(networkModule, repositoryModule, viewModelModule)
        }
    }
}