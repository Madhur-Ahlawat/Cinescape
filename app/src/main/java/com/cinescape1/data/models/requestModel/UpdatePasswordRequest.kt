package com.cinescape1.data.models.requestModel

data class UpdatePasswordRequest(
    val eotp: String,
    val password: String,
    val userid: String,)