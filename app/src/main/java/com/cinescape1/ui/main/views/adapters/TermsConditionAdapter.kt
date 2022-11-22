package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoreTabResponse
import com.cinescape1.data.models.responseModel.MoviesResponse

class TermsConditionAdapter(private val termsCondition: ArrayList<MoreTabResponse.Tnc>,
                            context: Context, var listener: TypefaceListenerTermsCondition) :
    RecyclerView.Adapter<TermsConditionAdapter.TodoViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_privacy_list, null)
        return TodoViewHolder(view)

    }

    override fun getItemCount(): Int {
        return termsCondition.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val obj = termsCondition[position]
        holder.todoTitle.text = obj.name
        holder.todoDesc.text = obj.description
        println("termsCheck--->${termsCondition.size}")

        listener.onTypefaceTermsCondition(holder.todoTitle,holder.todoDesc)
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle: TextView = view.findViewById(R.id.txtTermsCondition) as TextView
        var todoDesc: TextView = view.findViewById(R.id.txtTermsConditionDesc) as TextView
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }

    interface TypefaceListenerTermsCondition {
        fun onTypefaceTermsCondition(todoTitle: TextView, todoDesc: TextView)
    }

}