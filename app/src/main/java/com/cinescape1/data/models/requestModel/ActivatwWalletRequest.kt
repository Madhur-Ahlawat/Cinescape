package com.cinescape1.data.models.requestModel

data class ActivatwWalletRequest(
    val cardNumber: String,
    val countryCode: String,
    val dob: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val lastName: String,
    val mobilePhone: String,
    val password: String,
    val promoEmail: Boolean,
    val promoMobile: Boolean,
    val reserveNotification: Boolean,
    val socialId: String,
    val socialType: String
)