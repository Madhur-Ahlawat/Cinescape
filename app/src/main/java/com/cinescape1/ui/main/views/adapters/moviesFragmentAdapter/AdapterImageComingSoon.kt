package com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.ImgComingSoon
import com.cinescape1.ui.main.views.ComingSoonMovieInfoActivity



class AdapterImageComingSoon ( private var comingSoonList: List<ImgComingSoon>) :
    RecyclerView.Adapter<AdapterImageComingSoon.MyViewHolderImageComingSoon>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderImageComingSoon {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_coming_soon_img_item, parent, false)
        return MyViewHolderImageComingSoon(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderImageComingSoon, position: Int) {
        if (comingSoonList.isEmpty()) {
            holder.movieImg.setImageResource(R.drawable.img_demo)
        } else {
            val comingSoonItem = comingSoonList[position]
            holder.movieImg.setImageResource(comingSoonItem.img!!)

            holder.movieImg.setOnClickListener {
                Log.d("AdapterComingSoon", "item click success")

                holder.movieImg.context.startActivity(Intent(holder.movieImg.context, ComingSoonMovieInfoActivity::class.java))

            }


        }
    }

    override fun getItemCount(): Int {
        return if (comingSoonList.isNotEmpty()) comingSoonList.size else 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newPhotoslist: List<ImgComingSoon>) {
        comingSoonList = newPhotoslist
        notifyDataSetChanged()
    }
    class MyViewHolderImageComingSoon(view: View) : RecyclerView.ViewHolder(view) {
        var movieImg: ImageView = view.findViewById(R.id.image_coming_soon)
    }
}