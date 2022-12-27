package com.cinescape1.data.models.responseModel

data class ProfileResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String)
{
    data class Output(
        val city: String,
        val email: String,
        val experience: ArrayList<Experience>,
        val firstName: String,
        val gender: String,
        val lastName: String,
        val mobilePhone: String,
        val dob: String,
        val countryCode: String,
        val cardNumber: String,
        val balance: String,
        val guest: Boolean,
        val password: Any,
        val rating: ArrayList<Rating>,
        val seatCategory: String,
        val seatType: String,
        val userName: String
    ) {
        data class Experience(
            val likes: Boolean,
            val name: String,
            var count: Int
        )
        data class Rating(
            val likes: Boolean,
            val name: String,
            var count: Int
        )
    }
}