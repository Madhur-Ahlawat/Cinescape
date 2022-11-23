package com.cinescape1.ui.main.views.adapters.showTimesAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CinemaSessionResponse
import com.cinescape1.ui.main.views.adapters.cinemaSessionAdapters.AdapterCinemaSessionDimension
import com.cinescape1.utils.hide
import com.cinescape1.utils.show

class AdapterShowTimesCinemaTitle(
    private var context: Context,
    private var showTimeTitleList: List<CinemaSessionResponse.ExperienceSession>,
    val listener: CinemaAdapterListener, var typeFaceListener : TypeFaceItem
) : RecyclerView.Adapter<AdapterShowTimesCinemaTitle.MyViewHolderShowTimesTitle>(),
    AdapterCinemaSessionDimension.SessionAdapterListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderShowTimesTitle {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.show_times_title_item, parent, false)
        return MyViewHolderShowTimesTitle(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderShowTimesTitle, position: Int) {
        val showtimeListItem = showTimeTitleList[position]
        holder.textTitle.text = showtimeListItem.experience
        holder.textTitle.hide()
        holder.imageCinema.show()

        typeFaceListener.onTypeFaceCinemaTittle(holder.textTitle)
        println("image--->${showtimeListItem.experienceIcon}")
        try {
            Glide.with(context)
                .load(showtimeListItem.experienceIcon)
                .into(holder.imageCinema)
        } catch (e: Exception) {
            println("exception--->${e.message}")
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.recyclerShowTimeDimensions.layoutManager = layoutManager
        val adapter = AdapterCinemaSessionDimension(
            context,
            showtimeListItem.shows,
            this,
            showtimeListItem.experience,
            position
        )
        holder.recyclerShowTimeDimensions.adapter = adapter
    }

    override fun getItemCount(): Int {
        return if (showTimeTitleList.isNotEmpty()) showTimeTitleList.size else 0
    }

    override fun onShowClicked(
        show: CinemaSessionResponse.Show,
        name: String,
        position: Int,
        cinemaPos: Int
    ) {
        listener.onShowClicked(show, name, position, cinemaPos)
    }

    interface CinemaAdapterListener {
        fun onShowClicked(
            show: CinemaSessionResponse.Show,
            name: String,
            position: Int,
            cinemaPos: Int
        )
    }

    class MyViewHolderShowTimesTitle(view: View) : RecyclerView.ViewHolder(view) {
        var textTitle: TextView = view.findViewById(R.id.txt_title)
        var imageCinema: ImageView = view.findViewById(R.id.imageCinema)
        val recyclerShowTimeDimensions =
            view.findViewById<View>(R.id.recyler_time_dimension) as RecyclerView
    }

    interface TypeFaceItem{
        fun onTypeFaceCinemaTittle(textTitle: TextView)
    }

}