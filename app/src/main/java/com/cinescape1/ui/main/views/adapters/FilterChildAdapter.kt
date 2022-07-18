package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoviesResponse

class FilterChildAdapter(private val mContext: Context, private val items: ArrayList<*>) :
    RecyclerView.Adapter<FilterChildAdapter.TodoViewHolder>() {
    private var rowIndex = -1

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.filter_alert_expanded_item, null)
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
        holder.todoTitle.text = obj.toString()
        holder.todoTitle.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
        }
        if (rowIndex == position) {
            holder.todoTitle.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.text_alert_color_red
                )
            )
        } else {
            holder.todoTitle.setTextColor(ContextCompat.getColor(mContext, R.color.hint_color))
        }
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle: TextView = view.findViewById(R.id.text_exp_title) as TextView
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }
}