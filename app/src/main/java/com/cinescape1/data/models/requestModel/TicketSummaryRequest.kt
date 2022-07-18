package com.cinescape1.data.models.requestModel

data class TicketSummaryRequest(
    val transid: String,
    val bookingId:String,
    val userId:String
)