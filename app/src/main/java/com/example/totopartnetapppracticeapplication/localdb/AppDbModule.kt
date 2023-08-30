package com.example.totopartnetapppracticeapplication.localdb

import android.content.Context
import androidx.room.Room
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appDbModule = module {
    factory { provideDao(get()) }
    single { provideRoomDB(get()) }
}

fun provideRoomDB(appContext: Context): AppDatabase {
    return Room.databaseBuilder(appContext, AppDatabase::class.java, "lat_lng_db")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()
}

fun provideDao(database: AppDatabase): Dao {
    return database.getDao()

}