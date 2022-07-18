package com.cinescape1.ui.main.views.adapters.sliderAdapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.github.islamkhsh.CardSliderAdapter
import kotlinx.android.synthetic.main.home_slider_item.view.*

class HomeBackSliderAdapter (private val mContext:Activity, private val movies : ArrayList<HomeDataResponse.MovieData>) : CardSliderAdapter<HomeBackSliderAdapter.MovieViewHolder>() {

    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slider_layout_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun bindVH(holder: MovieViewHolder, position: Int) {
        val obj = movies[position]
        if (obj.mobimgsmall.isEmpty()) {
            holder.image.setImageResource(R.drawable.bombshell)
        } else {
            Glide.with(mContext).load(obj.mobimgsmall).placeholder(R.drawable.bombshell).into(holder.image)
        }
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var image = itemView.image
    }
}