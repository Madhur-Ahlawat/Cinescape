package com.cinescape1.ui.main.views.adapters.checkoutAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.TicketSummaryResponse


class AdapterCheckoutFoodItem (context: Context, private var foodComboList: List<TicketSummaryResponse.ConcessionFood>) :
    RecyclerView.Adapter<AdapterCheckoutFoodItem.MyViewHolderCheckoutFoodItem>() {
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderCheckoutFoodItem {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.checkout_food_item, parent, false)
        return MyViewHolderCheckoutFoodItem(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderCheckoutFoodItem, position: Int) {
        if (foodComboList.isEmpty()) {
//            holder.title.setText(R.string.empty_photo)
        } else {
            val foodSelctedItem = foodComboList[position]
            holder.foodTitleName.text = foodSelctedItem.itemType
            holder.foodItemcomboName.text = foodSelctedItem.description
            holder.foodKd.text = foodSelctedItem.itemPrice.toString()
            holder.imgCheckoutFood.setImageResource(R.drawable.img_nachos_soda_combo)

        }
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