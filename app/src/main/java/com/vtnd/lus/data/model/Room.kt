package com.vtnd.lus.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Room(
    @Json(name = "_id")
    val id: String?,
    @Json(name = "user_id")
    val userId: List<String>? = emptyList()
) : Parcelable
