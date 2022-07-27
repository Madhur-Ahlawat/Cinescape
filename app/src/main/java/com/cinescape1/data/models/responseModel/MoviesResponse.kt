package com.cinescape1.data.models.responseModel

data class MoviesResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
) {
    data class Output(
        val comingsoon: ArrayList<Nowshowing>,
        val nowshowing: ArrayList<Nowshowing>,
        val alllanguages: ArrayList<String>,
        val cinemas: ArrayList<Cinema>?,
        val cslanguages: ArrayList<*>,
        val movieTimings: ArrayList<MovieTimings>,
        val experiences: ArrayList<*>,
        val genreList: ArrayList<*>,
        val nslanguages: ArrayList<String>,
        val ratings: ArrayList<String>
    )
    data class MovieTimings(
        val id:String,
        val name:String,
        val nameAlt:String,
        val timing:String,
        val timingAlt:String
    )
    data class Nowshowing(
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
        val language: String,
        val mobimgbig: Any,
        val mobimgsmall: Any,
        val openingDate: String,
        val producer: Any,
        val rating: String,
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
    )
    data class Director(
        val firstName: String,
        val id: String,
        val lastName: String,
        val personType: String,
        val urlToDetails: String,
        val urlToPicture: String
    )
    data class Cast(
        val firstName: String,
        val id: String,
        val lastName: String,
        val personType: String,
        val urlToDetails: String,
        val urlToPicture: String
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
        val descriptionAlt: Any,
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
    data class Rating(
        val image: String,
        val key: String,
        val value: String,
        val valueAlt: String
    )
}
