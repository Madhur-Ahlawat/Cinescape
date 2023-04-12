package com.cinescape1.ui.main.views.payment.paymentList.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.databinding.ItemOfferBinding
import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse
import kotlinx.android.synthetic.main.account_preference_layout.*


class GiftCardAdapter(
    private var context: Activity,
    private var respPayModes: ArrayList<PaymentListResponse.Output.PayMode.RespPayMode>,
    private var paymentListAdapter: RecycleViewItemClickListener
) : RecyclerView.Adapter<GiftCardAdapter.ViewHolder>() {
    private var rowIndex = -0

    inner class ViewHolder(val binding: ItemOfferBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOfferBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(respPayModes[position]) {
                println("offerList--->${this}")
                //title
                binding.labelGiftCard.text = this.name
                Glide.with(context)
                    .load(this.imageUrl)
                    .into(binding.imageView55)

                if (rowIndex == position) {
                    Glide.with(context).load(this.activeImageUrl).into(binding.imageView55)
                    binding.labelGiftCard.setTextColor(context.getColor(R.color.red))
                } else {
                    Glide.with(context).load(this.imageUrl).into(binding.imageView55)
                    binding.labelGiftCard.setTextColor(context.getColor(R.color.hint_color))
                }

                holder.itemView.setOnClickListener {
                    rowIndex = position
                    notifyDataSetChanged()
                    paymentListAdapter.giftCardClick(this)
                }
            }
        }

    }


    override fun getItemCount(): Int {
        return respPayModes.size
    }


    interface RecycleViewItemClickListener {
        fun giftCardClick(view: PaymentListResponse.Output.PayMode.RespPayMode)
    }
}