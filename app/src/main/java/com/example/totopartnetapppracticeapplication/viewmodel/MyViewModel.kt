package com.example.totopartnetapppracticeapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.totopartnetapppracticeapplication.localdb.LocalRepo
import com.example.totopartnetapppracticeapplication.model.SaveLatLng

class MyViewModel(private val localRepo: LocalRepo): ViewModel() {

    fun insertLatLng(lat: Double?, lng: Double?, currentTime: String?) {
        localRepo.insertLatLng(lat,lng,currentTime)
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

}