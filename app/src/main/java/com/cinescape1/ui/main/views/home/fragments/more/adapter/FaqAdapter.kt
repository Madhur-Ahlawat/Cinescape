package com.cinescape1.ui.main.views.home.fragments.more.adapter

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

class FaqAdapter(
    private val faqList: ArrayList<MoreTabResponse.Faq>,
    private val context: Activity,
    var  language: String?,
    var listener: TypefaceListenerFaq
) :
    RecyclerView.Adapter<FaqAdapter.TodoViewHolder>() {

    private val dataList: ArrayList<String> = ArrayList()
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq_list, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val obj = faqList[position]
        dataList.add(obj.toString())
        holder.todoTitle.text = obj.name
        listener.onTypefaceFaq(holder.todoTitle)

        if (language=="ar"){
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            val adapter = FaqChildArabicAdapter(obj.faqs,context,language)
            holder.faqChildRecycler.setHasFixedSize(true)
            holder.faqChildRecycler.layoutManager = layoutManager
            holder.faqChildRecycler.adapter = adapter
        }else{
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            val adapter = FaqChildAdapter(obj.faqs,context,language)
            holder.faqChildRecycler.setHasFixedSize(true)
            holder.faqChildRecycler.layoutManager = layoutManager
            holder.faqChildRecycler.adapter = adapter
        }
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle: TextView = view.findViewById(R.id.textView42) as TextView
        var faqChildRecycler: RecyclerView = view.findViewById(R.id.faqChildRecycler) as RecyclerView
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }

    interface TypefaceListenerFaq {
        fun onTypefaceFaq(todoTitle: TextView)
    }

}