package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.ui.main.views.details.ShowTimesActivity
import com.cinescape1.utils.Constant
import kotlinx.android.synthetic.main.home_slider_item.view.*
import kotlinx.android.synthetic.main.recommended_item.view.*


class HomeChildAdapter(
    var context: Activity,
    private var recommendedList: ArrayList<HomeDataResponse.MovieData>,
    private val type: Int,
    private val comingSoon: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "AdapterRecommended"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, ".onCreateViewHolder new view requested---$viewType")
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