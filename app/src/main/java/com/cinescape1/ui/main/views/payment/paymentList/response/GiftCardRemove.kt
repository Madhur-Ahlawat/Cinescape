package com.cinescape1.ui.main.views.payment.paymentList.response

data class GiftCardRemove(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
):java.io.Serializable{
    data class Output(
        val PAID: String,
        val amount: String
    )
}