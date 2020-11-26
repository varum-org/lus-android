package com.vtnd.lus.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Service(
    @Json(name = "service_code")
    val serviceCode: Int,
    @Json(name = "service_name")
    val serviceName: String,
    @Json(name = "service_price")
    val servicePrice: Int,
    @Json(name = "service_description")
    val serviceDescription: String
) : Parcelable
