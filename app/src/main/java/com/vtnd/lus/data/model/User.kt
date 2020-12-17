package com.vtnd.lus.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vtnd.lus.ui.main.container.registerIdol.addressIdol.DomainLocation
import kotlinx.android.parcel.Parcelize
import java.util.*

@JsonClass(generateAdapter = true)
@Parcelize
data class User(
    @Json(name = "address")
    val address: String?,
    @Json(name = "birthday")
    val birthday: Date?,
    @Json(name = "created_date")
    val createdDate: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "email_active")
    val emailActive: Int?,
    @Json(name = "gender")
    val gender: Int?,
    @Json(name = "_id")
    val id: String?,
    @Json(name = "image_path")
    val imagePath: String?,
    @Json(name = "phone")
    val phone: String?,
    @Json(name = "phone_active")
    val phoneActive: Int?,
    @Json(name = "role_id")
    val roleId: Int?,
    @Json(name = "user_name")
    val userName: String?,
    @Json(name = "__v")
    val v: Int?,
    @Json(name = "location")
    val location: DomainLocation? = null,
    @Json(name = "wallet")
    val wallet: Int? = 0
) : Parcelable
