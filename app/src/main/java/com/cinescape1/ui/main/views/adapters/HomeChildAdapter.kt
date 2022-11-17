package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.ui.main.views.details.ShowTimesActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import kotlinx.android.synthetic.main.home_slider_item.view.*
import kotlinx.android.synthetic.main.recommended_item.view.*


class HomeChildAdapter(
    var context: Activity,
    private var recommendedList: ArrayList<HomeDataResponse.MovieData>,
    private val type: Int,
    private val comingSoon: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val headerLayout =
                    LayoutInflater.from(context).inflate(R.layout.home_slider_item, parent, false)
                MyViewHolderSlider(headerLayout)
            }
            else -> {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.recommended_item, parent, false)
                MyViewHolderRecommended(view)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            val photoItem = recommendedList[position]
            if (type == 0) {
                val holder = holder as MyViewHolderSlider
                Glide.with(context)
                    .load(photoItem.mobimgsmall)
                    .error(R.drawable.placeholder_home_small_poster)
                    .into(holder.image)

            } else {
                val holder = holder as MyViewHolderRecommended
                Glide.with(context)
                    .load(photoItem.mobimgsmall)
                    .error(R.drawable.placeholder_home_small_poster)
                    .into(holder.thumbnail)

                println("tags--->${photoItem.tag}")
                if (photoItem.tag == "") {
                    holder.background.hide()
                    holder.tag.hide()
                } else {
                    holder.background.show()
                    holder.tag.show()
                    holder.tag.text = photoItem.tag+"           "
                    val tagColor = photoItem.tagColor
                    holder.background.setColorFilter(Color.parseColor(tagColor))
                }
                if (comingSoon) {
                    holder.thumbnail.setOnClickListener {
                        val intent = Intent(holder.thumbnail.context, ShowTimesActivity::class.java)
                        intent.putExtra(Constant.IntentKey.MOVIE_ID, photoItem.id)
                        intent.putExtra("type", "comingSoon")
                        holder.thumbnail.context.startActivity(intent)
                    }
                } else {
                    holder.thumbnail.setOnClickListener {
                        val intent = Intent(holder.thumbnail.context, ShowTimesActivity::class.java)
                        intent.putExtra(Constant.IntentKey.MOVIE_ID, photoItem.id)
                        holder.thumbnail.context.startActivity(intent)
                    }
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (type == 0) {
            0
        } else {
            1
        }
    }

    override fun getItemCount(): Int {
        return if (recommendedList.isNotEmpty()) recommendedList.size else 0
    }

    class MyViewHolderRecommended(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = itemView.image_recommended
        var background: ImageView = view.findViewById(R.id.imageView60)
        var tag: TextView = view.findViewById(R.id.tag)
    }

    class MyViewHolderSlider(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = itemView.image
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(updatedList: ArrayList<HomeDataResponse.MovieData>) {
        recommendedList = updatedList
        notifyDataSetChanged()
    }
}