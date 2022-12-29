package com.cinescape1.data.models.responseModel

import java.io.Serializable

data class CSessionResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
):Serializable{
    data class Output(
        val cinema: Cinema,
        val daySessions: ArrayList<DaySession>,
        val days: ArrayList<Day>
    ):Serializable{
        data class Cinema(
            val active: Boolean,
            val address1: String,
            val address2: String,
            val workingHours: String,
            val alerttxt: String,
            val allowOnlineVoucherValidation: Boolean,
            val allowPrintAtHomeBookings: Boolean,
            val appImageUrl: Any,
            val appThumbImageUrl: Any,
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
            val timeZoneId: String,
            val webImageUrl: Any,
            val webThumbImageUrl: Any
        ):Serializable
        data class Day(
            val d: String,
            val dt: String,
            val showdate: String,
            val wd: String,
            val enable:Boolean,
            val wdf: String
        ):Serializable
        data class DaySession(
            val movie: Movie,
            val showCount: Int,
            val shows: ArrayList<Show>
        ):Serializable{
            data class Show(
                val allocatedSeating: Boolean,
                val allowChildAdmits: Boolean,
                val allowComplimentaryTickets: Boolean,
                val allowTicketSales: String,
                val cinemaId: String,
                val cinemaOperatorCode: String,
                val experience: String,
                val experienceAlt: String,
                val format: String,
                val formatCode: String,
                val formatHOPK: String,
                val id: String,
                val priceGroupCode: String,
                val scheduledFilmId: String,
                val screenName: String,
                val screenNameAlt: String,
                val screenNumber: Int,
                val seatsAvailable: Int,
                val sessionBusinessDate: String,
                val sessionBusinessDateStr: String,
                val sessionDisplayPriority: Int,
                val sessionId: String,
                val showTime: String,
                val showtime: String,
                val soldoutStatus: Int,
                val typeCode: String,
                val premium: Boolean,
            ):Serializable
            data class Movie(
                val cast: List<Cast>,
                val comingSoon: Boolean,
                val director: Director,
                val distributorName: String,
                val genre: String,
                val genreId: String,
                val genreId2: Any,
                val genreId3: Any,
                val hOFilmCode: String,
                val id: String,
                val language: String,
                val languageAlt: String,
                val mobimgbig: String,
                val mobimgsmall: String,
                val openingDate: String,
                val producer: Any,
                val rating: String,
                val ratingColor: String,
                val tag: String,
                val tagColor: String,
                val ratingDescription: String,
                val runTime: Int,
                val scheduledAtCinema: Boolean,
                val synopsis: String,
                val synopsisAlt: String,
                val title: String,
                val titleAlt: String,
                val trailerUrl: String,
                val webimgbig: String,
                val webimgsmall: String
            ):Serializable{
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
            }
        }
    }

}