package com.cinescape1.ui.main.views.payment.paymentFaield.reponse

import java.io.Serializable

data class PaymentFailedResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
) : Serializable {
    data class Output(
        val addFood: Boolean,
        val balance: String,
        val bookingId: String,
        val bookingTime: String,
        val bookingType: String,
        val cancelReserve: Boolean,
        val category: String,
        val categoryAlt: String,
        val cinemacode: String,
        val cinemaname: String,
        val clubEnable: Boolean,
        val concessionFoods: List<Any>,
        val discount: String,
        val endDate: Any,
        val endTime: Any,
        val experience: String,
        val experienceAlt: String,
        val foodPickup: Boolean,
        val foodTotal: String,
        val format: String,
        val kioskId: String,
        val language: String,
        val mcensor: String,
        val moviename: String,
        val numofseats: String,
        val payDone: String,
        val paymodes: Paymodes,
        val pickupInfo: String,
        val poster: String,
        val posterhori: String,
        val qr: Any,
        val referenceId: String,
        val screenId: String,
        val seatsArr: List<Any>,
        val showDate: Any,
        val showDateTime: Any,
        val showTime: Any,
        val ticketPrice: String,
        val totalPrice: String,
        val totalTicketPrice: String,
        val trackId: String,
        val trailerUrl: String,
        val transId: Int
    ) : Serializable {
        class Paymodes
    }
}