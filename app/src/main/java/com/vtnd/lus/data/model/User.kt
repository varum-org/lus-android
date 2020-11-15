package com.vtnd.lus.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "address")
    val address: String?,
    @Json(name = "birthday")
    val birthday: String?,
    @Json(name = "created_date")
    val createdDate: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "email_active")
    val emailActive: Int?,
    @Json(name = "gender")
    val gender: String?,
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
    val v: Int?
)
