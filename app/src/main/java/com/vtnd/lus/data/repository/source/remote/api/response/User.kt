package com.vtnd.lus.data.repository.source.remote.api.response

import com.squareup.moshi.Json

data class User(
    @Json(name = "email")
    val email: String?,
    @Json(name = "_id")
    val id: String?,
    @Json(name = "image_path")
    val imagePath: String?,
    @Json(name = "role_id")
    val roleId: Int?,
    @Json(name = "user_name")
    val userName: String?
)
