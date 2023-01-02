package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.utils.Constant
import com.cinescape1.utils.LocaleHelper

class FilterChildCinemaAdapter(private val mContext: Context, private val items: ArrayList<MoviesResponse.Cinema>, private val selected: ArrayList<String>) :
    RecyclerView.Adapter<FilterChildCinemaAdapter.TodoViewHolder>() {


    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_location_item, null)
        return TodoViewHolder(view)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: TodoViewHolder,
        @SuppressLint("RecyclerView") position: Int) {
        val obj = items[position]
        holder.todoTitle.text = obj.name
        if (Constant.LANGUAGE == "ar"){
            LocaleHelper.setLocale(mContext, "ar")
            val regular = ResourcesCompat.getFont(mContext, R.font.gess_light)
            val bold = ResourcesCompat.getFont(mContext, R.font.gess_bold)
            val medium = ResourcesCompat.getFont(mContext, R.font.gess_medium)

            holder.todoTitle.typeface = regular


        }else if (Constant.LANGUAGE == "en"){
            LocaleHelper.setLocale(mContext, "en")
            val regular = ResourcesCompat.getFont(mContext, R.font.sf_pro_text_regular)
            val bold = ResourcesCompat.getFont(mContext, R.font.sf_pro_text_bold)
            val heavy = ResourcesCompat.getFont(mContext, R.font.sf_pro_text_heavy)
            val medium = ResourcesCompat.getFont(mContext, R.font.sf_pro_text_medium)
            holder.todoTitle.typeface = regular

        }else{
            LocaleHelper.setLocale(mContext, "en")
            val regular = ResourcesCompat.getFont(mContext, R.font.sf_pro_text_regular)
            val bold = ResourcesCompat.getFont(mContext, R.font.sf_pro_text_bold)
            val heavy = ResourcesCompat.getFont(mContext, R.font.sf_pro_text_heavy)
            val medium = ResourcesCompat.getFont(mContext, R.font.sf_pro_text_medium)
            holder.todoTitle.typeface = regular
        }

        holder.todoTitle.setOnClickListener {
            if (selected.contains(obj.name+"-"+obj.id)){
                selected.remove(obj.name+"-"+obj.id)
//                holder.todoTitle.setTextColor(ContextCompat.getColor(mContext, R.color.hint_color))
                holder.layoutFilterBg.setBackgroundDrawable(mContext.getDrawable(R.drawable.filter_unselect))

                holder.todoTitle.setTextColor(ContextCompat.getColor(mContext, R.color.hint_color))

            }else {
                selected.add(obj.name+"-"+obj.id)
//                holder.todoTitle.setTextColor(
//                    ContextCompat.getColor(
//                        mContext,
//                        R.color.text_alert_color_red
//                    )
//                )

                holder.todoTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                holder.layoutFilterBg.setBackgroundDrawable(mContext.getDrawable(R.drawable.filter_select))

            }
        }
        println("data.selectedList---"+selected+"----"+obj.name)
        if (selected.contains(obj.name+"-"+obj.id)) {
            holder.todoTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            holder.layoutFilterBg.setBackgroundDrawable(mContext.getDrawable(R.drawable.filter_select))

        } else {
            holder.layoutFilterBg.setBackgroundDrawable(mContext.getDrawable(R.drawable.filter_unselect))

            holder.todoTitle.setTextColor(ContextCompat.getColor(mContext, R.color.hint_color))
        }
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle: TextView = view.findViewById(R.id.text_exp_title) as TextView
        var layoutFilterBg: ConstraintLayout = view.findViewById(R.id.layoutFilterBg) as ConstraintLayout

    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }
}