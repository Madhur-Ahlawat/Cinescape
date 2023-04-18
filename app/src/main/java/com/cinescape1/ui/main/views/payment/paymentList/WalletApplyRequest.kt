package com.cinescape1.ui.main.views.payment.paymentList

data class WalletApplyRequest(
    val bookingid: String,
    val booktype: String,
    val payFull: Boolean=false,
    val transid: String,
    val userid: String
)