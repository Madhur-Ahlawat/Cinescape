package com.cinescape1.ui.main.views.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.ui.main.views.details.nowShowing.ShowTimesActivity
import com.cinescape1.utils.Constant


class SliderBackAdapter(
    private val mContext: Activity, private var movieData: ArrayList<HomeDataResponse.MovieData>
) : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val sliderLayout: View = inflater.inflate(R.layout.slider_back, null)
        val gradient = sliderLayout.findViewById<ImageView>(R.id.gradientView)

        Toast.makeText(mContext, position.toString(), Toast.LENGTH_SHORT).show()
        println("sliderImage---->${(movieData[position].sliderimgurl)}")
//
//        gradient.apply {
//            // Set Color Start
//            start = movieData[position].sliderimgurl.hashCode()
//            alphaStart = 0.5f
//            // Set Color End
//            end = Color.TRANSPARENT
//            alphaEnd = 0.0f
//
//            // Set Gradient Direction
//            direction = GradientView.GradientDirection.TOP_TO_BOTTOM
//        }

        gradient.setOnClickListener {
            val intent = Intent(mContext, ShowTimesActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID, movieData[position].id)
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
