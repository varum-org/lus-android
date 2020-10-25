package com.vtnd.lus.data.repository.source.remote.api.response

import com.squareup.moshi.Json

data class BaseResponse<T>(
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "status_code")
    val status: Int,
    @Json(name = "messages")
    val message: String,
    @Json(name = "data")
    var data: T
)
