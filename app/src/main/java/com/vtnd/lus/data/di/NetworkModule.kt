package com.vtnd.lus.data.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vtnd.lus.BuildConfig
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.model.MessageJsonAdapter
import com.vtnd.lus.data.model.RoomJsonAdapter
import com.vtnd.lus.data.model.ServiceJsonAdapter
import com.vtnd.lus.data.model.UserJsonAdapter
import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.remote.api.ApiService
import com.vtnd.lus.data.repository.source.remote.api.GeocoderApiService
import com.vtnd.lus.data.repository.source.remote.api.middleware.CoroutineCallAdapterFactory
import com.vtnd.lus.data.repository.source.remote.api.middleware.InterceptorImpl
import com.vtnd.lus.data.repository.source.remote.api.response.GeocoderErrorResponseJsonAdapter
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponseJsonAdapter
import com.vtnd.lus.data.repository.source.remote.api.response.RoomResponseJsonAdapter
import com.vtnd.lus.shared.constants.Constants.KEY_BASE_URL
import com.vtnd.lus.shared.constants.Constants.KEY_GEOCODE_URL
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

fun provideGeocoderApiService(retrofit: Retrofit): GeocoderApiService = GeocoderApiService(retrofit)

fun provideMoshi(): Moshi {
    return Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .build()
}

private fun provideUserLocalJsonAdapter(moshi: Moshi): UserJsonAdapter {
    return UserJsonAdapter(moshi)
}
private fun provideIdolResponseJsonAdapter(moshi: Moshi): IdolResponseJsonAdapter {
    return IdolResponseJsonAdapter(moshi)
}

private fun provideServiceLocalJsonAdapter(moshi: Moshi): ServiceJsonAdapter {
    return ServiceJsonAdapter(moshi)
}
private fun provideMessageLocalJsonAdapter(moshi: Moshi): MessageJsonAdapter {
    return MessageJsonAdapter(moshi)
}
private fun provideRoomJsonAdapter(moshi: Moshi): RoomJsonAdapter {
    return RoomJsonAdapter(moshi)
}
private fun provideRoomResponseJsonAdapter(moshi: Moshi): RoomResponseJsonAdapter {
    return RoomResponseJsonAdapter(moshi)
}
fun provideAuthInterceptor(
    tokenRepository: TokenRepository,
    userDataSource: UserDataSource.Local
): InterceptorImpl {
    return InterceptorImpl(tokenRepository, userDataSource)
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

    factory { provideUserLocalJsonAdapter(moshi = get()) }

    factory { provideServiceLocalJsonAdapter(moshi = get()) }

    factory { provideMessageLocalJsonAdapter(moshi = get()) }

    factory { provideIdolResponseJsonAdapter(moshi = get()) }

    factory { provideRoomJsonAdapter(moshi = get()) }

    factory { provideRoomResponseJsonAdapter(moshi = get()) }

    factory { GeocoderErrorResponseJsonAdapter(moshi = get()) }

    factory { provideAuthInterceptor(get(), get()) }

    factory { provideLoggingInterceptor() }

    single { provideOkHttpClient(listOf(get<InterceptorImpl>(), get<HttpLoggingInterceptor>())) }

    factory(named(KEY_BASE_URL)) { provideRetrofit(get(named(KEY_BASE_URL)), get(), get()) }

    factory(named(KEY_GEOCODE_URL)) { provideRetrofit(get(named(KEY_GEOCODE_URL)), get(), get()) }

    single { provideApiService(get(named(KEY_BASE_URL))) }

    single { provideGeocoderApiService(get(named(KEY_GEOCODE_URL))) }

}
