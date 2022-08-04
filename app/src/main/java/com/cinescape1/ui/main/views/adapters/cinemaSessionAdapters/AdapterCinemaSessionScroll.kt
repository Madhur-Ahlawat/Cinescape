package com.cinescape1.ui.main.views.adapters.cinemaSessionAdapters

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CSessionResponse
import com.cinescape1.ui.main.views.adapters.CinemaSessionMovieAdapter

class AdapterCinemaSessionScroll(
    private val context: Context,
    private var cinemaSessionList: ArrayList<CSessionResponse.Output.DaySession>,
    val listener: LocationListener
) :
    RecyclerView.Adapter<AdapterCinemaSessionScroll.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(ContentValues.TAG, ".onCreateViewHolder new view requested")
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.showtimes_scroll_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val showtimeListItem = cinemaSessionList[position]
        holder.name.text = showtimeListItem.movie.title
        holder.duration.text =
            showtimeListItem.movie.language + " | " + showtimeListItem.movie.runTime + "min."
        holder.cateogry.text = showtimeListItem.movie.rating
        holder.genre.text = showtimeListItem.movie.genre

        when (showtimeListItem.movie.rating) {
            "PG" -> {
                holder.cateogry.setBackgroundResource(R.color.grey)

            }
            "G" -> {
                holder.cateogry.setBackgroundResource(R.color.green)

            }
            "18+" -> {
                holder.cateogry.setBackgroundResource(R.color.red)

            }
            "13+" -> {
                holder.cateogry.setBackgroundResource(R.color.yellow)

            }
            "15+" -> {
                holder.cateogry.setBackgroundResource(R.color.yellow)

            }
            "E" -> {
                holder.cateogry.setBackgroundResource(R.color.wowOrange)

            }
            "T" -> {
                holder.cateogry.setBackgroundResource(R.color.tabIndicater)

            }
            else -> {
                holder.cateogry.setBackgroundResource(R.color.blue)
            }
        }

        Glide.with(context)
            .load(showtimeListItem.movie.mobimgsmall)
            .error(R.drawable.pos_not_avilbale)
            .into(holder.image)

        holder.name.isSelected = true
        val gridLayout = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        holder.recyclerView.layoutManager = LinearLayoutManager(context)
        val movieCinemaId = showtimeListItem.movie.id.toString()
        val adapter = CinemaSessionMovieAdapter(
            context,
            showtimeListItem.shows,
            this,
            showtimeListItem.movie.title,
            position,
            movieCinemaId
        )

        holder.recyclerView.layoutManager = gridLayout
        holder.recyclerView.adapter = adapter

    }

    fun onLocationShowClicked(
        show: CSessionResponse.Output.DaySession.Show,
        name: String,
        position: Int,
        cinemaPos: Int,
        movieCinemaId: String
    ) {
        listener.onShowClicked(show, name, position, cinemaPos, movieCinemaId)
    }

    interface LocationListener {
        fun onShowClicked(
            show: CSessionResponse.Output.DaySession.Show,
            name: String,
            position: Int,
            cinemaPos: Int,
            movieCinemaId: String
        )
    }

    override fun getItemCount(): Int {
        return if (cinemaSessionList.isNotEmpty()) cinemaSessionList.size else 0
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.image_showtimes)
        var name: TextView = view.findViewById(R.id.text_film_name)
        var genre: TextView = view.findViewById(R.id.genre)
        var cateogry: TextView = view.findViewById(R.id.text_category)
        var duration: TextView = view.findViewById(R.id.text_film_types_duration)
        val recyclerView =
            view.findViewById(R.id.recylerview_cinema_session_timing_dimension) as RecyclerView
    }
}