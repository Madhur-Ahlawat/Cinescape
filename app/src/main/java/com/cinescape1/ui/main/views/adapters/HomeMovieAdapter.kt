package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.ui.main.views.details.cinemaLocation.CinemaLocationActivity
import com.cinescape1.utils.Constant

class HomeMovieAdapter(private  val mContext: Activity, private  val cinemas: ArrayList<HomeDataResponse.Cinema>) :
    RecyclerView.Adapter<HomeMovieAdapter.MyViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cinema_layout, null)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cinemas.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val obj = cinemas[position]
        holder.title.text=obj.name


        Glide.with(mContext)
            .load(obj.appThumbImageUrl)
            .error(R.drawable.placeholder_home_locations)
            .into(holder.todoImage)

        holder.todoImage.setOnClickListener {
            val intent = Intent(mContext, CinemaLocationActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID, obj.id)
            intent.putExtra("type", "movie")
            mContext.startActivity(intent)
        }
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoImage: ImageView = view.findViewById(R.id.image_recommended) as ImageView
        var title: TextView = view.findViewById(R.id.textView58) as TextView
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }
}