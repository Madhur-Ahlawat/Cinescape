package com.cinescape1.ui.main.views.home.fragments.movie.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.ui.main.views.details.nowShowing.ShowTimesActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.LocaleHelper
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import com.haozhang.lib.SlantedTextView

class AdapterNowShowing(
    private var nowShowingList: ArrayList<MoviesResponse.Nowshowing>, context: Activity) :
    RecyclerView.Adapter<AdapterNowShowing.MyViewHolderNowShowing>() {
    private var mContext = context
    private val displayMetrics = DisplayMetrics()
    private var screenWidth = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_now_showing_item_tesy, parent, false)
        return MyViewHolderNowShowing(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

//        mContext.windowManager.defaultDisplay.getMetrics(displayMetrics)
//        screenWidth = displayMetrics.widthPixels
//        holder.thumbnail.layoutParams.width = ((screenWidth)/2.25f).toInt()

        println("NowShowingScreenWidth-------->${((screenWidth)/2.27f).toInt()}--------->${comingSoonItem.mobimgsmall}")

//        holder.thumbnail.layout(0,0,0,0)

        Glide.with(mContext).load(comingSoonItem.mobimgsmall).error(R.drawable.app_icon).into(holder.thumbnail)

        holder.movieTitle.text = comingSoonItem.title
        holder.type.text = comingSoonItem.rating

        if (comingSoonItem.tag != "") {
            if (Constant.LANGUAGE == "ar") {
                LocaleHelper.setLocale(mContext, "ar")
                holder.imageView60.setImageResource(R.drawable.ar_tab)
                holder.tag.setSlantedBackgroundColor(Color.parseColor(comingSoonItem.tagColor))
                holder.tag.mode = SlantedTextView.MODE_RIGHT
                holder.tag.text = comingSoonItem.tag

            } else if (Constant.LANGUAGE == "en") {
                LocaleHelper.setLocale(mContext, "en")
                holder.imageView60.setImageResource(R.drawable.en_tab)
                holder.tag.text = comingSoonItem.tag
                holder.tag.setSlantedBackgroundColor(Color.parseColor(comingSoonItem.tagColor))
                holder.tag.mode = SlantedTextView.MODE_LEFT
            }

        }


        val ratingColor = comingSoonItem.ratingColor
        holder.type.setBackgroundColor(Color.parseColor(ratingColor))

        if (comingSoonItem.language == null) {
            holder.movieCategory.text = "" + " | " + comingSoonItem.genre
        } else {
            holder.movieCategory.text = comingSoonItem.language + " | " + comingSoonItem.genre
        }

        if (comingSoonItem.tag == "") {
//          holder.background.hide()
            holder.tag.hide()
        } else {
//          holder.background.show()
            holder.tag.show()
            holder.tag.text = comingSoonItem.tag
            val tagColor = comingSoonItem.tagColor
            holder.tag.setSlantedBackgroundColor(Color.parseColor(comingSoonItem.tagColor))
//          holder.background.setColorFilter(Color.parseColor(tagColor))
        }


        holder.thumbnail.setOnClickListener {
            val intent = Intent(holder.thumbnail.context, ShowTimesActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID, comingSoonItem.id)
            holder.thumbnail.context.startActivity(intent)

        }

    }


    override fun getItemCount(): Int {
        return nowShowingList.size
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.image_now_showing)
        var imageView60: ImageView = view.findViewById(R.id.imageView60)
        var movieTitle: TextView = view.findViewById(R.id.text_movie_title)
        var movieCategory: TextView = view.findViewById(R.id.text_movie_category)
        var type: TextView = view.findViewById(R.id.movieRating)
        var background: ImageView = view.findViewById(R.id.imageView60)
        var tag: SlantedTextView = view.findViewById(R.id.tag)
        var cardView: CardView = view.findViewById(R.id.rating_ui)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(updatedList: ArrayList<MoviesResponse.Nowshowing>) {
        nowShowingList = updatedList
        notifyDataSetChanged()
    }


}