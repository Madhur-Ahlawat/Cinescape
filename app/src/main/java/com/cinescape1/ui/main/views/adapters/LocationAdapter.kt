package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoreTabResponse
import com.cinescape1.data.models.responseModel.MoviesResponse


class LocationAdapter(private val locationList: ArrayList<MoreTabResponse.Cinema>, private val context: Context) :
    RecyclerView.Adapter<LocationAdapter.TodoViewHolder>() {

    private val dataList: ArrayList<String> = ArrayList()
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location_list, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val obj = locationList[position]
        dataList.add(obj.toString())
        holder.workingHour.text = "Working hours  11:30 to 01:30"
        holder.address.text = obj.address1
        holder.title.text = obj.name
        holder.map.setOnClickListener {
            val strUri =
                "http://maps.google.com/maps?q=loc:" + obj.latitude + "," + obj.longitude.toString() + " (" + "Label which you want" + ")"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
            intent.setClassName(
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity"
            )
            context.startActivity(intent)
        }
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.textView36) as TextView
        var workingHour: TextView = view.findViewById(R.id.textView37) as TextView
        var address: TextView = view.findViewById(R.id.textView38) as TextView
        var map: ImageView = view.findViewById(R.id.imageView22) as ImageView
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }
}