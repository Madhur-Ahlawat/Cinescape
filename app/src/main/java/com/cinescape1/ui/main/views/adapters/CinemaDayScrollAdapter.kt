package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CSessionResponse
import com.cinescape1.data.models.responseModel.CinemaSessionResponse
import com.cinescape1.ui.main.views.adapters.cinemaSessionAdapters.AdapterCinemaSessionScroll
import kotlinx.android.synthetic.main.showtime_day_date_item.view.*


class CinemaDayScrollAdapter(
    var context: Activity, private var dayDateList: ArrayList<CSessionResponse.Output.DaySession.Show>,
    var listener: AdapterCinemaSessionScroll
) : RecyclerView.Adapter<CinemaDayScrollAdapter.MyViewHolderDayDate>() {
    var mContext: Activity = context
    private var rowIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderDayDate {
        Log.d(ContentValues.TAG, ".onCreateViewHolder new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.showtime_day_date_item, parent, false)
        return MyViewHolderDayDate(view)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderDayDate, @SuppressLint("RecyclerView") position: Int) {

            val dayDateItem = dayDateList[position]
            holder.day.text = dayDateItem.screenName
            holder.date.text = dayDateItem.scheduledFilmId

            if (rowIndex == position){
                holder.consBackground.background = ContextCompat.getDrawable(context, R.drawable.red_rectangle)
                holder.day.setTextColor(ContextCompat.getColor(context, R.color.white))
//                holder.views1.show()

            }else if (position==dayDateList.size){
//                holder.views1.hide()
            }
            else {
                holder.consBackground.background = ContextCompat.getDrawable(context, R.drawable.primarydark_rectangle)
                holder.day.setTextColor(ContextCompat.getColor(context, R.color.text_color))
//                holder.views1.hide()
            }

            holder.itemView.setOnClickListener {
//                listener.onMovieDateClick(dayDateItem,holder.itemView,position)
                rowIndex = position
                notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return  dayDateList.size
    }

    interface RecycleViewItemClickListener {
        fun onMovieDateClick(city: CinemaSessionResponse.Days, view: View, pos:Int)
    }

    class MyViewHolderDayDate(view: View) : RecyclerView.ViewHolder(view) {
        var day: TextView = view.text_day
        var date: TextView = view.text_date
        var consBackground: View = view.consBackground
    }

}
