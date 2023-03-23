package com.cinescape1.ui.main.views.home.fragments.account.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HistoryResponse
import com.cinescape1.ui.main.views.adapters.HistoryFoodListAdapter
import com.cinescape1.ui.main.views.home.fragments.account.response.RechargeAmountResponse
import kotlinx.android.synthetic.main.payment_items_history.view.*

class PaymentHistoryAdapter(
    private  val context: Activity,
    private  val concessionFoods: ArrayList<HistoryResponse.Output.ConcessionFood>) :
    RecyclerView.Adapter<PaymentHistoryAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textPaymentName = view.textPaymentName
        var textPrices = view.textPrices
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_items_history, null)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return concessionFoods.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }



}