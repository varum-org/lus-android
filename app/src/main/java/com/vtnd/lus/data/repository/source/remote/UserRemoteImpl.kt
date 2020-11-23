package com.vtnd.lus.data.repository.source.remote

import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.remote.api.ApiService
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.data.repository.source.remote.api.request.SignInRequest
import com.vtnd.lus.data.repository.source.remote.api.request.SignUpRequest
import com.vtnd.lus.data.repository.source.remote.api.request.VerifyRequest
import com.vtnd.lus.data.repository.source.remote.api.response.BaseResponse
import com.vtnd.lus.data.repository.source.remote.api.response.UserResponse
import com.vtnd.lus.shared.type.CategoryIdolType

class UserRemoteImpl(private val apiService: ApiService) : UserDataSource.Remote {

    override suspend fun signIn(email: String, password: String) =
        apiService.signIn(SignInRequest(email, password, "123456"))

    override suspend fun signUp(signUpRequest: SignUpRequest) =
        apiService.signUp(signUpRequest)

    override suspend fun verifyAccount(verifyRequest: VerifyRequest) =
        apiService.verifyAccount(verifyRequest)

    override suspend fun getUser(id: String): BaseResponse<UserResponse> = apiService.getUser(id)

    override suspend fun getIdols(
        isLogin: Boolean,
        category: CategoryIdolType
    ): BaseResponse<List<IdolResponse>> =
        if (isLogin) apiService.getIdols(category.titleName)
        else apiService.getIdolsNoToken(category.titleName)
}
