package com.cinescape1.ui.main.views.adapters.finalTicket

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.TicketSummaryResponse
import com.cinescape1.ui.main.views.adapters.HomeChildAdapter
import com.cinescape1.ui.main.views.adapters.SeatListAdapter
import com.cinescape1.ui.main.views.adapters.checkoutAdapter.AdapterCheckoutConFirmFoodDetail
import com.cinescape1.ui.main.views.adapters.finalTicket.model.FinalTicketLocalModel
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.final_ticket_parent.view.*


class FinalTicketParentAdapter(
    var mContext: Activity,
    private var homeDataList: ArrayList<FinalTicketLocalModel>,
    private var output: TicketSummaryResponse.Output
) :
    RecyclerView.Adapter<FinalTicketParentAdapter.MyViewHolder>() {
    var adapter: HomeChildAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.final_ticket_parent, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val obj = homeDataList[position]
        when (obj.value) {
            "one" -> {
                print("TypeKeyParent--->${obj.value}")

                holder.one.show()
                holder.three.hide()
                holder.two.hide()
                holder.oneBookingId.text = output.kioskId
                holder.oneTitle.text = output.moviename
                holder.oneRating.text = output.mcensor
                holder.oneRating.text = output.mcensor
                holder.oneLocation.text = output.cinemaname
                holder.oneDateTime.text = output.showDate + " " + output.showTime
                holder.oneScreen.text = output.screenId
                holder.oneType.text = output.experience
                holder.oneCategoryName.text = output.category

                val layoutManager = FlexboxLayoutManager(mContext)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager.alignItems = AlignItems.STRETCH
                val adapter = SeatListAdapter(output.seatsArr)
                holder.oneSeatList.setHasFixedSize(true)
                holder.oneSeatList.layoutManager = layoutManager
                holder.oneSeatList.adapter = adapter

                holder.onePayMode.text= output.payDone
                holder.onePrice.text= output.totalTicketPrice
            }
            "two" -> {
                print("TypeKeyParent--->${obj.value}")
                holder.one.show()
                holder.two.show()
                holder.three.hide()
//One
                holder.oneBookingId.text = output.kioskId
                holder.oneTitle.text = output.moviename
                holder.oneRating.text = output.mcensor
                holder.oneRating.text = output.mcensor
                holder.oneLocation.text = output.cinemaname
                holder.oneDateTime.text = output.showDate + " " + output.showTime
                holder.oneScreen.text = output.screenId
                holder.oneType.text = output.experience
                holder.oneCategoryName.text = output.category

                val layoutManager = FlexboxLayoutManager(mContext)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager.alignItems = AlignItems.STRETCH
                val adapter = SeatListAdapter(output.seatsArr)
                holder.oneSeatList.setHasFixedSize(true)
                holder.oneSeatList.layoutManager = layoutManager
                holder.oneSeatList.adapter = adapter

                holder.onePayMode.text= output.payDone
                holder.onePrice.text= output.totalTicketPrice
//Two
                holder.twoBookingId.text= output.kioskId
                if (!output.foodPickup) {
                    holder.twoPickupInfo.hide()
                } else {
                    holder.twoPickupInfo.show()
                }
                holder.onePayMode.text = output.payDone
                holder.onePrice.text = output.ticketPrice

                val gridLayout = GridLayoutManager(mContext,1, GridLayoutManager.VERTICAL,false)
                holder.twoFood.layoutManager = LinearLayoutManager(mContext)
                val adapter2 = AdapterCheckoutConFirmFoodDetail(mContext, output.concessionFoods )
                holder.twoFood.layoutManager = gridLayout
                holder.twoFood.adapter = adapter2
                adapter2.loadNewData(output.concessionFoods)

                holder.twoPayMode.text = output.payDone
                holder.twoPayPrice.text = output.foodTotal

            }
            "three" -> {
                print("TypeKeyParent--->${obj.value}")
                //three
                holder.three.show()
                holder.two.show()
                holder.one.show()
                //One
                holder.oneBookingId.text = output.kioskId
                holder.oneTitle.text = output.moviename
                holder.oneRating.text = output.mcensor
                holder.oneRating.text = output.mcensor
                holder.oneLocation.text = output.cinemaname
                holder.oneDateTime.text = output.showDate + " " + output.showTime
                holder.oneScreen.text = output.screenId
                holder.oneType.text = output.experience
                holder.oneCategoryName.text = output.category

                val layoutManager = FlexboxLayoutManager(mContext)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager.alignItems = AlignItems.STRETCH
                val adapter = SeatListAdapter(output.seatsArr)
                holder.oneSeatList.setHasFixedSize(true)
                holder.oneSeatList.layoutManager = layoutManager
                holder.oneSeatList.adapter = adapter

                holder.onePayMode.text= output.payDone
                holder.onePrice.text= output.totalTicketPrice
//Two
                holder.twoBookingId.text= output.kioskId
                if (!output.foodPickup) {
                    holder.twoPickupInfo.hide()
                } else {
                    holder.twoPickupInfo.show()
                }
                holder.onePayMode.text = output.payDone
                holder.onePrice.text = output.ticketPrice

                val gridLayout = GridLayoutManager(mContext,1, GridLayoutManager.VERTICAL,false)
                holder.twoFood.layoutManager = LinearLayoutManager(mContext)
                val adapter2 = AdapterCheckoutConFirmFoodDetail(mContext, output.concessionFoods )
                holder.twoFood.layoutManager = gridLayout
                holder.twoFood.adapter = adapter2
                adapter2.loadNewData(output.concessionFoods)

                holder.twoPayMode.text = output.payDone
                holder.twoPayPrice.text = output.foodTotal


                holder.threeBookingId.text= output.kioskId
                holder.threeTicket.text= output.category + " " + output.ticketPrice + " x " + output.numofseats
                holder.threeFood.text= output.concessionFoods.size.toString() + mContext.getString(R.string.item)
                holder.threeReferenceId.isSelected= true
                holder.threeReferenceId.text= output.referenceId

                holder.threeTrackId.text= output.trackId
                holder.threeDateTime.text= output.bookingTime
                holder.threePayMode.text= output.payDone
                holder.threeTotalPrice.text= output.totalPrice
            }
        }
    }


    override fun getItemCount(): Int {
        return homeDataList.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var one = itemView.one!!
        var two = itemView.two!!
        var three = itemView.three!!

        //One
        var oneBookingId = itemView.text_bookin_id_no!!
        var oneTitle = itemView.text_name_movie!!
        var oneRating = itemView.text_types!!
        var oneLocation = itemView.text_location_names!!
        var oneDateTime = itemView.txt_date!!
        var oneScreen = itemView.txt_scrrens!!
        var oneType = itemView.tv_screenx!!
        var oneCategoryName = itemView.categoryName!!
        var oneSeatList = itemView.seats_list!!
        var onePayMode = itemView.text_wallet!!
        var onePrice = itemView.text_kd_total_ticket_price!!

        //two
        var twoBookingId = itemView.text_bookin_id_no1!!
        var twoPickupInfo = itemView.textFoodPicUp!!
        var twoFood = itemView.recyclerview_food_details!!
        var twoPayMode = itemView.text_wallet1!!
        var twoPayPrice = itemView.text_kd_total_ticket_price1!!

        //two
        var threeBookingId = itemView.text_booking_id_no1!!
        var threeTicket = itemView.textView125!!
        var threeTicketPrice = itemView.textView126!!
        var threeFood = itemView.textView128!!
        var threeFoodPrice = itemView.textView129!!
        var threeReferenceId = itemView.textView131!!
        var threeTrackId = itemView.textView133!!
        var threeDateTime = itemView.textView135!!
        var threePayMode = itemView.textView136!!
        var threeTotalPrice = itemView.textView137!!
    }


}