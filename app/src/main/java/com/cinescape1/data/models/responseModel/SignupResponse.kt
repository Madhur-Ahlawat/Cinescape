package com.cinescape1.data.models.responseModel

import java.io.Serializable

data class SignupResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
):Serializable{
    data class Output(
        val otp_require: String,
        val userid: String
    ):Serializable
}