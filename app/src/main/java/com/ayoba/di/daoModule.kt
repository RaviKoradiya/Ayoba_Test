package com.ayoba.di

import com.ayoba.room.db.AppDatabase
import org.koin.dsl.module

val daoModule = module {
    single {
        val database = get<AppDatabase>()
        database.chatDao()
    }
}