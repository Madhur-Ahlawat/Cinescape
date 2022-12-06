package com.cinescape1.data.models.responseModel

data class ChangePasswordResponses(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    data class Output(
        val arabic: Boolean,
        val balance: String,
        val balanceInCent: Int,
        val cardNumber: String,
        val cinema: Any,
        val city: String,
        val countryCode: Any,
        val dob: String,
        val email: String,
        val experience: List<Experience>,
        val experienceIconUrl: String,
        val firstName: String,
        val gender: String,
        val guest: Boolean,
        val lastName: String,
        val memberid: String,
        val mobilePhone: String,
        val promoEmail: Boolean,
        val promoMobile: Boolean,
        val rating: List<Rating>,
        val reserveNotification: Boolean,
        val seatCategory: String,
        val seatType: String,
        val userId: String,
        val userName: String
    )

    data class Experience(
        val likes: Boolean,
        val name: String
    )

    data class Rating(
        val likes: Boolean,
        val name: String
    )
}