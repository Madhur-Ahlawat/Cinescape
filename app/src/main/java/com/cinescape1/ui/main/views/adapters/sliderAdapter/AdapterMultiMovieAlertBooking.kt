package com.cinescape1.ui.main.views.adapters.sliderAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.NextBookingResponse
import com.cinescape1.ui.main.views.finalTicket.FinalTicketActivity
import com.cinescape1.utils.Constant
import kotlinx.android.synthetic.main.alert_booking.view.*

class AdapterMultiMovieAlertBooking(context: Context,
    private var sliderMultiMovieItemList: ArrayList<NextBookingResponse.Current>, var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<AdapterMultiMovieAlertBooking.MyViewHolderMultiMovieAlertBooking>() {
    private var mContext = context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolderMultiMovieAlertBooking {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return MyViewHolderMultiMovieAlertBooking(inflate)
    }

    override fun onBindViewHolder(holder: MyViewHolderMultiMovieAlertBooking, position: Int) {
        val showtimeListItem = sliderMultiMovieItemList[position]

        holder.title.isSelected = true

        holder.title.text = showtimeListItem.moviename
        holder.location.text = showtimeListItem.cinemaname
        holder.screen.text = showtimeListItem.screenId.toString()
        holder.date.text = showtimeListItem.showDate
        holder.time.text = showtimeListItem.showTime
        holder.rating.text = showtimeListItem.mcensor
        val ratingColor=showtimeListItem.ratingColor

        try {
            holder.rating.setBackgroundColor(Color.parseColor(ratingColor))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.image.setOnClickListener {
            val intent = Intent(mContext, FinalTicketActivity::class.java)
            intent.putExtra(Constant.IntentKey.BOOKING_ID, showtimeListItem.bookingId)
            intent.putExtra(Constant.IntentKey.TRANSACTION_ID, showtimeListItem.transId.toString())
            intent.putExtra(Constant.IntentKey.BOOK_TYPE, showtimeListItem.bookingType)
            intent.putExtra("FROM", "MTicket")
            mContext.startActivity(intent)
        }

        Glide.with(mContext)
            .load(showtimeListItem.posterhori)
            .placeholder(R.drawable.movie_default)
            .into(holder.image)

        Glide.with(mContext)
            .load(showtimeListItem.experienceIcon)
            .into(holder.experience)


        when (showtimeListItem.experience) {
            "4DX" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.four_dx)
                    .into(holder.experience)

            }
            "STANDARD" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.standard)
                    .into(holder.experience)
            }
            "VIP" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.vip)
                    .into(holder.experience)
            }
            "IMAX" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.imax)
                    .into(holder.experience)
            }
            "3D" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.threed_black)
                    .into(holder.experience)
            }
            "DOLBY" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.dolby_black)
                    .into(holder.experience)
            }
            "ELEVEN" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.eleven_black)
                    .into(holder.experience)
            }
            "SCREENX" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.screenx_black)
                    .into(holder.experience)
            }
            "PREMIUM" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.premium_black)
                    .into(holder.experience)
            }
        }


        holder.bookings.text = mContext.getString(R.string.go_to_bookings)
        holder.bookings.setOnClickListener {
            listener.onDateClick(showtimeListItem)
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

    class MyViewHolderMultiMovieAlertBooking(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.text_bombshell
        var location: TextView = view.text_location_name
        var screen: TextView = view.text_screen_number
        var date: TextView = view.text_date_visible
        var time: TextView = view.text_time_visible
        var rating: TextView = view.text13
        var image: ImageView = view.image_booking_alert
        var experience: ImageView = view.text_experience_name
        var ratingUi: CardView = view.ratingUi
        var bookings: TextView = view.go_to_booking_btn1
    }
    interface RecycleViewItemClickListener {
        fun onDateClick(showtimeListItem: NextBookingResponse.Current)
    }
}