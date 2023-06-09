package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoreTabResponse
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.utils.hide
import com.cinescape1.utils.show

class AgeRatingAdapter(private val ratingList: ArrayList<MoreTabResponse.Rating>,
                       private val context: Context, var listener: TypefaceListenerAgeRating) :
    RecyclerView.Adapter<AgeRatingAdapter.TodoViewHolder>() {

    private val dataList: ArrayList<String> = ArrayList()
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_agerules_list, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ratingList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val obj = ratingList[position]
        dataList.add(obj.toString())
        holder.todoTitle.text = obj.name
        holder.todoDesc.text = obj.description

        listener.onTypefaceAgeRating(holder.type,holder.todoTitle,holder.todoDesc)

        if (obj.more_key==""){
            holder.type.hide()
            holder.cardView.hide()
        }else{
            holder.cardView.show()
            holder.type.text = obj.more_key
            holder.type.show()
            val colorCode =obj.colorCode

            println("colorCode---->${colorCode}")
            holder.type.setBackgroundColor(Color.parseColor(colorCode))

//            holder.type.setBackgroundColor(Color.parseColor(obj.ratingColor))

//            when (obj.more_key) {
//                "PG" -> {
//                    holder.type.setBackgroundResource(R.color.grey)
//                }
//                "G" -> {
//                    holder.type.setBackgroundResource(R.color.green)
//                }
//                "18+" -> {
//                    holder.type.setBackgroundResource(R.color.red)
//                }
//                "13+" -> {
//                    holder.type.setBackgroundResource(R.color.yellow)
//                }
//                "15+" -> {
//                    holder.type.setBackgroundResource(R.color.yellow)
//                }
//                "E" -> {
//                    holder.type.setBackgroundResource(R.color.wowOrange)
//
//                }
//                "T" -> {
//                    holder.type.setBackgroundResource(R.color.tabIndicater)
//
//                }
//                else -> {
//                    holder.type.setBackgroundResource(R.color.blue)
//
//                }
//            }

        }
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var type: TextView = view.findViewById(R.id.textView39) as TextView
        var todoTitle: TextView = view.findViewById(R.id.textView40) as TextView
        var todoDesc: TextView = view.findViewById(R.id.textView41) as TextView
        var cardView: CardView = view.findViewById(R.id.cardView) as CardView
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }

    interface TypefaceListenerAgeRating {
        fun onTypefaceAgeRating(type: TextView,todoTitle: TextView, todoDesc: TextView)
    }

}