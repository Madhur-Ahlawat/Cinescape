package com.cinescape1.ui.main.views.payment.paymentList.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.GetMovieResponse
import com.cinescape1.databinding.ItemPaymentListBinding
import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import kotlinx.android.synthetic.main.account_preference_layout.*


class PaymentListAdapter(
    private val context: Activity,
    private val payMode: ArrayList<PaymentListResponse.Output.PayMode>,
    private val listner: RecycleViewItemClickListener
) : RecyclerView.Adapter<PaymentListAdapter.ViewHolder>(),
    GiftCardAdapter.RecycleViewItemClickListener {
    private var clickName = ""

    inner class ViewHolder(val binding: ItemPaymentListBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPaymentListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)

    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(payMode[position]) {
                //title
                binding.textView156.text = this.name
                //wallet
                if (this.payType == "GATEWAY") {
                    binding.imageView63.setImageResource(R.drawable.arrow_up)
                    binding.cardUi.show()
                } else {
                    binding.cardUi.hide()
                }

                //Card Click
                binding.creditCard.setOnClickListener {
                    listner.onCreditCardItemClick(this)
                }
                binding.knet.setOnClickListener {
                    listner.onKnetItemClick(this)
                }
                //show Hide
                binding.offerEditText.hint = clickName
                binding.imageView63.setOnClickListener {
                    when (this.payType) {
                        "BANK" -> {
                            if (binding.bankOffer.visibility == View.GONE) {
                                binding.imageView63.setImageResource(R.drawable.arrow_up)
                                binding.bankOffer.show()
                                val customAdapter = BankOfferAdapter(
                                    context, this.respPayModes[0].payModeBanks
                                )
                                binding.spinner2.adapter = customAdapter
                                binding.spinner2.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parent: AdapterView<*>,
                                            view: View,
                                            position: Int,
                                            id: Long
                                        ) {
//                                        cinema = locationlist[position].name
                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {

                                        }
                                    }
                            } else {
                                binding.imageView63.setImageResource(R.drawable.arrow_down)

                                binding.bankOffer.hide()

                            }
                        }
                        "OFFER" -> {
                            if (binding.giftCardUi.visibility == View.GONE) {
                                binding.imageView63.setImageResource(R.drawable.arrow_up)
                                binding.giftCardUi.show()
                                println("------>${this.respPayModes}")
                                val adapter = GiftCardAdapter(
                                    context,
                                    this.respPayModes,
                                    this@PaymentListAdapter
                                )
                                binding.recyclerOffer.layoutManager = LinearLayoutManager(
                                    context, LinearLayoutManager.HORIZONTAL, false
                                )
                                binding.recyclerOffer.adapter = adapter

                            } else {
                                binding.imageView63.setImageResource(R.drawable.arrow_down)
                                binding.giftCardUi.hide()

                            }
                        }
                        "WALLET" -> {
                            if (binding.wallet.visibility == View.GONE) {
                                binding.wallet.show()
                                binding.imageView63.setImageResource(R.drawable.arrow_up)

                                binding.textView158.text =
                                    context.getString(R.string.wallet_balance) + this.respPayModes[0].balance
                                binding.textView159.setOnClickListener {
                                    listner.walletItemApply(this)
                                }
                            } else {
                                binding.imageView63.setImageResource(R.drawable.arrow_down)

                                binding.wallet.hide()

                            }
                        }
                        "GATEWAY" -> {
                            if (binding.cardUi.visibility == View.GONE) {
                                binding.imageView63.setImageResource(R.drawable.arrow_up)
                                binding.cardUi.show()
                                binding.creditCard.setOnClickListener {
                                    listner.onCreditCardItemClick(this)
                                }
                                binding.knet.setOnClickListener {
                                    listner.onKnetItemClick(this)
                                }
                            } else {
                                binding.imageView63.setImageResource(R.drawable.arrow_down)
                                binding.cardUi.hide()

                            }

                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return payMode.size
    }


    interface RecycleViewItemClickListener {
        fun walletItemApply(view: PaymentListResponse.Output.PayMode)
        fun onSimilarItemClick(view: GetMovieResponse.Output.Similar)
        fun onCreditCardItemClick(view: PaymentListResponse.Output.PayMode)
        fun onKnetItemClick(view: PaymentListResponse.Output.PayMode)
    }

    override fun giftCardClick(view: PaymentListResponse.Output.PayMode.RespPayMode) {
        clickName = view.name
        notifyDataSetChanged()
    }
}