package com.cinescape1.ui.main.views.adapters.accountPageAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.text.Html
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
import com.cinescape1.ui.main.views.finalTicket.FinalTicketActivity
import com.cinescape1.ui.main.views.food.FoodActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.hide
import com.cinescape1.utils.show


class UpcomingBookingAdapter(
    private val context: Context,
    private var upcomingBookingList: ArrayList<NextBookingResponse.Current> ,
    private var listener: RecycleViewItemClickListener,
    private var listenerMail: ReesendMailItemClickListener,

    ) :
    RecyclerView.Adapter<UpcomingBookingAdapter.MyViewHolderUpcomingBooking>() {
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderUpcomingBooking {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.account_booking_item, parent, false)
        return MyViewHolderUpcomingBooking(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderUpcomingBooking, position: Int) {
        val foodSelctedItem = upcomingBookingList[position]

        holder.textNameMovie.isSelected = true
        holder.btClick.isSelected = true

        Glide.with(mContext)
            .load(foodSelctedItem.posterhori)
            .placeholder(R.drawable.pos_not_avilbale)
            .into(holder.thumbnail)

        holder.textNameMovie.text = foodSelctedItem.moviename
        holder.rating.text = foodSelctedItem.mcensor

        holder.adressName.text = foodSelctedItem.cinemaname
        holder.screenNumber.text = foodSelctedItem.screenId.toString()
        holder.cinemaName.text = foodSelctedItem.experience
        holder.date.text = foodSelctedItem.showDate
        holder.times.text = foodSelctedItem.showTime


        val ratingColor=foodSelctedItem.ratingColor
        try {
            holder.rating.setBackgroundColor(Color.parseColor(ratingColor))
        }catch (e:Exception){
            e.printStackTrace()
        }
        if (foodSelctedItem.mcensor.isNullOrEmpty()) {
            holder.cardView.hide()
        } else {
            holder.cardView.show()

        }

        if (foodSelctedItem.addFood==true) {
            holder.btClick.show()
            holder.btClick.text = mContext.resources.getString(R.string.addFoodMsg)
            holder.btClick.setOnClickListener {
                val intent = Intent(mContext, FoodActivity::class.java)
                    .putExtra("CINEMA_ID", foodSelctedItem.cinemacode)
                    .putExtra("BOOKING", "FOOD")
                    .putExtra("type", "FOOD")
                mContext.startActivity(intent)
            }

        }else if (foodSelctedItem.foodPickup==true) {
            holder.btClick.show()
            holder.btClick.text = mContext.resources.getString(R.string.food_pickup_info)
            holder.btClick.setOnClickListener {
                val mDialogView =
                    LayoutInflater.from(context).inflate(R.layout.food_pickup_dialog, null)
                val mBuilder = AlertDialog.Builder(context, R.style.NewDialog).setView(mDialogView)
                val mAlertDialog = mBuilder.show()
                mAlertDialog.show()
                mAlertDialog.window?.setBackgroundDrawableResource(R.color.black70)
                val closeDialog = mDialogView.findViewById<TextView>(R.id.close_dialog)
                val text=mAlertDialog.findViewById<TextView>(R.id.textView105)

                   text?.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(foodSelctedItem.pickupInfo, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(foodSelctedItem.pickupInfo)
                }

                closeDialog.setOnClickListener {
                    mAlertDialog.dismiss()
                }
            }
        }else{
            holder.btClick.hide()
        }

        if (foodSelctedItem.trailerUrl == "") {
            holder.trailer.hide()
        } else {
            holder.trailer.hide()
        }
        if (!foodSelctedItem.cancelReserve){
            holder.cancelReservation.hide()
        }else{
            holder.cancelReservation.show()

        }

        holder.cancelReservation.setOnClickListener {
            listener.cancelReserv(foodSelctedItem)

        }
        holder.resendMail.setOnClickListener {
            listenerMail.resenDmail(foodSelctedItem)

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
    interface RecycleViewItemClickListener {
        fun cancelReserv(foodSelctedItem: NextBookingResponse.Current)


    }
    interface ReesendMailItemClickListener {
        fun resenDmail(foodSelctedItem: NextBookingResponse.Current)

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
        var rating: TextView = view.findViewById(R.id.text_types)
        var cardView: CardView = view.findViewById(R.id.ratingUi)
        var thumbnail: ImageView = view.findViewById(R.id.imageView7)
        var trailer: ImageView = view.findViewById(R.id.imageView30)
        var btClick: TextView = view.findViewById(R.id.food_pickup_btn)
        var cancelReservation: TextView = view.findViewById(R.id.imageView31)
        var resendMail: TextView = view.findViewById(R.id.imageView32)

    }
}