package com.vtnd.lus.data

import com.vtnd.lus.data.model.Service
import com.vtnd.lus.data.model.User
import com.vtnd.lus.shared.scheduler.DataResult
import kotlinx.coroutines.flow.Flow

interface RepoRepository {

    //Local
    fun isOpenFirstApp(): String?

    fun setOpenFirstApp()

    suspend fun services(): List<Service>?

    fun servicesObservable(): Flow<List<Service>?>

    //Service
    suspend fun getServices(): DataResult<Unit>
}
