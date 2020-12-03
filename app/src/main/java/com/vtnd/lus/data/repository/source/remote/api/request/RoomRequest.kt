package com.vtnd.lus.data.repository.source.remote.api.request

import com.squareup.moshi.Json

data class RoomRequest(
    @Json(name = "id")
    val userId: String
)
