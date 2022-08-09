package com.cinescape1.ui.main.views.adapters.sliderAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.cinescape1.ui.main.FinalTicketActivity
import com.cinescape1.utils.Constant
import kotlinx.android.synthetic.main.alert_booking.view.*

class AdapterMultiMovieAlertBooking(
    context: Context,
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
        holder.experience.text = showtimeListItem.experience

        when (showtimeListItem.mcensor) {
            "PG" -> {
                holder.ratingUi.setCardBackgroundColor(mContext.getColor(R.color.grey))

            }
            "G" -> {
                holder.ratingUi.setCardBackgroundColor(mContext.getColor(R.color.green))

            }
            "18+" -> {
                holder.ratingUi.setCardBackgroundColor(mContext.getColor(R.color.red))

            }
            "13+" -> {
                holder.ratingUi.setCardBackgroundColor(mContext.getColor(R.color.yellow))

            }
            "15+" -> {
                holder.ratingUi.setCardBackgroundColor(mContext.getColor(R.color.yellow))

            }
            "E" -> {
                holder.ratingUi.setCardBackgroundColor(mContext.getColor(R.color.wowOrange))

            }
            "T" -> {
                holder.ratingUi.setCardBackgroundColor(mContext.getColor(R.color.tabIndicater))

            }
            else -> {
                holder.ratingUi.setCardBackgroundColor(mContext.getColor(R.color.blue))

            }
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
        var experience: TextView = view.text_experience_name
        var ratingUi: CardView = view.ratingUi
        var bookings: TextView = view.go_to_booking_btn1
    }
    interface RecycleViewItemClickListener {
        fun onDateClick(showtimeListItem: NextBookingResponse.Current)
    }
}