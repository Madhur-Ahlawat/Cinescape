package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.MovieTypeModel
import com.cinescape1.utils.Constant
import com.cinescape1.utils.hide
import com.cinescape1.utils.show

class MovieTypeAdapter(
    private val arrayList: ArrayList<MovieTypeModel>,
    private val context: Context,
    private var listener: RecycleViewItemClickListener,
    private var listenerTypeface: TypeFaceListener
) :
    RecyclerView.Adapter<MovieTypeAdapter.ViewHolder>() {
    private var rowIndex = Constant.SEE_ALL_TYPE

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_type_item, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val list = arrayList[position]
        holder.text.text = list.name

        val heavy: Typeface = context.resources.getFont(R.font.sf_pro_text_heavy)
        val regular: Typeface = context.resources.getFont(R.font.sf_pro_text_regular)

        listenerTypeface.onTypeFace(holder.text)

        if (rowIndex == position) {
            holder.view.show()
            holder.text.setTextColor(ContextCompat.getColor(context, R.color.white))
//            holder.text.textSize = 17f
            holder.text.textSize = 14f
            holder.text.typeface = heavy
        } else {
            holder.view.hide()
            holder.text.setTextColor(ContextCompat.getColor(context, R.color.text_color))
            holder.text.typeface = regular
            holder.text.textSize = 14F
        }
        holder.itemView.setOnClickListener {
            rowIndex = position
            listener.onMovieTypeClick(position)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var text: TextView = view.findViewById(R.id.textView52)
        var view: View = view.findViewById(R.id.view69)
    }

    interface RecycleViewItemClickListener {
        fun onMovieTypeClick(position: Int)
    }

    interface TypeFaceListener {
        fun onTypeFace(text: TextView)
    }


}