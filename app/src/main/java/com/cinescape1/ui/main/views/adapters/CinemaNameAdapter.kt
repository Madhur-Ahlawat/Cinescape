package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CinemaSessionResponse
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.utils.hide
import com.cinescape1.utils.invisible
import com.cinescape1.utils.show

class CinemaNameAdapter(
    private val
    daySessionResponse: ArrayList<CinemaSessionResponse.DaySession>, private val
    context: Context
) :
    RecyclerView.Adapter<CinemaNameAdapter.TodoViewHolder>() {
    private var rowIndex = 0

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cinematxt, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return daySessionResponse.size
    }

    override fun onBindViewHolder(
        holder: TodoViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val moreListPos = daySessionResponse[position]
        println("privacyCheckPolicy--->${moreListPos}")
        if (rowIndex == position) {
//            holder.image.invisible()
            val bold: Typeface = context.resources.getFont(R.font.sf_pro_text_semibold)
            holder.todoTitle.textSize = 20f
            holder.todoTitle.typeface = bold
        } else {
//            holder.image.hide()
            val regular: Typeface = context.resources.getFont(R.font.sf_pro_text_regular)
            holder.todoTitle.typeface = regular
            holder.todoTitle.textSize = 13F
        }
        holder.itemView.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
        }
        holder.todoTitle.text = moreListPos.cinema.name
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle: TextView = view.findViewById(R.id.cinemaName) as TextView
        var image: ImageView = view.findViewById(R.id.imageView48)
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }
}