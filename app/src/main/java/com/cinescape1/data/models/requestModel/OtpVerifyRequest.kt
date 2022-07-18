package com.cinescape1.data.models.requestModel

data class OtpVerifyRequest(
    val eotp: String,
    val motp: String,
    val userid: String
)