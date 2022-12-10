package com.cinescape1.ui.main.views.adapters.cinemaSessionAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.cinescape1.ui.main.views.details.ShowTimesActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.hide
import com.cinescape1.utils.show

class AdapterCinemaSessionScroll(
    private val context: Context,
    private var cinemaSessionList: ArrayList<CSessionResponse.Output.DaySession>,
    private val listener: LocationListener, var listenerSession : TypeFaceSession
) :
    RecyclerView.Adapter<AdapterCinemaSessionScroll.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
            showtimeListItem.movie.language + " | " + showtimeListItem.movie.runTime + " min."
        holder.cateogry.text = showtimeListItem.movie.rating
        holder.genre.text = showtimeListItem.movie.genre

        listenerSession.onTypeFaceSession(holder.name, holder.genre, holder.cateogry, holder.duration,holder.tag)


        val ratingColor=showtimeListItem.movie.ratingColor
        holder.cateogry.setBackgroundColor(Color.parseColor(ratingColor))

        Glide.with(context)
            .load(showtimeListItem.movie.mobimgsmall)
            .error(R.drawable.pos_not_avilbale)
            .into(holder.image)


        if (showtimeListItem.movie.tag == "") {
            holder.background.hide()
            holder.tag.hide()
        } else {
            holder.background.show()
            holder.tag.show()
            holder.tag.text = showtimeListItem.movie.tag+"           "
            val tagColor = showtimeListItem.movie.tagColor
            holder.background.setColorFilter(Color.parseColor(tagColor))
        }

        holder.image.setOnClickListener {
            val intent = Intent(context, ShowTimesActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID, showtimeListItem.movie.id)
            holder.image.context.startActivity(intent)
        }

//        holder.name.isSelected = true
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
        movieCinemaId: String,
        showTime: String
    ) {
        listener.onShowClicked(show, name, position, cinemaPos, movieCinemaId,showTime)
    }

    interface LocationListener {
        fun onShowClicked(
            show: CSessionResponse.Output.DaySession.Show,
            name: String,
            position: Int,
            cinemaPos: Int,
            movieCinemaId: String,
            showTime: String
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
        var background: ImageView = view.findViewById(R.id.imageView60)
        var tag: TextView = view.findViewById(R.id.tag)

        val recyclerView =
            view.findViewById(R.id.recylerview_cinema_session_timing_dimension) as RecyclerView
    }

    interface TypeFaceSession{
        fun onTypeFaceSession(name: TextView,genre: TextView,cateogry: TextView, duration: TextView, tag: TextView)
    }

}