package com.cinescape1.ui.main.views.summery.response

data class GiftCardResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
):java.io.Serializable{
    data class Output(
        val amount: String,
        val PAID: String
    )
}