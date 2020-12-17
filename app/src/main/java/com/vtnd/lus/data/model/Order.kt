package com.vtnd.lus.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Order(
    @Json(name = "amount")
    val amount: Int?,
    @Json(name = "coupon")
    val coupon: String?,
    @Json(name = "created_date")
    val createdDate: Date?,
    @Json(name = "discount")
    val discount: Int?,
    @Json(name = "_id")
    val id: String?,
    @Json(name = "idol_address")
    val idolAddress: String?,
    @Json(name = "idol_email")
    val idolEmail: String?,
    @Json(name = "idol_image")
    val idolImage: String?,
    @Json(name = "idol_phone")
    val idolPhone: String?,
    @Json(name = "idol_user_name")
    val idolUserName: String?,
    @Json(name = "note")
    val note: String? = "",
    @Json(name = "payment_method")
    val paymentMethod: String?,
    @Json(name = "start_date")
    val startDate: Date?,
    @Json(name = "status")
    val status: Int?,
    @Json(name = "user_address")
    val userAddress: String?,
    @Json(name = "user_email")
    val userEmail: String?,
    @Json(name = "user_image")
    val userImage: String?,
    @Json(name = "user_name")
    val userName: String?,
    @Json(name = "user_phone")
    val userPhone: String?
)
