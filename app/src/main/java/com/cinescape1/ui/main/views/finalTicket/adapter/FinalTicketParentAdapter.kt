package com.cinescape1.ui.main.views.finalTicket.adapter

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
import com.cinescape1.ui.main.views.finalTicket.model.FinalTicketLocalModel
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.final_ticket_item_one.view.*
import kotlinx.android.synthetic.main.final_ticket_item_three.view.*
import kotlinx.android.synthetic.main.final_ticket_item_two.view.*


class FinalTicketParentAdapter(
    var mContext: Activity,
    private var homeDataList: ArrayList<FinalTicketLocalModel>,
    private var output: TicketSummaryResponse.Output
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var adapter: HomeChildAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val headerLayout =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.final_ticket_item_one, parent, false)
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
        println("homeDataList---"+homeDataList.size)
        return homeDataList.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
    }

    class MyViewHolderTwo(view: View) : RecyclerView.ViewHolder(view) {
        //two
        var twoBookingId = itemView.text_bookin_id_no1!!
        var twoPickupInfo = itemView.textFoodPicUp!!
        var twoFood = itemView.recyclerview_food_details!!
        var twoPayMode = itemView.text_wallet1!!
        var twoPayPrice = itemView.text_kd_total_ticket_price1!!
    }

    class MyViewHolderThree(view: View) : RecyclerView.ViewHolder(view) {
        //three
        var threeBookingId = itemView.text_booking_id_no1!!
        var threeTicket = itemView.textView125!!
        var threeTicketPrice = itemView.textView126!!
        var threeFood = itemView.textView128!!
        var threeFoodPrice = itemView.textView129!!
        var threeReferenceId = itemView.textView131!!
        var threeReferenceTxt = itemView.textView130!!
        var threeTrackId = itemView.textView133!!
        var threeDateTime = itemView.textView135!!
        var threePayMode = itemView.textView136!!
        var threeTotalPrice = itemView.textView137!!
    }

    override fun getItemViewType(position: Int): Int {
        println("outputBookingType--->${output.bookingType}--->position--->${position}")

        if (output.bookingType == "BOOKING") {
            return if (output.concessionFoods.isEmpty()) {
                1
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

            return if (position == 0) {
                1
            } else {
                2
            }
        }
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val obj = homeDataList[position]
        print("TypeKeyParent--->${obj.value}")

        when (getItemViewType(position)) {
            1 -> {
                val holder = holder as MyViewHolder
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
            2 -> {
//Two
                val holderTwo = holder as MyViewHolderTwo
                holderTwo.twoBookingId.text= output.kioskId
                if (!output.foodPickup) {
                    holderTwo.twoPickupInfo.hide()
                } else {
                    holderTwo.twoPickupInfo.show()
                }

                val gridLayout = GridLayoutManager(mContext,1, GridLayoutManager.VERTICAL,false)
                holderTwo.twoFood.layoutManager = LinearLayoutManager(mContext)
                val adapter2 = AdapterCheckoutConFirmFoodDetail(mContext, output.concessionFoods )
                holderTwo.twoFood.layoutManager = gridLayout
                holderTwo.twoFood.adapter = adapter2
                holderTwo.twoPayMode.text = output.payDone
                holderTwo.twoPayPrice.text = output.foodTotal
            }
            3 -> {
                //Three
                val holderThree = holder as MyViewHolderThree
                holderThree.threeBookingId.text= output.kioskId
                holderThree.threeTicket.text= output.category + " " + output.ticketPrice + " x " + output.numofseats
                holderThree.threeFood.text= output.concessionFoods.size.toString() + mContext.getString(R.string.item)
                holderThree.threeReferenceId.isSelected= true
                if(output.referenceId.isNotEmpty()){
                    holderThree.threeReferenceTxt.show()
                    holderThree.threeReferenceId.show()
                    holderThree.threeReferenceId.text= output.referenceId
                }else{
                    holderThree.threeReferenceId.hide()
                    holderThree.threeReferenceTxt.hide()
                }
                holderThree.threeTrackId.text= output.trackId
                holderThree.threeDateTime.text= output.bookingTime
                holderThree.threePayMode.text= output.payDone
                holderThree.threeTotalPrice.text= output.totalPrice
            }
        }
    }
}