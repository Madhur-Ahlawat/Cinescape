package com.cinescape1.data.models.responseModel

import java.io.Serializable

data class FoodResponse(val code: Int,
    val msg: String,
    val output: Output,
    val result: String
):Serializable{
    data class Output(
        val cinemas: ArrayList<Cinema>
    ):Serializable{
        data class Cinema(
            val active: Boolean,
            val address1: String,
            val address2: String,
            val alerttxt: String,
            val allowOnlineVoucherValidation: Boolean,
            val allowPrintAtHomeBookings: Boolean,
            val city: String,
            val currencyCode: String,
            val description: String,
            val descriptionAlt: String,
            val displaySofaSeats: Boolean,
            val emailAddress: String,
            val giftStore: Boolean,
            val icon: String,
            val id: String,
            val latitude: String,
            val longitude: String,
            val loyaltyCode: String,
            val name: String,
            val nameAlt: String,
            val parkingInfo: String,
            val phoneNumber: String,
            val timeZoneId: String
        ):Serializable
    }
}