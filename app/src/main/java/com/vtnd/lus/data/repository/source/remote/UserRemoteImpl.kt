package com.vtnd.lus.data.repository.source.remote

import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.remote.api.ApiService
import com.vtnd.lus.data.repository.source.remote.api.request.SignInRequest

class UserRemoteImpl(private val apiService: ApiService) : UserDataSource.Remote {

    override suspend fun signIn(email: String, password: String) =
        apiService.signIn(SignInRequest(email, password, "123456"))
}
