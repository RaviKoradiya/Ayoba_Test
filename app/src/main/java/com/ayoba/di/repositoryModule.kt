package com.ayoba.di

import com.ayoba.repository.AyobaRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { AyobaRepository(get(), Unit) }
}