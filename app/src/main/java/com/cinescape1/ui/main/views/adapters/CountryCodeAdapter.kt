package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CountryCodeResponse


class CountryCodeAdapter(
    private var recyclerDataArrayList: ArrayList<CountryCodeResponse.Output>,
    private var listener: RecycleViewItemClickListener,
    private val mcontext: Activity
) :
    RecyclerView.Adapter<CountryCodeAdapter.RecyclerViewHolder>() {
    private var rowIndex = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountryCodeAdapter.RecyclerViewHolder {
        // Inflate Layout
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_country_code, parent, false)
        return RecyclerViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(
        holder: RecyclerViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {

        try {
            val recyclerData = recyclerDataArrayList[position]
            holder.countryCode.text =  recyclerData.countryName + " " + "(" + recyclerData.isdCode + ")"

            if (rowIndex==position){
               holder.imgSelect.setImageResource(R.drawable.ic_check)
            }else{
                holder.imgSelect.setImageDrawable(null)
            }

            holder.itemView.setOnClickListener {
                rowIndex = position
                listener.onItemClick(recyclerData)
                notifyDataSetChanged()
            }

        } catch (e: Exception) {
            println("CountryCodeException--->${e.message}")
        }

    }

    override fun getItemCount(): Int {
        return recyclerDataArrayList.size
    }


    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryCode: TextView = itemView.findViewById(R.id.textView24)
        val imgSelect: ImageView = itemView.findViewById(R.id.imgSelect)
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: CountryCodeResponse.Output)

    }
    @SuppressLint("NotifyDataSetChanged")
    public fun updateList(updatedList: java.util.ArrayList<CountryCodeResponse.Output>){
        recyclerDataArrayList = updatedList
        notifyDataSetChanged()
    }
}
