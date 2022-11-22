package com.cinescape1.ui.main.views.home.fragments.movie.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter.ComingSoonChildAdapter

class AdapterComingSoon(
    private var nowShowingList: ArrayList<MoviesResponse.Comingsoon>,
    context: Context, var listener: TypefaceListener2
) :
    RecyclerView.Adapter<AdapterComingSoon.MyViewHolderNowShowing>() {
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comming_soon_parent, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

        holder.movieTitle.text = comingSoonItem.dated
        val gridLayout = GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false)
        val adapter = ComingSoonChildAdapter(comingSoonItem.comingSoon, mContext)
        holder.recycler.layoutManager = gridLayout
        holder.recycler.adapter = adapter

        listener.typeFaceComingSoon(holder.movieTitle)

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var recycler: RecyclerView = view.findViewById(R.id.recyclerComParent)
        var movieTitle: TextView = view.findViewById(R.id.textView121)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(updatedList: ArrayList<MoviesResponse.Comingsoon>) {
        nowShowingList = updatedList
        notifyDataSetChanged()
    }

    interface TypefaceListener2{
        fun typeFaceComingSoon(movieTitle: TextView)
    }

}