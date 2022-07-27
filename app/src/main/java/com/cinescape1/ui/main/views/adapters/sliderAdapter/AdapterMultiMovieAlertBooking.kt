package com.cinescape1.ui.main.views.adapters.sliderAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.NextBookingResponse
import com.cinescape1.ui.main.FinalTicketActivity
import com.cinescape1.utils.Constant
import kotlinx.android.synthetic.main.booking_alert2_item.view.*

class AdapterMultiMovieAlertBooking(context: Context, private var sliderMultiMovieItemList: ArrayList<NextBookingResponse.Current>) :
    RecyclerView.Adapter<AdapterMultiMovieAlertBooking.MyViewHolderMultiMovieAlertBooking>() {
    private var mContext = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderMultiMovieAlertBooking {
        val inflate: View = LayoutInflater.from(parent.context).inflate(R.layout.booking_alert2_item, parent,false)
        return MyViewHolderMultiMovieAlertBooking(inflate)
    }

    override fun onBindViewHolder(holder: MyViewHolderMultiMovieAlertBooking, position: Int) {
        if (sliderMultiMovieItemList.isEmpty()) {
//            holder.showTimeTitleList.setImageResource(R.drawable.img_demo)
        } else {
            val showtimeListItem = sliderMultiMovieItemList[position]
            holder.tvCinemas.text = showtimeListItem.cinemaname
            holder.textviewDates.text = showtimeListItem.showDate
            holder.textviewTime1.text = showtimeListItem.showTime
            Glide.with(mContext)
                .load(showtimeListItem.poster)
                .placeholder(R.drawable.movie_default)
                .into(holder.imgFilm)
            holder.itemView.setOnClickListener {
//                Toast.makeText(mContext, "This is item in position " + position, Toast.LENGTH_SHORT).show()
                val intent = Intent(mContext, FinalTicketActivity::class.java)
                intent.putExtra(Constant.IntentKey.BOOKING_ID, showtimeListItem.bookingId)
                intent.putExtra(Constant.IntentKey.TRANSACTION_ID, showtimeListItem.transId.toString())
                intent.putExtra(Constant.IntentKey.BOOK_TYPE, showtimeListItem.bookingType)
                mContext.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return if (sliderMultiMovieItemList.isNotEmpty()) sliderMultiMovieItemList.size else 0
    }
    @SuppressLint("NotifyDataSetChanged")
    fun renewItems(sliderItems: ArrayList<NextBookingResponse.Current>) {
        this.sliderMultiMovieItemList = sliderItems
        notifyDataSetChanged()
    }

    class MyViewHolderMultiMovieAlertBooking (view: View) : RecyclerView.ViewHolder(view) {
        var tvCinemas: TextView = view.tv_cinemas
        var textviewDates: TextView = view.textView_dates
        var textviewTime1: TextView = view.textView_time1
        var imgFilm: ImageView = view.img_film
    }

}