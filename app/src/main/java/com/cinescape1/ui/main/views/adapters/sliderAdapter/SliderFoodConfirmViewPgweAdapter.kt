package com.cinescape1.ui.main.views.adapters.sliderAdapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class SliderFoodConfirmViewPgweAdapter(var layouts: ArrayList<Int>?, var context: Activity) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = context.layoutInflater.inflate(layouts!![position], container, false)
        container.addView(view)
        return view
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun getCount(): Int {
        println("layouts--->3---New---${layouts?.size}")

        return layouts?.size!!
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}