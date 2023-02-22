package com.cinescape1.ui.main.views.finalTicket.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.NextBookingResponse
import com.cinescape1.data.models.responseModel.TicketSummaryResponse
import com.cinescape1.ui.main.views.adapters.HomeChildAdapter
import com.cinescape1.ui.main.views.adapters.SeatListAdapter
import com.cinescape1.ui.main.views.adapters.checkoutAdapter.AdapterCheckoutConFirmFoodDetail
import com.cinescape1.ui.main.views.finalTicket.model.FinalTicketLocalModel
import com.cinescape1.ui.main.views.food.FoodActivity
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.alert_booking.view.*
import kotlinx.android.synthetic.main.final_ticket_item_one.view.*
import kotlinx.android.synthetic.main.final_ticket_item_three.view.*
import kotlinx.android.synthetic.main.final_ticket_item_two.view.*

class FinalTicketParentAdapter(
    var mContext: Activity,
    private var homeDataList: ArrayList<FinalTicketLocalModel>,
    private var output: TicketSummaryResponse.Output,
    var listener1: TypeFaceFinalTicket0ne, var listener2: TypeFaceFinalTicketTwo,
    var listener3: TypeFaceFinalTicketThree,
    var listenerFoodPrepare: RecycleViewItemFoodPrepare,
    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var adapter: HomeChildAdapter? = null
    var cinemaId = ""
    var payMode = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val headerLayout =
                    LayoutInflater.from(parent.context).inflate(R.layout.final_ticket_item_one, parent, false)
                MyViewHolder(headerLayout)
            }
            2 -> {
                val headerLayout =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.final_ticket_item_two, parent, false)
                MyViewHolderTwo(headerLayout)
            }
            else -> {
                val headerLayout =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.final_ticket_item_three, parent, false)
                MyViewHolderThree(headerLayout)
            }
        }
    }

    override fun getItemCount(): Int {
        println("homeDataList---" + homeDataList.size)
        return homeDataList.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //One
        var oneBookingId = itemView.text_bookin_id_no!!
        var oneTitle = itemView.text_film_house_name!!
        var oneRating = itemView.text_types!!
        var oneLocation = itemView.text_location_names!!
        var oneDateTime = itemView.txt_date!!
        var oneScreen = itemView.txt_scrrens!!
        var oneType = itemView.tv_screenx!!
        var oneCategoryName = itemView.categoryName!!
        var cancelReservation = itemView.textView48!!
        var oneSeatList = itemView.seats_list!!
        var onePayMode = itemView.text_wallet!!
        var addFood = itemView.textView49!!
        var onePrice = itemView.text_kd_total_ticket_price!!

        var btFoodPrepare = itemView.btnPreparedFood
        var foodAddBtn = itemView.btnAddFood
        var consFoodPickupId = itemView.consFoodPickupId
        var txtFoodPickupId = itemView.textView167
    }

    class MyViewHolderTwo(view: View) : RecyclerView.ViewHolder(view) {
        //two
        var twoBookingId = itemView.text_bookin_id_no1!!
        var twoPickupInfo = itemView.textFoodPicUp!!
        var twoFood = itemView.recyclerview_food_details!!
        var twoPayMode = itemView.text_wallet1!!
        var twoPayPrice = itemView.text_kd_total_ticket_price1!!
        var text_payment_mode2 = itemView.text_payment_mode2!!
    }

    class MyViewHolderThree(view: View) : RecyclerView.ViewHolder(view) {
        //three
        var threeBookingId = itemView.text_booking_id_no1!!
        var threeTicket = itemView.textView125!!
        var threeTicketPrice = itemView.textView126!!
        var threeFood = itemView.textView128!!
        var textFood = itemView.textView127!!
        var threeFoodPrice = itemView.textView129!!
        var threeReferenceId = itemView.textView131!!
        var threeReferenceTxt = itemView.textView130!!
        var threeTrackId = itemView.textView133!!
        var threeDateTime = itemView.textView135!!
        var threePayMode = itemView.textView136!!
        var threeTotalPrice = itemView.textView137!!
    }

    override fun getItemViewType(position: Int): Int {
        println("FinalTicketBooking -------->${output.bookingType}")

        if (output.bookingType == "BOOKING") {
            return if (output.concessionFoods.isEmpty()) {
                return if (position == 0) {
                    1
                } else {
                    3
                }
            } else {

                when (position) {
                    0 -> {
                        1
                    }
                    1 -> {
                        2
                    }
                    else -> {
                        3
                    }
                }
            }

        } else {
             if (position == 0) {
                 payMode = 1
                println("FinalTicketBooking21 -------->yes")
               return 2
            }

        }
        return super.getItemViewType(position)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val obj = homeDataList[position]

        when (getItemViewType(position)) {
            1 -> {

                val holder = holder as MyViewHolder
                holder.oneBookingId.text = output.kioskId
                holder.oneTitle.text = output.moviename
                holder.oneRating.text = output.mcensor
                holder.oneRating.setBackgroundColor(Color.parseColor(output.ratingColor))

                holder.oneLocation.text = output.cinemaname
                holder.oneDateTime.text = output.showDate + " " + output.showTime
                holder.oneScreen.text = output.screenId
                holder.txtFoodPickupId.text = output.pickUpNumber

                println("output.experience90------->${output.experience}------->${output.food}")

                when (output.experience) {
                    "4DX" -> {
                        Glide
                            .with(mContext)
                            .load(R.drawable.four_dx)
                            .into(holder.oneType)
                    }
                    "Standard" -> {
                        Glide
                            .with(mContext)
                            .load(R.drawable.standard)
                            .into(holder.oneType)
                    }
                    "VIP" -> {
                        Glide
                            .with(mContext)
                            .load(R.drawable.vip)
                            .into(holder.oneType)
                    }
                    "IMAX" -> {
                        Glide
                            .with(mContext)
                            .load(R.drawable.imax)
                            .into(holder.oneType)
                    }
                    "3D" -> {
                        Glide
                            .with(mContext)
                            .load(R.drawable.threed_black)
                            .into(holder.oneType)
                    }
                    "DOLBY" -> {
                        Glide
                            .with(mContext)
                            .load(R.drawable.dolby_black)
                            .into(holder.oneType)
                    }
                    "ELEVEN" -> {
                        Glide
                            .with(mContext)
                            .load(R.drawable.eleven_black)
                            .into(holder.oneType)
                    }
                    "SCREENX" -> {
                        Glide
                            .with(mContext)
                            .load(R.drawable.screenx_black)
                            .into(holder.oneType)
                    }
                    "PREMIUM" -> {
                        Glide
                            .with(mContext)
                            .load(R.drawable.premium_black)
                            .into(holder.oneType)
                    }
                }

                holder.oneCategoryName.text = output.category

                val layoutManager = FlexboxLayoutManager(mContext)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager.alignItems = AlignItems.STRETCH
                val adapter = SeatListAdapter(output.seatsArr)
                holder.oneSeatList.setHasFixedSize(true)
                holder.oneSeatList.layoutManager = layoutManager
                holder.oneSeatList.adapter = adapter
                holder.onePayMode.text = output.payDone
                holder.onePrice.text = output.totalTicketPrice

                //Cancel Reverse
                if (!output.cancelReserve) {
                    holder.cancelReservation.hide()
                } else {
                    holder.cancelReservation.show()
                }

                // Add Food

                if (!output.addFood) {
                    holder.addFood.hide()
                } else {
                    holder.addFood.show()
                }

                holder.addFood.setOnClickListener {
                    val intent = Intent(mContext, FoodActivity::class.java)
                    intent.putExtra("CINEMA_ID", output.cinemacode)
                    intent.putExtra("BOOKING", "FOOD")
                    intent.putExtra("type", "FOOD")
                    mContext.startActivity(intent)
                }

                //Cancel Ticket
                holder.cancelReservation.setOnClickListener {
                    listener1.cancelReserv(output)
                }

                listener1.onTypeFaceFinalTicketOne(
                    holder.oneBookingId, holder.oneTitle, holder.oneRating,
                    holder.oneLocation, holder.oneDateTime, holder.oneScreen,
                    holder.oneCategoryName, holder.onePayMode, holder.onePrice
                )

                if (output.food == 0){
                    holder.btFoodPrepare.hide()
                    holder.foodAddBtn.hide()
                    holder.consFoodPickupId.hide()
                }

                if (output.food == 1){
                    holder.btFoodPrepare.hide()
                    holder.foodAddBtn.show()
                    holder.consFoodPickupId.hide()
                }

                if (output.food == 2) {
                    holder.btFoodPrepare.show()
                    holder.foodAddBtn.hide()
                    holder.consFoodPickupId.hide()
                    holder.btFoodPrepare.background = ContextCompat.getDrawable(mContext,R.drawable.food_pickup_bg)
                    holder.btFoodPrepare.isClickable = false
                }

                if (output.food == 3){
                    holder.btFoodPrepare.show()
                    holder.foodAddBtn.hide()
                    holder.consFoodPickupId.hide()
                    holder.btFoodPrepare.isClickable = true
                }

                if (output.food == 4){
                    holder.btFoodPrepare.hide()
                    holder.foodAddBtn.hide()
                    holder.consFoodPickupId.show()
                }

                holder.consFoodPickupId.setOnClickListener {

                    val mDialogView = LayoutInflater.from(mContext).inflate(R.layout.food_pickup_dialog, null)
                    val mBuilder = AlertDialog.Builder(mContext, R.style.NewDialog).setView(mDialogView)
                    val mAlertDialog = mBuilder.show()
                    mAlertDialog.show()
                    mAlertDialog.window?.setBackgroundDrawableResource(R.color.black70)
                    val closeDialog = mDialogView.findViewById<TextView>(R.id.close_dialog)
                    val text=mAlertDialog.findViewById<TextView>(R.id.textView105)

                    text?.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(output.pickupInfo, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(output.pickupInfo)
                    }

                    closeDialog.setOnClickListener {
                        mAlertDialog.dismiss()
                    }

                }

                holder.foodAddBtn.setOnClickListener {
                    val intent = Intent(mContext, FoodActivity::class.java)
                        .putExtra("CINEMA_ID", output.cinemacode)
                        .putExtra("BOOKING", "FOOD")
                        .putExtra("type", "FOOD")
                    mContext.startActivity(intent)
                }

                holder.btFoodPrepare.setOnClickListener {

                    listenerFoodPrepare.foodPrepareClick(output)

                }


            }

            2 -> {
//Two
                println("called this method.....")
                val holderTwo = holder as MyViewHolderTwo
                holderTwo.twoBookingId.text = output.kioskId
                if (!output.foodPickup) {
                    holderTwo.twoPickupInfo.hide()
                } else {
                    holderTwo.twoPickupInfo.hide()
                }

                if (payMode == 1){
                    holderTwo.text_payment_mode2.show()
                    holderTwo.twoPayMode.show()
                }else{
                    holderTwo.text_payment_mode2.hide()
                    holderTwo.twoPayMode.hide()
                }

                val gridLayout = GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false)
                holderTwo.twoFood.layoutManager = LinearLayoutManager(mContext)
                val adapter2 = AdapterCheckoutConFirmFoodDetail(mContext, output.concessionFoods)
                holderTwo.twoFood.layoutManager = gridLayout
                holderTwo.twoFood.adapter = adapter2
                holderTwo.twoPayMode.text = output.payDone
                holderTwo.twoPayPrice.text = output.foodTotal

                listener2.onTypeFaceFinalTicketTwo(
                    holderTwo.twoBookingId, holderTwo.twoPickupInfo,
                    holderTwo.twoPayMode, holderTwo.twoPayPrice
                )
            }

            3 -> {
                //Three
                val holderThree = holder as MyViewHolderThree
                holderThree.threeBookingId.text = output.kioskId
                holderThree.threeTicket.text = output.category + " " + output.ticketPrice + " x " + output.numofseats

                holderThree.threeTicketPrice.text = output.totalTicketPrice
                holderThree.threeFood.text = output.concessionFoods.size.toString()+" " + mContext.getString(R.string.item)

                holderThree.threeFoodPrice.text = output.foodTotal

                if (output.concessionFoods.isNullOrEmpty()) {
                    holderThree.threeFood.hide()
                    holder.textFood.hide()
                    holder.threeFoodPrice.hide()
                } else {
                    holderThree.threeFood.show()
                    holder.textFood.show()
                    holder.threeFoodPrice.show()
                }
                holderThree.threeReferenceId.isSelected = true
                if (output.referenceId.isNotEmpty()) {
                    holderThree.threeReferenceTxt.show()
                    holderThree.threeReferenceId.show()
                    holderThree.threeReferenceId.text = output.referenceId
                } else {
                    holderThree.threeReferenceId.hide()
                    holderThree.threeReferenceTxt.hide()
                }
                holderThree.threeTrackId.text = output.trackId
                holderThree.threeDateTime.text = output.bookingTime
                holderThree.threePayMode.text = output.payDone
                holderThree.threeTotalPrice.text = output.totalPrice

                listener3.onTypeFaceFinalTicketThree(
                    holderThree.threeBookingId,
                    holderThree.threeTicket,
                    holderThree.threeTicketPrice,
                    holderThree.threeFood,
                    holderThree.threeFoodPrice,
                    holderThree.threeReferenceId,
                    holderThree.threeReferenceTxt,
                    holderThree.threeTrackId,
                    holderThree.threeDateTime,
                    holderThree.threePayMode
                )
            }
        }
    }

    interface TypeFaceFinalTicket0ne {
        fun onTypeFaceFinalTicketOne(
            oneBookingId: TextView, oneTitle: TextView,
            oneRating: TextView, oneLocation: TextView,
            oneDateTime: TextView, oneScreen: TextView,
            oneCategoryName: TextView,
            onePayMode: TextView, onePrice: TextView
        )

        fun cancelReserv(foodSelctedItem: TicketSummaryResponse.Output)

    }

    interface TypeFaceFinalTicketTwo {
        fun onTypeFaceFinalTicketTwo(
            twoBookingId: TextView, twoPickupInfo: TextView,
            twoPayMode: TextView, twoPayPrice: TextView
        )
    }

    interface TypeFaceFinalTicketThree {
        fun onTypeFaceFinalTicketThree(
            threeBookingId: TextView,
            threeTicket: TextView, threeTicketPrice: TextView, threeFood: TextView,
            threeFoodPrice: TextView, threeReferenceId: TextView, threeReferenceTxt: TextView,
            threeTrackId: TextView, threeDateTime: TextView, threePayMode: TextView
        )
    }

    interface RecycleViewItemFoodPrepare {
        fun foodPrepareClick(output: TicketSummaryResponse.Output)
    }

}