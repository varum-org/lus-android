package com.vtnd.lus.data.repository.source.remote

import android.app.Application
import com.vtnd.lus.data.model.Idol
import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.remote.api.ApiService
import com.vtnd.lus.data.repository.source.remote.api.request.*
import com.vtnd.lus.data.repository.source.remote.api.response.BaseResponse
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import com.vtnd.lus.shared.type.CategoryIdolType
import okhttp3.MultipartBody
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named

class UserRemoteImpl(
    private val apiService: ApiService,
    private val application: Application
) : UserDataSource.Remote, KoinComponent {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.IO)).dispatcher()

    override suspend fun signIn(email: String, password: String) =
        apiService.signIn(SignInRequest(email, password, "123456"))

    override suspend fun signUp(signUpRequest: SignUpRequest) =
        apiService.signUp(signUpRequest)

    override suspend fun verifyAccount(verifyRequest: VerifyRequest) =
        apiService.verifyAccount(verifyRequest)

    override suspend fun getUser() = apiService.getUser()

    override suspend fun logout(deviceToken: String) = apiService.logout(deviceToken)

    override suspend fun getIdols(
        isLogin: Boolean,
        category: CategoryIdolType
    ): BaseResponse<List<IdolResponse>> =
        if (isLogin) apiService.getIdols(category.titleName)
        else apiService.getIdolsNoToken(category.titleName)

    override suspend fun getIdol(isLogin: Boolean, id: String) =
        if (isLogin) apiService.getIdol(id)
        else apiService.getIdolNoToken(id)

    override suspend fun getRoom(roomRequest: RoomRequest) = apiService.getRoom(roomRequest)

    override suspend fun getMessageFromRoom(id: String) = apiService.getMessageFromRoom(id)

    override suspend fun getRooms() = apiService.getRooms()

    override suspend fun registerIdol(idol: Idol) =
        apiService.registerIdol(idol)

    override suspend fun addCoin(coin: Int) =
        apiService.addCoin(coin)

    override suspend fun order(order: OrderRequest) =
        apiService.order(order)


    override suspend fun search(
        nickName: String?,
        rating: Int?
    ) = apiService.search(nickName, rating)

    override suspend fun uploadFile(body: List<MultipartBody.Part>) =
        apiService.uploadFile(body)

    override suspend fun getOrders(status: Int) =
        apiService.getOrders(status)

    override suspend fun getOrdersUser(status: Int) =
        apiService.getOrdersUser(status)

    override suspend fun updateOder(historyRequest: HistoryRequest) =
        apiService.updateOder(historyRequest)

    override suspend fun deleteOrder(orderId: String) =
        apiService.deleteOrder(orderId)
}
