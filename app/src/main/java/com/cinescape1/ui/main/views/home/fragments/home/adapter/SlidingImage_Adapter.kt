package com.cinescape1.ui.main.views.home.fragments.home.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.cinescape1.R
import com.cinescape1.data.models.ModelSliderHome
import java.util.*

class SlidingImage_Adapter(private val context: Context, private val IMAGES: ArrayList<ModelSliderHome>) : PagerAdapter() {
    private val inflater: LayoutInflater
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return IMAGES.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false)!!
        val imageView = imageLayout.findViewById<ImageView>(R.id.image)

        val imageItem = IMAGES[position]
        imageView.setImageResource(imageItem.imgUrl)
        view.addView(imageLayout, 0)
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}
    override fun saveState(): Parcelable? {
        return null
    }

    init {
        inflater = LayoutInflater.from(context)
    }
}