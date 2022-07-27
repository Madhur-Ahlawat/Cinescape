package com.cinescape1.data.models.requestModel

data class MovieRequest(
    val cinema: String = "ALL",
    val experience: String= "ALL",
    val genre: String= "ALL",
    val language: String= "ALL",
    val rating: String= "ALL",
    val time: String= "ALL"
)
