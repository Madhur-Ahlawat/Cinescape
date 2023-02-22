package com.cinescape1.data.models.requestModel

data class FoodPrepareRequest(
    val bookingid: String,
    val booktype: String,
    val transid: Int,
    val userid: String
)