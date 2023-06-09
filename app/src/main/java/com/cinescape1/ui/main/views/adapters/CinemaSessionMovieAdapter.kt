package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CSessionResponse
import com.cinescape1.ui.main.views.adapters.cinemaSessionAdapters.AdapterCinemaSessionScroll
import com.cinescape1.utils.hide
import com.cinescape1.utils.show

class CinemaSessionMovieAdapter(
    var context: Context,
    private var showtimeList: ArrayList<CSessionResponse.Output.DaySession.Show>,
    val listener: AdapterCinemaSessionScroll,
    private var title: String,
    private var moviePos: Int,
    private var movieCinemaId: String

) : RecyclerView.Adapter<CinemaSessionMovieAdapter.MyViewHolderCinemaSession>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderCinemaSession {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cinema_time, parent, false)
        return MyViewHolderCinemaSession(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderCinemaSession, position: Int) {
        val showtimeListItem = showtimeList[position]

        val lowerCase = showtimeListItem.experience.toLowerCase()
        val url = "https://s3.eu-west-1.amazonaws.com/cinescape.uat/experience/${lowerCase}.png"

        holder.textDimension.text = showtimeListItem.format
        holder.textTimes.text = showtimeListItem.showTime

//        holder.type.text=showtimeListItem.experience
//        Glide.with(context).load(url).into(holder.type)

        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.movie_default)
            .into(holder.type)


        if (!showtimeListItem.premium) {
            holder.textMoviesCategory.hide()
        } else {
            holder.textMoviesCategory.show()
        }
        holder.type.show()

        holder.cardScroll.setOnClickListener {
            listener.onLocationShowClicked(
                showtimeList[position],
                title,
                position,
                moviePos,
                movieCinemaId,
                showtimeListItem.showTime
            )
        }

    }

    override fun getItemCount(): Int {
        return showtimeList.size
    }

    class MyViewHolderCinemaSession(view: View) : RecyclerView.ViewHolder(view) {
        var textTimes: TextView = view.findViewById(R.id.text_times)
        var type: ImageView = view.findViewById(R.id.type)
        var textDimension: TextView = view.findViewById(R.id.text_dimension)
        var textMoviesCategory: TextView = view.findViewById(R.id.text_movies_category)
        var cardScroll: CardView = view.findViewById(R.id.card_scroll)
    }
}