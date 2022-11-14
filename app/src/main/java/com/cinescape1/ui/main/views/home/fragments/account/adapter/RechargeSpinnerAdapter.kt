package com.cinescape1.ui.main.views.home.fragments.account.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.cinescape1.R
import com.cinescape1.ui.main.views.home.fragments.account.response.RechargeAmountResponse


class RechargeSpinnerAdapter(context: Context, private val locationList: List<RechargeAmountResponse.Output.Amount>):BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.recharge_item, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text = locationList[position].amountStr
        return view
    }

    override fun getItem(position: Int): Any {
        return locationList[position]
    }

    override fun getCount(): Int {
        return locationList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.textView21) as TextView

    }

}