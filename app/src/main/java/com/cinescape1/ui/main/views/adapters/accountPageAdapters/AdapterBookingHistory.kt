package com.cinescape1.ui.main.views.adapters.accountPageAdapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HistoryResponse
import com.cinescape1.ui.main.views.adapters.HistoryFoodListAdapter
import com.cinescape1.utils.*

class AdapterBookingHistory(private val context: Activity,
                            private var bookingHistoryList: ArrayList<HistoryResponse.Output>,
                            var listener : typeFaceItem) :
    RecyclerView.Adapter<AdapterBookingHistory.MyViewHolderBookingHistory>() {

    private var rowIndex = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderBookingHistory {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.account_booking_history_item, parent, false)
        return MyViewHolderBookingHistory(view)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolderBookingHistory, @SuppressLint("RecyclerView") position: Int) {
        val bookingHistoryItem = bookingHistoryList[position]

        when (Constant.LANGUAGE) {
            "ar" -> {
                LocaleHelper.setLocale(context, "ar")
                val regular = ResourcesCompat.getFont(context, R.font.gess_light)
                val bold = ResourcesCompat.getFont(context, R.font.gess_bold)
                val medium = ResourcesCompat.getFont(context, R.font.gess_medium)

                holder.textBookingHistoryTitle.typeface = bold
                holder.textBookingHistoryDate.typeface = regular
                holder.textBookingHistoryTime.typeface = regular
                holder.textAddress.typeface = regular
                holder.textviewScreenNumber.typeface = regular
                holder.textviewDateInfo.typeface = regular
                holder.textviewTimeInfo.typeface = regular
                holder.textviewExperienceName.typeface = regular
                holder.textviewSeatName.typeface = regular
                holder.textKdTicketPrice.typeface = regular
                holder.payDone.typeface = regular
                holder.rechargeTime.typeface = regular
                holder.rechargePrice.typeface = regular
                holder.rechargeDate.typeface = regular
                holder.paidBy.typeface = regular
                holder.foodTotalPrice.typeface = regular
                holder.foodPaidby.typeface = regular


            }
            "en" -> {
                LocaleHelper.setLocale(context, "en")
                val regular = ResourcesCompat.getFont(context, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(context, R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(context, R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(context, R.font.sf_pro_text_medium)

                holder.textBookingHistoryTitle.typeface = bold
                holder.textBookingHistoryDate.typeface = regular
                holder.textBookingHistoryTime.typeface = regular
                holder.textAddress.typeface = regular
                holder.textviewScreenNumber.typeface = regular
                holder.textviewDateInfo.typeface = regular
                holder.textviewTimeInfo.typeface = regular
                holder.textviewExperienceName.typeface = regular
                holder.textviewSeatName.typeface = regular
                holder.textKdTicketPrice.typeface = regular
                holder.payDone.typeface = regular
                holder.rechargeTime.typeface = regular
                holder.rechargePrice.typeface = regular
                holder.rechargeDate.typeface = regular
                holder.paidBy.typeface = regular
                holder.foodTotalPrice.typeface = regular
                holder.foodPaidby.typeface = regular

            }
            else -> {

                LocaleHelper.setLocale(context, "en")
                val regular = ResourcesCompat.getFont(context, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(context, R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(context, R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(context, R.font.sf_pro_text_medium)

                holder.textBookingHistoryTitle.typeface = bold
                holder.textBookingHistoryDate.typeface = regular
                holder.textBookingHistoryTime.typeface = regular
                holder.textAddress.typeface = regular
                holder.textviewScreenNumber.typeface = regular
                holder.textviewDateInfo.typeface = regular
                holder.textviewTimeInfo.typeface = regular
                holder.textviewExperienceName.typeface = regular
                holder.textviewSeatName.typeface = regular
                holder.textKdTicketPrice.typeface = regular
                holder.payDone.typeface = regular
                holder.rechargeTime.typeface = regular
                holder.rechargePrice.typeface = regular
                holder.rechargeDate.typeface = regular
                holder.paidBy.typeface = regular
                holder.foodTotalPrice.typeface = regular
                holder.foodPaidby.typeface = regular

            }
        }

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

        listener.datatypeFace(holder.textBookingHistoryTitle,holder.textBookingHistoryDate,
            holder.textBookingHistoryTime,holder.textAddress,holder.textviewScreenNumber,
            holder.textviewDateInfo,holder.textviewTimeInfo,holder.textviewExperienceName,holder.textviewSeatName,
            holder.textKdTicketPrice, holder.payDone, holder.rechargeTime,holder.rechargePrice,holder.rechargeDate,
            holder.paidBy,holder.foodTotalPrice,holder.foodPaidby)


        holder.textBookingHistoryDate.text = bookingHistoryItem.showDate
        holder.textBookingHistoryTime.text = bookingHistoryItem.showTime

        holder.itemView.setOnClickListener {
            rowIndex = position
            when (bookingHistoryItem.bookingType) {
                "CLUBRECHARGE" -> {
                    if (holder.rechargeUi.visibility == View.GONE) {

                        //recharge
                        holder.payDone.text=bookingHistoryItem.payDone
                        holder.rechargePrice.text=bookingHistoryItem.totalPrice
                        if (bookingHistoryItem.showDate!=null) {
                            holder.rechargeDate.text= bookingHistoryItem.showDate
                        }
                        if (bookingHistoryItem.showTime!=null) {
                            holder.rechargeTime.text = bookingHistoryItem.showTime
                        }


                        holder.imageArrowDrop.setImageResource(R.drawable.arrow_up)
                        holder.paymentUi.show()
                        holder.rechargeUi.show()
                        holder.foodUi.hide()
                        holder.movieui.hide()
                    }else{
                        holder.rechargeUi.hide()
                        holder.foodUi.hide()
                        holder.movieui.hide()
                        holder.paymentUi.hide()
                        holder.imageArrowDrop.setImageResource(R.drawable.arrow_down)
                    }
                }

                "FOOD" -> {
                    if (holder.foodUi.visibility == View.GONE) {

                        //food
                        holder.foodTotalPrice.text=bookingHistoryItem.totalPrice
                        holder.foodPaidby.text=context.resources.getString(R.string.paid_by_club_card)+" "+bookingHistoryItem.payDone
                        holder.paidBy.text=context.resources.getString(R.string.paid_by_club_card)+" "+bookingHistoryItem.payDone

                        holder.foodUi.show()
                        holder.rechargeUi.hide()
                        holder.movieui.hide()
                        holder.imageArrowDrop.setImageResource(R.drawable.arrow_up)

                        if (bookingHistoryItem.concessionFoods.isNullOrEmpty()){
                            println("---------Check>1")
                            holder.foodList.hide()
                        }else{
                            println("---------Check>2---->${bookingHistoryItem.concessionFoods.size}")
                            holder.paymentUi.hide()
                            holder.foodList.show()
                            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            holder.foodList.layoutManager = LinearLayoutManager(context)
                            val adapter = HistoryFoodListAdapter(context, bookingHistoryItem.concessionFoods)
                            holder.foodList.layoutManager = layoutManager
                            holder.foodList.adapter = adapter
                        }

                    }else{
                        holder.paymentUi.hide()
                        holder.foodUi.hide()
                        holder.rechargeUi.hide()
                        holder.movieui.hide()
                        holder.imageArrowDrop.setImageResource(R.drawable.arrow_down)
                    }
                }
                "BOOKING" -> {
                    if (holder.movieui.visibility == View.GONE) {
                        holder.imageArrowDrop.setImageResource(R.drawable.arrow_up)
                        holder.foodUi.hide()
                        holder.rechargeUi.hide()
                        holder.movieui.show()
                        holder.paymentUi.show()

                        if (bookingHistoryItem.concessionFoods.isNotEmpty()) {
                            holder.foodMUi.show()
                        } else {
                            holder.foodMUi.hide()
                        }


                        //food
                        holder.foodTotalPrice.text=bookingHistoryItem.totalPrice
                        holder.foodPaidby.text=context.resources.getString(R.string.paid_by_club_card)+" "+bookingHistoryItem.payDone
                        holder.paidBy.text=context.resources.getString(R.string.paid_by_club_card)+" "+bookingHistoryItem.payDone


                        if (bookingHistoryItem.concessionFoods.isNullOrEmpty()){
                            holder.foodList.hide()
                        }else{
                            holder.foodList.show()
                            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            holder.foodList.layoutManager = LinearLayoutManager(context)
                            val adapter = HistoryFoodListAdapter(context, bookingHistoryItem.concessionFoods)
                            holder.foodList.layoutManager = layoutManager
                            holder.foodList.adapter = adapter
                        }


                        holder.textviewDateInfo.text = bookingHistoryItem.showDate
                        holder.textviewTimeInfo.text = bookingHistoryItem.showTime
                        holder.textAddress.text = bookingHistoryItem.cinemaname
                        holder.textviewScreenNumber.text = bookingHistoryItem.screenId
                        holder.textviewExperienceName.text = bookingHistoryItem.experience

                        holder.textviewCategoryName.text = bookingHistoryItem.category
                        val commaSeparatedString = bookingHistoryItem.seatsArr.joinToString { "$it" }
                        holder.textviewSeatName.text = commaSeparatedString

                        holder.textKdTicketPrice.text = bookingHistoryItem.totalTicketPrice



                    }else{
                        holder.foodUi.hide()
                        holder.rechargeUi.hide()
                        holder.movieui.hide()
                        holder.paymentUi.hide()
                        holder.imageArrowDrop.setImageResource(R.drawable.arrow_down)
                    }
                }
            }
        }

    }
     override fun getItemCount(): Int {
          if (bookingHistoryList.size == 0)
              return 0 else
                return bookingHistoryList.size
     }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
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
        var paymentUi: ConstraintLayout = view.findViewById(R.id.constraintLayout36)

        var payDone: TextView = view.findViewById(R.id.textView85)
        var rechargeTime: TextView = view.findViewById(R.id.textView89)
        var rechargePrice: TextView = view.findViewById(R.id.textView83)
        var rechargeDate: TextView = view.findViewById(R.id.textView87)
        var paidBy: TextView = view.findViewById(R.id.textView_paid_by_club_card)
        var foodTotalPrice: TextView = view.findViewById(R.id.textView96)
        var foodPaidby: TextView = view.findViewById(R.id.textView98)

    }

    interface typeFaceItem{
       fun datatypeFace(textBookingHistoryTitle: TextView, textBookingHistoryDate: TextView,
                        textBookingHistoryTime: TextView, textAddress: TextView,
                        textviewScreenNumber: TextView, textviewDateInfo: TextView,
                        textviewTimeInfo: TextView,textviewExperienceName: TextView,
                        textviewSeatName: TextView, textKdTicketPrice: TextView,
                        payDone: TextView, rechargeTime: TextView, rechargePrice: TextView,
                        rechargeDate: TextView, paidBy: TextView, foodTotalPrice: TextView,
                        foodPaidby: TextView)
    }

}