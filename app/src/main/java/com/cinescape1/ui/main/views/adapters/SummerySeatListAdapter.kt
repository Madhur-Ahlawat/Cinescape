package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoreTabResponse
import com.cinescape1.data.models.responseModel.MoviesResponse

class SummerySeatListAdapter(private val faqList: List<String>, var listener: TypeFaceSeatLists) :
    RecyclerView.Adapter<SummerySeatListAdapter.TodoViewHolder>() {
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_summery_seat_list, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val obj = faqList[position]
        holder.seatNumber.text = obj

        listener.onTypeFaceSeatList(holder.seatNumber)

    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var seatNumber: TextView = view.findViewById(R.id.seatNumber)
    }

    interface TypeFaceSeatLists{
      fun  onTypeFaceSeatList(seatNumber: TextView)
    }

}