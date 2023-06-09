package com.cinescape1.ui.main.views.adapters

import android.app.Activity
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CinemaSessionResponse
import com.cinescape1.utils.Constant.Companion.select_pos
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

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val obj = movies[position]


        val transfer = CompositePageTransformer()
        //transfer.addTransformer(MarginPageTransformer(40))
        transfer.addTransformer(object : com.github.islamkhsh.viewpager2.ViewPager2.PageTransformer,
            ViewPager2.PageTransformer {
            override fun transformPage(page: View, position: Float) {
                println("rowIndex---->1->$position")
                rowIndex = viewpager?.currentItem!!
                val r = 1 - abs(position)
                try {
                    notifyDataSetChanged()
                }catch (e:Exception){

                }
            }

        })
        viewpager?.setPageTransformer(transfer)

        if (rowIndex == position) {
            val bold: Typeface = mContext.resources.getFont(R.font.sf_pro_text_bold)
            holder.cinemaName.typeface = bold
            holder.cinemaName.textSize = 17F
            holder.cinemaName.setTextColor(mContext.getColor(R.color.white))
//                    holder.image.show()
        } else {

            val bold: Typeface = mContext.resources.getFont(R.font.sf_pro_text_bold)
            holder.cinemaName.textSize = 17f
            holder.cinemaName.typeface = bold
            holder.cinemaName.setTextColor(mContext.getColor(R.color.white))
//                    holder.image.hide()
        }

        holder.cinemaName.text = obj.cinema.name
        println("holder.cinemaName21------>${holder.cinemaName.text}")

//        holder.address.text = obj.cinema.address1

    }
}