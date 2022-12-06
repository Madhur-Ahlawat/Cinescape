package com.cinescape1.data.models.requestModel

data class ChangePasswordRequest(
    val password: String,
    val userId: String
)