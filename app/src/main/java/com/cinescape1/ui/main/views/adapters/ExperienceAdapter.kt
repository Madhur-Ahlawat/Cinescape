package com.cinescape1.ui.main.views.adapters

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
import com.cinescape1.data.models.responseModel.ProfileResponse
import com.cinescape1.utils.Constant
import com.cinescape1.utils.hide
import com.cinescape1.utils.show


class ExperienceAdapter(
    private val context: Context, private var listener: RecycleViewItemClickListener,
    private val profileList: ArrayList<ProfileResponse.Output.Experience>
) :
    RecyclerView.Adapter<ExperienceAdapter.TodoViewHolder>() {
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.experience_item, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val obj = profileList[position]
        when (obj.name) {
            "VIP" -> {
                holder.experienceNameImg.setImageResource(R.drawable.img_vip)
            }
            "IMAX" -> {
                holder.experienceNameImg.setImageResource(R.drawable.img_imax)
            }
            "4DX" -> {
                holder.experienceNameImg.setImageResource(R.drawable.img_4dx)
            }
            "3D" -> {
                holder.experienceNameImg.setImageResource(R.drawable.img_3d)
            }
            "2D" -> {
                holder.experienceNameImg.setImageResource(R.drawable.img_2d)
            }
            "DOLBY" -> {
                holder.experienceNameImg.setImageResource(R.drawable.img_dolby)
            }
            "ELEVEN" -> {
                holder.experienceNameImg.setImageResource(R.drawable.img_eleven)
            }
            "SCREENX" -> {
                holder.experienceNameImg.setImageResource(R.drawable.img_screenx)
            }
            else -> {
                holder.experienceNameImg.hide()
                holder.experienceNameText.show()
                holder.experienceNameText.text = obj.name
            }
        }

        if(Constant.taglist.contains(obj.name)){
            holder.experienceNameText.backgroundTintList =
                ColorStateList.valueOf(context.getColor(R.color.red))// Add tint color
            holder.experienceNameImg.backgroundTintList =
                ColorStateList.valueOf(context.getColor(R.color.red))// Add tint color
        }else{
            holder.experienceNameText.backgroundTintList =
                ColorStateList.valueOf(context.getColor(R.color.transparent))// Add tint color
            holder.experienceNameImg.backgroundTintList =
                ColorStateList.valueOf(context.getColor(R.color.transparent))// Add tint color
        }
        holder.itemView.setOnClickListener {
            listener.onItemClick(obj.name)
            holder.experienceNameImg.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.text_alert_color_red
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
//            holder.experienceNameText.backgroundTintList =
//                ColorStateList.valueOf(context.getColor(R.color.red))// Add tint color
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