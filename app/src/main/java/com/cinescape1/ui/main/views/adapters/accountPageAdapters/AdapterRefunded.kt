package com.cinescape1.ui.main.views.adapters.accountPageAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.ModelsRefunded

class AdapterRefunded (context: Context, private var refundedList: List<ModelsRefunded>) :
    RecyclerView.Adapter<AdapterRefunded.MyViewHolderRefunded>() {
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderRefunded {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.account_refunded_layout_item, parent, false)
        return MyViewHolderRefunded(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderRefunded, position: Int) {
            val refundedItem = refundedList[position]
            holder.textAmount.text = refundedItem.amount!!
            holder.textAmountKd.text = refundedItem.amountKD!!
            holder.textExpiryDate.text = refundedItem.expiryDate!!
            holder.textDateNames.text = refundedItem.dateInfo!!
    }

    override fun getItemCount(): Int {
        return if (refundedList.isNotEmpty()) refundedList.size else 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newFoodSelectors: List<ModelsRefunded>) {
        refundedList = newFoodSelectors
        notifyDataSetChanged()
    }

    class MyViewHolderRefunded(view: View) : RecyclerView.ViewHolder(view) {
        var textAmount: TextView = view.findViewById(R.id.text1_amount)
        var textAmountKd: TextView = view.findViewById(R.id.text_amount_kd1)
        var textExpiryDate: TextView = view.findViewById(R.id.text_expiry_date)
        var textDateNames: TextView = view.findViewById(R.id.text_date_names)

    }
}