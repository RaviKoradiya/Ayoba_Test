package com.ayoba.di

import com.ayoba.viewmodels.AyobaViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AyobaViewModel(get()) }
}