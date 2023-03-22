package com.cinescape1.data.models.responseModel

import java.io.Serializable

data class HistoryResponse(
    val code: Int,
    val msg: String,
    val output: ArrayList<Output>,
    val result: String
):Serializable{
    data class Output(
        val addFood: Boolean,
        val balance: String,
        val bookingId: String,
        val bookingType: String,
        val cancelReserve: Boolean,
        val cinemacode: String,
        val cinemaname: String,
        val clubEnable: Boolean,
        val concessionFoods: ArrayList<ConcessionFood>,
        val discount: String,
        val endDate: Any,
        val endTime: Any,
        val experience: String,
        val experienceAlt: String,
        val format: String,
        val kioskId: String,
        val language: String,
        val mcensor: String,
        val moviename: String,
        val numofseats: String,
        val payDone: String,
        val paymodes: Paymodes,
        val poster: String,
        val posterhori: String,
        val qr: String,
        val screenId: String,
        val category: String,
        val seatsArr: List<String>,
        val showDate: String,
        val showDateTime: String,
        val showTime: String,
        val ticketPrice: String,
        val totalPrice: String,
        val foodTotal: String,
        val totalTicketPrice: String,
        val trailerUrl: String,
        val transId: Int
    ):Serializable{
        data class ConcessionFood(
            val description: String,
            val descriptionAlt: String,
            val headOfficeItemCode: String,
            val itemId: String,
            val itemPrice: String,
            val itemType: String,
            val items: ArrayList<Item>,
            val parentId: String,
            val priceInCents: Int,
            val quantity: Int
        ):Serializable{
            data class Item(
                val description: String,
                val descriptionAlt: String,
                val headOfficeItemCode: String,
                val itemId: String,
                val itemPrice: Any,
                val itemType: String,
                val items: List<Any>,
                val parentId: String,
                val priceInCents: Int,
                val quantity: Int
            )
        }
        class Paymodes
    }
}