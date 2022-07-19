package com.cinescape1.ui.main.views.adapters.seatlayout

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.SeatLayoutResponse
import com.cinescape1.utils.hide
import com.cinescape1.utils.show

class SeatLayoutCinemaSessionAdapter(
    var context: Context, private var showtimeList: List<SeatLayoutResponse.DaySession.Show>, val listener: SeatSessionAdapterListener, val name:String,
    private val cinemaPos:Int) : RecyclerView.Adapter<SeatLayoutCinemaSessionAdapter.MyViewHolderCinemaSession>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderCinemaSession {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cinema_time, parent, false)
        return MyViewHolderCinemaSession(view)}

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderCinemaSession, position: Int) {
            val showtimeListItem = showtimeList[position]
            holder.textDimension.text = showtimeListItem.format
        if (!showtimeListItem.premium){
            holder.textMoviesCategory.hide()
        }else{
            holder.textMoviesCategory.show()
        }

            holder.textTimes.text = showtimeListItem.showTime
            holder.cardScroll.setOnClickListener {
                listener.onSeatShowClicked(showtimeList[position],name,position,cinemaPos)
            }
    }

    override fun getItemCount(): Int {
        return showtimeList.size
    }

    interface SeatSessionAdapterListener {
        fun onSeatShowClicked(show: SeatLayoutResponse.DaySession.Show, name: String, position: Int, cinemaPos: Int)
    }

    class MyViewHolderCinemaSession(view: View) : RecyclerView.ViewHolder(view) {
        var textTimes: TextView = view.findViewById(R.id.text_times)
        var textDimension: TextView = view.findViewById(R.id.text_dimension)
        var textMoviesCategory: TextView = view.findViewById(R.id.text_movies_category)
        var cardScroll: CardView = view.findViewById(R.id.card_scroll)
    }
}