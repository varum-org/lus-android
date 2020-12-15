package com.vtnd.lus.data

import android.net.Uri
import com.vtnd.lus.data.model.Idol
import com.vtnd.lus.data.model.Message
import com.vtnd.lus.data.model.Room
import com.vtnd.lus.data.model.User
import com.vtnd.lus.data.repository.source.remote.api.request.RoomRequest
import com.vtnd.lus.data.repository.source.remote.api.request.SignUpRequest
import com.vtnd.lus.data.repository.source.remote.api.request.VerifyRequest
import com.vtnd.lus.data.repository.source.remote.api.response.BaseResponse
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.data.repository.source.remote.api.response.RoomResponse
import com.vtnd.lus.shared.scheduler.DataResult
import com.vtnd.lus.shared.type.CategoryIdolType
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body

interface UserRepository {

    //Local
    suspend fun user(): IdolResponse?

    fun userObservable(): Flow<IdolResponse?>

    //Remote
    //User
    suspend fun signIn(email: String, password: String): DataResult<Any>

    suspend fun signUp(signUpRequest: SignUpRequest): DataResult<Any>

    suspend fun verifyAccount(verifyRequest: VerifyRequest): DataResult<Any>

    suspend fun getUser(id: String): DataResult<Unit>

    suspend fun logout(): DataResult<Any>

    //Idol
    suspend fun getIdols(
        isLogin: Boolean,
        category: CategoryIdolType
    ): DataResult<List<IdolResponse>>

    suspend fun search(
        nickName: String?,
        rating: Int?
    ): DataResult<List<Idol>>

    suspend fun getIdol(
        id: String
    ): DataResult<IdolResponse>

    suspend fun registerIdol(idol: Idol,uris: List<Uri>): DataResult<Any>

    //Room

    //Room
    suspend fun getRoom(roomRequest: RoomRequest): DataResult<Room>

    suspend fun getMessageFromRoom(id: String): DataResult<List<Message>>

    suspend fun getRooms(): DataResult<List<RoomResponse>>
}
