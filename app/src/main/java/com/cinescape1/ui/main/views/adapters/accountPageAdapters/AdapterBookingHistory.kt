package com.cinescape1.ui.main.views.adapters.accountPageAdapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HistoryResponse
import com.cinescape1.ui.main.views.adapters.HistoryFoodListAdapter
import com.cinescape1.utils.hide
import com.cinescape1.utils.show

class AdapterBookingHistory(private val context: Activity, private var bookingHistoryList: ArrayList<HistoryResponse.Output>) :
    RecyclerView.Adapter<AdapterBookingHistory.MyViewHolderBookingHistory>() {
    private var rowIndex = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderBookingHistory {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.account_booking_history_item, parent, false)
        return MyViewHolderBookingHistory(view)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolderBookingHistory, @SuppressLint("RecyclerView") position: Int) {
        val bookingHistoryItem = bookingHistoryList[position]
        println("Booktype--->${bookingHistoryItem.bookingType}")
        when (bookingHistoryItem.bookingType) {
            "CLUBRECHARGE" -> {
                holder.textBookingHistoryTitle.text= context.getString(R.string.clubCardrecharge)
            }
            "FOOD" -> {
                holder.textBookingHistoryTitle.text= context.getString(R.string.foodorder)
            }
            "BOOKING" -> {
                holder.textBookingHistoryTitle.text= bookingHistoryItem.moviename
            }
            else -> {
                holder.textBookingHistoryTitle.text=bookingHistoryItem.bookingType
            }
        }
        holder.textBookingHistoryDate.text = bookingHistoryItem.showDate
        holder.textBookingHistoryTime.text = bookingHistoryItem.showTime

        holder.textviewDateInfo.text = bookingHistoryItem.showDate
        holder.textviewTimeInfo.text = bookingHistoryItem.showTime
        holder.textAddress.text = bookingHistoryItem.cinemaname
        holder.textviewScreenNumber.text = bookingHistoryItem.screenId
        holder.textviewExperienceName.text =bookingHistoryItem.experience
        holder.textviewCategoryName.text = "Bachelor"
        val commaSeparatedString = bookingHistoryItem.seatsArr.joinToString { "$it" }
        holder.textviewSeatName.text = commaSeparatedString

        holder.textKdTicketPrice.text = bookingHistoryItem.totalTicketPrice


        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.foodList.layoutManager = LinearLayoutManager(context)
        val adapter = HistoryFoodListAdapter(context, bookingHistoryItem.concessionFoods)
        holder.foodList.layoutManager = layoutManager
        holder.foodList.adapter = adapter

        //recharge
        holder.payDone.text=bookingHistoryItem.payDone
        holder.rechargePrice.text=bookingHistoryItem.totalPrice
        if (bookingHistoryItem.showDate!=null) {
            holder.rechargeDate.text= bookingHistoryItem.showDate
        }
        if (bookingHistoryItem.showTime!=null) {
            holder.rechargeTime.text = bookingHistoryItem.showTime
        }

        //food
        holder.foodTotalPrice.text=bookingHistoryItem.totalPrice
        holder.foodPaidby.text=context.resources.getString(R.string.paid_by_club_card)+" "+bookingHistoryItem.payDone
        holder.paidBy.text=context.resources.getString(R.string.paid_by_club_card)+" "+bookingHistoryItem.payDone

        if (rowIndex == position) {
            holder.imageArrowDrop.setImageResource(R.drawable.arrow_up)
            when (bookingHistoryItem.bookingType) {
                "CLUBRECHARGE" -> {
                    holder.rechargeUi.show()
                    holder.foodUi.hide()
                    holder.movieui.hide()
                }
                "FOOD" -> {
                    holder.foodUi.show()
                    holder.rechargeUi.hide()
                    holder.movieui.hide()
                }
                "BOOKING" -> {
                    holder.foodUi.hide()
                    holder.rechargeUi.hide()
                    holder.movieui.show()
                    if (bookingHistoryItem.concessionFoods.isNotEmpty()){
                        holder.foodMUi.show()
                    }else{
                        holder.foodMUi.hide()
                    }
                }
            }
        }else {
            holder.imageArrowDrop.setImageResource(R.drawable.arrow_down)
            holder.movieui.hide()
        }

        holder.itemView.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
        }

    }
     override fun getItemCount(): Int {
         return bookingHistoryList.size
     }

      @SuppressLint("NotifyDataSetChanged")
      fun loadNewData(newFoodSelectors: List<HistoryResponse.Output>) {
          bookingHistoryList = newFoodSelectors as ArrayList<HistoryResponse.Output>
          notifyDataSetChanged()
      }

    class MyViewHolderBookingHistory(view: View) : RecyclerView.ViewHolder(view) {
        var textBookingHistoryTitle: TextView = view.findViewById(R.id.text_booking_history_title)
        var textBookingHistoryDate: TextView = view.findViewById(R.id.text_booking_history_date)
        var textBookingHistoryTime: TextView = view.findViewById(R.id.text_booking_history_time)
        var imageArrowDrop: ImageView = view.findViewById(R.id.image_arrow_drop)

        var textAddress: TextView = view.findViewById(R.id.text_address)
        var textviewScreenNumber: TextView = view.findViewById(R.id.textView_screen_number)
        var textviewDateInfo: TextView = view.findViewById(R.id.textView_date_info)

        var textviewTimeInfo: TextView = view.findViewById(R.id.textView_time_info)
        var textviewExperienceName: TextView = view.findViewById(R.id.textView_experience_name)
        var textviewCategoryName: TextView = view.findViewById(R.id.textView_category_name)

        var textviewSeatName: TextView = view.findViewById(R.id.textView_seat_name)
        var textKdTicketPrice: TextView = view.findViewById(R.id.text_kd_ticket_price)
        var movieui: ConstraintLayout = view.findViewById(R.id.movieUi)
        var rechargeUi: ConstraintLayout = view.findViewById(R.id.rechargeUi)
        var foodUi: ConstraintLayout = view.findViewById(R.id.foodUi)
        var foodMUi: ConstraintLayout = view.findViewById(R.id.foodMUi)
        var foodList: RecyclerView = view.findViewById(R.id.recyclerView)
        var payDone: TextView = view.findViewById(R.id.textView85)
        var rechargeTime: TextView = view.findViewById(R.id.textView89)
        var rechargePrice: TextView = view.findViewById(R.id.textView83)
        var rechargeDate: TextView = view.findViewById(R.id.textView87)
        var paidBy: TextView = view.findViewById(R.id.textView_paid_by_club_card)
        var foodTotalPrice: TextView = view.findViewById(R.id.textView96)
        var foodPaidby: TextView = view.findViewById(R.id.textView98)

    }
}