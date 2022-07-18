package com.cinescape1.data.models.responseModel

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