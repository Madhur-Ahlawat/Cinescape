package com.cinescape1.data.models.requestModel

data class MySingleTicketRequest(
    val bookingid: String,
    val booktype: String,
    val transid: Int,
    val userid: String
)