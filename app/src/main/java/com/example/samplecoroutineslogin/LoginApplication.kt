package com.example.samplecoroutineslogin

import android.app.Application
import com.example.samplecoroutineslogin.di.loginModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class LoginApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@LoginApplication)
            modules(loginModule)
        }
    }
}
