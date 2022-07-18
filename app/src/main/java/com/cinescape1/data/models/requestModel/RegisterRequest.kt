package com.cinescape1.data.models.requestModel

data class RegisterRequest(
    val countryCode: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val password: String,
    val dob: String,
    val mobilePhone: String,
    val promoEmail: Boolean,
    val promoMobile: Boolean,
    val reserveNotification: Boolean)