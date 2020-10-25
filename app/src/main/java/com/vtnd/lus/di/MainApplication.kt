package com.vtnd.lus.di

import android.app.Application
import com.vtnd.lus.BuildConfig
import com.vtnd.lus.data.di.dataSourceModule
import com.vtnd.lus.data.di.networkModule
import com.vtnd.lus.data.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val modules = listOf(
            appModule,
            dispatcherModule,
            networkModule,
            dataSourceModule,
            repositoryModule,
            viewModelModule
        )
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(modules)
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
