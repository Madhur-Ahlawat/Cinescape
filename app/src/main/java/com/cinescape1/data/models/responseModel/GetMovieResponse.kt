package com.cinescape1.data.models.responseModel

import java.io.Serializable

data class GetMovieResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
):Serializable{
    data class Output(
        val movie: Movie,
        val similar: List<Similar>
    ):Serializable{
        data class Movie(
            val cast: List<Any>,
            val comingSoon: Boolean,
            val director: Director,
            val distributorName: String,
            val genre: String,
            val genreAlt: Any,
            val hOFilmCode: String,
            val id: String,
            val language: String,
            val languageAlt: String,
            val mobimgbig: Any,
            val mobimgsmall: Any,
            val movieCinemas: List<Any>,
            val movieExperience: List<Any>,
            val movieTimings: List<Any>,
            val openingDate: String,
            val producer: Any,
            val rating: String,
            val ratingDescription: String,
            val ratingDescriptionAlt: Any,
            val runTime: Int,
            val runTimeStr: String,
            val scheduledAtCinema: Boolean,
            val shareUrl: String,
            val sliderimgurl: Any,
            val subTitle: String,
            val subTitleAlt: String,
            val synopsis: String,
            val synopsisAlt: String,
            val title: String,
            val titleAlt: String,
            val trailerUrl: String,
            val webimgbig: Any,
            val webimgsmall: Any
        ):Serializable
        data class Similar(
            val cast: List<Cast>,
            val comingSoon: Boolean,
            val director: Director,
            val distributorName: String,
            val genre: String,
            val genreAlt: Any,
            val hOFilmCode: String,
            val id: String,
            val language: String,
            val languageAlt: String,
            val mobimgbig: String,
            val mobimgsmall: String,
            val movieCinemas: List<Any>,
            val movieExperience: List<Any>,
            val movieTimings: List<Any>,
            val openingDate: String,
            val producer: Any,
            val rating: String,
            val ratingDescription: String,
            val ratingDescriptionAlt: String,
            val runTime: Int,
            val runTimeStr: String,
            val scheduledAtCinema: Boolean,
            val shareUrl: String,
            val sliderimgurl: String,
            val subTitle: String,
            val subTitleAlt: String,
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
                val firstNameAlt: Any,
                val id: String,
                val lastName: String,
                val lastNameAlt: Any,
                val personType: String,
                val urlToDetails: String,
                val urlToPicture: String
            )
        }
        data class Director(
            val firstName: String,
            val firstNameAlt: Any,
            val id: String,
            val lastName: String,
            val lastNameAlt: Any,
            val personType: String,
            val urlToDetails: String,
            val urlToPicture: String
        )
    }
}