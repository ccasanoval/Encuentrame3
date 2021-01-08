package com.cesoft.encuentrame3.di

import com.cesoft.feature_login.AuthService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val AppModule = module {
    single { AuthService(androidContext()) }
}