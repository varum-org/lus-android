package com.vtnd.lus.data.repository.source.remote

import com.vtnd.lus.data.repository.source.RepoDataSource
import com.vtnd.lus.data.repository.source.remote.api.ApiService

class RepoRemoteImpl(private val apiService: ApiService) : RepoDataSource.Remote {

    override suspend fun getServices() = apiService.getServices()
}
