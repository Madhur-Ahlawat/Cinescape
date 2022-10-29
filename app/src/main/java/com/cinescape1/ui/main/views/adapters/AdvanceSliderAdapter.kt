package com.cinescape1.ui.main.views.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
    var ratingColor:String=""
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val sliderLayout: View = inflater.inflate(R.layout.slider_advance, null)
        val featured_image = sliderLayout.findViewById<ImageView>(R.id.my_featured_image)
        val caption_title = sliderLayout.findViewById<TextView>(R.id.my_caption_title)
        val rating = sliderLayout.findViewById<TextView>(R.id.textView77)
        Glide.with(mContext)
            .load(movieData[position].mobadvance)
            .placeholder(R.drawable.placeholder_advance_booking_banner)
            .into(featured_image)
        caption_title.text= movieData[position].title
        rating.text= movieData[position].rating
        ratingColor=movieData[position].ratingColor
        try {
            rating.setBackgroundColor(Color.parseColor(ratingColor))
        }catch (e:Exception){
            e.printStackTrace()
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
