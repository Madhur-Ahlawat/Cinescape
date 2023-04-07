package com.cinescape1.ui.main.views.summery.response

import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse

data class GiftCardResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
):java.io.Serializable{
    data class Output(
        val amount: String,
        val PAID: String,
        val CAN_PAY:String,
        val payInfo: ArrayList<PaymentListResponse.Output.PayInfo>,
    )
}