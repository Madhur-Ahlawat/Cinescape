package com.cinescape1.ui.main.views.home.fragments.movie.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoviesResponse
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class FilterExperiencesAdapter(
    private val mContext: Activity,
    private val items: ArrayList<*>,
    private val selected: ArrayList<String>
) :
    RecyclerView.Adapter<FilterExperiencesAdapter.TodoViewHolder>() {

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
    override fun onBindViewHolder(
        holder: TodoViewHolder,
        @SuppressLint("RecyclerView") position: Int) {
        val obj = items[position]

        val lowerCase = obj.toString().toLowerCase()
        val url = "https://s3.eu-west-1.amazonaws.com/cinescape.uat/experience/${lowerCase}.png"
        val urlActive =
            "https://s3.eu-west-1.amazonaws.com/cinescape.uat/experience/${lowerCase}-Active.png"

        var width1 = 0
        var height1 = 0




        Thread(Runnable {
            val bmp = getBitmapFromURL(url)
            println("url---->${bmp?.height}")
            width1 = bmp?.width!!
            height1 = bmp.height

//            holder.todoTitle.layoutParams.width = bmp.width
//            holder.layoutFilterBg.layoutParams.width = bmp.width
//            holder.todoTitle.layoutParams.height = bmp.height
//            holder.layoutFilterBg.layoutParams.height = bmp.height
            println("url1---->${bmp.width}---->${holder.todoTitle.layoutParams.width}")

        }).start()

//        (holder.todoTitle.layoutParams as ConstraintLayout.LayoutParams).apply {
//            width = width1
//            height = height1
//        }

        holder.layoutFilterBg.setOnClickListener {

            if (selected.contains(obj)) {
                selected.remove(obj)
                holder.layoutFilterBg.setBackgroundResource(R.drawable.filter_unselect)
            } else {
                selected.add(obj.toString())
                holder.layoutFilterBg.setBackgroundResource(R.drawable.filter_select)
            }

        }

        if (selected.contains(obj)) {
//            Glide.with(mContext).load(url).error(R.drawable.filter_select).into(holder.todoTitle)
            Glide.with(mContext).load(url).override(width1, height1).into(holder.todoTitle)
//            holder.layoutFilterBg.setBackgroundResource(R.drawable.filter_select)
        } else {
//            holder.layoutFilterBg.setBackgroundResource(R.drawable.filter_unselect)
//            Glide.with(mContext).load(url).error(R.drawable.filter_unselect).into(holder.todoTitle)
            Glide.with(mContext).load(url).override(width1, height1).into(holder.todoTitle)
        }


    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle: ImageView = view.findViewById(R.id.text_exp_title) as ImageView
        var layoutFilterBg: ConstraintLayout =
            view.findViewById(R.id.layoutFilterBg) as ConstraintLayout
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            println("ErrorBitmap-------->${e.printStackTrace()}")
            e.printStackTrace()
            null
        }
    } // Author: sil


}