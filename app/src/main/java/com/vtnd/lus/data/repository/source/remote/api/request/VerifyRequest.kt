package com.vtnd.lus.data.repository.source.remote.api.request

import com.squareup.moshi.Json

data class VerifyRequest(
    @Json(name = "email")
    val email: String?,
    @Json(name = "email_code")
    val emailCode: String?
)
