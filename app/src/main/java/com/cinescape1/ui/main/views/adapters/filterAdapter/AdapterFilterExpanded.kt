package com.cinescape1.ui.main.views.adapters.filterAdapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.ModelFilterExpanded


class AdapterFilterExpanded(private var fFilterExpandedList: List<ModelFilterExpanded>) :
    RecyclerView.Adapter<AdapterFilterExpanded.MyViewHolderFilterExpanded>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFilterExpanded {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.filter_alert_expanded_item, parent, false)
        return MyViewHolderFilterExpanded(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderFilterExpanded, position: Int) {
        if (fFilterExpandedList.isEmpty()) {
//            holder.thumbnail.setImageResource(R.drawable.img_demo)
//            holder.title.setText(R.string.empty_photo)
        } else {

            val filterExpItem = fFilterExpandedList[position]

            holder.textExpTitle.text = filterExpItem.expTitle!!
            Log.d("textFilterExpTitle", "expTitle " + filterExpItem.expTitle)


        }
    }

    override fun getItemCount(): Int {
        return if (fFilterExpandedList.isNotEmpty()) fFilterExpandedList.size else 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newFilterExplist: List<ModelFilterExpanded>) {
        fFilterExpandedList = newFilterExplist
        notifyDataSetChanged()
    }

    class MyViewHolderFilterExpanded(view: View) : RecyclerView.ViewHolder(view) {

        var textExpTitle: TextView = view.findViewById(R.id.text_exp_title)
//    var imageArrowDrop: ImageView = view.findViewById(R.id.image_arrow_up)


    }

}