package com.example.totopartnetapppracticeapplication.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "lat_lng_record")
data class SaveLatLng(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo var lat: Double? = 0.0,
    @ColumnInfo var lng: Double? = 0.0,
    @ColumnInfo var time: String? = ""
) : Parcelable
