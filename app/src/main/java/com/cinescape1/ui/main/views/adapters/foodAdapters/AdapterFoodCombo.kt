package com.cinescape1.ui.main.views.adapters.foodAdapters

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
import com.cinescape1.data.models.responseModel.GetFoodResponse
import com.cinescape1.utils.hide
import com.cinescape1.utils.show

class AdapterFoodCombo(
    context: Context, private var foodComboList: ArrayList<GetFoodResponse.ConcessionItem>,
    private val listener: RecycleViewItemClickListener, val type: String,
    var listener1 : TypeFaceListenerFoodCombo
) :
    RecyclerView.Adapter<AdapterFoodCombo.MyViewHolderFoodCombo>() {

    private var mContext = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFoodCombo {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_selected_combo_item, parent, false)
        return MyViewHolderFoodCombo(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderFoodCombo, position: Int) {
        val foodSelectedItem = foodComboList[position]
        if (type == "combo") {
            foodSelectedItem.description
//            foodSelectedItem.title = "Combo " + (position + 1)
            holder.foodcomboName.text =foodSelectedItem.title
            holder.foodTitleName.text = foodSelectedItem.description
            holder.foodTitleName.show()
        } else {
            holder.foodcomboName.text = foodSelectedItem.description
            holder.foodTitleName.hide()
        }
        holder.foodKdName.text = foodSelectedItem.itemPrice
        holder.foodKdName.show()

        if (foodSelectedItem.quantityUpdate>0){
            holder.totalItems.show()
            holder.totalItems.text = foodSelectedItem.quantityUpdate.toString()+" "+mContext.getString(R.string.ItemAdded)
        }else{
            holder.totalItems.hide()
        }

        Glide
            .with(mContext)
            .load(foodSelectedItem.itemImageUrl)
            .placeholder(R.drawable.food_placeholder)
            .into(holder.imgFood)

        holder.addBtn.setOnClickListener {
            listener.onAddFood(foodSelectedItem, position,foodComboList)
        }

        holder.btnIncrease.setOnClickListener {
            listener.onIncreaseFood(foodSelectedItem, position)
        }

        holder.btnDecrease.setOnClickListener {
            listener.onDecreaseFood(foodSelectedItem, position)
        }

        listener1.onTypeFaceFoodCombo(holder.foodTitleName, holder.foodcomboName,
            holder.foodKdName, holder.addBtn, holder.btnDecrease, holder.btnIncrease, holder.totalItems)
    }

    override fun getItemCount(): Int {
        return foodComboList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newFoodSelectable: ArrayList<GetFoodResponse.ConcessionItem>) {
        println("notifyDataSetChanged-----")
        foodComboList = newFoodSelectable
        notifyDataSetChanged()
    }

    interface RecycleViewItemClickListener {
        fun onAddFood(foodItem: GetFoodResponse.ConcessionItem, position: Int,foodComboList: ArrayList<GetFoodResponse.ConcessionItem>)
        fun onDecreaseFood(foodItem: GetFoodResponse.ConcessionItem, position: Int)
        fun onIncreaseFood(foodItem: GetFoodResponse.ConcessionItem, position: Int)
    }

    class MyViewHolderFoodCombo(view: View) : RecyclerView.ViewHolder(view) {
        var foodTitleName: TextView = view.findViewById(R.id.text_food_name)
        var foodcomboName: TextView = view.findViewById(R.id.text_combo)
        var foodKdName: TextView = view.findViewById(R.id.text_kd)
        var addBtn: TextView = view.findViewById(R.id.text_add_btn)
        var btnDecrease: TextView = view.findViewById(R.id.text_decrease)
        var txtNumber: TextView = view.findViewById(R.id.text_number)
        var textIt1emAdded: TextView = view.findViewById(R.id.text_item_added)
        var btnIncrease: TextView = view.findViewById(R.id.text_increase)
        var totalItems: TextView = view.findViewById(R.id.textView107)
        var viewIncreaseDecrease: View = view.findViewById(R.id.view_increase_decrease)
        val imgFood = view.findViewById<View>(R.id.image_food) as ImageView
    }

    interface TypeFaceListenerFoodCombo {
        fun onTypeFaceFoodCombo(foodTitleName: TextView, foodcomboName: TextView,
                                     foodKdName: TextView,addBtn: TextView,btnDecrease: TextView,
                                btnIncrease: TextView,totalItems: TextView)
    }

}