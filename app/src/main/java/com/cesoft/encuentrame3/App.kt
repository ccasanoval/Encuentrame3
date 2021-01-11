package com.cesoft.encuentrame3

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

//HMS Apk download
//https://www.huaweicentral.com/download-the-latest-hms-core-apk/
//Navigation
//https://medium.com/swlh/another-navigation-in-multi-module-architecture-1d4945c1fed0

//TODO:
// + guardar fecha de ultima entrada a la app en bbdd -> para poder eliminar datos de usuarios sin uso
//   (solo tenemos fecha entrada usuario de usuario y eliminar sus datos es mas complejo: podrias hacer app para eso)
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(arrayListOf(
                com.cesoft.encuentrame3.di.AppModule,
                com.cesoft.encuentrame3.di.VMModule,
                com.cesoft.feature_login.di.VMModule
            ))
        }
    }
}