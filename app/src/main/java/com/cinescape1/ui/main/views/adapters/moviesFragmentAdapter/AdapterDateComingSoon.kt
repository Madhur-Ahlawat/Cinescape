package com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.ImgComingSoon
import com.cinescape1.data.models.ModelsComingSoon



class AdapterDateComingSoon (context: Context, private var comingSoonList: List<ModelsComingSoon>) :
    RecyclerView.Adapter<AdapterDateComingSoon.MyViewHolderComingSoonMovie>() {

    private var mcontext: Context = context

    private var comingSoonImgnList: List<ImgComingSoon>? = arrayListOf(
        ImgComingSoon(R.drawable.img_demo),
        ImgComingSoon(R.drawable.img_coming_soon),
        ImgComingSoon(R.drawable.wonderwoman),
        ImgComingSoon(R.drawable.img_demo),
        ImgComingSoon(R.drawable.img_coming_soon),
        ImgComingSoon(R.drawable.img_demo),
        ImgComingSoon(R.drawable.img_coming_soon),
        ImgComingSoon(R.drawable.wonderwoman),
        ImgComingSoon(R.drawable.img_demo),
        ImgComingSoon(R.drawable.img_coming_soon)

        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderComingSoonMovie {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_coming_soon_item, parent, false)
        return MyViewHolderComingSoonMovie(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderComingSoonMovie, position: Int) {
        if (comingSoonList.isEmpty()) {
//            holder.title.setText(R.string.empty_photo)
        } else {
            val comingSoonItem = comingSoonList[position]
            holder.movieTitle.text = comingSoonItem.dateFilm!!

            val gridLayout = GridLayoutManager(mcontext, 3, GridLayoutManager.VERTICAL, false)
            holder.recyclerComingSoonImg.layoutManager = LinearLayoutManager(mcontext)
            val adapter = AdapterImageComingSoon(comingSoonImgnList!!)
            holder.recyclerComingSoonImg.layoutManager = gridLayout
            holder.recyclerComingSoonImg.adapter = adapter
            adapter.loadNewData(comingSoonImgnList!!)


        }
    }

    override fun getItemCount(): Int {
        return if (comingSoonList.isNotEmpty()) comingSoonList.size else 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newPhotoslist: List<ModelsComingSoon>) {
        comingSoonList = newPhotoslist
        notifyDataSetChanged()
    }
    class MyViewHolderComingSoonMovie(view: View) : RecyclerView.ViewHolder(view) {
        var movieTitle: TextView = view.findViewById(R.id.text_movie_date)
        val recyclerComingSoonImg =
            view.findViewById<View>(R.id.recyclerview_img_comoing_soon) as RecyclerView
    }
}