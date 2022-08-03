package com.cinescape1.ui.main.views.adapters.sliderAdapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.ui.main.views.ShowTimesActivity
import com.cinescape1.utils.Constant
import com.github.islamkhsh.CardSliderAdapter
import kotlinx.android.synthetic.main.home_slider_item.view.*

class HomeFrontSliderAdapter (private val mContext:Activity,private val movies : ArrayList<HomeDataResponse.MovieData>) : CardSliderAdapter<HomeFrontSliderAdapter.MovieViewHolder>() {

    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_slider_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun bindVH(holder: MovieViewHolder, position: Int) {
        //TODO bind item object with item layout view
        val obj = movies[position]
        if (obj.mobimgsmall.isEmpty()) {
            holder.image.setImageResource(R.drawable.pos_not_avilbale)
        } else {
            Glide.with(mContext)
                .load(obj.mobimgsmall)
                .placeholder(R.drawable.pos_not_avilbale)
                .into(holder.image)
            println("FrontData--->${obj.mobimgsmall}")
        }

        holder.image.setOnClickListener {
            val intent = Intent(mContext, ShowTimesActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID,obj.id)
//            intent.putExtra("type","movie")
            mContext.startActivity(intent)
        }
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var image = itemView.image

    }
}