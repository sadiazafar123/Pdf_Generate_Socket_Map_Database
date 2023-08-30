package com.example.totopartnetapppracticeapplication.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.totopartnetapppracticeapplication.model.SaveLatLng

@Database(entities =[SaveLatLng::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase(){
  abstract fun getDao(): Dao

}