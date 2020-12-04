package com.vtnd.lus.di

import android.app.Application
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
        "http://172.16.25.150:5000"
        try {
            socket = IO.socket( baseUrl)
        } catch (e: Exception) {
            Timber.i(e)
        }
    }
}
