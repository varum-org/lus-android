package com.vtnd.lus.data.repository.source.remote.api.request

import com.squareup.moshi.Json

data class SignUpRequest(
    @Json(name = "user_name")
    val userName: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "password")
    val password: String?,
    @Json(name = "phone")
    val phone: String?
)
