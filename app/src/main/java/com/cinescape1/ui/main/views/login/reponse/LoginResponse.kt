package com.cinescape1.ui.main.views.login.reponse

data class LoginResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
):java.io.Serializable{
    data class Output(
        val otp_require: String,
        val user: User,
        val userId: String
    ):java.io.Serializable{
        data class User(
            val arabic: Boolean,
            val balance: String,
            val balanceInCent: Int,
            val cardNumber: String,
            val cinema: String,
            val city: String,
            val countryCode: String,
            val dob: String,
            val email: String,
            val experience: List<Any>,
            val experienceIconUrl: String,
            val firstName: String,
            val gender: String,
            val guest: Boolean,
            val lastName: String,
            val memberid: String,
            val mobilePhone: String,
            val promoEmail: Boolean,
            val promoMobile: Boolean,
            val rating: List<Any>,
            val reserveNotification: Boolean,
            val seatCategory: String,
            val seatType: String,
            val userId: String,
            val userName: String
        ):java.io.Serializable
    }
}