package com.cinescape1.ui.main.views.adapters.foodAdapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.GetFoodResponse
import com.cinescape1.utils.hide
import com.cinescape1.utils.show

class AdapterIndividual(
    context: Context,
    private var foodComboList: ArrayList<GetFoodResponse.ConcessionItem>,
    private val listener: RecycleViewItemClickListener, var listener1: TypeFaceListenerFoodIndividual
) :
    RecyclerView.Adapter<AdapterIndividual.MyViewHolderFoodCombo>() {
    private var mContext = context
    var quantity = 0
    var dialog: Dialog? = null
    var displayMetrics = DisplayMetrics()
    private var screenWidth = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFoodCombo {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.individual_food_item, parent, false)
        return MyViewHolderFoodCombo(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderFoodCombo, position: Int) {
        val foodSelectedItem = foodComboList[position]
        holder.foodTitleName.hide()
        holder.textItemAdded.show()

        holder.foodcomboName.text = foodSelectedItem.description
        if (foodSelectedItem.quantity > 0) {
            holder.viewIncreaseDecrease.show()
            holder.addBtn.hide()
            holder.txtNumber.text = foodSelectedItem.quantity.toString()
            holder.textItemAdded.text = foodSelectedItem.quantity.toString() + " item added"
        } else {
            holder.textItemAdded.text = ""
            holder.viewIncreaseDecrease.hide()
            holder.addBtn.show()
        }
        holder.foodKdName.text = foodSelectedItem.itemPrice
        holder.foodKdName.show()

        Glide.with(mContext)
            .load(foodSelectedItem.itemImageUrl)
            .placeholder(R.drawable.movie_default)
            .into(holder.imgFood)

        holder.addBtn.setOnClickListener {
            holder.addBtn.hide()
            holder.txtNumber.show()
            holder.btnDecrease.show()
            holder.btnIncrease.show()
            holder.viewIncreaseDecrease.show()
            listener.onAddIndiVidual(foodSelectedItem, position)
        }

        holder.btnIncrease.setOnClickListener {
            listener.onIncreaseIndiVidual(foodSelectedItem, position)
        }

        holder.btnDecrease.setOnClickListener {
            listener.onDecreaseIndiVidual(foodSelectedItem, position)
        }

        listener1.onTypeFaceFoodIndividual(holder.foodTitleName,holder.foodcomboName,
            holder.foodKdName, holder.btnDecrease, holder.txtNumber, holder.textItemAdded,
            holder.btnIncrease)

    }

    override fun getItemCount(): Int {
        return foodComboList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newFoodSelectors: ArrayList<GetFoodResponse.ConcessionItem>) {
        println("newFoodSelectors---$newFoodSelectors")
        foodComboList = newFoodSelectors
        notifyDataSetChanged()
    }


    interface RecycleViewItemClickListener {
        fun onAddIndiVidual(foodItem: GetFoodResponse.ConcessionItem, position: Int)
        fun onDecreaseIndiVidual(foodItem: GetFoodResponse.ConcessionItem, position: Int)
        fun onIncreaseIndiVidual(foodItem: GetFoodResponse.ConcessionItem, position: Int)
    }

    class MyViewHolderFoodCombo(view: View) : RecyclerView.ViewHolder(view) {
        var foodTitleName: TextView = view.findViewById(R.id.text_food_name)
        var foodcomboName: TextView = view.findViewById(R.id.text_combo)
        var foodKdName: TextView = view.findViewById(R.id.text_kd)
        var addBtn: CardView = view.findViewById(R.id.text_add_btn)
        var btnDecrease: TextView = view.findViewById(R.id.text_decrease)
        var txtNumber: TextView = view.findViewById(R.id.text_number)
        var textItemAdded: TextView = view.findViewById(R.id.text_item_added)
        var btnIncrease: TextView = view.findViewById(R.id.text_increase)
        var viewIncreaseDecrease: View = view.findViewById(R.id.view_increase_decrease)
        val imgFood = view.findViewById<View>(R.id.image_food) as ImageView
    }

    interface TypeFaceListenerFoodIndividual {
        fun onTypeFaceFoodIndividual(foodTitleName: TextView, foodcomboName: TextView,
                               foodKdName: TextView,btnDecrease: TextView,
                               txtNumber: TextView,textItemAdded: TextView, btnIncrease: TextView)
    }

}