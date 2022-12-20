package com.cinescape1.ui.main.views.home.fragments.movie.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.ui.main.views.details.ShowTimesActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.LocaleHelper
import com.cinescape1.utils.hide
import com.cinescape1.utils.show

class AdapterNowShowing(
    private var nowShowingList: ArrayList<MoviesResponse.Nowshowing>,
    context: Context, var listener : TypefaceListenerNowShowing) :
    RecyclerView.Adapter<AdapterNowShowing.MyViewHolderNowShowing>() {
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_now_showing_item, parent, false)
        return MyViewHolderNowShowing(view)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]
        Glide.with(mContext)
            .load(comingSoonItem.mobimgsmall)
            .error(R.drawable.app_icon)
            .into(holder.thumbnail)

        holder.movieTitle.text = comingSoonItem.title
        holder.type.text = comingSoonItem.rating

        if (Constant.LANGUAGE == "ar"){
            LocaleHelper.setLocale(mContext, "ar")
//            holder.imageView60.setImageResource(R.drawable.arebic_red_icon)
            holder.imageView60.setImageResource(R.drawable.ar_tab)
            holder.tag.rotation = 30f
            (holder.tag.layoutParams as ConstraintLayout.LayoutParams).apply {
                marginStart=20
//                        topMargin=2
                marginEnd=80
                bottomMargin=20
                holder.tag.text = comingSoonItem.tag
//                        bottomMargin=8.dpToPixels()
            }

        }else if (Constant.LANGUAGE == "en"){
            LocaleHelper.setLocale(mContext, "en")
//            holder.imageView60.setImageResource(R.drawable.now_showing_diagonal)
            holder.imageView60.setImageResource(R.drawable.en_tab)
            holder.tag.rotation = -30f
            (holder.tag.layoutParams as ConstraintLayout.LayoutParams).apply {
                marginStart=20
//              topMargin=2
                marginEnd=80
                bottomMargin=20
                holder.tag.text = comingSoonItem.tag
            }
        }

        if (comingSoonItem.tag == "") {
            holder.background.hide()
            holder.tag.hide()
        } else {
            holder.background.show()
            holder.tag.show()
            holder.tag.text = comingSoonItem.tag
            val tagColor = comingSoonItem.tagColor
            holder.background.setColorFilter(Color.parseColor(tagColor))
        }
        val ratingColor = comingSoonItem.ratingColor
        holder.type.setBackgroundColor(Color.parseColor(ratingColor))

        holder.movieCategory.text = comingSoonItem.language + " | " + comingSoonItem.genre

        listener.typeFaceNowShowing(holder.movieTitle, holder.movieCategory, holder.type, holder.tag)

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
        var tag: TextView = view.findViewById(R.id.tag)
        var cardView: CardView = view.findViewById(R.id.rating_ui)
    }

    fun updateList(updatedList: ArrayList<MoviesResponse.Nowshowing>) {
        nowShowingList = updatedList
        notifyDataSetChanged()
    }

    interface TypefaceListenerNowShowing{
        fun typeFaceNowShowing(movieTitle: TextView,movieCategory: TextView,type: TextView,tag: TextView)
    }

}