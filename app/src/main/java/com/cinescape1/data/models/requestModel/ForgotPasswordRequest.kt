package com.cinescape1.data.models.requestModel

data class ForgotPasswordRequest(
    val password: String,
    val userName: String)