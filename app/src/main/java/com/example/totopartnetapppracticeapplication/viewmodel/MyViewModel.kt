package com.example.totopartnetapppracticeapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.totopartnetapppracticeapplication.localdb.LocalRepo
import com.example.totopartnetapppracticeapplication.model.SaveLatLng
import io.reactivex.Single

class MyViewModel(private val localRepo: LocalRepo): ViewModel() {

    fun insertLatLng(lat: Double?, lng: Double?, currentTime: String?, distanceInMeters: Float) {
        localRepo.insertLatLng(lat,lng,currentTime,distanceInMeters)
    }

    fun deleteAllRecord() {
        localRepo.deleteAllRecord()

    }

    fun deleteSpecificRecord() {
        localRepo.deleteSpecificRecord()
    }

    fun getWholeRecord():LiveData <List<SaveLatLng>> {
      return localRepo.getWholeRecord()
    }

    fun calculateTotalDistance(): Single<Int> {
       return localRepo.calculateTotalDistance()
    }

}