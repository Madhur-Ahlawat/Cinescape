package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoreTabResponse
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.utils.hide
import com.cinescape1.utils.show

class FaqChildAdapter(
    private val faqList: ArrayList<MoreTabResponse.Faq.Faqs>,
    private val context: Activity
) :
    RecyclerView.Adapter<FaqChildAdapter.TodoViewHolder>() {
    private var rowIndex = -1

    private val dataList: ArrayList<String> = ArrayList()
    private var up = true
    var displayMetrics = DisplayMetrics()

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq_child, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: TodoViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val obj = faqList[position]
        dataList.add(obj.toString())
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        holder.todoTitle.text = obj.ques
        holder.desc.text = obj.answer


        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        holder.itemView.layoutParams = params

        val lp = LinearLayout.LayoutParams(
            (width),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        // or set height to any fixed value you want
        holder.linearLayout.layoutParams = lp



        if(rowIndex==position){
            holder.todoTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.arrow_up,
                0
            )
            holder.desc.show()

        }else{
            holder.todoTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.arrow_down,
                0
            )
            holder.desc.hide()
        }
        holder.todoTitle.setOnClickListener {
            rowIndex = if (position==rowIndex){
                -1
            }else {
                position
            }
            notifyDataSetChanged()
        }
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle: TextView = view.findViewById(R.id.textView54) as TextView
        var desc: TextView = view.findViewById(R.id.textView55) as TextView
        var linearLayout: LinearLayout = view.findViewById(R.id.TopUi)
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }
}