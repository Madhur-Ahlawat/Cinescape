package com.cinescape1.ui.main.views.login.activeWallet.repsonse

data class ActivateWalletResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
):java.io.Serializable{
    data class Output(
        val otp_require: String,
        val userid: String
    )
}