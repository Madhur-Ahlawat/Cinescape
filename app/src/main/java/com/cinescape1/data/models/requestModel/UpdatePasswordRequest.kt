package com.cinescape1.data.models.requestModel

data class UpdatePasswordRequest(
    val eotp: String,
    val motp: String,
    val password: String,
    val userid: String,)