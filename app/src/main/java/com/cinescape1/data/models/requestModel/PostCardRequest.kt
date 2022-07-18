package com.cinescape1.data.models.requestModel

data class PostCardRequest(
    val bookingid: String,
    val cardNumber: String,
    val cvNumber: String,
    val expirationMonth: String,
    val expirationYear: String,
    val referenceId: String
)