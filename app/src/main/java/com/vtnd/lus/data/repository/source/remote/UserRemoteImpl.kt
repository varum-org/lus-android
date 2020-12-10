package com.vtnd.lus.data.repository.source.remote

import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.remote.api.ApiService
import com.vtnd.lus.data.repository.source.remote.api.request.RoomRequest
import com.vtnd.lus.data.repository.source.remote.api.request.SignInRequest
import com.vtnd.lus.data.repository.source.remote.api.request.SignUpRequest
import com.vtnd.lus.data.repository.source.remote.api.request.VerifyRequest
import com.vtnd.lus.data.repository.source.remote.api.response.BaseResponse
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.data.repository.source.remote.api.response.UserResponse
import com.vtnd.lus.shared.type.CategoryIdolType

class UserRemoteImpl(private val apiService: ApiService) : UserDataSource.Remote {

    override suspend fun signIn(email: String, password: String) =
        apiService.signIn(SignInRequest(email, password, "123456"))

    override suspend fun signUp(signUpRequest: SignUpRequest) =
        apiService.signUp(signUpRequest)

    override suspend fun verifyAccount(verifyRequest: VerifyRequest) =
        apiService.verifyAccount(verifyRequest)

    override suspend fun getUser(id: String) = apiService.getUser(id)

    override suspend fun logout(deviceToken: String) = apiService.logout(deviceToken)

    override suspend fun getIdols(
        isLogin: Boolean,
        category: CategoryIdolType
    ): BaseResponse<List<IdolResponse>> =
        if (isLogin) apiService.getIdols(category.titleName)
        else apiService.getIdolsNoToken(category.titleName)

    override suspend fun getIdol(isLogin: Boolean, id: String)=
        if (isLogin) apiService.getIdol(id)
        else apiService.getIdolNoToken(id)

    override suspend fun getRoom(roomRequest: RoomRequest) = apiService.getRoom(roomRequest)

    override suspend fun getMessageFromRoom(id: String) = apiService.getMessageFromRoom(id)

    override suspend fun getRooms() = apiService.getRooms()

    override suspend fun search(
        nickName: String?,
        rating: Int?
    ) = apiService.search(nickName, rating)
}
