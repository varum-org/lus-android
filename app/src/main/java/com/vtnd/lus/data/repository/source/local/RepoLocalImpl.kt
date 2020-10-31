package com.vtnd.lus.data.repository.source.local

import com.vtnd.lus.data.repository.source.RepoDataSource
import com.vtnd.lus.data.repository.source.local.api.DatabaseApi
import com.vtnd.lus.data.repository.source.local.api.DatabaseConfig.OPEN_FIRST_APP
import com.vtnd.lus.data.repository.source.local.api.SharedPrefApi

class RepoLocalImpl(
//    private val databaseApi: DatabaseApi,
    private val sharedPrefApi: SharedPrefApi
) : RepoDataSource.Local {

    init {
        sharedPrefApi.put(OPEN_FIRST_APP, true)
    }

    override fun isOpenFirstApp() =
        sharedPrefApi.get(OPEN_FIRST_APP, Boolean::class.java)

    override fun setOpenFirstApp(isFirst: Boolean) =
        sharedPrefApi.put(OPEN_FIRST_APP, isFirst)
}
