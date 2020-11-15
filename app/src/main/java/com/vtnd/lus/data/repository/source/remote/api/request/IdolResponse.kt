package com.vtnd.lus.data.repository.source.remote.api.request

import android.os.Parcelable
import com.squareup.moshi.Json
import com.vtnd.lus.data.model.Idol
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IdolResponse(
    @Json(name = "idol")
    val idol: Idol,
    @Json(name = "liked")
    val liked: Boolean?
) : Parcelable
