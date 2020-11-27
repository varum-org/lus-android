package com.vtnd.lus.data.repository.source

import com.vtnd.lus.data.model.Service
import com.vtnd.lus.data.repository.source.remote.api.response.BaseResponse
import com.vtnd.lus.shared.scheduler.DataResult
import kotlinx.coroutines.flow.Flow

interface RepoDataSource {

    interface Local {
        fun isOpenFirstApp(): String?

        fun setOpenFirstApp()

        suspend fun saveServices(services: List<Service>)

        suspend fun services(): List<Service>?

        fun servicesObservable(): Flow<List<Service>?>
    }

    interface Remote {
        //Service
        suspend fun getServices(): BaseResponse<List<Service>?>
    }
}
