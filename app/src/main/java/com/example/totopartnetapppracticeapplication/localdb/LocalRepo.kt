package com.example.totopartnetapppracticeapplication.localdb

import androidx.lifecycle.LiveData
import com.example.totopartnetapppracticeapplication.model.SaveLatLng

class LocalRepo (private val dao:Dao) {

    fun insertLatLng(lat: Double?, lng: Double?, currentTime: String?) {
        dao.insertLatLng(SaveLatLng(0, lat, lng, currentTime))
    }

    fun deleteAllRecord() {
        dao.deleteAllRecord()

    }

    fun deleteSpecificRecord() {
        dao.deleteSpecificRecord()
    }

    fun getWholeRecord(): LiveData<List<SaveLatLng>> {
       return dao.getWholeRecord()
    }
}