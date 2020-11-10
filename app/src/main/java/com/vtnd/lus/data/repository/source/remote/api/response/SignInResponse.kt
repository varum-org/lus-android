package com.vtnd.lus.data.repository.source.remote.api.response

import com.squareup.moshi.Json

data class SignInResponse(
    @Json(name = "is_admin")
    val isAdmin: Boolean?,
    @Json(name = "token")
    val token: String?,
    @Json(name = "user")
    val user: User?
)
