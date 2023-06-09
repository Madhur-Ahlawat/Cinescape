package com.cinescape1.ui.main.views.adapters.checkoutAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.TicketSummaryResponse
import com.cinescape1.utils.Constant


class AdapterCheckoutFoodItem ( context: Context, private var foodComboList: List<TicketSummaryResponse.ConcessionFood>) :
    RecyclerView.Adapter<AdapterCheckoutFoodItem.MyViewHolderCheckoutFoodItem>() {
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderCheckoutFoodItem {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.checkout_food_item, parent, false)
        return MyViewHolderCheckoutFoodItem(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderCheckoutFoodItem, position: Int) {
            val foodSelctedItem = foodComboList[position]
            var sub_item = foodSelctedItem.description
            foodSelctedItem.items.forEachIndexed {index,it ->
                var spacer:String = if(index<(foodSelctedItem.items.size-1)) " & " else ""
                sub_item= sub_item + " " + "( "+it.description+" )" + spacer  }
            holder.foodTitleName.text = foodSelctedItem.itemType
            holder.foodItemcomboName.text = sub_item
            val price = (foodSelctedItem.priceInCents*foodSelctedItem.quantity)/100.0
            holder.foodKd.text = mContext.getString(R.string.price_kd)+" " + Constant.DECIFORMAT.format(price)

            Glide.with(mContext).load(foodSelctedItem.itemImageUrl)
                .placeholder(R.drawable.app_icon)
                .into(holder.imgCheckoutFood)
        println("foodSelctedItem.itemType21-------->${foodSelctedItem.itemType}---->${foodSelctedItem.description}")
    }

    override fun getItemCount(): Int {
        return if (foodComboList.isNotEmpty()) foodComboList.size else 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newFoodSelectedlist: List<TicketSummaryResponse.ConcessionFood>) {
        foodComboList = newFoodSelectedlist
        notifyDataSetChanged()
    }

    class MyViewHolderCheckoutFoodItem(view: View) : RecyclerView.ViewHolder(view) {
        var foodTitleName: TextView = view.findViewById(R.id.text_food_item_names)
        var foodItemcomboName: TextView = view.findViewById(R.id.text_item_combo)
        var foodKd: TextView = view.findViewById(R.id.text_kd_food_item)
        val imgCheckoutFood = view.findViewById<View>(R.id.image_food_item) as ImageView

    }
}