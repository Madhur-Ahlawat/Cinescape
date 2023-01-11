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
    val listener: CinemaAdapterListener,
    var typeFaceListener: TypeFaceItem
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
        println("checkCategory--->${showtimeListItem.experience}")

        try {
            when (showtimeListItem.experience) {
                "4DX" -> {
                    Glide.with(context).load(R.drawable.fourdx_gray).into(holder.imageCinema)
                }
                "Standard" -> {
                    Glide.with(context).load(R.drawable.standard_gray).into(holder.imageCinema)
                }
                "VIP" -> {
                    Glide.with(context).load(R.drawable.vip_gray).into(holder.imageCinema)
                }
                "IMAX" -> {
                    Glide.with(context).load(R.drawable.imax_gray).into(holder.imageCinema)
                }
                "3D" -> {
                    Glide.with(context).load(R.drawable.threed_gray).into(holder.imageCinema)
                }
                "DOLBY" -> {
                    Glide.with(context).load(R.drawable.dolby_gray).into(holder.imageCinema)
                }
                "ELEVEN" -> {
                    Glide.with(context).load(R.drawable.eleven_gray).into(holder.imageCinema)
                }
                "SCREENX" -> {
                    Glide.with(context).load(R.drawable.screenx_gray).into(holder.imageCinema)
                }
                "PREMIUM" -> {
                    Glide.with(context).load(R.drawable.premium_gray).into(holder.imageCinema)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.recyclerShowTimeDimensions.layoutManager = layoutManager
        val adapter = AdapterCinemaSessionDimension(
            context, showtimeListItem.shows, this, showtimeListItem.experience, position
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
        cinemaPos: Int,
        cinemaId: String,
        showTime: String
    ) {
        listener.onShowClicked(show, name, position, cinemaPos, cinemaId, showTime)
    }

    interface CinemaAdapterListener {
        fun onShowClicked(
            show: CinemaSessionResponse.Show,
            name: String,
            position: Int,
            cinemaPos: Int,
            cinemaId: String,
            showTime: String
        )
    }

    class MyViewHolderShowTimesTitle(view: View) : RecyclerView.ViewHolder(view) {
        var textTitle: TextView = view.findViewById(R.id.txt_title)
        var imageCinema: ImageView = view.findViewById(R.id.imageCinema)
        val recyclerShowTimeDimensions =
            view.findViewById<View>(R.id.recyler_time_dimension) as RecyclerView
    }

    interface TypeFaceItem {
        fun onTypeFaceCinemaTittle(textTitle: TextView)
    }

}