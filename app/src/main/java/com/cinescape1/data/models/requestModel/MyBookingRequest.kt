package com.cinescape1.data.models.requestModel

data class MyBookingRequest(
    val bookingid: String,
    val booktype: String,
    val transid: Int,
    val userid: String
)