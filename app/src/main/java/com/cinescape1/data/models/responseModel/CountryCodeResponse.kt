package com.cinescape1.data.models.responseModel

import java.io.Serializable

data class CountryCodeResponse(
    val code: Int,
    val msg: String,
    val output: ArrayList<Output>,
    val result: String
):Serializable{
    data class Output(
        val countryCode: String,
        val countryName: String,
        val flag: String,
        val id: Int,
        val isdCode: String
    ):Serializable
}