package com.vtnd.lus.data.repository.source.remote.api

import com.vtnd.lus.data.model.*
import com.vtnd.lus.data.repository.source.remote.api.request.*
import com.vtnd.lus.data.repository.source.remote.api.response.BaseResponse
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.data.repository.source.remote.api.response.RoomResponse
import com.vtnd.lus.data.repository.source.remote.api.response.SignInResponse
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.*

interface ApiService {
    @Headers("@: NoAuth")
    @POST("api/v1/user/login")
    suspend fun signIn(@Body signInRequest: SignInRequest): BaseResponse<SignInResponse>

    @Headers("@: NoAuth")
    @POST("api/v1/user/register")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): BaseResponse<Any>

    @Headers("@: NoAuth")
    @POST("api/v1/user/verify_email")
    suspend fun verifyAccount(@Body verifyRequest: VerifyRequest): BaseResponse<Any>

    @Headers("@: NoAuth")
    @GET("api/v1/idols")
    suspend fun getIdolsNoToken(@Query("category") category: String): BaseResponse<List<IdolResponse>>

    @GET("api/v1/idols")
    suspend fun getIdols(@Query("category") category: String): BaseResponse<List<IdolResponse>>

    @GET("api/v1/user")
    suspend fun getUser(): BaseResponse<IdolResponse>

    @GET("api/v1/services")
    suspend fun getServices(): BaseResponse<List<Service>?>

    @POST("api/v1/room")
    suspend fun getRoom(@Body roomRequest: RoomRequest): BaseResponse<Room>

    @GET("api/v1/room/{id}")
    suspend fun getMessageFromRoom(@Path("id") id: String): BaseResponse<List<Message>>

    @GET("api/v1/rooms")
    suspend fun getRooms(): BaseResponse<List<RoomResponse>>

    @GET("api/v1/idols/search")
    suspend fun search(
        @Query("name") nickName: String?,
        @Query("rating") rating: Int?
    ): BaseResponse<List<Idol>>

    @GET("api/v1/idol/{id}")
    suspend fun getIdol(
        @Path("id") id: String
    ): BaseResponse<IdolResponse>

    @Headers("@: NoAuth")
    @GET("api/v1/idol/{id}")
    suspend fun getIdolNoToken(
        @Path("id") id: String
    ): BaseResponse<IdolResponse>

    @PUT("api/v1/user/logout")
    @FormUrlEncoded
    suspend fun logout(
        @Field("device_token") deviceToken: String
    ): BaseResponse<Any>

    @Headers("@: NoAuth")
    @Multipart
    @POST("api/v1/uploads")
    suspend fun uploadFile(@Part body: List<MultipartBody.Part>): BaseResponse<List<String>>

    @POST("api/v1/idol")
    suspend fun registerIdol(@Body idol: Idol): BaseResponse<Idol>

    @POST("api/v1/user/order")
    suspend fun order(@Body order: OrderRequest): BaseResponse<Any>

    @FormUrlEncoded
    @POST("api/v1/wallet")
    suspend fun addCoin(@Field("coin") coin: Int): BaseResponse<Any>

    @GET("api/v1/idols/orders")
    suspend fun getOrders(@Query("status") status: Int): BaseResponse<List<Order>>

    @GET("api/v1/orders")
    suspend fun getOrdersUser(@Query("status") status: Int): BaseResponse<List<Order>>

    @PATCH("api/v1/order")
    suspend fun updateOder(@Body historyRequest: HistoryRequest): BaseResponse<Order>

    @DELETE("api/v1/orders/{order_id}")
    suspend fun deleteOrder(@Path("order_id") orderId: String): BaseResponse<Any>

    companion object Factory {
        operator fun invoke(retrofit: Retrofit) = retrofit.create<ApiService>()
    }
}
