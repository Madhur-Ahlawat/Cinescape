package com.cinescape1.ui.main.views.splash.response

data class SplashResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    class Output
}