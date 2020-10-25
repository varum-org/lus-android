package com.vtnd.lus.data.repository.source.local

import com.sun.wsm.data.repository.source.local.api.SharedPrefApi
import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.local.api.DatabaseApi
import com.vtnd.lus.data.repository.source.local.api.pref.SharedPrefKey

class UserLocalImpl(
    private val databaseApi: DatabaseApi,
    private val sharedPrefApi: SharedPrefApi
) : UserDataSource.Local {

    override fun getToken() =
        sharedPrefApi.get(SharedPrefKey.KEY_TOKEN, String::class.java)

    override fun saveToken(token: String) =
        sharedPrefApi.put(SharedPrefKey.KEY_TOKEN, token)

    override fun clearToken() = sharedPrefApi.removeKey(SharedPrefKey.KEY_TOKEN)
}
