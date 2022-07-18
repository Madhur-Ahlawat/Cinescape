package com.cinescape1.data.models.requestModel

data class ResendRequest(
    val bookingid: String,
    val booktype: String,
    val transid: String,
    val userid: String
)