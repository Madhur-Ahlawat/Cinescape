package com.cinescape1.ui.main.views.adapters.showTimesAdapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CinemaSessionResponse

class AdpaterShowTimesCast(
    private val mContext: Activity,
    private var showTimesCasCastList: ArrayList<CinemaSessionResponse.Cast>,
    var listener :TypeFaceListenerShowTime ) :
    RecyclerView.Adapter<AdpaterShowTimesCast.MyViewHolderShowTimesCast>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderShowTimesCast {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_coming_soon_info_cast_item, parent, false)
        return MyViewHolderShowTimesCast(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderShowTimesCast, position: Int) {
            val comingSoonItem = showTimesCasCastList[position]
            holder.movieCastName.text = comingSoonItem.firstName + " " + comingSoonItem.lastName
            Glide.with(mContext)
                .load(comingSoonItem.urlToPicture)
                .placeholder(R.drawable.ic_back_button)
                .into(holder.imgCast)

        listener.onTypeFaceFoodShowTime(holder.movieCastName)
    }

    override fun getItemCount(): Int {
        return if (showTimesCasCastList.isNotEmpty()) showTimesCasCastList.size else 0
    }

    class MyViewHolderShowTimesCast(view: View) : RecyclerView.ViewHolder(view) {
        var movieCastName: TextView = view.findViewById(R.id.text_movie_cast_name)
        val imgCast = view.findViewById<View>(R.id.image_cast) as ImageView
    }

    interface TypeFaceListenerShowTime {
        fun onTypeFaceFoodShowTime(movieCastName: TextView)
    }

}