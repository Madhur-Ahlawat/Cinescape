package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoviesResponse

class RatingAdapter(private val context: Context, private val items: ArrayList<MoviesResponse.Rating>) :
    RecyclerView.Adapter<RatingAdapter.TodoViewHolder>() {

    private val dataList: ArrayList<String> = ArrayList()
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_alert_expanded_item, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
//        val obj = items[position]
////        dataList.add(obj.toString())
//        holder.todoTitle.text = obj.valueAlt
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle: TextView = view.findViewById(R.id.text_exp_title) as TextView
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }
}