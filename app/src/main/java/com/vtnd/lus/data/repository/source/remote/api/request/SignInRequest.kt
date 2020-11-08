package com.vtnd.lus.data.repository.source.remote.api.request

import com.squareup.moshi.Json

data class SignInRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "device_token")
    val deviceToken: String
)
