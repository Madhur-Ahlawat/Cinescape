package com.cinescape1.ui.main.views.payment.paymentList.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.GetMovieResponse
import com.cinescape1.databinding.ItemPaymentListBinding
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity
import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import com.cinescape1.utils.toast
import kotlinx.android.synthetic.main.account_preference_layout.*
import java.util.regex.Pattern


class PaymentListAdapter(
    private val context: Activity,
    private val payMode: ArrayList<PaymentListResponse.Output.PayMode>,
    private val listner: RecycleViewItemClickListener
) : RecyclerView.Adapter<PaymentListAdapter.ViewHolder>(),
    GiftCardAdapter.RecycleViewItemClickListener {
    private var clickName = ""
    private var clickId = ""
    private var offerId = ""
    private var cardNo = ""

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
                if (PaymentListActivity.offerApplied){
                    binding.imageView63.isClickable = false
                    binding.imageView63.isEnabled = false
                    binding.knet.isClickable = false
                    binding.knet.isEnabled = false
                }else{
                    binding.knet.isClickable = true
                    binding.knet.isEnabled = true
                    binding.imageView63.isClickable = true
                    binding.imageView63.isEnabled = true
                }

                //Card Click
                binding.creditCard.setOnClickListener {
                    listner.onCreditCardItemClick(this, cardNo)
                    notifyDataSetChanged()
                }
                binding.knet.setOnClickListener {
                    listner.onKnitItemClick(this)
                    notifyDataSetChanged()
                }

                //show Hide
                binding.offerEditText.hint = clickName
                binding.imageView63.setOnClickListener {
                    when (this.payType) {
                        "BANK" -> {
                            if (binding.bankOffer.visibility == View.GONE) {
                                binding.imageView63.setImageResource(R.drawable.arrow_up)
                                binding.bankOffer.show()
                                val list: ArrayList<PaymentListResponse.Output.PayMode.RespPayMode.PayModeBank> =
                                    this.respPayModes[0].payModeBanks
                                val customAdapter = BankOfferAdapter(
                                    context, list
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
                                            offerId = list[position].id.toString()
                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {

                                        }
                                    }

                                binding.bankApply.setOnClickListener {
                                    cardNo = binding.bankEdit.text.toString().replace(" ", "")
                                    if (cardNo==""){
                                        val dialog = OptionDialog(context,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            "Bank offer can not we empty",
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {},
                                            negativeClick = {})
                                        dialog.show()
                                    }else  if(cardNo.length!=16){
                                        val dialog = OptionDialog(context,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            "Enter valid card number.",
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {},
                                            negativeClick = {})
                                        dialog.show()
                                    }else{
                                        listner.bankItemApply(
                                            offerId,
                                            cardNo,
                                            binding.checkBox,
                                            binding.imageView64,
                                            binding.bankApply,
                                            binding.bankEdit,
                                            binding.editTextTextPersonName2,
                                            binding.knet,
                                            binding.textView158,
                                            binding.textView157,
                                            binding.offerEditText
                                        )
                                    }
                                }

//                                binding.bankEdit.addTextChangedListener(
//                                    FourDigitCardFormatWatcher()
//                                )


                                binding.bankEdit.addTextChangedListener(object : TextWatcher {
                                    private val space =
                                        " " // you can change this to whatever you want
                                    private val pattern: Pattern =
                                        Pattern.compile("^(\\d{4}$space{1}){0,3}\\d{1,4}$") // check whether we need to modify or not

                                    override fun onTextChanged(
                                        s: CharSequence,
                                        st: Int,
                                        be: Int,
                                        count: Int
                                    ) {
                                        val currentText: String = binding.bankEdit.text.toString()
                                        if (currentText.isEmpty() || pattern.matcher(currentText)
                                                .matches()
                                        ) return  // no need to modify
                                        val numbersOnly = currentText.trim { it <= ' ' }
                                            .replace("[^\\d.]".toRegex(), "")
                                        // remove everything but numbers
                                        var formatted = ""
                                        var i = 0
                                        while (i < numbersOnly.length) {
                                            formatted += if (i + 4 < numbersOnly.length) numbersOnly.substring(
                                                i,
                                                i + 4
                                            ) + space else numbersOnly.substring(i)
                                            i += 4
                                        }
                                        binding.bankEdit.setText(formatted)
                                        binding.bankEdit.setSelection(
                                            binding.bankEdit.text.toString().length
                                        )
                                    }

                                    override fun beforeTextChanged(
                                        s: CharSequence,
                                        start: Int,
                                        count: Int,
                                        after: Int
                                    ) {
                                    }

                                    override fun afterTextChanged(e: Editable) {}
                                })

                                //remove
                                binding.imageView64.setOnClickListener {
                                    cardNo = binding.bankEdit.text.toString().replace(" ", "")
                                    listner.bankItemRemove(
                                        offerId,
                                        cardNo,
                                        binding.checkBox,
                                        binding.imageView64,
                                        binding.bankApply,
                                        binding.bankEdit,
                                        binding.editTextTextPersonName2,
                                        binding.knet,
                                        binding.textView158,
                                        binding.textView157,
                                        binding.offerEditText
                                    )
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
                                clickName = this.respPayModes[0].name
                                val adapter = GiftCardAdapter(
                                    context, this.respPayModes, this@PaymentListAdapter
                                )
                                binding.recyclerOffer.layoutManager = LinearLayoutManager(
                                    context, LinearLayoutManager.HORIZONTAL, false
                                )
                                binding.recyclerOffer.adapter = adapter

                                binding.textView157.setOnClickListener {
                                    val offerCode = binding.offerEditText.text.toString()
                                    if (offerCode == "") {
                                        val dialog = OptionDialog(context,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            "$clickName can not we empty",
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {},
                                            negativeClick = {})
                                        dialog.show()
                                    } else {
                                        listner.onVoucherItemClick(
                                            this, offerCode, clickName, clickId
                                        )
                                    }
                                }
                                //remove voucher
                                binding.imageView66.setOnClickListener {
                                    val offerCode = binding.offerEditText.text.toString()
                                    listner.onGiftCardItemRemove(
                                        this, offerCode, clickName, clickId
                                    )

                                }
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
                                    listner.onCreditCardItemClick(this,cardNo)
                                }
                                binding.knet.setOnClickListener {
                                    listner.onKnitItemClick(this)
                                }
                            } else {
                                binding.imageView63.setImageResource(R.drawable.arrow_down)
                                binding.cardUi.hide()

                            }

                        }
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return payMode.size
    }


    interface RecycleViewItemClickListener {
        fun walletItemApply(view: PaymentListResponse.Output.PayMode)
        fun bankItemApply(
            view: String,
            cardNo: String,
            binding: ImageView,
            checkBox: ImageView,
            imageView64: TextView,
            bankEdit: EditText,
            editTextTextPersonName2: TextView,
            knet: LinearLayout,
            textView158: TextView,
            textView157: TextView,
            offerEditText: EditText
        )

        fun bankItemRemove(
            view: String,
            cardNo: String,
            binding: ImageView,
            imageView64: ImageView,
            bankApply: TextView,
            bankEdit: EditText,
            editTextTextPersonName2: TextView,
            knet: LinearLayout,
            textView158: TextView,
            textView157: TextView,
            offerEditText: EditText
        )

        fun onSimilarItemClick(view: GetMovieResponse.Output.Similar)
        fun onCreditCardItemClick(view: PaymentListResponse.Output.PayMode, cardNo: String)
        fun onKnitItemClick(view: PaymentListResponse.Output.PayMode)
        fun onVoucherItemClick(
            view: PaymentListResponse.Output.PayMode,
            offerCode: String,
            clickName: String,
            clickId: String
        )

        fun onGiftCardItemRemove(
            view: PaymentListResponse.Output.PayMode,
            offerCode: String,
            clickName: String,
            clickId: String
        )
    }

    override fun giftCardClick(view: PaymentListResponse.Output.PayMode.RespPayMode) {
        clickName = view.name
        clickId = view.id.toString()
        notifyDataSetChanged()
    }
}