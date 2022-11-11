package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CinemaSessionResponse
import com.cinescape1.utils.show
import kotlinx.android.synthetic.main.showtime_day_date_item.view.*

class AdapterDayDate(var context: Activity, private var dayDateList: ArrayList<CinemaSessionResponse.Days>,
     var listener: RecycleViewItemClickListener) : RecyclerView.Adapter<AdapterDayDate.MyViewHolderDayDate>() {
     var mContext: Activity = context
     private var rowIndex = 0
     private var backIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderDayDate {
        Log.d(ContentValues.TAG, ".onCreateViewHolder new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.showtime_day_date_item, parent, false)
        return MyViewHolderDayDate(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderDayDate, @SuppressLint("RecyclerView") position: Int) {
            val dayDateItem = dayDateList[position]
            holder.day.text = dayDateItem.wd
            holder.date.text = dayDateItem.d

            if (rowIndex == position){
                holder.consBackground.setBackgroundResource(R.drawable.day_rectangle)
                holder.day.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.date.setTextColor(ContextCompat.getColor(context, R.color.white))
                val heavy: Typeface = context.resources.getFont(R.font.sf_pro_text_heavy)
                val bold: Typeface = context.resources.getFont(R.font.sf_pro_text_semibold)
                holder.day.textSize = 13f
                holder.date.textSize = 20f
                holder.day.typeface = bold
                holder.date.typeface = heavy
            }
            else {
                holder.views1.show()
                holder.consBackground.setBackgroundResource(R.drawable.day_un_select_rectangle)
                holder.day.setTextColor(ContextCompat.getColor(context, R.color.hint_color))
                holder.date.setTextColor(ContextCompat.getColor(context, R.color.hint_color))
                val regular: Typeface = context.resources.getFont(R.font.sf_pro_text_regular)
                holder.day.typeface = regular
                holder.date.typeface = regular
                holder.day.textSize = 13F
                holder.date.textSize = 16F
            }

        when (backIndex) {
            position+1 -> {
                holder.views1.visibility = View.INVISIBLE
            }
            position -> {
                holder.views1.visibility = View.INVISIBLE
            }
            else -> {
                holder.views1.visibility = View.VISIBLE
            }
        }

        if (dayDateItem.enable){
            holder.itemView.setOnClickListener {
                listener.onDateClick(dayDateItem,holder.itemView,position)
                rowIndex = position
                backIndex = position
                notifyDataSetChanged()
            }
        }else{
            holder.views1.show()
            holder.consBackground.setBackgroundResource(R.drawable.primarydark_rectangle)
            holder.day.setTextColor(ContextCompat.getColor(context, R.color.countrySearch))
            holder.date.setTextColor(ContextCompat.getColor(context, R.color.countrySearch))
            val regular: Typeface = context.resources.getFont(R.font.sf_pro_text_regular)
            holder.day.typeface = regular
            holder.date.typeface = regular
            holder.day.textSize = 13F
            holder.date.textSize = 16F
        }



    }

    override fun getItemCount(): Int {
        return  dayDateList.size
    }

    interface RecycleViewItemClickListener {
        fun onDateClick(city: CinemaSessionResponse.Days,view: View,pos:Int)
    }

    class MyViewHolderDayDate(view: View) : RecyclerView.ViewHolder(view) {
        var day: TextView = view.text_day
        var date: TextView = view.text_date
        var views1: View = view.view
        var consBackground: View = view.consBackground
    }

}
