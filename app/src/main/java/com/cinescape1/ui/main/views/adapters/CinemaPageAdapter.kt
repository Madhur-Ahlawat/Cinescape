package com.cinescape1.ui.main.views.adapters

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CinemaSessionResponse
import com.cinescape1.utils.Constant
import com.cinescape1.utils.hide
import com.cinescape1.utils.invisible
import com.cinescape1.utils.show
import com.github.islamkhsh.CardSliderAdapter
import com.github.islamkhsh.CardSliderViewPager
import kotlinx.android.synthetic.main.activity_show_times.view.*
import kotlinx.android.synthetic.main.item_cinematxt.view.*


class CinemaPageAdapter(
    private val mContext: Activity,
    private val movies: ArrayList<CinemaSessionResponse.DaySession>,
    private val viewpager: CardSliderViewPager?
) : CardSliderAdapter<CinemaPageAdapter.MovieViewHolder>() {

    private var rowIndex = 0
    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cinematxt, parent, false)
        return MovieViewHolder(view)
    }

    override fun bindVH(holder: MovieViewHolder, position: Int) {
        val obj = movies[position]

        viewpager?.registerOnPageChangeCallback(object :
            com.github.islamkhsh.viewpager2.ViewPager2.OnPageChangeCallback() {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                rowIndex = viewpager.currentItem
                println("viewpager.currentItem---$rowIndex----$position")

                if (rowIndex == Constant.select_pos) {
                    val regular: Typeface = mContext.resources.getFont(R.font.sf_pro_text_heavy)
                    holder.cinemaName.typeface = regular
                    holder.cinemaName.textSize = 20F
//                    holder.image.show()
                } else {
                    val bold: Typeface = mContext.resources.getFont(R.font.sf_pro_text_regular)
                    holder.cinemaName.textSize = 13f
                    holder.cinemaName.typeface = bold
//                    holder.image.hide()

                }
            }

        })

        holder.cinemaName.text = obj.cinema.name
//        holder.address.text = obj.cinema.address1

    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cinemaName = itemView.cinemaName!!
//        var address = itemView.textView110!!
//        var image = itemView.imageView48!!

    }
}