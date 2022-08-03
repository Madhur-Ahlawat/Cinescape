package com.cinescape1.ui.main.views.adapters

import `in`.goodiebag.carouselpicker.CarouselPicker
import android.app.Activity
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CinemaSessionResponse
import com.cinescape1.utils.Constant.Companion.select_pos
import com.github.islamkhsh.CardSliderAdapter
import com.github.islamkhsh.CardSliderViewPager
import kotlinx.android.synthetic.main.activity_show_times.view.*
import kotlinx.android.synthetic.main.item_cinematxt.view.*
import kotlin.math.abs


class CinemaPageAdapter(
    private val mContext: Activity,
    private val movies: ArrayList<CinemaSessionResponse.DaySession>,
    private val viewpager: ViewPager2?
) : RecyclerView.Adapter<CinemaPageAdapter.MovieViewHolder>() {

    private var rowIndex = 0
    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cinematxt, parent, false)
        return MovieViewHolder(view)
    }


    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cinemaName = itemView.cinemaName!!
//        var address = itemView.textView110!!
//        var image = itemView.imageView48!!

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val obj = movies[position]

//        val textAdapter = CarouselPicker.CarouselViewAdapter(mContext, textItems, 0)

        val transfer = CompositePageTransformer()
        //transfer.addTransformer(MarginPageTransformer(40))
        transfer.addTransformer(object : com.github.islamkhsh.viewpager2.ViewPager2.PageTransformer,
            androidx.viewpager2.widget.ViewPager2.PageTransformer {
            override fun transformPage(page: View, position: Float) {
                println("rowIndex---->1->$position")
                rowIndex = viewpager?.currentItem!!
                val r = 1- abs(position)
                //page.scaleY = (0.85f+ r*0.14f)
                try {
                    notifyDataSetChanged()
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

        })
        viewpager?.setPageTransformer(transfer)

        println("rowIndex---->123$rowIndex----$position----$select_pos")
        if (rowIndex == position) {
            val regular: Typeface = mContext.resources.getFont(R.font.sf_pro_text_heavy)
            holder.cinemaName.typeface = regular
            holder.cinemaName.textSize = 17F
            holder.cinemaName.setTextColor(mContext.getColor(R.color.white))
//                    holder.image.show()
        } else {
            val bold: Typeface = mContext.resources.getFont(R.font.sf_pro_text_regular)
            holder.cinemaName.textSize = 11f
            holder.cinemaName.typeface = bold
            holder.cinemaName.setTextColor(mContext.getColor(R.color.text_color))

//                    holder.image.hide()

        }

        holder.cinemaName.text = obj.cinema.name
//        holder.address.text = obj.cinema.address1

    }
}