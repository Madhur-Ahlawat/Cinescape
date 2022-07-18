package com.cinescape1.data.models.requestModel

data class CancelTransRequest(
    val bookingId: String,
    val transid: String
)