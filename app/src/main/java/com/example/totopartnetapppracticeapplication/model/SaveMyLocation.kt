package com.example.totopartnetapppracticeapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lat_lng_record")
data class SaveMyLocation(
     @PrimaryKey(autoGenerate = true) val id:Int= 0,
     @ColumnInfo(name = "latitude") val latitude: Double,
     @ColumnInfo(name = "longitude") val longitude: Double,
     @ColumnInfo(name = "driver_status") val driver_status: Int?,
     @ColumnInfo(name = "booking_id") val booking_id: Int,
     @ColumnInfo(name = "area_name") val area_name: String?,
     @ColumnInfo(name = "city") val city: String?,
     @ColumnInfo(name = "bearing") val bearing: Float?,
     @ColumnInfo(name = "user_id") val user_Id: Int,
)
