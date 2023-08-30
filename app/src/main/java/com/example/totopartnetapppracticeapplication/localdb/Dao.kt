package com.example.totopartnetapppracticeapplication.localdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.DeleteTable
import androidx.room.Insert
import androidx.room.Query
import com.example.totopartnetapppracticeapplication.model.SaveLatLng

@Dao
interface Dao {
    @Insert
    fun insertLatLng(saveLatLng: SaveLatLng)
    @Query("delete from lat_lng_record")
    fun deleteAllRecord()
    @Query("delete from lat_lng_record where id = 10")
    fun deleteSpecificRecord()
    @Query("select * from lat_lng_record")
    fun getWholeRecord(): LiveData<List<SaveLatLng>>
}