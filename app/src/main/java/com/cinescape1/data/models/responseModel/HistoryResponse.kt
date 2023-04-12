package com.cinescape1.data.models.responseModel

import java.io.Serializable

data class HistoryResponse(
    val code: Int,
    val msg: String,
    val output: ArrayList<Output>,
    val result: String
):Serializable{
    data class Output(
        val balance: String,
        val bookingId: String,
        val bookingTime: String,
        val bookingType: String,
        val cancelReserve: Boolean,
        val category: String,
        val categoryAlt: String,
        val cinemacode: String,
        val cinemaname: String,
        val clubEnable: Boolean,
        val concessionFoods: ArrayList<ConcessionFood>,
        val discount: String,
        val endDate: String,
        val endTime: String,
        val experience: String,
        val experienceAlt: String,
        val experienceIcon: String,
        val food: Int,
        val foodTotal: String,
        val format: String,
        val kioskId: String,
        val language: String,
        val mcensor: String,
        val moviename: String,
        val numofseats: String,
        val payDone: String,
        val payInfos: ArrayList<PayInfo>,
        val pickUpNumber: String,
        val pickupInfo: String,
        val poster: String,
        val posterhori: String,
        val qr: String,
        val ratingColor: String,
        val referenceId: String,
        val screenId: String,
        val seatsArr: List<String>,
        val showDate: String,
        val showDateTime: String,
        val showTime: String,
        val ticketPrice: String,
        val totalPrice: String,
        val totalTicketPrice: String,
        val totalTicketPriceInt: Int,
        val trackId: String,
        val trailerUrl: String,
        val transId: Int
    ):Serializable{
        data class ConcessionFood(
            val description: String,
            val descriptionAlt: String,
            val headOfficeItemCode: String,
            val itemId: String,
            val itemImageUrl: String,
            val itemPrice: String,
            val itemType: String,
            val items: ArrayList<Item>,
            val parentId: String,
            val priceInCents: Int,
            val quantity: Int,
            val title: String,
            val titleAlt: String
        ):Serializable{
            data class Item(
                val description: String,
                val descriptionAlt: String,
                val headOfficeItemCode: String,
                val itemId: String,
                val itemImageUrl: String,
                val itemPrice: Any,
                val itemType: String,
                val items: List<Any>,
                val parentId: String,
                val priceInCents: Int,
                val quantity: Int,
                val title: String,
                val titleAlt: String
            )
        }
        data class PayInfo(
            val amt: String,
            val highlight: Boolean,
            val key: String
        )
    }
}