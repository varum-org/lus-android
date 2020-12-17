package com.vtnd.lus.di

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.vtnd.lus.BuildConfig
import com.vtnd.lus.data.di.dataSourceModule
import com.vtnd.lus.data.di.networkModule
import com.vtnd.lus.data.di.repositoryModule
import com.vtnd.lus.shared.constants.Constants
import io.socket.client.IO
import io.socket.client.Socket
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.get
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import timber.log.Timber

class MainApplication : Application(), KoinComponent {
    lateinit var baseUrl: String
    lateinit var socket: Socket

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
        setupPlaceApi()
        startKoin {
            // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger(Level.INFO)

            // use the Android context given there
            androidContext(this@MainApplication)

            // module list
            koin.loadModules(modules)
            koin.createRootScope()
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        baseUrl = get(named(Constants.KEY_BASE_URL))
        try {
            socket = IO.socket( "https://lusss.herokuapp.com")
        } catch (e: Exception) {
            Timber.i(e)
        }
    }
    private fun setupPlaceApi() {
        Places.initialize(this, "AIzaSyDCrGZXXsyMYRY2Ewmznl0zVMHtpkRWkEc")
    }

}
