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
import com.cinescape1.utils.Constant
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import kotlinx.android.synthetic.main.cart_items.view.*

class AdapterCart(
    context: Context,
    private var cartComboList: ArrayList<GetFoodResponse.FoodDtls>,
    var listener: RecycleViewItemClickListener) :
    RecyclerView.Adapter<AdapterCart.MyViewHolderCart>() {
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderCart {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_items, parent, false)
        return MyViewHolderCart(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderCart, position: Int) {
        val cartSelctedItem = cartComboList[position]

//        holder.cartFoodTitleName.text = cartSelctedItem.foodName+" ( "+cartSelctedItem.foodModifiers.removePrefix(", ")+" ) "
        holder.cartFoodTitleName.text = cartSelctedItem.foodName +cartSelctedItem.foodModifiers
        holder.cartKdPrice.text = mContext.getString(R.string.price_kd)+" ${Constant.DECIFORMAT.format((cartSelctedItem.foodAmount / 100.0))}"
        holder.textNumber.text = cartSelctedItem.foodQuan.toString()

        println("CartQtyNumber--------->${cartSelctedItem.foodName}---->${cartSelctedItem.foodModifiers}--->${cartSelctedItem.foodType}")

        if (cartSelctedItem.foodType=="combo"){
            holder.cartComboName.text = cartSelctedItem.title
//            holder.cartFoodTitleName.text = cartSelctedItem.foodName+" ( "+cartSelctedItem.foodModifiers.removePrefix(", ")+" ) "
            holder.cartFoodTitleName.show()
        }else{
//            holder.cartFoodTitleName.text = cartSelctedItem.foodName+" ( "+cartSelctedItem.foodModifiers.removePrefix(", ")+" ) "
            holder.cartComboName.text = cartSelctedItem.title
            holder.cartFoodTitleName.hide()
        }

        Glide.with(mContext).load(cartSelctedItem.foodUrl)
            .placeholder(R.drawable.placeholder_icon)
            .into(holder.imgCartItem)

        holder.textDecrease.setOnClickListener {
            listener.onDecreaseCart(cartSelctedItem, position)
        }

        holder.textIncrease.setOnClickListener {
            listener.onIncreaseCart(cartSelctedItem, position)
        }

        holder.viewRemove.setOnClickListener {
            listener.onRemoveCart(cartSelctedItem, position)
        }


    }

    override fun getItemCount(): Int {
        return cartComboList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newFoodSelectedlist: ArrayList<GetFoodResponse.FoodDtls>) {
        cartComboList = newFoodSelectedlist
        notifyDataSetChanged()
    }

    interface RecycleViewItemClickListener {
        fun onIncreaseCart(foodItem: GetFoodResponse.FoodDtls, pos: Int)
        fun onDecreaseCart(foodItem: GetFoodResponse.FoodDtls, pos: Int)
        fun onRemoveCart(foodItem: GetFoodResponse.FoodDtls, pos: Int)
    }

    class MyViewHolderCart(view: View) : RecyclerView.ViewHolder(view) {
        var cartFoodTitleName: TextView = view.tv_food_item_name
        var cartComboName: TextView = view.tv_combo
        var cartKdPrice: TextView = view.tvKD
        var viewRemove: View = view.viewRemove
        val imgCartItem: ImageView = view.img_cart_item
        var textDecrease: TextView = view.text_decrease
        var viewIncreaseDecrease: View = view.view_increase_decrease
        var textAddBtn: TextView = view.text_add_btn
        var textIncrease: TextView = view.text_increase
        var textNumber: TextView = view.text_number
    }

}