package com.cinescape1.ui.main.views.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.ui.main.views.details.ShowTimesActivity
import com.cinescape1.utils.Constant


class AdvanceSliderAdapter(private  val mContext: Activity,private var movieData: ArrayList<HomeDataResponse.MovieData>) :
    PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val sliderLayout: View = inflater.inflate(R.layout.slider_advance, null)
        val featured_image = sliderLayout.findViewById<ImageView>(R.id.my_featured_image)
        val caption_title = sliderLayout.findViewById<TextView>(R.id.my_caption_title)
        val rating = sliderLayout.findViewById<TextView>(R.id.textView77)
        Glide.with(mContext)
            .load(movieData[position].mobimgbig)
            .placeholder(R.drawable.placeholder_advance_booking_banner)
            .into(featured_image)
        caption_title.text= movieData[position].title
        rating.text= movieData[position].rating
        when (movieData[position].rating) {
            "PG" -> {
                rating.setBackgroundResource(R.color.grey)
//                ratingUi.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey))

            }
            "G" -> {
                rating.setBackgroundResource(R.color.green)
//                ratingUi.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green))

            }
            "18+" -> {
                rating.setBackgroundResource(R.color.red)
//                ratingUi.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red))

            }
            "13+" -> {
                rating.setBackgroundResource(R.color.yellow)
//                ratingUi.setBackgroundColor(ContextCompat.getColor(mContext, R.color.yellow))

            }
            "15+" -> {
                rating.setBackgroundResource(R.color.yellow)
//                ratingUi.setBackgroundColor(ContextCompat.getColor(mContext, R.color.yellow))

            }
            "E" -> {
                rating.setBackgroundResource(R.color.wowOrange)
//                ratingUi.setBackgroundColor(ContextCompat.getColor(mContext, R.color.wowOrange))

            }
            "T" -> {
                rating.setBackgroundResource(R.color.tabIndicater)
//                ratingUi.setBackgroundColor(ContextCompat.getColor(mContext, R.color.tabIndicater))

            }
            else -> {
                rating.setBackgroundResource(R.color.blue)

//                ratingUi.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue))
//                cardView.setBackgroundColor(con.getColor(this, R.color.my_color));

            }
        }

        featured_image.setOnClickListener {
            val intent = Intent(mContext, ShowTimesActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID,movieData[position].id)
//            intent.putExtra("type","movie")
            mContext.startActivity(intent)
        }

        container.addView(sliderLayout)
        return sliderLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return movieData.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    init {
        this.movieData = movieData
    }
}
