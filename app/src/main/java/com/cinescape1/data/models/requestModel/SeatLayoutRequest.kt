package com.cinescape1.data.models.requestModel

data class SeatLayoutRequest(
    val cinemaId: String,
    val dated: String,
    val mid: String,
    val sessionId: String
)