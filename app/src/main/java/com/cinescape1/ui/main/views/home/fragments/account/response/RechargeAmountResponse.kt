package com.cinescape1.ui.main.views.home.fragments.account.response

import java.io.Serializable

data class RechargeAmountResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
) : Serializable {
    data class Output(
        val amounts: ArrayList<Amount>,
        val paymodes: Any
    ) : Serializable {
        data class Amount(
            val amount: Int,
            val amountStr: String
        )
    }
}