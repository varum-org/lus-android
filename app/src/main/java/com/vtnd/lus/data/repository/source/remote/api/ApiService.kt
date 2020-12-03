package com.vtnd.lus.data.repository.source.remote.api

import com.vtnd.lus.data.model.Message
import com.vtnd.lus.data.model.Room
import com.vtnd.lus.data.model.Service
import com.vtnd.lus.data.repository.source.remote.api.request.RoomRequest
import com.vtnd.lus.data.repository.source.remote.api.request.SignInRequest
import com.vtnd.lus.data.repository.source.remote.api.request.SignUpRequest
import com.vtnd.lus.data.repository.source.remote.api.request.VerifyRequest
import com.vtnd.lus.data.repository.source.remote.api.response.BaseResponse
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.data.repository.source.remote.api.response.SignInResponse
import com.vtnd.lus.data.repository.source.remote.api.response.UserResponse
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
    suspend fun getUser(@Path("id") id: String): BaseResponse<UserResponse>

    @GET("api/v1/services")
    suspend fun getServices(): BaseResponse<List<Service>?>

    @POST("api/v1/room")
    suspend fun getRoom(@Body roomRequest: RoomRequest): BaseResponse<Room>

    @GET("api/v1/room/{id}")
    suspend fun getMessageFromRoom(@Path("id") id: String): BaseResponse<List<Message>>

    companion object Factory {
        operator fun invoke(retrofit: Retrofit) = retrofit.create<ApiService>()
    }
}
