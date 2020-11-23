package com.vtnd.lus.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Idol(
    @Json(name = "_id")
    val id: String,
    @Json(name = "address")
    val address: String,
    @Json(name = "completion_rate")
    val completionRate: Int,
    @Json(name = "description")
    val description: String,
    @Json(name = "image_gallery")
    val imageGallery: List<String>,
    @Json(name = "nick_name")
    val nickName: String,
    @Json(name = "rating")
    val rating: Int,
    @Json(name = "rating_total")
    val ratingTotal: Int,
    @Json(name = "relationship")
    val relationship: String,
    @Json(name = "rent_time_total")
    val rentTimeTotal: Int,
    @Json(name = "rent_total")
    val rentTotal: Int,
    @Json(name = "rent_total_accepted")
    val rentTotalAccepted: Int,
    @Json(name = "services")
    val services: List<Service> = emptyList(),
    @Json(name = "status")
    val status: Int,
    @Json(name = "user_id")
    val userId: String
) : Parcelable
