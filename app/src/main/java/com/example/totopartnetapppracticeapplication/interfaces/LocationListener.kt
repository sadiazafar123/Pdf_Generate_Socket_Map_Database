package com.example.totopartnetapppracticeapplication.interfaces

import com.google.android.gms.maps.model.LatLng

interface LocationListener {
    fun onGetLocation(latLng: LatLng)
}