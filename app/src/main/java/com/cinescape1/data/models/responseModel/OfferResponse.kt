package com.cinescape1.data.models.responseModel

import java.io.Serializable

data class OfferResponse(
    val code: Int,
    val msg: String,
    val output: ArrayList<Output>,
    val result: String
):Serializable{
    data class Output(
        val appImageUrl: String,
        val appThumbImageUrl: String,
        val description: String,
        val detailText: String,
        val howAvail: String,
        val id: Int,
        val name: String,
        val priority: Int,
        val quota: String,
        val source: String,
        val text: String,
        val tnc: String,
        val type: String,
        val url: String,
        val validfrom: String,
        val validon: String,
        val validto: String,
        val webDescription: String,
        val webImageUrl: String,
        val webThumbImageUrl: String
    )
}