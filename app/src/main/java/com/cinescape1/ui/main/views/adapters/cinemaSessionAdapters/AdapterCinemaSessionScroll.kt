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
import com.cinescape1.ui.main.views.details.nowShowing.ShowTimesActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.LocaleHelper
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import com.haozhang.lib.SlantedTextView

class AdapterCinemaSessionScroll(
    private val context: Context,
    private var cinemaSessionList: ArrayList<CSessionResponse.Output.DaySession>,
    private val listener: LocationListener, var listenerSession : TypeFaceSession) :
    RecyclerView.Adapter<AdapterCinemaSessionScroll.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.showtimes_scroll_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val showtimeListItem = cinemaSessionList[position]
        try {

            Glide.with(context)
                .load(showtimeListItem.movie.mobimgsmall)
                .error(R.drawable.pos_not_avilbale)
                .into(holder.image)

            holder.cateogry.text = showtimeListItem.movie.rating
            holder.genre.text = showtimeListItem.movie.genre

            println("holder.genre.text21--->${holder.genre.text}")

            listenerSession.onTypeFaceSession(holder.name, holder.genre, holder.cateogry, holder.duration)

            val ratingColor=showtimeListItem.movie.ratingColor
            holder.cateogry.setBackgroundColor(Color.parseColor(ratingColor))

            if (showtimeListItem.movie.language.isNullOrEmpty()){
                holder.duration.text = "| " + showtimeListItem.movie.runTime + " min."
            }else{
                holder.duration.text = showtimeListItem.movie.language + " | " + showtimeListItem.movie.runTime + " min."
            }

            if (showtimeListItem.movie.title == null){
                println("showtimeListItem.movie.title -------->${showtimeListItem.movie.title}")
            }else{
                holder.name.text = showtimeListItem.movie.title
            }

        }catch (e : Exception){
            println("showtimeListItem.movie.titleError -------->${e.message}")
        }


        if (Constant.LANGUAGE == "ar"){
            LocaleHelper.setLocale(context, "ar")

            try {
                holder.background.setImageResource(R.drawable.ar_tab)
                holder.tag.setSlantedBackgroundColor(Color.parseColor(showtimeListItem.movie.tagColor))
                holder.tag.mode = SlantedTextView.MODE_RIGHT
                holder.tag.text = showtimeListItem.movie.tag
            }catch (e : Exception){
                e.printStackTrace()
            }


//            holder.tag.rotation = 30f
//            (holder.tag.layoutParams as ConstraintLayout.LayoutParams).apply {
//                marginStart=25
////                        topMargin=2
//                marginEnd=40
//                bottomMargin=40
//                holder.tag.text = showtimeListItem.movie.tag
////                        bottomMargin=8.dpToPixels()
//            }

        }else if (Constant.LANGUAGE == "en"){
            LocaleHelper.setLocale(context, "en")

            try {
                holder.background.setImageResource(R.drawable.en_tab)
                holder.tag.text = showtimeListItem.movie.tag
                holder.tag.setSlantedBackgroundColor(Color.parseColor(showtimeListItem.movie.tagColor))
                holder.tag.mode = SlantedTextView.MODE_LEFT
            }catch (e : Exception){
                e.printStackTrace()
            }


//            holder.tag.rotation = -30f
//            (holder.tag.layoutParams as ConstraintLayout.LayoutParams).apply {
//                marginStart=25
//                marginEnd=40
//                bottomMargin=40
//                holder.tag.text = showtimeListItem.movie.tag
//            }

        }


        if (showtimeListItem.movie.tag == "") {
//                    holder.background.hide()
            holder.tag.hide()
        } else {
//                    holder.background.show()
            holder.tag.show()
            holder.tag.text = showtimeListItem.movie.tag
            val tagColor = showtimeListItem.movie.tagColor
            holder.tag.setSlantedBackgroundColor(Color.parseColor(showtimeListItem.movie.tagColor))
//                    holder.background.setColorFilter(Color.parseColor(tagColor))
        }

//        if (showtimeListItem.movie.tag == "") {
//            holder.background.hide()
//            holder.tag.hide()
//        } else {
//            holder.background.show()
//            holder.tag.show()
//            holder.tag.text = showtimeListItem.movie.tag+"           "
//            val tagColor = showtimeListItem.movie.tagColor
//            holder.background.setColorFilter(Color.parseColor(tagColor))
//        }

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
        showTime: String) {
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
        var tag: SlantedTextView = view.findViewById(R.id.tag)
        val recyclerView =
            view.findViewById(R.id.recylerview_cinema_session_timing_dimension) as RecyclerView

    }

    interface TypeFaceSession{
        fun onTypeFaceSession(name: TextView,genre: TextView,cateogry: TextView, duration: TextView)
    }

}