package com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.ImgComingSoon


class AdapterComingSoonSimilarMovies ( private var comingSoonSimilarMoviesList: List<ImgComingSoon>) :
    RecyclerView.Adapter<AdapterComingSoonSimilarMovies.MyViewHolderComingSoonSimilarMovies>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderComingSoonSimilarMovies {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.similar_movie_now_showing_item, parent, false)
        return MyViewHolderComingSoonSimilarMovies(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderComingSoonSimilarMovies, position: Int) {
        if (comingSoonSimilarMoviesList.isEmpty()) {
//            holder.title.setText(R.string.empty_photo)
        } else {
            val comingSoonItem = comingSoonSimilarMoviesList[position]

            holder.imgSimilarMovies.setImageResource(comingSoonItem.img!!)

//            val img = photoItem.images
//            Picasso.get().load(img)
//                .error(R.drawable.img_demo)
//                .placeholder(R.drawable.img_demo)
//                .into(holder.thumbnail)
//            holder.title.text = photoItem.title

//                    Picasso.get().load(R.drawable.img_demo).into(holder.thumbnail)
        }
    }

    override fun getItemCount(): Int {
        return if (comingSoonSimilarMoviesList.isNotEmpty()) comingSoonSimilarMoviesList.size else 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newPhotoslist: List<ImgComingSoon>) {
        comingSoonSimilarMoviesList = newPhotoslist
        notifyDataSetChanged()
    }
    class MyViewHolderComingSoonSimilarMovies(view: View) : RecyclerView.ViewHolder(view) {
        val imgSimilarMovies: ImageView = view.findViewById(R.id.image_similar_movies)
    }

}