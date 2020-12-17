package com.vtnd.lus.data.repository.source

import com.vtnd.lus.data.model.Idol
import com.vtnd.lus.data.model.Message
import com.vtnd.lus.data.model.Order
import com.vtnd.lus.data.model.Room
import com.vtnd.lus.data.repository.source.remote.api.request.*
import com.vtnd.lus.data.repository.source.remote.api.response.BaseResponse
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.data.repository.source.remote.api.response.RoomResponse
import com.vtnd.lus.data.repository.source.remote.api.response.SignInResponse
import com.vtnd.lus.shared.type.CategoryIdolType
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Path
import retrofit2.http.Query

interface UserDataSource {

    interface Local {

        fun userObservable(): Flow<IdolResponse?>

        suspend fun user(): IdolResponse?

        suspend fun saveUser(user: IdolResponse)

        suspend fun clearUser()
    }

    interface Remote {
        // User

        suspend fun signIn(email: String, password: String): BaseResponse<SignInResponse>

        suspend fun signUp(signUpRequest: SignUpRequest): BaseResponse<Any>

        suspend fun verifyAccount(verifyRequest: VerifyRequest): BaseResponse<Any>

        suspend fun getUser(): BaseResponse<IdolResponse>

        suspend fun logout(deviceToken: String): BaseResponse<Any>

        //Idol

        suspend fun getIdols(
            isLogin: Boolean,
            category: CategoryIdolType
        ): BaseResponse<List<IdolResponse>>

        suspend fun getIdol(
            isLogin: Boolean,
            id: String
        ): BaseResponse<IdolResponse>

        suspend fun getOrders(status: Int): BaseResponse<List<Order>>

        suspend fun getOrdersUser(status: Int): BaseResponse<List<Order>>

        suspend fun updateOder( historyRequest: HistoryRequest): BaseResponse<Order>

        suspend fun deleteOrder(orderId: String): BaseResponse<Any>

        //Room
        suspend fun getRoom(roomRequest: RoomRequest): BaseResponse<Room>

        suspend fun getMessageFromRoom(id: String): BaseResponse<List<Message>>

        suspend fun getRooms(): BaseResponse<List<RoomResponse>>

        suspend fun search(
            nickName: String?,
            rating: Int?
        ): BaseResponse<List<Idol>>

        suspend fun uploadFile(body: List<MultipartBody.Part>): BaseResponse<List<String>>

        suspend fun registerIdol(idol: Idol): BaseResponse<Idol>

        suspend fun addCoin(coin: Int): BaseResponse<Any>

        //order
        suspend fun order(order: OrderRequest): BaseResponse<Any>
    }
}
