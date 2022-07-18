package com.cinescape1.data.models.responseModel

import java.io.Serializable

data class UpdateAccountResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
):Serializable{
    data class Output(
        val arabic: Boolean,
        val balance: String,
        val cardNumber: String,
        val city: String,
        val countryCode: String,
        val dob: String,
        val email: String,
        val experience: ArrayList<Experience>,
        val firstName: String,
        val gender: String,
        val guest: Boolean,
        val lastName: String,
        val memberid: String,
        val mobilePhone: String,
        val promoEmail: Boolean,
        val promoMobile: Boolean,
        val rating: ArrayList<Rating>,
        val reserveNotification: Boolean,
        val seatCategory: String,
        val seatType: String,
        val userId: String,
        val userName: String
    ):Serializable{
        data class Experience(
            val likes: Boolean,
            val name: String
        ):Serializable
        data class Rating(
            val likes: Boolean,
            val name: String
        ):Serializable
    }
}