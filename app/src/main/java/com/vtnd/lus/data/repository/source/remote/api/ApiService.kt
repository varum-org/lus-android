package com.vtnd.lus.data.repository.source.remote.api

import com.vtnd.lus.data.model.Idol
import com.vtnd.lus.data.model.Message
import com.vtnd.lus.data.model.Room
import com.vtnd.lus.data.model.Service
import com.vtnd.lus.data.repository.source.remote.api.request.RoomRequest
import com.vtnd.lus.data.repository.source.remote.api.request.SignInRequest
import com.vtnd.lus.data.repository.source.remote.api.request.SignUpRequest
import com.vtnd.lus.data.repository.source.remote.api.request.VerifyRequest
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

    @GET("api/v1/user/{id}")
    suspend fun getUser(@Path("id") id: String): BaseResponse<IdolResponse>

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

    @Multipart
    @POST("api/v1/uploads")
    suspend fun uploadFile(@Part body: List<MultipartBody.Part>): BaseResponse<List<String>>

    @POST("api/v1/idol")
    suspend fun registerIdol(@Body idol: Idol): BaseResponse<Any>

    companion object Factory {
        operator fun invoke(retrofit: Retrofit) = retrofit.create<ApiService>()
    }
}
