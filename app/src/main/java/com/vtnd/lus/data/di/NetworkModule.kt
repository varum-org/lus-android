package com.vtnd.lus.data.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vtnd.lus.BuildConfig
import com.vtnd.lus.data.repository.source.remote.api.ApiService
import com.vtnd.lus.data.repository.source.remote.api.ServiceGenerator
import com.vtnd.lus.data.repository.source.remote.api.middleware.InterceptorImpl
import com.vtnd.lus.shared.constants.Constants.KEY_BASE_URL
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.*

private fun buildHttpLog(): HttpLoggingInterceptor {
    val logLevel =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    return HttpLoggingInterceptor().setLevel(logLevel)
}

val networkModule = module {

    single<Moshi> {
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()
    }

    single<Interceptor> {
        InterceptorImpl(userRepository = get())
    }

    single {
        ServiceGenerator.generate(
            baseUrl = get(named(KEY_BASE_URL)),
            serviceClass = ApiService::class.java,
            moshi = get(),
            interceptors = listOf(buildHttpLog(), get())
        )
    }
}
