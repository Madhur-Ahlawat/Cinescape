package com.cinescape1.data.models.responseModel

data class GetFoodResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    data class Output(
        val bookingInfoId: Int,
        val transid: String,
        val booktype: String,
        val cinemaId: Any,
        val concessionTabs: List<ConcessionTab>,
        val foodQuantity: Int,
        val sessionId: Any,
        val status: Any,
        val userSessionId: Any
    )
    data class ConcessionTab(
        val concessionItems: ArrayList<ConcessionItem>,
        val id: Int,
        val name: String,
        val tabImageUrl: String
    )
    data class ConcessionItem(
        val addVisibility: Boolean,
        val alternateItems: List<AlternateItem>,
        var packageChildItems: ArrayList<ComboItem>,
        var description: String,
        val descriptionAlt: String,
        val flvourHeaderDesc: String,
        val foodtype: String,
        val foodtypeDisplay: String,
        var footItemCount: Int,
        var quantity: Int=0,
        var quantityUpdate: Int=0,
        val hOPK: Any,
        val headOfficeItemCode: String,
        val id: String,
        val itemDetailImageUrl: String,
        val itemImageUrl: String,
        val itemPopupImageUrl: String,
        val itemPrice: String,
        val itemPrice_dummy: Any,
        var itemTotal: Int,
        val modifierGroups: List<ModifierGroup>,
        val priceInCents: Int,
        val priceTextVisibility: Boolean,
        val tabImageUrl: Any,
        var title: String=""

    ) {
    }

    data class AlternateItem(
        val description: String,
        val descriptionAlt: Any,
        val extendedDescription: Any,
        val extendedDescriptionAlt: Any,
        val hOPK: String,
        val headOfficeItemCode: String,
        val id: String,
        val itemPrice: String,
        val itemPrice_dummy: String,
        val modifierGroups: List<ModifierGroup>,
        val priceInCents: Int,
        var subItemCount: Int,
        var checkFlag: Boolean=false,
        var itemTotal: String=""

    ) {
    }

    data class ModifierGroup(
        val Description: String,
        val Descriptionalt: Any,
        val FreeQuantity: Int,
        val Id: String,
        var selected:Boolean,
        val MaximumQuantity: Int,
        val MinimumQuantity: Int,
        val Modifiers: List<Modifiers>
    )
    data class Modifiers(var description: String,var checkFlag: Boolean=false,val id: String) {


    }

    data class ComboItem(
        val description: String,
        val descriptionAlt: Any,
        val headOfficeItemCode: Any,
        val id: String,
        val alternateItems: ArrayList<Item>,
        val parentId: String,
        val quantity: Int
    )

    data class Item(
        val description: String,
        val descriptionAlt: String,
        val extendedDescription: Any,
        val headOfficeItemCode: String,
        val id: String,
        var selected:Boolean,
        val priceInCents: Int,
        var checkFlag: Boolean=false

    ) {
    }

    class FoodDtls {
        lateinit var foodName: String
        lateinit var foodModifiers: String
        var foodQuan = 0
        var title = ""
        lateinit var foodId: String
        lateinit var foodModifierId: String
        var foodAmount = 0.0
        lateinit var foodUrl: String
        lateinit var foodType: String
        var itemPrice = 0.0
    }
}