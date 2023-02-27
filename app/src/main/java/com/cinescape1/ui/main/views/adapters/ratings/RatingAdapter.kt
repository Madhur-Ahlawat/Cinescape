package com.cinescape1.ui.main.views.adapters.ratings

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R


class RatingAdapter(private val personNames: ArrayList<String>, private val context: Context) :
    RecyclerView.Adapter<RatingAdapter.TodoViewHolder>() {

    private val dataList: ArrayList<String> = ArrayList()

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.age_rating_item, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return personNames.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val obj = personNames[position]
        dataList.add(obj)

        holder.todoTitle.text = obj
        holder.itemView.setOnClickListener {
            when (obj) {
                "E" -> {
                    holder.todoTitle.setTextColor(context.resources.getColor(R.color.green))

                }
                "PG" -> {
                    holder.todoTitle.setTextColor(context.resources.getColor(R.color.grey))

                }
                "13+" -> {
                    holder.todoTitle.setTextColor(context.resources.getColor(R.color.yellow))

                }
                "15+" -> {
                    holder.todoTitle.setTextColor(context.resources.getColor(R.color.yellow))

                }
                "18+" -> {
                    holder.todoTitle.setTextColor(context.resources.getColor(R.color.red))

                }
            }

        }

    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle: TextView = view.findViewById(R.id.age_rating_name) as TextView
    }

    interface RecycleViewItemClickListener {
        fun onItemClick()
    }
}