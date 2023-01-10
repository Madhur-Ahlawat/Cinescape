package com.cinescape1.ui.main.views.home.fragments.movie.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoviesResponse
import kotlinx.android.synthetic.main.alert_booking.view.*
import java.util.*


class FilterExperiencesAdapter(
    private val mContext: Activity,
    private val items: ArrayList<*>,
    private val selected: ArrayList<String>
) : RecyclerView.Adapter<FilterExperiencesAdapter.TodoViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.filter_experience_item, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: TodoViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val obj = items[position]
        println("FilterExperience------>${obj}")
        
        when (obj) {
            "4DX" -> {
                Glide.with(mContext).load(R.drawable.fourdx_gray).into(holder.todoTitle)
            }
            "STANDARD" -> {
                Glide.with(mContext).load(R.drawable.standard_gray).into(holder.todoTitle)
            }
            "VIP" -> {
                Glide.with(mContext).load(R.drawable.vip_gray).into(holder.todoTitle)
            }
            "IMAX" -> {
                Glide.with(mContext).load(R.drawable.imax_gray).into(holder.todoTitle)
            }
            "3D" -> {
                Glide.with(mContext).load(R.drawable.threed_gray).into(holder.todoTitle)
            }
            "DOLBY" -> {
                Glide.with(mContext).load(R.drawable.dolby_gray).into(holder.todoTitle)
            }

            "ELEVEN" -> {
                Glide.with(mContext).load(R.drawable.eleven_gray).into(holder.todoTitle)
            }
            "SCREENX" -> {
                Glide.with(mContext).load(R.drawable.screenx_gray).into(holder.todoTitle)
            }
            "PREMIUM" -> {
                Glide.with(mContext).load(R.drawable.premium_gray).into(holder.todoTitle)
            }
        }
        holder.layoutFilterBg.setBackgroundResource(R.drawable.filter_unselect)

        holder.layoutFilterBg.setOnClickListener {
            if (selected.contains(obj)) {
                selected.remove(obj)

//                when (obj) {
//                    "4DX" -> {
//                        Glide.with(mContext).load(R.drawable.fourdx_gray).into(holder.todoTitle)
//                    }
//                    "STANDARD" -> {
//                        Glide.with(mContext).load(R.drawable.standard_gray).into(holder.todoTitle)
//                    }
//                    "VIP" -> {
//                        Glide.with(mContext).load(R.drawable.vip_gray).into(holder.todoTitle)
//                    }
//                    "IMAX" -> {
//                        Glide.with(mContext).load(R.drawable.imax_gray).into(holder.todoTitle)
//                    }
//                    "3D" -> {
//                        Glide.with(mContext).load(R.drawable.threed_gray).into(holder.todoTitle)
//                    }
//                    "DOLBY" -> {
//                        Glide.with(mContext).load(R.drawable.dolby_gray).into(holder.todoTitle)
//                    }
//                    "ELEVEN" -> {
//                        Glide.with(mContext).load(R.drawable.eleven_gray).into(holder.todoTitle)
//                    }
//                    "SCREENX" -> {
//                        Glide.with(mContext).load(R.drawable.screenx_gray).into(holder.todoTitle)
//                    }
//                    "PREMIUM" -> {
//                        Glide.with(mContext).load(R.drawable.premium_gray).into(holder.todoTitle)
//                    }
//                }

                holder.layoutFilterBg.setBackgroundResource(R.drawable.filter_unselect)
                holder.todoTitle.setColorFilter(ContextCompat.getColor(mContext, R.color.gray))

            } else {
                selected.add(obj.toString())
//                when (obj) {
//                    "4DX" -> {
//                        Glide.with(mContext).load(R.drawable.fourdx_white).into(holder.todoTitle)
//                    }
//                    "STANDARD" -> {
//                        Glide.with(mContext).load(R.drawable.standard_white).into(holder.todoTitle)
//                    }
//                    "VIP" -> {
//                        Glide.with(mContext).load(R.drawable.vip_white).into(holder.todoTitle)
//                    }
//                    "IMAX" -> {
//                        Glide.with(mContext).load(R.drawable.imax_white).into(holder.todoTitle)
//                    }
//                    "3D" -> {
//                        Glide.with(mContext).load(R.drawable.threed_white).into(holder.todoTitle)
//                    }
//                    "DOLBY" -> {
//                        Glide.with(mContext).load(R.drawable.dolby_white).into(holder.todoTitle)
//                    }
//                    "ELEVEN" -> {
//                        Glide.with(mContext).load(R.drawable.eleven_white).into(holder.todoTitle)
//                    }
//                    "SCREENX" -> {
//                        Glide.with(mContext).load(R.drawable.screenx_white).into(holder.todoTitle)
//                    }
//                    "PREMIUM" -> {
//                        Glide.with(mContext).load(R.drawable.premium_white).into(holder.todoTitle)
//                    }
//                }
                holder.layoutFilterBg.setBackgroundResource(R.drawable.filter_select)
                holder.todoTitle.setColorFilter(ContextCompat.getColor(mContext, R.color.colorWhite))

            }
        }
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle: ImageView = view.findViewById(R.id.text_exp_title)
        var layoutFilterBg: ConstraintLayout = view.findViewById(R.id.constraintLayout29)
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }
}