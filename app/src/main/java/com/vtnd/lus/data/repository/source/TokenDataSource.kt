package com.vtnd.lus.data.repository.source

import kotlinx.coroutines.flow.Flow

interface TokenDataSource {
    interface Local {
        fun getToken(): String?

        fun saveToken(token: String)

        fun clearToken()

        fun tokenObservable(): Flow<String?>
    }

    interface Remote
}
