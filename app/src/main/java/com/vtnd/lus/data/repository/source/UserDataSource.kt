package com.vtnd.lus.data.repository.source

interface UserDataSource {

    interface Local{

        fun getToken(): String?

        fun saveToken(token: String)

        fun clearToken()
    }

    interface Remote
}
