package com.vtnd.lus.data.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vtnd.lus.BuildConfig
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.source.remote.api.ApiService
import com.vtnd.lus.data.repository.source.remote.api.middleware.InterceptorImpl
import com.vtnd.lus.shared.constants.Constants.KEY_BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

fun provideApiService(retrofit: Retrofit): ApiService = ApiService(retrofit)

fun provideMoshi(): Moshi {
    return Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .build()
}

fun provideAuthInterceptor(tokenRepository: TokenRepository): InterceptorImpl {
    return InterceptorImpl(tokenRepository)
}

fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor()
        .apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
}

fun provideRetrofit(baseUrl: String, moshi: Moshi, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()
}

fun provideOkHttpClient(
    authInterceptor: List<Interceptor>
): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS).apply {
            for (interceptor in authInterceptor) addInterceptor(interceptor)
        }
        .build()
}

val networkModule = module {

    single { provideMoshi() }

    factory{ provideAuthInterceptor(get()) }

    factory{ provideLoggingInterceptor() }

    single { provideOkHttpClient(listOf(get<InterceptorImpl>(), get<HttpLoggingInterceptor>())) }

    single { provideRetrofit(get(named(KEY_BASE_URL)), get(), get()) }

    single { provideApiService(get()) }
}
