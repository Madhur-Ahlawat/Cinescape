package com.cinescape1.data.models.requestModel

data class UpdateAccountRequest(
    val city: String,
    val dob: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val lastName: String,
    val userId: String,
    val mobilePhone: String
    )