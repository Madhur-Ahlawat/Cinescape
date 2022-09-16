package com.cinescape1.ui.main.views.home.fragments.movie.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R


class AdapterFilterCategory(
    private val context: Activity,
    private var categoryFilterList: ArrayList<String>,private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<AdapterFilterCategory.MyViewHolderCategoryFilter>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderCategoryFilter {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_filter_items, parent, false)
        return MyViewHolderCategoryFilter(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderCategoryFilter, position: Int) {
        val categoryFilterItem = categoryFilterList[position]
        holder.categoryTitle.text = categoryFilterItem.split("-")[0]
        holder.cross.setOnClickListener {
            listener.onCrossClick(categoryFilterItem)
        }
    }

    override fun getItemCount(): Int {
        return if (categoryFilterList.isNotEmpty()) categoryFilterList.size else 0
    }

    class MyViewHolderCategoryFilter(view: View) : RecyclerView.ViewHolder(view) {
        var categoryTitle: TextView = view.findViewById(R.id.tv_category_titles)
        var cross: ImageView = view.findViewById(R.id.imageView15)

    }

    interface RecycleViewItemClickListener {
        fun onCrossClick(item:String)
    }

}