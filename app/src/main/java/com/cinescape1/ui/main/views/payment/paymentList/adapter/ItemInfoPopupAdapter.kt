package com.cinescape1.ui.main.views.payment.paymentList.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse
import kotlinx.android.synthetic.main.account_preference_layout.*
import kotlinx.android.synthetic.main.paymnet_info_item.view.*

class ItemInfoPopupAdapter(
    private var context: Activity,
    private var respPayModes: ArrayList<PaymentListResponse.Output.PayInfo>,
) : RecyclerView.Adapter<ItemInfoPopupAdapter.ItemInfoPopupViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemInfoPopupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.paymnet_info_item, null)
        return ItemInfoPopupViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ItemInfoPopupViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val paymentItem = respPayModes[position]
        holder.priceTitle.text = paymentItem.key
        holder.prices.text = paymentItem.amt

        if (paymentItem.highlight == true){
            holder.priceTitle.setTextColor(context.getColor(R.color.white))
            holder.prices.setTextColor(context.getColor(R.color.white))
        }else{
            holder.priceTitle.setTextColor(context.getColor(R.color.grey))
            holder.prices.setTextColor(context.getColor(R.color.grey))
        }

    }

    override fun getItemCount(): Int {
        return respPayModes.size
    }

    inner class ItemInfoPopupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var priceTitle = view.title_ticketPrice
        var prices = view.text_ticket1_price
    }

}