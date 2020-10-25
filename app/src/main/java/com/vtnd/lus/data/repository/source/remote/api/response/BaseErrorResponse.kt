package com.vtnd.lus.data.repository.source.remote.api.response

import com.squareup.moshi.Json

class BaseErrorResponse(
    @Json(name ="status")
    val status: Int,
    @Json(name ="messages")
    val message: String
)
