package com.cinescape1.data.models.responseModel

class FilterModel(
    var title: String,
    var type: Int,
    var dataList: ArrayList<*>,
    var selectedList: ArrayList<String>,
    var cinemaList: ArrayList<MoviesResponse.Cinema>?,
    var movieTimings: ArrayList< MoviesResponse.MovieTimings>?,
    var selectedMovie: ArrayList<MoviesResponse.MovieTimings>?,
    var selectedCinema: ArrayList< MoviesResponse.Cinema>?
)