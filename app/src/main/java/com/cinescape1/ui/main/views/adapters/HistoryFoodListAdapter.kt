package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HistoryResponse
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import com.cinescape1.utils.toast


class HistoryFoodListAdapter(
    private  val context: Activity,
    private  val concessionFoods: ArrayList<HistoryResponse.Output.ConcessionFood>) :
    RecyclerView.Adapter<HistoryFoodListAdapter.TodoViewHolder>() {

    var displayMetrics = DisplayMetrics()


    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_items_history, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return concessionFoods.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val concessionFoods = concessionFoods[position]
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        if (position==0){
            holder.consUi.show()
        }else{
            holder.consUi.hide()
        }

        val lp = LinearLayout.LayoutParams(
            (width),
            LinearLayout.LayoutParams.WRAP_CONTENT
        ) // or set height to any fixed value you want

        holder.linearLayout.layoutParams = lp
//        holder.linearLayout.weightSum = 5f

        println("FoodDescription--->${concessionFoods.description}")

        holder.title.text=concessionFoods.description
        holder.qty.text=concessionFoods.quantity.toString()
//        holder.total.text=(concessionFoods.priceInCents/100).toString()+" "+ context.getString(R.string.price_kd)
        holder.total.text=(concessionFoods.priceInCents/100).toDouble().toString()
        holder.price.text=((concessionFoods.priceInCents*concessionFoods.quantity)/100).toDouble().toString()
//        holder.price.text=((concessionFoods.priceInCents*concessionFoods.quantity)/100).toString()+" "+context.getString(R.string.price_kd)
        holder.type.text=concessionFoods.itemType

    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.textView91)
        var qty: TextView = view.findViewById(R.id.textView92)
        var price: TextView = view.findViewById(R.id.textView94)
        var total: TextView = view.findViewById(R.id.textView93)
        var type: TextView = view.findViewById(R.id.textView95)
        var linearLayout: LinearLayout = view.findViewById(R.id.linearLayout3)
        var consUi: LinearLayout = view.findViewById(R.id.consUi)
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }

}