package com.cesoft.feature_login.di

import com.cesoft.feature_login.ui.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val VMModule = module {
    viewModel { LoginViewModel(get()) }
}