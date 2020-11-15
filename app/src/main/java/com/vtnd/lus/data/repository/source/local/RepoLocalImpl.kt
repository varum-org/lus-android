package com.vtnd.lus.data.repository.source.local

import com.vtnd.lus.data.repository.source.RepoDataSource
import com.vtnd.lus.data.repository.source.local.api.SharedPrefApi
import com.vtnd.lus.data.repository.source.local.api.pref.SharedPrefKey.KEY_OPEN_FIRST_APP
import com.vtnd.lus.data.repository.source.local.api.pref.SharedPrefKey.VALUE_OPEN_FIRST_APP

class RepoLocalImpl(
//    private val databaseApi: DatabaseApi,
    private val sharedPrefApi: SharedPrefApi
) : RepoDataSource.Local {

    override fun isOpenFirstApp() =
        sharedPrefApi.get(KEY_OPEN_FIRST_APP, String::class.java)

    override fun setOpenFirstApp() =
        sharedPrefApi.put(KEY_OPEN_FIRST_APP, VALUE_OPEN_FIRST_APP)
}
