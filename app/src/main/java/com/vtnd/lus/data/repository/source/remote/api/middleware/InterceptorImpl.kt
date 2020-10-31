package com.vtnd.lus.data.repository.source.remote.api.middleware

import android.app.Application
import com.vtnd.lus.data.UserRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.net.HttpURLConnection

class InterceptorImpl(
    private var userRepository: UserRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val customHeaders = request.headers.values(KEY_VALUE)
        val newRequest = when (KEY_NO_AUTH) {
            in customHeaders -> request
            else -> {
                when (val token =
                    runBlocking { userRepository.getToken() }
                        .also { Timber.d("Current Token $it") }) {
                    null -> request
                    else -> request
                        .newBuilder()
                        .addHeader(KEY_AUTH, "$KEY_BEARER $token")
                        .build()
                }
            }
        }

        val response = newRequest.newBuilder()
            .removeHeader(KEY_VALUE)
            .build()
            .let(chain::proceed)

        if (response.code in arrayOf(
                HttpURLConnection.HTTP_UNAUTHORIZED,
                HttpURLConnection.HTTP_FORBIDDEN
            )
        ) {
            Timber.d("remove User And Token")
            runBlocking { userRepository.clearToken() }
        }
        return response
    }

    companion object {
        private const val KEY_NO_AUTH = "NoAuth"
        private const val KEY_AUTH = "Authorization"
        private const val KEY_BEARER = "Bearer"
        private const val KEY_VALUE = "@"
    }
}
