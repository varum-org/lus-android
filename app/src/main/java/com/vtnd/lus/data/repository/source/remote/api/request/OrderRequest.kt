package com.vtnd.lus.data.repository.source.remote.api.request

import com.squareup.moshi.Json
import com.vtnd.lus.data.model.Service
import java.util.*

data class OrderRequest(
    @Json(name = "user_id")
    val user_id: String,
    @Json(name = "payment_method")
    val paymentMethod: String = "Xu",
    @Json(name = "start_date")
    val startDate: Date,
    @Json(name = "note")
    val note: String = "",
    @Json(name = "services")
    val services: List<Service> = emptyList()
)
