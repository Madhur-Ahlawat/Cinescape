package com.cinescape1.data.models.responseModel

data class LoginResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    data class Output(
        val arabic: Boolean,
        val balance: String,
        val cardNumber: String,
        val countryCode: String,
        val dob: String,
        val email: String,
        val firstName: String,
        val gender: String,
        val lastName: String,
        val memberid: String,
        val mobilePhone: String,
        val promoEmail: Boolean,
        val promoMobile: Boolean,
        val reserveNotification: Boolean,
        val userId: String,
        val userName: String,
        val otp_require: String
    )
}