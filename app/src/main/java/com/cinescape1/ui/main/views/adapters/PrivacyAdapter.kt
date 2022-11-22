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

class PrivacyAdapter(
    private val moreTabList: ArrayList<MoreTabResponse.Privacy>,
    context: Context, var listener: TypefaceListenerPrivacy
) :
    RecyclerView.Adapter<PrivacyAdapter.TodoViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_privacy_policy, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return moreTabList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val moreListPos = moreTabList[position]
        println("privacyCheckPolicy--->${moreListPos}")
        holder.todoTitle.text = moreListPos.name
        holder.todoDesc.text = moreListPos.description

        listener.onTypefacePrivacy(holder.todoTitle,holder.todoDesc)


        println("privacyList--->${moreListPos.name}")
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle: TextView = view.findViewById(R.id.textView42) as TextView
        var todoDesc: TextView = view.findViewById(R.id.textView43) as TextView
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }

    interface TypefaceListenerPrivacy {
        fun onTypefacePrivacy(todoTitle: TextView, todoDesc: TextView)
    }

}