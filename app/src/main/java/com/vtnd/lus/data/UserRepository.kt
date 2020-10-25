package com.vtnd.lus.data

interface UserRepository {

    //Local

    fun getToken(): String?

    fun saveToken(token: String)

    fun clearToken()

    //Remote
}
