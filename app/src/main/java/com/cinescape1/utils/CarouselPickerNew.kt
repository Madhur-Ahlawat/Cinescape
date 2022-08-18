package com.cinescape1.utils

import `in`.goodiebag.carouselpicker.CarouselPicker
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.PagerAdapter
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CinemaSessionResponse

class CarouselPickerNew(
    var context: Context,
    items: ArrayList<CinemaSessionResponse.DaySession>,
    var viewpager: CarouselPicker?
) :
    PagerAdapter() {
    var items: ArrayList<CinemaSessionResponse.DaySession> =
        ArrayList<CinemaSessionResponse.DaySession>()
    var textColor = 0
    var rowIndex = 0

    override fun getCount(): Int {
        return items.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cinematxt, null)
        val tv = view.findViewById<View>(R.id.cinemaName) as TextView
        val pickerItem: CinemaSessionResponse.DaySession = items[position]
        tv.text = pickerItem.cinema.name
        rowIndex = viewpager?.currentItem!!
        if (rowIndex == position) {
            val regular: Typeface = context.resources.getFont(R.font.sf_pro_text_heavy)
            tv.typeface = regular
            tv.textSize = 20F
//                    holder.image.show()
        } else {
            val bold: Typeface = context.resources.getFont(R.font.sf_pro_text_regular)
            tv.textSize = 13f
            tv.typeface = bold
//                    holder.image.hide()

        }
        view.tag = position
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}