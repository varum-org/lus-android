package com.vtnd.lus.data.repository.source.remote.api.response

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vtnd.lus.data.model.Room
import com.vtnd.lus.data.model.User
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class RoomResponse(
        @Json(name = "room")
        val room: Room?,
        @Json(name = "users")
        val users: List<User>?= emptyList()
) : Parcelable
