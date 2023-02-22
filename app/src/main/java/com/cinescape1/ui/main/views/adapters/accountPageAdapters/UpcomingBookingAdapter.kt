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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.NextBookingResponse
import com.cinescape1.ui.main.views.finalTicket.FinalTicketActivity
import com.cinescape1.ui.main.views.food.FoodActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import kotlinx.android.synthetic.main.alert_booking.view.*

class UpcomingBookingAdapter(
    private val context: Context,
    private var upcomingBookingList: ArrayList<NextBookingResponse.Current> ,
    private var listener: RecycleViewItemClickListener,
    private var listenerMail: ReesendMailItemClickListener,
    private var listenerFoodPrepare: RecycleViewItemFoodPrepare
    ) :
    RecyclerView.Adapter<UpcomingBookingAdapter.MyViewHolderUpcomingBooking>() {
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderUpcomingBooking {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.account_booking_item, parent, false)
        return MyViewHolderUpcomingBooking(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderUpcomingBooking, position: Int) {
        val foodSelctedItem = upcomingBookingList[position]

        holder.textNameMovie.isSelected = true

//        holder.btClick.isSelected = true

        Glide.with(mContext)
            .load(foodSelctedItem.posterhori)
            .placeholder(R.drawable.pos_not_avilbale)
            .into(holder.thumbnail)

        holder.textNameMovie.text = foodSelctedItem.moviename
        holder.rating.text = foodSelctedItem.mcensor

        holder.adressName.text = foodSelctedItem.cinemaname
        holder.screenNumber.text = foodSelctedItem.screenId.toString()
//        holder.cinemaName.text = foodSelctedItem.experience
        holder.date.text = foodSelctedItem.showDate
        holder.times.text = foodSelctedItem.showTime

        holder.txtFoodPickupId.text = foodSelctedItem.pickUpNumber

        println("foodSelectedItem.experience---->${foodSelctedItem.experience}")

        when (foodSelctedItem.experience) {
            "4DX" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.four_dx)
                    .into(holder.cinemaName)

            }
            "Standard" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.standard)
                    .into(holder.cinemaName)
            }
            "VIP" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.vip)
                    .into(holder.cinemaName)
            }
            "IMAX" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.imax)
                    .into(holder.cinemaName)
            }
            "3D" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.threed_black)
                    .into(holder.cinemaName)
            }
            "DOLBY" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.dolby_black)
                    .into(holder.cinemaName)
            }
            "ELEVEN" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.eleven_black)
                    .into(holder.cinemaName)
            }
            "SCREENX" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.screenx_black)
                    .into(holder.cinemaName)
            }
            "PREMIUM" -> {
                Glide
                    .with(mContext)
                    .load(R.drawable.premium_black)
                    .into(holder.cinemaName)
            }
        }


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

//        if (foodSelctedItem.addFood==true) {
//            holder.btClick.show()
//            holder.btClick.text = mContext.resources.getString(R.string.addFoodMsg)
//            holder.btClick.setOnClickListener {
//                val intent = Intent(mContext, FoodActivity::class.java)
//                    .putExtra("CINEMA_ID", foodSelctedItem.cinemacode)
//                    .putExtra("BOOKING", "FOOD")
//                    .putExtra("type", "FOOD")
//                mContext.startActivity(intent)
//            }
//
//        }else if (foodSelctedItem.foodPickup==true) {
//            holder.btClick.show()
//            holder.btClick.text = mContext.resources.getString(R.string.food_pickup_info)
//            holder.btClick.setOnClickListener {
//                val mDialogView =
//                    LayoutInflater.from(context).inflate(R.layout.food_pickup_dialog, null)
//                val mBuilder = AlertDialog.Builder(context, R.style.NewDialog).setView(mDialogView)
//                val mAlertDialog = mBuilder.show()
//                mAlertDialog.show()
//                mAlertDialog.window?.setBackgroundDrawableResource(R.color.black70)
//                val closeDialog = mDialogView.findViewById<TextView>(R.id.close_dialog)
//                val text=mAlertDialog.findViewById<TextView>(R.id.textView105)
//
//                   text?.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    Html.fromHtml(foodSelctedItem.pickupInfo, Html.FROM_HTML_MODE_COMPACT)
//                } else {
//                    Html.fromHtml(foodSelctedItem.pickupInfo)
//                }
//
//                closeDialog.setOnClickListener {
//                    mAlertDialog.dismiss()
//                }
//            }
//        }else{
//
//            holder.btClick.hide()
//        }

        println("foodSelectedItem.food-------->${foodSelctedItem.food}")

        if (foodSelctedItem.food == 0){
            holder.btFoodPrepare.hide()
            holder.foodAddBtn.hide()
            holder.consFoodPickupId.hide()
        }

        if (foodSelctedItem.food == 1){
            holder.btFoodPrepare.hide()
            holder.foodAddBtn.show()
            holder.consFoodPickupId.hide()
        }

        if (foodSelctedItem.food == 2) {
            holder.btFoodPrepare.show()
            holder.foodAddBtn.hide()
            holder.consFoodPickupId.hide()
            holder.btFoodPrepare.background = ContextCompat.getDrawable(mContext,R.drawable.food_pickup_bg)
            holder.btFoodPrepare.isClickable = false
        }

        if (foodSelctedItem.food == 3){
            holder.btFoodPrepare.show()
            holder.foodAddBtn.hide()
            holder.consFoodPickupId.hide()
            holder.btFoodPrepare.isClickable = true
        }

        if (foodSelctedItem.food == 4){
            holder.btFoodPrepare.hide()
            holder.foodAddBtn.hide()
            holder.consFoodPickupId.show()
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
            intent.putExtra("FROM_ACCOUNT", "account")
            mContext.startActivity(intent)
        }

        holder.btFoodPrepare.setOnClickListener {
            listenerFoodPrepare.foodPrepareClick(foodSelctedItem)
        }

        holder.foodAddBtn.setOnClickListener {

            val intent = Intent(mContext, FoodActivity::class.java)
                    .putExtra("CINEMA_ID", foodSelctedItem.cinemacode)
                    .putExtra("BOOKING", "FOOD")
                    .putExtra("type", "FOOD")
                mContext.startActivity(intent)
        }

        holder.consFoodPickupId.setOnClickListener {

            val mDialogView = LayoutInflater.from(context).inflate(R.layout.food_pickup_dialog, null)
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

    }


    interface RecycleViewItemFoodPrepare {
        fun foodPrepareClick(foodPrepareItem: NextBookingResponse.Current)
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newFoodSelectedlist: List<NextBookingResponse.Current>) {
        upcomingBookingList = newFoodSelectedlist as ArrayList<NextBookingResponse.Current>
        notifyDataSetChanged()
    }

    class MyViewHolderUpcomingBooking(view: View) : RecyclerView.ViewHolder(view) {
        var adressName: TextView = view.findViewById(R.id.text_location_names)
        var screenNumber: TextView = view.findViewById(R.id.tv_no_of_screen)
        var cinemaName: ImageView = view.findViewById(R.id.text_ss)
        var date: TextView = view.findViewById(R.id.txt_date)
        var times: TextView = view.findViewById(R.id.text_times2)
        var textNameMovie: TextView = view.findViewById(R.id.text_name_movie)
        var rating: TextView = view.findViewById(R.id.text_types)
        var cardView: CardView = view.findViewById(R.id.ratingUi)
        var thumbnail: ImageView = view.findViewById(R.id.imageView7)
        var trailer: ImageView = view.findViewById(R.id.imageView30)

        var btFoodPrepare: TextView = view.findViewById(R.id.food_pickup_btn)
        var foodAddBtn: TextView = view.findViewById(R.id.foodAddBtn)
        var consFoodPickupId: ConstraintLayout = view.findViewById(R.id.consFoodPickupId)

        var txtFoodPickupId: TextView = view.findViewById(R.id.textView167)

        var cancelReservation: TextView = view.findViewById(R.id.imageView31)
        var resendMail: TextView = view.findViewById(R.id.imageView32)

    }
}