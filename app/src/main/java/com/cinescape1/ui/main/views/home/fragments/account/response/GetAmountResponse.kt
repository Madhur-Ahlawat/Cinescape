package com.cinescape1.ui.main.views.home.fragments.account.response

data class GetAmountResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    data class Output(
        val amounts: ArrayList<String>,
        val paymodes: Any
    )
}