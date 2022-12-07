package com.cinescape1.ui.main.views.home.fragments.movie.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoviesResponse

class FilterExperiencesAdapter(private val mContext: Context, private val items: ArrayList<*>, private val selected: ArrayList<String>) :
    RecyclerView.Adapter<FilterExperiencesAdapter.TodoViewHolder>() {


    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.filter_experience_item, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: TodoViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val obj = items[position]
        val lowerCase = obj.toString().toLowerCase()
        val url = "https://s3.eu-west-1.amazonaws.com/cinescape.uat/experience/${lowerCase}.png"
        val urlActive = "https://s3.eu-west-1.amazonaws.com/cinescape.uat/experience/${lowerCase}-Active.png"
//        holder.todoTitle.text = obj.toString()

        holder.layoutFilterBg.setOnClickListener {
            if (selected.contains(obj)){
                selected.remove(obj)
//                holder.layoutFilterBg.setBackgroundDrawable(mContext.getDrawable(R.drawable.filter_unselect))
                Glide.with(mContext).load(url).error(R.drawable.filter_unselect).into(holder.todoTitle)
//                holder.todoTitle.setColorFilter(ContextCompat.getColor(mContext, R.color.hint_color))
//                holder.todoTitle.setTextColor(ContextCompat.getColor(mContext, R.color.hint_color))

            }else {
                selected.add(obj.toString())
//                holder.todoTitle.setColorFilter(ContextCompat.getColor(mContext, R.color.white))
                Glide.with(mContext).load(urlActive).error(R.drawable.transparent).into(holder.todoTitle)
//                holder.layoutFilterBg.setBackgroundDrawable(mContext.getDrawable(R.drawable.filter_select))

            }
        }

        if (selected.contains(obj)) {
//            holder.todoTitle.setColorFilter(ContextCompat.getColor(mContext, R.color.white))
            Glide.with(mContext).load(urlActive).error(R.drawable.transparent).into(holder.todoTitle)
//            holder.layoutFilterBg.setBackgroundDrawable(mContext.getDrawable(R.drawable.filter_select))
        } else {
//            holder.layoutFilterBg.setBackgroundDrawable(mContext.getDrawable(R.drawable.filter_unselect))

            Glide.with(mContext).load(url).error(R.drawable.filter_unselect).into(holder.todoTitle)
//            holder.todoTitle.setColorFilter(ContextCompat.getColor(mContext, R.color.hint_color))

        }
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle: ImageView = view.findViewById(R.id.text_exp_title) as ImageView
        var layoutFilterBg: ConstraintLayout = view.findViewById(R.id.layoutFilterBg) as ConstraintLayout
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }
}