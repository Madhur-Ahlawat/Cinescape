package com.cinescape1.ui.main.views.adapters.foodAdapters

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.FoodResponse
import com.cinescape1.ui.main.views.HomeActivity

class FoodLoListAdapter(var recyclerDataArrayList: ArrayList<FoodResponse.Output.Cinema>, private var listener: HomeActivity, private val context: Activity) :
    RecyclerView.Adapter<FoodLoListAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // Inflate Layout
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.food_location, parent, false)
        return RecyclerViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: RecyclerViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val recyclerData = recyclerDataArrayList[position]
        holder.title.text=recyclerData.name
        holder.title.setOnClickListener{
//            listener.onItemClick(recyclerData)
        }
    }

    override fun getItemCount(): Int {
        return recyclerDataArrayList.size
    }

    //View Holder Class to handle Recycler View.
    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textView21)

    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: FoodResponse.Output.Cinema)
    }
}
