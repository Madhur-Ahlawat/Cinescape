package com.cinescape1.ui.main.views.payment.paymentList.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.cinescape1.R
import com.cinescape1.data.models.BankModel
import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse

//private val locationlist: List<PaymentListResponse.Output.PayMode.RespPayMode.PayModeBank>

class BankOfferAdapter(context: Context, private val locationlist: List<PaymentListResponse.Output.PayMode.RespPayMode.PayModeBank>
):BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

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

            vh.label.text = locationlist[position].name


        println("foodAdapter--->${locationlist}")
        return view
    }

    override fun getItem(position: Int): Any? {
        return locationlist[position]
    }

    override fun getCount(): Int {
        return locationlist.size
    }

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val constraintLayout = (super.getDropDownView(position, convertView, parent) as ConstraintLayout)
        val view: TextView = constraintLayout.findViewById(R.id.textView21)

//        constraintLayout.setBackgroundColor(Color.parseColor("#000000"))
        //set the color of first item in the drop down list to gray
        if(position == 0) {
            view.setTextColor(Color.parseColor("#ADADAD"))
        } else {
            view.setTextColor(Color.parseColor("#FFFFFF"))

        }
        return constraintLayout
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.textView21) as TextView

    }

}