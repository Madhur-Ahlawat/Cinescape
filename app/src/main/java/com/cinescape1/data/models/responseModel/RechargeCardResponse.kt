package com.cinescape1.data.models.responseModel

data class RechargeCardResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    data class Output(
        val bookingid: String,
        val booktype: String,
        val success: String,
        val transid: String
    )
}