package com.cinescape1.ui.main.views.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.ModelPreferenceType
import com.cinescape1.data.models.responseModel.ProfileResponse
import com.cinescape1.utils.Constant

 class SeatTypeAdapter(
        private val context: Context, private var listener: RecycleViewItemClickListener,
        private val seatTypeList: ArrayList<ModelPreferenceType>) :
        RecyclerView.Adapter<SeatTypeAdapter.TodoViewHolder>() {
         @SuppressLint("InflateParams")
         override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
                 val view =
                         LayoutInflater.from(parent.context).inflate(R.layout.seat_type_item, null)
                 return TodoViewHolder(view)
         }

         override fun getItemCount(): Int {
                 return seatTypeList.size
         }

         override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
                 val obj = seatTypeList[position]

                 if (Constant.taglist.contains(obj.seatType)) {
                         holder.experienceNameText.backgroundTintList =
                                 ColorStateList.valueOf(context.getColor(R.color.red))// Add tint color
                         holder.experienceNameImg.backgroundTintList =
                                 ColorStateList.valueOf(context.getColor(R.color.red))// Add tint color
                 } else {
                         holder.experienceNameText.backgroundTintList =
                                 ColorStateList.valueOf(context.getColor(R.color.transparent))// Add tint color
                         holder.experienceNameImg.backgroundTintList =
                                 ColorStateList.valueOf(context.getColor(R.color.transparent))// Add tint color
                 }

                 holder.itemView.setOnClickListener {
                         listener.onItemClick(obj.seatType)
                         holder.experienceNameImg.setColorFilter(
                                 ContextCompat.getColor(context, R.color.text_alert_color_red), android.graphics.PorterDuff.Mode.MULTIPLY
                         )
                         notifyDataSetChanged()
                 }

         }

         inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
                 var experienceNameText: TextView = view.findViewById(R.id.experience_nametxt)
                 var experienceNameImg: ImageView = view.findViewById(R.id.experience_name)
         }

         interface RecycleViewItemClickListener {
                 fun onItemClick(view: String)
         }
 }
