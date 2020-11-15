package com.vtnd.lus.data.repository.source.remote.api.response

import com.squareup.moshi.Json
import com.vtnd.lus.data.model.User

data class UserResponse(
    @Json(name = "user")
    val user: User?
)
