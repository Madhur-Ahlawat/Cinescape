package com.cinescape1.data.models.responseModel

import java.io.Serializable

data class WalletResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
):Serializable{
    data class Output(
        val PAID: String
    )
}