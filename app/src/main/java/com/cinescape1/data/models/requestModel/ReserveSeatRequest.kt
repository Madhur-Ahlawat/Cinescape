package com.cinescape1.data.models.requestModel

data class ReserveSeatRequest(
    val cinemaId: String,
    val reseveSeatVOs: ArrayList<ReseveSeatVO>,
    val sessionid: String,
    val mid: String,
    val ttypeCode: String,
)
{
    data class ReseveSeatVO(
        val seatBookingId: String
    )
}