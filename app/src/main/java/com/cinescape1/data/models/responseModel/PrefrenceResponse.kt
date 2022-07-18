package com.cinescape1.data.models.responseModel

data class PrefrenceResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    data class Output(
        val city: String,
        val email: String,
        val experience: List<Experience>,
        val firstName: String,
        val gender: String,
        val lastName: String,
        val mobile: String,
        val password: Any,
        val rating: List<Rating>,
        val seatCategory: String,
        val seatType: String,
        val userName: String
    ){
        data class Experience(
            val likes: Boolean,
            val name: String
        )
        data class Rating(
            val likes: Boolean,
            val name: String
        )
    }

}