package com.cinescape1.ui.main.views.adapters.foodAdapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.GetFoodResponse

class AdapterFoodSelectedItem(
    var contextS: Activity,
    private var foodSelectList: List<GetFoodResponse.ConcessionTab>,
    var listener: RecycleViewItemClickListener, var listener2 : TypeFaceListenerFoodItem) :
    RecyclerView.Adapter<AdapterFoodSelectedItem.MyViewHolderFoodSelectedItem>() {
    var mContext: Activity = contextS
    var rowIndex = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFoodSelectedItem {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_selected_item, parent, false)
        return MyViewHolderFoodSelectedItem(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolderFoodSelectedItem, position: Int) {
        val foodSelctedItem = foodSelectList[position]
        holder.foodSelectItemName.text = foodSelctedItem.name
        Glide.with(mContext).load(foodSelctedItem.tabImageUrl).placeholder(R.drawable.app_icon).into(holder.imgFoodSelect)
        if (rowIndex == position) {
            holder.itemView.background = ContextCompat.getDrawable(contextS, R.drawable.red_rectangle_food)
            holder.imageArrowDown.visibility = View.VISIBLE
        } else {
            holder.itemView.background =
                ContextCompat.getDrawable(contextS, R.drawable.food_item_bg)
            holder.imageArrowDown.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            listener.onFoodCatClick(foodSelctedItem, holder.itemView)
            rowIndex = position
            notifyDataSetChanged()
        }

        listener2.onTypeFaceFoodItem(holder.foodSelectItemName)
    }

    override fun getItemCount(): Int {
        return if (foodSelectList.isNotEmpty()) foodSelectList.size else 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newFoodSelectedlist: List<GetFoodResponse.ConcessionTab>) {
        foodSelectList = newFoodSelectedlist
        notifyDataSetChanged()
    }

    interface RecycleViewItemClickListener {
        fun onFoodCatClick(foodItem: GetFoodResponse.ConcessionTab, view: View)
    }

    class MyViewHolderFoodSelectedItem(view: View) : RecyclerView.ViewHolder(view) {
        var foodSelectItemName: TextView = view.findViewById(R.id.text_food_item_name)
        val imgFoodSelect = view.findViewById<View>(R.id.image_food_select) as ImageView
        val imageArrowDown = view.findViewById<View>(R.id.image_arrow_down) as ImageView

    }


    interface TypeFaceListenerFoodItem {
        fun onTypeFaceFoodItem(foodSelectItemName: TextView)
    }


}