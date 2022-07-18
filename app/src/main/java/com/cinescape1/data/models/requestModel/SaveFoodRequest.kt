package com.cinescape1.data.models.requestModel

 class SaveFoodRequest{
     lateinit var concessionFoods: ArrayList<ConcessionFood>
     lateinit var transid: String
     lateinit var cinemaId: String
     lateinit var booktype: String
     lateinit var userId: String
     lateinit var sessionId: String


     open class ConcessionFood{
         lateinit var deliveryOption: String
         lateinit var headOfficeItemCode: String
         lateinit var description: String
         lateinit var itemImageUrl: String
         lateinit var descriptionAlt: String
         lateinit var id: String
         var finalPriceInCents: String = ""
         lateinit var priceInCents: String
         lateinit var itemId: String
         lateinit var itemType: String
         lateinit var itemPrice: String
         lateinit var items: ArrayList<Item>
         lateinit var modifiers: ArrayList<Modifier>
         lateinit var parentId: String
         var quantity: Int=0
         var ModifiersText: String = ""

     }

    class Item{
         var deliveryOption: Int=0
        lateinit var headOfficeItemCode: String
        lateinit var description: String
        lateinit var descriptionAlt: String
        lateinit var finalPriceInCents: String
        lateinit var priceInCents: String
        lateinit var itemId: String
        lateinit var itemType: String
        lateinit var itemPrice: String
        lateinit var modifiers: ArrayList<Modifier>
        lateinit var parentId: String
        var quantity: Int =0
    }

     class Modifier{
        lateinit var modifierItemId: String
        var quantity: Int=0
    }
}