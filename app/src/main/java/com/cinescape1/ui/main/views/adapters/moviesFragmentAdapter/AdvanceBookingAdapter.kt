package com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
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

class AdvanceBookingAdapter(private var nowShowingList: List<MoviesResponse.AdvanceBooking>, context: Activity,
                            var listener : TypefaceListener1) :
    RecyclerView.Adapter<AdvanceBookingAdapter.MyViewHolderNowShowing>() {
    private var mContext = context
    private val displayMetrics = DisplayMetrics()
    private var screenWidth = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_now_showing_item, parent, false)
        mContext.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

        screenWidth = displayMetrics.widthPixels
        holder.thumbnail.layoutParams.width = ((screenWidth)/2.2f).toInt()

        holder.movieTitle.isSelected=true
        holder.movieCategory.isSelected=true

        if (Constant.LANGUAGE == "ar"){
            LocaleHelper.setLocale(mContext, "ar")
            holder.tag.text = comingSoonItem.tag
            holder.imageView60.setImageResource(R.drawable.ar_tab)
            holder.tag.setSlantedBackgroundColor(Color.parseColor(comingSoonItem.ratingColor))
            holder.tag.mode = SlantedTextView.MODE_RIGHT


//            holder.tag.rotation = 30f
//            (holder.tag.layoutParams as ConstraintLayout.LayoutParams).apply {
//                marginStart=20
////                        topMargin=2
//                marginEnd=20
//                bottomMargin=50
////                holder.tag.text = comingSoonItem.tag
////                        bottomMargin=8.dpToPixels()
//            }

        }else if (Constant.LANGUAGE == "en"){
            LocaleHelper.setLocale(mContext, "en")
            holder.tag.text = comingSoonItem.tag
            holder.imageView60.setImageResource(R.drawable.en_tab)
            holder.tag.setSlantedBackgroundColor(Color.parseColor(comingSoonItem.ratingColor))
            holder.tag.mode = SlantedTextView.MODE_LEFT

//            holder.tag.rotation = -30f
//            (holder.tag.layoutParams as ConstraintLayout.LayoutParams).apply {
//                marginStart=20
////              topMargin=2
//                marginEnd=20
//                bottomMargin=50
////                holder.tag.text = comingSoonItem.tag
//            }
        }

        if (comingSoonItem.tag == "") {
//                    holder.background.hide()
            holder.tag.hide()
        } else {
//                    holder.background.show()
            holder.tag.show()
            holder.tag.text = comingSoonItem.tag
            holder.tag.setSlantedBackgroundColor(Color.parseColor(comingSoonItem.tagColor))
//                    holder.background.setColorFilter(Color.parseColor(tagColor))
        }



        Glide.with(mContext)
            .load(comingSoonItem.mobimgsmall)
            .error(R.drawable.placeholder_icon)
            .into(holder.thumbnail)

        holder.movieTitle.text = comingSoonItem.title
        holder.type.text=comingSoonItem.rating

        println("RatingAdvance-------->${comingSoonItem.rating}")

        val ratingColor=comingSoonItem.ratingColor
        holder.type.setBackgroundColor(Color.parseColor(ratingColor))
//        holder.cardView.setBackgroundColor(Color.parseColor(ratingColor))

        if (comingSoonItem.language == null){
            holder.movieCategory.text = ""+comingSoonItem.genre
        }else{
            holder.movieCategory.text = comingSoonItem.language+" | "+comingSoonItem.genre
        }


        listener.typeFaceAdvanceBooking(holder.movieTitle,holder.movieCategory,holder.type)

        holder.thumbnail.setOnClickListener {
            val intent = Intent(holder.thumbnail.context, ShowTimesActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID, comingSoonItem.id)
            holder.thumbnail.context.startActivity(intent)
        }

        holder.thumbnail.setOnClickListener {
            val intent = Intent(holder.thumbnail.context, ShowTimesActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID, comingSoonItem.id)
            intent.putExtra("type","advance")
            holder.thumbnail.context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.image_now_showing)
        var imageView60: ImageView = view.findViewById(R.id.imageView60)
        var movieTitle: TextView = view.findViewById(R.id.text_movie_title)
        var movieCategory: TextView = view.findViewById(R.id.text_movie_category)
        var type: TextView = view.findViewById(R.id.movieRating)
        var tag: SlantedTextView = view.findViewById(R.id.tag)
        var cardView: CardView = view.findViewById(R.id.rating_ui)
    }

    @SuppressLint("NotifyDataSetChanged")
    public fun updateList(updatedList: ArrayList<MoviesResponse.AdvanceBooking>){
        nowShowingList = updatedList
        notifyDataSetChanged()
    }

    interface TypefaceListener1{
        fun typeFaceAdvanceBooking(movieTitle: TextView,movieCategory: TextView, type: TextView)
    }

}