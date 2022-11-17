package com.cinescape1.data.models.responseModel

import java.io.Serializable


data class HomeDataResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
):Serializable{
    data class Output(
        val homeOnes: ArrayList<HomeOne>,
    ):Serializable
    data class HomeOne(
        val cinemas: ArrayList<Cinema>,
        val count: Int,
        val key: String,
        val movieData: ArrayList<MovieData>,
        val name: String,
        val namealt: String,
        val offers: ArrayList<Offer>,
        val priority: Int,
        val type: String
    ):Serializable
    data class Offer(
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
    ):Serializable
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
        val descriptionAlt: Any,
        val displaySofaSeats: Boolean,
        val emailAddress: String,
        val giftStore: Boolean,
        val icon: String,
        val appImageUrl: String,
        val appThumbImageUrl: String,
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
    data class MovieData(
        val cast: List<Cast>,
        val comingSoon: Boolean,
        val director: Director,
        val distributorName: String,
        val genre: String,
        val genreId: String,
        val genreId2: String,
        val genreId3: Any,
        val hOFilmCode: String,
        val id: String,
        val mlanguage: String,
        val mobimgbig: String,
        val mobimgsmall: String,
        val tag: String,
        val tagColor: String,
        val sliderimgurl: String,
        val mobadvance: String,
        val openingDate: String,
        val producer: Any,
        val rating: String,
        val ratingColor: String,
        val ratingDescription: String,
        val runTime: Int,
        val scheduledAtCinema: Boolean,
        val synopsis: String,
        val synopsisAlt: String,
        val title: String,
        val titleAlt: String,
        val trailerUrl: String,
        val webimgbig: Any,
        val webimgsmall: Any
    ):Serializable
    data class Cast(
        val firstName: String,
        val id: String,
        val lastName: String,
        val personType: String,
        val urlToDetails: String,
        val urlToPicture: String
    ):Serializable
    data class Director(
        val firstName: String,
        val id: String,
        val lastName: String,
        val personType: String,
        val urlToDetails: String,
        val urlToPicture: String
    ):Serializable
    data class Offers(
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
    ):Serializable
}