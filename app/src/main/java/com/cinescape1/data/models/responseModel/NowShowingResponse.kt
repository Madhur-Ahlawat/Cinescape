package com.cinescape1.data.models.responseModel

data class NowShowingResponse(
    val code: Int,
    val msg: String,
    val output: ArrayList<Output>,
    val result: String
){
    data class Output(
        val cast: List<Cast>,
        val comingSoon: Boolean,
        val director: Director,
        val distributorName: String,
        val genreId: String,
        val hOFilmCode: String,
        val id: String,
        val mlanguage: String,
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
}