package com.cinescape1.data.models.responseModel

data class Response(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
)