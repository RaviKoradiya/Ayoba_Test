package com.ayoba.di

import androidx.room.Room
import com.ayoba.room.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "ayoba_db"
        ).build()
    }
}