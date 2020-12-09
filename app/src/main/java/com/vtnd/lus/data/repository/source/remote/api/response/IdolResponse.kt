package com.vtnd.lus.data.repository.source.remote.api.response

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vtnd.lus.data.model.Idol
import com.vtnd.lus.data.model.User
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class IdolResponse(
    @Json(name = "idol")
    val idol: Idol?,
    @Json(name = "liked")
    val liked: Boolean?,
    @Json(name = "user")
    val user: User
) : Parcelable
