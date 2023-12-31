package com.example.totopartnetapppracticeapplication

import android.app.Application
import com.example.totopartnetapppracticeapplication.localdb.appDbModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(networkModule,repositoryModule, appDbModule, viewModelModule))
        }
    }

}