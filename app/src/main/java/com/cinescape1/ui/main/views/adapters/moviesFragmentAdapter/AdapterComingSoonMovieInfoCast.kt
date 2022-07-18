package com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter

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
import com.cinescape1.data.models.MovieCastModel
import com.cinescape1.data.models.responseModel.MovieDetailResponse
import kotlinx.android.synthetic.main.activity_coming_soon_info.*


class AdapterComingSoonMovieInfoCast (private var mContext: Activity, private var comingSoonCastList: List<MovieDetailResponse.Cast>) :
    RecyclerView.Adapter<AdapterComingSoonMovieInfoCast.MyViewHolderComingSoonMovieInfoCast>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderComingSoonMovieInfoCast {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_coming_soon_info_cast_item, parent, false)
        return MyViewHolderComingSoonMovieInfoCast(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderComingSoonMovieInfoCast, position: Int) {
        val comingSoonItem = comingSoonCastList[position]
        if (comingSoonCastList.isEmpty()) {
//         holder.title.setText(R.string.empty_photo)
        } else {
            holder.movieCastName.text = comingSoonItem.firstName + " "+ comingSoonItem
            Glide.with(mContext).load(comingSoonItem.urlToPicture).placeholder(R.drawable.movie_default).into(holder.imgCast)
//            holder.imgCast.setImageResource(comingSoonItem.imgOfCast!!)

        }
    }

    override fun getItemCount(): Int {
        return if (comingSoonCastList.isNotEmpty()) comingSoonCastList.size else 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newPhotoslist: List<MovieDetailResponse.Cast>) {
        comingSoonCastList = newPhotoslist
        notifyDataSetChanged()
    }
    class MyViewHolderComingSoonMovieInfoCast(view: View) : RecyclerView.ViewHolder(view) {
        var movieCastName: TextView = view.findViewById(R.id.text_movie_cast_name)
        val imgCast = view.findViewById<View>(R.id.image_cast) as ImageView
    }

}