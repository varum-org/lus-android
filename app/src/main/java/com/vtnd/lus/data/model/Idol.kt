package com.vtnd.lus.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Idol(
    @Json(name = "_id")
    val id: String = "",
    @Json(name = "address")
    val address: String = "",
    @Json(name = "completion_rate")
    val completionRate: Int? = null,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "image_gallery")
    val imageGallery: List<String> = emptyList(),
    @Json(name = "nick_name")
    val nickName: String? = null,
    @Json(name = "rating")
    val rating: Int? = null,
    @Json(name = "rating_total")
    val ratingTotal: Int? = null,
    @Json(name = "relationship")
    val relationship: String? = null,
    @Json(name = "rent_time_total")
    val rentTimeTotal: Int? = null,
    @Json(name = "rent_total")
    val rentTotal: Int? = null,
    @Json(name = "rent_total_accepted")
    val rentTotalAccepted: Int? = null,
    @Json(name = "services")
    val services: List<Service> = emptyList(),
    @Json(name = "status")
    val status: Int? = null,
    @Json(name = "user_id")
    val userId: String = ""
) : Parcelable
