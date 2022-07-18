package com.cinescape1.data.models.responseModel

data class ReserveSeatResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    data class Output(
        val fc: Boolean,
        val transid: String,
        val totalPrice: String,
        val booktype: String
    )
}