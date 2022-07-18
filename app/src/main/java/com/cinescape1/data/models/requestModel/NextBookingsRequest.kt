package com.cinescape1.data.models.requestModel

data class NextBookingsRequest(
    val bookingid: String,
    val booktype: String,
    val transid: Int,
    val userid: String,
    val upcoming: Boolean
)