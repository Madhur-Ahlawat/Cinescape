package com.cinescape1.ui.main.views.payment.paymentList

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.cinescape1.R
import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse

class PaymentSpinnerAdapter(var context: Context,var list:List<PaymentListResponse.Output.PayMode.RespPayMode.PayModeBank>):SpinnerAdapter {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun registerDataSetObserver(observer: DataSetObserver?) {
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.bank_offer, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {

            view = convertView
            vh = view.tag as ItemHolder
        }

        vh.label.text = list[position].name


        println("foodAdapter--->${list}")
        return view
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun getViewTypeCount(): Int {
            return 1
    }

    override fun isEmpty(): Boolean {
        return true
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.bank_offer, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {

            view = convertView
            vh = view.tag as ItemHolder
        }

        vh.label.text = list[position].name


        println("foodAdapter--->${list}")
        return view
    }
    private class ItemHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.textView21) as TextView

    }
}