package com.vtnd.lus.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Message(
    @Json(name = "_id")
    val id: String?,
    @Json(name = "user_id")
    val userId: String?,
    @Json(name = "content")
    val content: String?,
    @Json(name = "room_id")
    val roomId: String?
) : Parcelable
