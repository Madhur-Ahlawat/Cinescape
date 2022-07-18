package com.cinescape1.data.models.requestModel

data class PreferenceRequest(
    val arabic:Boolean,
    val cinema:String,
    val experience:String,
    val rating: String,
    val seatCategory: String,
    val seatType: String,
    val userId: String
)