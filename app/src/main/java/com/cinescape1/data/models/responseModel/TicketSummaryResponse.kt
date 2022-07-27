package com.cinescape1.data.models.responseModel

data class TicketSummaryResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    data class Output(
        val bookingId: String,
        val bookingType: String,
        val cinemacode: String,
        val cinemaname: String,
        val concessionFoods: List<ConcessionFood>,
        val discount: String,
        val endDate: String,
        val endTime: String,
        val experience: String,
        val language: String,
        val mcensor: String,
        val moviename: String,
        val numofseats: Int,
        val paymodes: Paymodes,
        val poster: String,
        val posterhori: String,
        val screenId: Int,
        val seatsArr: List<String>,
        val showDate: String,
        val showTime: String,
        val ticketPrice: String,
        val totalPrice: String,
        val totalTicketPrice: String,
        val kioskId: String,
        val balance: String,
        val clubEnable: Boolean,
        val addFood: Boolean,
        val cancelReserve: Boolean,
        val category: String,
        val payDone: String,
        val qr: String
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
        val itemPrice: String,
        val quantity: String
    )

    class Paymodes
}