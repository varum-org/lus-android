package com.vtnd.lus.data.repository.source.remote

import com.vtnd.lus.data.repository.source.RepoDataSource
import com.vtnd.lus.data.repository.source.remote.api.ApiService
import com.vtnd.lus.data.repository.source.remote.api.GeocoderApiService
import com.vtnd.lus.data.repository.source.remote.api.response.BaseResponse
import com.vtnd.lus.data.repository.source.remote.api.response.GeocoderResponse
import com.vtnd.lus.ui.main.container.registerIdol.addressIdol.DomainLocation
import retrofit2.Response

class RepoRemoteImpl(
    private val apiService: ApiService,
    private val geocoderApiService: GeocoderApiService
) : RepoDataSource.Remote {

    override suspend fun getServices() = apiService.getServices()

    override suspend fun getAddressForCoordinates(
        latlng: String,
        key: String
    ): Response<GeocoderResponse> = geocoderApiService.getAddressForCoordinates(latlng, key)
}
