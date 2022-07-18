package com.cinescape1.data.models.requestModel

data class ValidateJWTRequest(
    val bookingid: String,
    val cardNumber: String,
    val cvNumber: String,
    val expirationMonth: String,
    val expirationYear: String,
    val payerAuth: String,
    val sessionId: String,
    val xid: String

)