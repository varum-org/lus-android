package com.vtnd.lus.data.repository.source.remote.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @Expose
    @SerializedName("sign_in")
    val signIn: SignInInfo
)

data class SignInInfo(
    @Expose
    @SerializedName("email")
    val email: String,
    @Expose
    @SerializedName("password")
    val password: String,
    @Expose
    @SerializedName("device_id")
    val deviceId: String)
