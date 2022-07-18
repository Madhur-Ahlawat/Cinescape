package com.cinescape1.data.models

data class ModelsComingSoon(
    val dateFilm: String?
)

data class ImgComingSoon(
    val img: Int?
)

data class MovieCastModel(
    val castName: String?,
    val imgOfCast: Int?
)

data class ModelsRefunded(
    val amount: String?,
    val amountKD: String?,
    val expiryDate: String?,
    val dateInfo: String?
)

data class ModelsMoreItems(
    val img: Int?,
    val dayNameMore: String?,
    val discountPrecentage: String?,
    val titleMore: String?,
    val descriptionsMore: String?
)

data class ModelFilterExpanded(
    val expTitle: String?
)

data class ModelSliderHome(
    var imgUrl: Int
)

data class ModelPreferenceCategory(
    var imgCate: Int,
    var cateTypeText: String,
    var count: Int
)
data class ModelPreferenceType(
    var seatType: String,
    var count: Int
)
data class ModelPreferenceExperience(
    var imgSeatExperience: Int,
    var count: Int
)

data class ModelPreferenceAgeRating(
    var seatAgeRating: String,
    var count: Int
)