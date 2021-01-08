package com.cesoft.encuentrame3.di

import com.cesoft.encuentrame3.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val VMModule = module {
    viewModel { MainViewModel(get()) }
}