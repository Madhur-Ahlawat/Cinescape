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
        val user: User,
        val userId: String
    ):Serializable {

        data class User(
            val arabic: Boolean,
            val balance: String,
            val balanceInCent: Int,
            val cardNumber: String,
            val cinema: Any,
            val city: Any,
            val countryCode: Any,
            val dob: Any,
            val email: Any,
            val experience: List<Any>,
            val experienceIconUrl: String,
            val firstName: Any,
            val gender: Any,
            val guest: Boolean,
            val lastName: Any,
            val memberid: String,
            val mobilePhone: Any,
            val needLogin: Boolean,
            val promoEmail: Boolean,
            val promoMobile: Boolean,
            val rating: List<Any>,
            val reserveNotification: Boolean,
            val seatCategory: Any,
            val seatType: Any,
            val userId: String,
            val userName: Any
        ):Serializable
    }
}