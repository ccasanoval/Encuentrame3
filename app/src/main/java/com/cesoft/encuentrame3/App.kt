package com.cesoft.encuentrame3

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

//HMS Apk download
//https://www.huaweicentral.com/download-the-latest-hms-core-apk/
//Navigation
//https://medium.com/swlh/another-navigation-in-multi-module-architecture-1d4945c1fed0
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(arrayListOf(
                com.cesoft.encuentrame3.di.AppModule,
                com.cesoft.encuentrame3.di.VMModule,
                com.cesoft.feature_login.di.VMModule))
        }
    }
}