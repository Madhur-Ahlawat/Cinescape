package com.cinescape1.ui.main.views.details.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.GetMovieResponse

class SimilarMovieAdapter(
    private val context: Context,
    private val items: ArrayList<GetMovieResponse.Output.Similar>,
    private val listner: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<SimilarMovieAdapter.TodoViewHolder>() {


    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_similer, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: TodoViewHolder, position: Int) {
        val obj = items[position]

        Glide.with(context)
            .load(obj.mobimgsmall).error(R.drawable.placeholder_home_small_poster)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            listner.onSimilarItemClick(obj)
        }
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view.findViewById(R.id.imageView61)
    }

    interface RecycleViewItemClickListener {
        fun onSimilarItemClick(view: GetMovieResponse.Output.Similar)
    }
}