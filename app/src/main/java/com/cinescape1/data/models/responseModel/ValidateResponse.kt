package com.cinescape1.data.models.responseModel

data class ValidateResponse(
    val code: Int,
    val msg: String,
    val output: String,
    val result: String
)