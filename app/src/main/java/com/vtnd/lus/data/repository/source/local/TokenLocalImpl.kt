package com.vtnd.lus.data.repository.source.local

import com.vtnd.lus.data.repository.source.TokenDataSource
import com.vtnd.lus.data.repository.source.local.api.SharedPrefApi
import com.vtnd.lus.data.repository.source.local.api.pref.SharedPrefKey


class TokenLocalImpl(private val sharedPrefApi: SharedPrefApi) :
    TokenDataSource.Local {

    override fun getToken() =
        sharedPrefApi.get(SharedPrefKey.KEY_TOKEN, String::class.java)

    override fun saveToken(token: String) =
        sharedPrefApi.put(SharedPrefKey.KEY_TOKEN, token)

    override fun clearToken() = sharedPrefApi.removeKey(SharedPrefKey.KEY_TOKEN)
}
