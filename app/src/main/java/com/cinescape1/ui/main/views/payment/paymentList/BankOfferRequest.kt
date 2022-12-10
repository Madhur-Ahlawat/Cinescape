package com.cinescape1.ui.main.views.payment.paymentList

data class BankOfferRequest(
    val bookingId: String,
    val cardNo: String,
    val offerId: String,
    val transid: String,
    val userId: String
)