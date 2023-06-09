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
import com.cinescape1.data.models.responseModel.CSessionResponse
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import com.cinescape1.utils.toast
import kotlinx.android.synthetic.main.showtime_day_date_item.view.*

class CinemaDayAdapter(
    var context: Activity, private var dayDateList: ArrayList<CSessionResponse.Output.Day>,
    var listener: RecycleViewItemClickListener, var listenerDay : TypeFaceDay
) : RecyclerView.Adapter<CinemaDayAdapter.MyViewHolderDayDate>() {
    var mContext: Activity = context
    private var rowIndex = 0
    private var backIndex = 0
    private var viewHide = 0

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
        viewHide = dayDateList.size-1

        listenerDay.onTypeFaceDay(holder.day, holder.date)

        if (position ==viewHide) {
            holder.views1.hide()
        }else{
            holder.views1.show()
        }


        if (rowIndex == position){
//            holder.consBackground.background = ContextCompat.getDrawable(context, R.drawable.day_rectangle)
            holder.consBackground.setBackgroundResource(R.drawable.day_rectangle)
            holder.day.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.date.setTextColor(ContextCompat.getColor(context, R.color.white))
            val heavy: Typeface = context.resources.getFont(R.font.sf_pro_text_heavy)
            val bold: Typeface = context.resources.getFont(R.font.sf_pro_text_semibold)
            val regular: Typeface = context.resources.getFont(R.font.sf_pro_text_regular)
            holder.day.textSize = 13f
            holder.date.textSize = 20f
            holder.day.typeface = regular
            holder.date.typeface = regular

        } else {
//            holder.views1.show()
            holder.consBackground.setBackgroundResource(R.drawable.day_un_select_rectangle)
            holder.day.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.date.setTextColor(ContextCompat.getColor(context, R.color.white))
            val regular: Typeface = context.resources.getFont(R.font.sf_pro_text_regular)
            holder.day.includeFontPadding = false
            holder.day.typeface = regular
            holder.date.typeface = regular
            holder.day.textSize = 13f
            holder.date.textSize = 18f
        }

        when (backIndex) {
            position + 1 -> {
                holder.views1.visibility = View.INVISIBLE
            }
            position -> {
                holder.views1.visibility = View.INVISIBLE
            }
        }

        if (dayDateItem.enable) {

            holder.itemView.setOnClickListener {
                listener.onMovieDateClick(dayDateItem,holder.itemView,position)
                rowIndex = position
                backIndex = position
                notifyDataSetChanged()
            }

        } else {
//            holder.views1.show()
            holder.consBackground.setBackgroundResource(R.drawable.day_un_select_rectangle)
//            holder.day.setTextColor(ContextCompat.getColor(context, R.color.disableColor))
//            holder.date.setTextColor(ContextCompat.getColor(context, R.color.disableColor))

            holder.day.setTextColor(ContextCompat.getColor(context, R.color.lineColor))
            holder.date.setTextColor(ContextCompat.getColor(context, R.color.lineColor))


            val regular: Typeface = context.resources.getFont(R.font.sf_pro_text_regular)
            holder.day.typeface = regular
            holder.date.typeface = regular
            holder.day.textSize = 13F
            holder.date.textSize = 16F
        }

//        holder.itemView.setOnClickListener {
//            listener.onMovieDateClick(dayDateItem,holder.itemView,position)
//            rowIndex = position
//            backIndex = position
//            notifyDataSetChanged()
//        }

    }

    override fun getItemCount(): Int {
        return  dayDateList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface RecycleViewItemClickListener {
        fun onMovieDateClick(dayDateItem: CSessionResponse.Output.Day, itemView: View, position: Int)

    }

    class MyViewHolderDayDate(view: View) : RecyclerView.ViewHolder(view) {
        var day: TextView = view.text_day
        var date: TextView = view.text_date
        var views1: View = view.viewLine
        var consBackground: View = view.consBackground
    }

    interface TypeFaceDay{
       fun onTypeFaceDay(day: TextView, date: TextView)
    }

}
