package com.cinescape1.data.models.responseModel

data class MoreTabResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    data class Output(
        val cinemas: ArrayList<Cinema>,
        val faqs: ArrayList<Faq>,
        val privacy: ArrayList<Privacy>,
        val ratings: ArrayList<Rating>,
        val tncs: ArrayList<Tnc>
    )
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
    )

    data class Faq(
        val faqs: ArrayList<Faqs>,
        val name: String,
        val name_ar: String
    ){
        data class Faqs(
            val answer: String,
            val answer_ar: String,
            val id: Int,
            val name: String,
            val name_ar: String,
            val ques: String,
            val ques_ar: String
        )
    }

    data class Privacy(
        val colorCode: String,
        val description: String,
        val descriptionAlt: String,
        val id: Int,
        val moreType: String,
        val more_key: String,
        val name: String,
        val nameAlt: String
    )

    data class Rating(
        val colorCode: String,
        val description: String,
        val descriptionAlt: String,
        val id: Int,
        val moreType: String,
        val more_key: String,
        val ratingColor: String,
        val name: String,
        val nameAlt: String
    )

    data class Tnc(
        val colorCode: String,
        val description: String,
        val descriptionAlt: String,
        val id: Int,
        val moreType: String,
        val more_key: String,
        val name: String,
        val nameAlt: String
    )
}