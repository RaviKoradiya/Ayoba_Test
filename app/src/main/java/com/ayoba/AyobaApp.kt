package com.ayoba

import android.app.Application
import com.ayoba.di.daoModule
import com.ayoba.di.databaseModule
import com.ayoba.di.repositoryModule
import com.ayoba.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AyobaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AyobaApp)
            modules(
                listOf(
                    databaseModule,
                    daoModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}