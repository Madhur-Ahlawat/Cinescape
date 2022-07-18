package com.cinescape1.data.models.responseModel

data class NextBookingResponse(
    val code: Int,
    val msg: String,
    val output: ArrayList<Current>,
    val result: String
){
//    data class Output(
//        val current: ArrayList<Current>
//    )

    data class Current(
        val bookingId: String,
        val bookingType: String,
        val cinemacode: String,
        val cinemaname: String,
        val concessionFoods: List<ConcessionFood>,
        val discount: String,
        val endDate: Any,
        val endTime: Any,
        val experience: String,
        val kioskId: String,
        val language: String,
        val mcensor: String,
        val moviename: String,
        val trailerUrl: String,
        val numofseats: Int,
        val payDone: String,
        val paymodes: Paymodes,
        val poster: String,
        val posterhori: String,
        val qr: String,
        val screenId: Int,
        val seatsArr: List<String>,
        val showDate: String,
        val showDateTime: String,
        val showTime: String,
        val ticketPrice: String,
        val rating: String,
        val totalPrice: String,
        val totalTicketPrice: String,
        val addFood: Boolean,
        val clubEnable: Boolean,
        val cancelReserve: Boolean,
        val transId: Int
    )

    data class ConcessionFood(
        val deliveryOption: Int,
        val description: String,
        val descriptionAlt: String,
        val headOfficeItemCode: String,
        val itemId: String,
        val itemType: String,
        val items: List<Any>,
        val modifiers: List<Any>,
        val parentId: String,
        val priceInCents: Int,
        val quantity: String
    )
    class Paymodes
}