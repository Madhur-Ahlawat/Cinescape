package com.cinescape1.ui.main.views.payment.paymentList.response

data class OfferRemove(
    val code: Int,
    val msg: String,
    val output: BankOfferApply.Output,
    val result: String
)