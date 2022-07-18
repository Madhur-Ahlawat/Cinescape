package com.cinescape1.data.models.responseModel

import java.io.Serializable

data class PaymentTokenResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
):Serializable{
    data class Output(
        val deviceSessionId: String,
        val fqdn: Any,
        val jwtToken: String,
        val merchantId: String,
        val orgId: String
    ):Serializable
}