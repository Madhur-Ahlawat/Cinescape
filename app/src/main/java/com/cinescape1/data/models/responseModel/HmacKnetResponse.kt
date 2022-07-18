package com.cinescape1.data.models.responseModel

data class HmacKnetResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    data class Output(
        val callingUrl: String,
        val msg: String,
        val status: Int
    )
}