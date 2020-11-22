package com.vtnd.lus.data

import kotlinx.coroutines.flow.Flow

interface TokenRepository {

    fun getToken(): String?

    fun clearToken()

    fun tokenObservable(): Flow<String?>
}
