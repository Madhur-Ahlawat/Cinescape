package com.cinescape1.data.models.responseModel

data class FoodItem(
    val description: String,
    val descriptionAlt: String,
    val headOfficeItemCode: String,
    val itemId: String,
    val itemImageUrl: String,
    val itemType: String,
    val items: List<Any>,
    val parentId: String,
    val priceInCents: Double,
    val quantity: Double
)