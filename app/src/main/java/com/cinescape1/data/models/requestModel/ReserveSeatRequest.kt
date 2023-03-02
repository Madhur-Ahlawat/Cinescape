package com.cinescape1.data.models.requestModel

data class ReserveSeatRequest(
    val category: String,
    val cinemaId: String,
    val reseveSeatVOs: ArrayList<ReseveSeatVO>,
    val sessionid: String,
    val mid: String,
    val ttypeCode: String,
    val areaCode: String,
)
{
    data class ReseveSeatVO(
        val seatBookingId: String
    )
}