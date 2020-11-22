package com.vtnd.lus.data.repository.source.remote.api.response

import com.squareup.moshi.Json
import com.vtnd.lus.data.model.User

data class SignInResponse(
    @Json(name = "token")
    val token: String?,
    @Json(name = "user")
    val user: User?,
    @Json(name = "is_admin")
    val isAdmin: Boolean?
)
