package com.vtnd.lus.data.repository.source.remote.api.middleware

import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.repository.source.UserDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.net.HttpURLConnection

class InterceptorImpl(
    private var tokenRepository: TokenRepository,
    private val userDataSource: UserDataSource.Local
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val customHeaders = request.headers.values(KEY_VALUE)
        val newRequest = when (KEY_NO_AUTH) {
            in customHeaders -> request
            else -> {
                when (val token =
                    runBlocking { tokenRepository.getToken() }
                        .also { Timber.d("Current Token $it") }) {
                    null -> request
                    else -> request
                        .newBuilder()
                        .addHeader(KEY_AUTH, token)
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
            runBlocking {
                tokenRepository.clearToken()
                userDataSource.clearUser()
            }
        }
        return response
    }

    companion object {
        private const val KEY_NO_AUTH = "NoAuth"
        private const val KEY_AUTH = "Authorization"
        private const val KEY_VALUE = "@"
    }
}
