package com.cinescape1.data.models.responseModel

data class MyBookingResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    data class Output(
        val current: List<Current>,
        val past: ArrayList<Past>
    )


    data class Current(
        val bookingId: String,
        val bookingType: String,
        val cinemacode: String,
        val trailerUrl: String,
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
        val numofseats: Int,
        val payDone: String,
        val poster: String,
        val posterhori: String,
        val qr: String,
        val screenId: Int,
        val seatsArr: List<String>,
        val showDate: String,
        val showDateTime: String,
        val showTime: String,
        val ticketPrice: String,
        val totalPrice: String,
        val totalTicketPrice: String,
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

    data class Past(
        val bookingId: String,
        val bookingType: String,
        val cinemacode: String,
        val cinemaname: String,
        val concessionFoods: List<ConcessionFoodX>,
        val discount: String,
        val endDate: Any,
        val endTime: Any,
        val experience: String,
        val kioskId: String,
        val language: String,
        val mcensor: String,
        val moviename: String,
        val numofseats: Int,
        val payDone: String,
        val paymodes: PaymodesX,
        val poster: String,
        val posterhori: String,
        val qr: String,
        val screenId: Int,
        val seatsArr: List<String>,
        val showDate: String,
        val showDateTime: String,
        val showTime: String,
        val ticketPrice: String,
        val totalPrice: String,
        val totalTicketPrice: String,
        val transId: Int
    )

    class PaymodesX


    data class ConcessionFoodX(
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
}