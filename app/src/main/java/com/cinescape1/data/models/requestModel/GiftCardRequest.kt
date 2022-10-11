package com.cinescape1.data.models.requestModel

data class GiftCardRequest(
    val bookingid: String,
    val booktype: String,
    val cardNumber: String,
    val transid: String,
    val userid: String
)