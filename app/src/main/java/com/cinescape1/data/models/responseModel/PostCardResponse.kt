package com.cinescape1.data.models.responseModel

data class PostCardResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    data class Output(
        val acsURL: String,
        val authTransId: String,
        val errorDescription: String,
        val jwtToken: String,
        val pares: String,
        val redirect: String,
        val xid: String
    )
}