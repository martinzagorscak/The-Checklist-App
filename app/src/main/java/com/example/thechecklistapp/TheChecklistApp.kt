package com.example.thechecklistapp

import android.app.Application
import com.example.thechecklistapp.data.di.dataModule
import com.example.thechecklistapp.device.deviceModule
import com.example.thechecklistapp.domain.di.domainModule
import com.example.thechecklistapp.ui.viewmodel.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TheChecklistApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TheChecklistApp)
            modules(dataModule, domainModule, viewModelModule, deviceModule)
        }
    }
}
