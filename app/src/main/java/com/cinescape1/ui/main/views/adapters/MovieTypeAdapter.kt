package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.MovieTypeModel
import com.cinescape1.data.models.responseModel.MoviesResponse

class MovieTypeAdapter(
    private val arrayList: ArrayList<MovieTypeModel>
) :
    RecyclerView.Adapter<MovieTypeAdapter.ViewHolder>() {
    private var count: Int = 0
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_type_item, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val list = arrayList[position]
        holder.text.text = list.name

        holder.itemView.setOnClickListener {
            count=position
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var text: TextView = view.findViewById(R.id.textView52)
        var view: View = view.findViewById(R.id.view69)
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }
}