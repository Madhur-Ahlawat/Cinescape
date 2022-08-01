package com.cinescape1.ui.main.views.adapters.accountPageAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.NextBookingResponse
import com.cinescape1.ui.main.FinalTicketActivity
import com.cinescape1.ui.main.views.FoodActivity
import com.cinescape1.ui.main.views.PlayerActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.hide
import com.cinescape1.utils.show


class UpcomingBookingAdapter(private val context: Context, private var upcomingBookingList: ArrayList<NextBookingResponse.Current>) :
    RecyclerView.Adapter<UpcomingBookingAdapter.MyViewHolderUpcomingBooking>() {
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderUpcomingBooking {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.account_booking_item, parent, false)
        return MyViewHolderUpcomingBooking(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderUpcomingBooking, position: Int) {
        if (upcomingBookingList.isEmpty()) {
            holder.thumbnail.setImageResource(R.drawable.img_demo)
        } else {

            val foodSelctedItem = upcomingBookingList[position]
            Glide.with(mContext).load(foodSelctedItem.posterhori).placeholder(R.drawable.movie_default).into(holder.thumbnail)
            holder.textNameMovie.text = foodSelctedItem.moviename
            holder.textTypes.text = foodSelctedItem.mcensor

            holder.adressName.text = foodSelctedItem.cinemaname
            holder.screenNumber.text = foodSelctedItem.screenId.toString()
            holder.cinemaName.text = foodSelctedItem.experience
            holder.date.text = foodSelctedItem.showDate
            holder.times.text = foodSelctedItem.showTime

            if (foodSelctedItem.mcensor.isNullOrEmpty()){
                holder.cardView.hide()
            }else{
                holder.cardView.show()
                when (foodSelctedItem.mcensor) {
                    "PG" -> {
                        holder.cardView.setCardBackgroundColor(mContext.resources.getColor(R.color.grey))

                    }
                    "G" -> {
                        holder.cardView.setCardBackgroundColor(mContext.resources.getColor(R.color.green))

                    }
                    "18+" -> {
                        holder.cardView.setCardBackgroundColor(mContext.resources.getColor(R.color.red))

                    }
                    "13+" -> {
                        holder.cardView.setCardBackgroundColor(mContext.resources.getColor(R.color.yellow))

                    }
                    "E" -> {
                        holder.cardView.setCardBackgroundColor(mContext.resources.getColor(R.color.wowOrange))

                    }
                    "T" -> {
                        holder.cardView.setCardBackgroundColor(mContext.resources.getColor(R.color.tabIndicater))

                    }
                    else -> {
                        holder.cardView.setCardBackgroundColor(mContext.resources.getColor(R.color.blue))

                    }
                }

            }

            if (!foodSelctedItem.addFood){
                holder.btClick.text=mContext.resources.getString(R.string.addFoodMsg)
                holder.btClick.setOnClickListener {
                    val intent = Intent(mContext, FoodActivity::class.java)
                        .putExtra("CINEMA_ID", foodSelctedItem.cinemacode)
                        .putExtra("BOOKING", "FOOD")
                        .putExtra("type", "0")
                    mContext.startActivity(intent)
                }

            }else{
                holder.btClick.text=mContext.resources.getString(R.string.food_pickup_info)

                holder.btClick.setOnClickListener {
                    val mDialogView =
                        LayoutInflater.from(context).inflate(R.layout.food_pickup_dialog, null)
                    val mBuilder = AlertDialog.Builder(context,R.style.NewDialog).setView(mDialogView)
                    val mAlertDialog = mBuilder.show()
                    mAlertDialog.show()
                    mAlertDialog.window?.setBackgroundDrawableResource(R.color.black70)
                    val close_dialog = mDialogView.findViewById<TextView>(R.id.close_dialog)

                    close_dialog.setOnClickListener {
                        mAlertDialog.dismiss()
                    }
                }

            }
            if (foodSelctedItem.trailerUrl==""){
                holder.trailer.hide()
            }else{
                holder.trailer.show()
            }
            holder.trailer.setOnClickListener {
                val intent = Intent(mContext, PlayerActivity::class.java)
                intent.putExtra("trailerUrl", foodSelctedItem.trailerUrl)
                mContext.startActivity(intent)
            }
            holder.thumbnail.setOnClickListener {
                val intent = Intent(mContext, FinalTicketActivity::class.java)
                intent.putExtra(Constant.IntentKey.BOOKING_ID, foodSelctedItem.bookingId)
                intent.putExtra(Constant.IntentKey.TRANSACTION_ID, foodSelctedItem.transId.toString())
                intent.putExtra(Constant.IntentKey.BOOK_TYPE, foodSelctedItem.bookingType)
                intent.putExtra("FROM", "MTicket")
                mContext.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return if (upcomingBookingList.isNotEmpty()) upcomingBookingList.size else 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newFoodSelectedlist: List<NextBookingResponse.Current>) {
        upcomingBookingList = newFoodSelectedlist as ArrayList<NextBookingResponse.Current>
        notifyDataSetChanged()
    }
    class MyViewHolderUpcomingBooking(view: View) : RecyclerView.ViewHolder(view) {
        var adressName: TextView = view.findViewById(R.id.text_location_names)
        var screenNumber: TextView = view.findViewById(R.id.tv_no_of_screen)
        var cinemaName: TextView = view.findViewById(R.id.text_ss)
        var date: TextView = view.findViewById(R.id.txt_date)
        var times: TextView = view.findViewById(R.id.text_times2)
        var textNameMovie: TextView = view.findViewById(R.id.text_name_movie)
        var textTypes: TextView = view.findViewById(R.id.text_types)
        var cardView: CardView = view.findViewById(R.id.ratingUi)
        var thumbnail: ImageView = view.findViewById(R.id.imageView7)
        var trailer: ImageView = view.findViewById(R.id.imageView30)
        var btClick: TextView = view.findViewById(R.id.food_pickup_btn)

    }
}