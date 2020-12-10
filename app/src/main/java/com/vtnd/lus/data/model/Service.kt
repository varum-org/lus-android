package com.vtnd.lus.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Service(
    @Json(name = "service_code")
    val serviceCode: Int? = null,
    @Json(name = "service_name")
    val serviceName: String? = null,
    @Json(name = "service_price")
    val servicePrice: Int? = null,
    @Json(name = "service_description")
    val serviceDescription: String? = null,
    @Json(name = "service_image_path")
    val serviceImagePath: String? = null
) : Parcelable
