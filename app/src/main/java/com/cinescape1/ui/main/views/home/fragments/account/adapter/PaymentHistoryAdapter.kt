package com.cinescape1.ui.main.views.home.fragments.account.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HistoryResponse
import kotlinx.android.synthetic.main.payment_items_history.view.*

class PaymentHistoryAdapter(
    private  val context: Activity,
    private  val paymentInfo: ArrayList<HistoryResponse.Output.PayInfo>) :
    RecyclerView.Adapter<PaymentHistoryAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textPaymentName = view.textPaymentNames
        var textPrices = view.textPrices
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_items_history, null)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val itemList = paymentInfo[position]
//        holder.textPaymentName.text = itemList.key
//        holder.textPrices.text = itemList.amt

        println("Food And TicketPrices------->${itemList.amt}---->${holder.textPaymentName.text}")
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return paymentInfo.size
    }



}