package com.cinescape1.ui.main.views.adapters

import android.app.Activity
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CinemaSessionResponse
import com.cinescape1.utils.hide
import com.cinescape1.utils.invisible
import com.cinescape1.utils.show
import com.github.islamkhsh.CardSliderAdapter
import com.github.islamkhsh.CardSliderViewPager
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

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                rowIndex = viewpager.currentItem

                if (rowIndex == position) {
                    val bold: Typeface = mContext.resources.getFont(R.font.sf_pro_text_heavy)
                    holder.cinemaName.textSize = 20f
                    holder.cinemaName.typeface = bold
                    holder.image.show()
                    holder.address.show()
                } else {
                    val regular: Typeface = mContext.resources.getFont(R.font.sf_pro_text_regular)
                    holder.cinemaName.typeface = regular
                    holder.cinemaName.textSize = 13F
                    holder.image.invisible()
                    holder.address.hide()

                }
            }

        })











        holder.cinemaName.text = obj.cinema.name
        holder.address.text = obj.cinema.address1

    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cinemaName = itemView.cinemaName!!
        var address = itemView.textView110!!
        var image = itemView.imageView48!!

    }
}