package com.example.totopartnetapppracticeapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.totopartnetapppracticeapplication.MyApplication
import com.google.android.gms.maps.model.LatLng

class MyBroadcastReceiver: BroadcastReceiver() {
    private var lat: Double? = 0.0
    private var lng: Double? = 0.0

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.v("location","intent is :$intent")
        if (intent?.action == "LocationToast"){
              //  lat= intent.getDoubleExtra("lat",0.0)
                lat = intent.extras?.getDouble("lat",0.0)
                lng = intent.extras?.getDouble("lng",0.0)
//                MyApplication.getLocationListener()?.onGetLocation(LatLng(lat?:0.0,lng?:0.0))


            }


    }
}