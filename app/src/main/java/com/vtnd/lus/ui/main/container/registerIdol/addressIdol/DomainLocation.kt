package com.vtnd.lus.ui.main.container.registerIdol.addressIdol

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class DomainLocation(
  @Json(name = "latitude")
  val latitude: Double,
  @Json(name = "longitude")
  val longitude: Double,
  @Json(name = "name")
  val name: String? = ""
) : Parcelable