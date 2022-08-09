package com.cinescape1.ui.main.views.adapters.checkoutAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.TicketSummaryResponse
import com.cinescape1.utils.Constant
import kotlinx.android.synthetic.main.checkout_booking_confirm_food_details_item.view.*


class AdapterCheckoutConFirmFoodDetail(
    context: Context,
    private var CheckoutConirmFoodDetailList: List<TicketSummaryResponse.ConcessionFood>
) :
    RecyclerView.Adapter<AdapterCheckoutConFirmFoodDetail.MyViewHolderCheckoutConFirmFoodDetail>() {
    private var mContext = context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolderCheckoutConFirmFoodDetail {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.checkout_booking_confirm_food_details_item, parent, false)
        return MyViewHolderCheckoutConFirmFoodDetail(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderCheckoutConFirmFoodDetail, position: Int) {

        val foodSelctedItem = CheckoutConirmFoodDetailList[position]
        holder.tvsItemTitle.text = foodSelctedItem.description
        holder.tvsOtemSubtitle.text = foodSelctedItem.itemType
        holder.textNQty.text = foodSelctedItem.quantity.toString()
        holder.textNPrice.isSelected = true
        holder.textNTotal.isSelected = true
        val price = (foodSelctedItem.priceInCents * foodSelctedItem.quantity) / 100.0
        holder.textNPrice.text =
           Constant.DECIFORMAT.format(price)
        holder.textNTotal.text = Constant.DECIFORMAT.format((foodSelctedItem.priceInCents.toDouble() / 100.0))

    }

    override fun getItemCount(): Int {
        return if (CheckoutConirmFoodDetailList.isNotEmpty()) CheckoutConirmFoodDetailList.size else 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newFoodSelectedlist: List<TicketSummaryResponse.ConcessionFood>) {
        CheckoutConirmFoodDetailList = newFoodSelectedlist
        notifyDataSetChanged()
    }

    class MyViewHolderCheckoutConFirmFoodDetail(view: View) : RecyclerView.ViewHolder(view) {
        var tvsItemTitle = view.tvs_item_title
        var tvsOtemSubtitle: TextView = view.findViewById(R.id.tvs_item_subtitle)
        var textNQty: TextView = view.findViewById(R.id.text_n_qty)
        var textNPrice: TextView = view.findViewById(R.id.text_n_price)
        var textNTotal: TextView = view.findViewById(R.id.text_n_total)

    }
}