package com.vtnd.lus.data.repository.source.remote.api.request

import com.squareup.moshi.Json

data class HistoryRequest(
    @Json(name = "oder_id")
    val orderId:String,
    @Json(name = "status")
    val status: Int
)