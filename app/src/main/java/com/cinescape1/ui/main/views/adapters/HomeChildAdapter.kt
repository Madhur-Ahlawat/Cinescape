package com.cinescape1.ui.main.views.adapters
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.ui.main.views.details.commingSoon.ComingSoonActivity
import com.cinescape1.ui.main.views.details.nowShowing.ShowTimesActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.LocaleHelper
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import com.haozhang.lib.SlantedTextView
import kotlinx.android.synthetic.main.activity_user_preferences.*
import kotlinx.android.synthetic.main.home_slider_item.view.*
import kotlinx.android.synthetic.main.recommended_item.view.*

class HomeChildAdapter(
    var context: Activity,
    private var recommendedList: ArrayList<HomeDataResponse.MovieData>,
    private val type: Int,
    private val comingSoon: Boolean,
    var listener : ImageChangeIcon) :
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
                    .error(R.drawable.placeholder_icon)
                    .into(holder.image)

            } else {

                val holder = holder as MyViewHolderRecommended

                listener.arabicClick(holder.imageView60)

                if (Constant.LANGUAGE == "ar"){
                    LocaleHelper.setLocale(context, "ar")
                    holder.imageView60.setImageResource(R.drawable.ar_tab)
                    holder.tag.setSlantedBackgroundColor(Color.parseColor(photoItem.tagColor))
                    holder.tag.mode = SlantedTextView.MODE_RIGHT
                    holder.tag.text = photoItem.tag

                }else if (Constant.LANGUAGE == "en"){
                    LocaleHelper.setLocale(context, "en")

//                    holder.imageView60.setImageResource(R.drawable.now_showing_diagonal)
                    holder.imageView60.setImageResource(R.drawable.en_tab)
                    holder.tag.text = photoItem.tag
                    holder.tag.setSlantedBackgroundColor(Color.parseColor(photoItem.tagColor))
                    holder.tag.mode = SlantedTextView.MODE_LEFT


                }

                Glide.with(context)
                    .load(photoItem.mobimgsmall)
                    .error(R.drawable.placeholder_icon)
                    .into(holder.thumbnail)

                println("tags--->${photoItem.tag}")
                if (photoItem.tag == "") {
//                    holder.background.hide()
                    holder.tag.hide()
                } else {
//                    holder.background.show()
                    holder.tag.show()
                    holder.tag.text = photoItem.tag
                    val tagColor = photoItem.tagColor
                    holder.tag.setSlantedBackgroundColor(Color.parseColor(photoItem.tagColor))
//                    holder.background.setColorFilter(Color.parseColor(tagColor))
                }

                if (comingSoon) {

                    holder.thumbnail.setOnClickListener {
                        val intent = Intent(holder.thumbnail.context, ComingSoonActivity::class.java)
                        intent.putExtra(Constant.IntentKey.MOVIE_ID, photoItem.id)
                        intent.putExtra("type", "comingSoon")
                        println("ComingSoonActivity2121--------->${"yes1"}")
                        holder.thumbnail.context.startActivity(intent)
                    }

                } else {

                    holder.thumbnail.setOnClickListener {
                        println("ComingSoonActivity2121--------->${"No1"}")
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
        var tag: SlantedTextView = view.findViewById(R.id.tag)
        var imageView60 = itemView.imageView60!!
    }

    class MyViewHolderSlider(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = itemView.image
    }

    interface ImageChangeIcon{
        fun arabicClick(imgArabic: ImageView)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(updatedList: ArrayList<HomeDataResponse.MovieData>) {
        recommendedList = updatedList
        notifyDataSetChanged()
    }
}