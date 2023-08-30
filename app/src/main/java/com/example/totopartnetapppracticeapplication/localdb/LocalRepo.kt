package com.example.totopartnetapppracticeapplication.localdb

import androidx.lifecycle.LiveData
import com.example.totopartnetapppracticeapplication.model.SaveLatLng
import io.reactivex.Single

class LocalRepo (private val dao:Dao) {

    fun insertLatLng(lat: Double?, lng: Double?, currentTime: String?, distanceInMeters: Float) {
        dao.insertLatLng(SaveLatLng(0, lat, lng, currentTime,distanceInMeters))
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

    fun calculateTotalDistance():Single<Int> {
       return dao.calculateTotalDistance()
    }
}