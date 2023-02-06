package com.cinescape1.ui.main.views.adapters.seatlayout

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
import com.cinescape1.data.models.responseModel.SeatLayoutResponse

class SeatShowTimesCinemaAdapter(
    private var context: Context, private var showTimeTitleList: ArrayList<SeatLayoutResponse.DaySession.ExperienceSession>,
    val listener: SeatCinemaAdapterListener) : RecyclerView.Adapter<SeatShowTimesCinemaAdapter.MyViewHolderShowTimesTitle>(),
    SeatLayoutCinemaSessionAdapter.SeatSessionAdapterListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderShowTimesTitle {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.show_times_title_item, parent, false)
        return MyViewHolderShowTimesTitle(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderShowTimesTitle, position: Int) {

        val showtimeListItem = showTimeTitleList[position]
        holder.textTitle.text = showtimeListItem.experience
        println("CinemasTittleName------------>${showtimeListItem.experience} ---->yes")

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


        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        holder.recyclerShowTimeDimensions.layoutManager = layoutManager
        val adapter = SeatLayoutCinemaSessionAdapter(context,showtimeListItem.shows,this,showtimeListItem.experience,position)
        holder.recyclerShowTimeDimensions.adapter = adapter

        println("CinemasTittleName212------------>${showtimeListItem.shows} ---->yes")
    }

    override fun getItemCount(): Int {
        return if (showTimeTitleList.isNotEmpty()) showTimeTitleList.size else 0
    }

//    fun onSeatShowClicked(
//        show: SeatLayoutResponse.DaySession.Show,
//        name: String,
//        position: Int,
//        cinemaPos: Int
//    ) {
//        listener.onSeatShowClicked(show, name, position, cinemaPos)
//    }

    override fun onSeatShowClicked(
        show: SeatLayoutResponse.DaySession.Show,
        name: String,
        position: Int,
        cinemaPos: Int
    ) {
        listener.onSeatShowClicked(show, name, position, cinemaPos)
    }

    interface SeatCinemaAdapterListener {
        fun onSeatShowClicked(
            show: SeatLayoutResponse.DaySession.Show,
            name: String,
            position: Int,
            cinemaPos: Int
        )
    }

    class MyViewHolderShowTimesTitle(view: View) : RecyclerView.ViewHolder(view) {
        var textTitle: TextView = view.findViewById(R.id.txt_title)
        var imageCinema: ImageView = view.findViewById(R.id.imageCinema)
        val recyclerShowTimeDimensions = view.findViewById<View>(R.id.recyler_time_dimension) as RecyclerView
    }


}