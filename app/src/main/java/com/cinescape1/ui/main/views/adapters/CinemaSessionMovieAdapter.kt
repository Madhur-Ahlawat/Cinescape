package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CSessionResponse
import com.cinescape1.ui.main.views.adapters.cinemaSessionAdapters.AdapterCinemaSessionScroll

class CinemaSessionMovieAdapter(
    var context: Context,
    private var showtimeList: ArrayList<CSessionResponse.Output.DaySession.Show>,
    val listener: AdapterCinemaSessionScroll,
    private var title: String,
    private var moviePos: Int,
    private var movieCinemaId: String

) : RecyclerView.Adapter<CinemaSessionMovieAdapter.MyViewHolderCinemaSession>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderCinemaSession {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.showtimes_time_dimensions_item, parent, false)
        return MyViewHolderCinemaSession(view)}

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderCinemaSession, position: Int) {
            val showtimeListItem = showtimeList[position]
            holder.textDimension.text = showtimeListItem.format
            holder.textMoviesCategory.text = showtimeListItem.showTime
            holder.textMoviesCategory.text = showtimeListItem.experience
            holder.textTimes.text = showtimeListItem.showTime
            holder.cardScroll.setOnClickListener {
                listener.onLocationShowClicked(showtimeList[position],title,position,moviePos,movieCinemaId)
            }
    }

    override fun getItemCount(): Int {
        return showtimeList.size
    }

    class MyViewHolderCinemaSession(view: View) : RecyclerView.ViewHolder(view) {
        var textTimes: TextView = view.findViewById(R.id.text_times)
        var textDimension: TextView = view.findViewById(R.id.text_dimension)
        var textMoviesCategory: TextView = view.findViewById(R.id.text_movies_category)
        var cardScroll: CardView = view.findViewById(R.id.card_scroll)
    }
}