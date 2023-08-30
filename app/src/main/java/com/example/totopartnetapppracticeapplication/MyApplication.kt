package com.example.totopartnetapppracticeapplication

import android.app.Application
import com.example.totopartnetapppracticeapplication.interfaces.LocationListener
import com.example.totopartnetapppracticeapplication.localdb.LocalRepo
import com.example.totopartnetapppracticeapplication.localdb.appDbModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
//    private val localRepo: LocalRepo by inject()
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(repositoryModule, appDbModule, viewModel))
        }
    }



//    companion object{
//        private var locationListener: LocationListener?= null
//        fun getLocationListener(): LocationListener?{
//            return this.locationListener
//
//        }
//        fun setLocationListener(listener: LocationListener?){
//            this.locationListener= listener
//        }
//
//    }
}