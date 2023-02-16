package com.cinescape1.ui.main.views.payment.paymentList.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.BankModel
import com.cinescape1.data.models.responseModel.GetMovieResponse
import com.cinescape1.databinding.ItemPaymentListBinding
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity
import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import kotlinx.android.synthetic.main.account_preference_layout.*
import java.util.regex.Pattern

class PaymentListAdapter(
    private val context: Activity,
    private val payMode: ArrayList<PaymentListResponse.Output.PayMode>,
    private val listner: RecycleViewItemClickListener) : RecyclerView.Adapter<PaymentListAdapter.ViewHolder>(),
    GiftCardAdapter.RecycleViewItemClickListener {
    private var clickName = ""
    private var clickId = ""
    private var offerId = ""
    private var cardNo = ""

    private var knetClick = false
    private var creditCardClick = false

    inner class ViewHolder(val binding: ItemPaymentListBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPaymentListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

                if (PaymentListActivity.giftApplied){
                    binding.imageView63.isClickable = false
                    binding.imageView63.isEnabled = false
                    binding.knet.isClickable = false
                    binding.knet.isEnabled = false
                    //offer Click
                    binding.textView157.isClickable = false
                    binding.textView157.isEnabled = false
                    //wallet
                    binding.textView159.isClickable = false
                    binding.textView159.isEnabled = false
                    //gift Cart
                    binding.offerEditText.isClickable = false
                    binding.offerEditText.isEnabled = false
                    binding.offerEditText.isFocusable = false
                }else{
                    binding.knet.isClickable = true
                    binding.knet.isEnabled = true
                    binding.imageView63.isClickable = true
                    binding.imageView63.isEnabled = true
                    //offer Click
                    binding.textView157.isClickable = true
                    binding.textView157.isEnabled = true
                    //wallet
                    binding.textView159.isClickable = true
                    binding.textView159.isEnabled = true
                    //gift Cart
                    binding.offerEditText.isClickable = true
                    binding.offerEditText.isEnabled = true
                    binding.offerEditText.isFocusable = true
                }

                if (PaymentListActivity.offerApplied) {
                    binding.imageView63.isClickable = false
                    binding.imageView63.isEnabled = false
                    binding.knet.isClickable = false
                    binding.knet.isEnabled = false
                    //offer Click
                    binding.textView157.isClickable = false
                    binding.textView157.isEnabled = false
                    //wallet
                    binding.textView159.isClickable = false
                    binding.textView159.isEnabled = false
                    //gift Cart
                    binding.offerEditText.isClickable = false
                    binding.offerEditText.isEnabled = false
                    binding.offerEditText.isFocusable = false
                } else {
                    binding.knet.isClickable = true
                    binding.knet.isEnabled = true
                    binding.imageView63.isClickable = true
                    binding.imageView63.isEnabled = true
                    //offer Click
                    binding.textView157.isClickable = true
                    binding.textView157.isEnabled = true
                    //wallet
                    binding.textView159.isClickable = true
                    binding.textView159.isEnabled = true
                    //gift Cart
                    binding.offerEditText.isClickable = true
                    binding.offerEditText.isEnabled = true
                    binding.offerEditText.isFocusable = true
                }

                if (this.payType == "GATEWAY") {
                    println("imageUrl----1>${this.respPayModes.size}")
                    Glide.with(context).load(this.respPayModes[1].imageUrl).into(binding.imageCreditCard)
                    Glide.with(context).load(this.respPayModes[0].imageUrl).into(binding.imageKnet)
                }


                //Card Click
                binding.creditCard.setOnClickListener {
                    creditCardClick = true
                    knetClick = false
                    binding.imageCreditCard.setImageResource(0)
                    binding.imageKnet.setImageResource(0)

//                    Glide.with(context).load(this.respPayModes[1].activeImageUrl).into(binding.imageCreditCard)
//                    Glide.with(context).load(this.respPayModes[0].imageUrl).into(binding.imageKnet)

                    binding.imageCreditCard.setColorFilter(ContextCompat.getColor(context, R.color.red))
                    binding.imageKnet.setColorFilter(ContextCompat.getColor(context, R.color.white))

                    binding.textKnetName.setTextColor(context.getColor(R.color.white))
                    binding.textCreditCardName.setTextColor(context.getColor(R.color.red))
                    listner.onCreditCardItemClick(this, cardNo, creditCardClick, knetClick)
                    notifyDataSetChanged()
                }

                binding.knet.setOnClickListener {
                    knetClick = true
                    creditCardClick = false
//                    Glide.with(context).load(this.respPayModes[0].activeImageUrl).into(binding.imageKnet)
//                    Glide.with(context).load(this.respPayModes[1].imageUrl).into(binding.imageCreditCard)

                    binding.imageCreditCard.setColorFilter(ContextCompat.getColor(context, R.color.white))
                    binding.imageKnet.setColorFilter(ContextCompat.getColor(context, R.color.red))

                    binding.textKnetName.setTextColor(context.getColor(R.color.red))
                    binding.textCreditCardName.setTextColor(context.getColor(R.color.white))
                    listner.onKnitItemClick(this, creditCardClick, knetClick)
                    notifyDataSetChanged()
                }

                //show Hide
                binding.offerEditText.hint = clickName
                binding.consItemClick.setOnClickListener {
                    when (this.payType) {
                        "BANK" -> {
                            if (binding.bankOffer.visibility == View.GONE) {
                                binding.imageView63.setImageResource(R.drawable.arrow_up)
                                binding.bankOffer.show()
                                val list: ArrayList<PaymentListResponse.Output.PayMode.RespPayMode.PayModeBank> = ArrayList()
                                list.add(PaymentListResponse.Output.PayMode.RespPayMode.PayModeBank(0,"Available Bank Offers"))
                                    list.addAll(this.respPayModes[0].payModeBanks)



//                                val array_adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)
//                                array_adapter.setDropDownViewResource(R.layout.bank_offer)
//
//                                binding.spinner2.adapter = array_adapter

                                val customAdapter = BankOfferAdapter(context, list)
                                binding.spinner2.adapter = customAdapter

                                binding.spinner2.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parent: AdapterView<*>,
                                            view: View,
                                            position: Int,
                                            id: Long) {

                                            println("customAdapterPosition------>${position}")
                                            offerId = list[position].id.toString()

                                            val value = parent.getItemAtPosition(position) as PaymentListResponse.Output.PayMode.RespPayMode.PayModeBank
                                            view.setBackgroundColor(Color.parseColor("#000000"))
                                            if(value == list[0]){
                                                (view as ConstraintLayout).findViewById<TextView>(R.id.textView21).setTextColor(Color.parseColor("#ADADAD"))
//                                                binding.linearLayout7.hide()
//                                                binding.constraintLayout25.hide()
                                            }
//                                            binding.linearLayout7.show()
//                                            binding.constraintLayout25.show()

                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {
//                                            view.setBackgroundColor(Color.parseColor("#000000"))
                                        }
                                    }

                                binding.bankApply.setOnClickListener {
                                    cardNo = binding.bankEdit.text.toString().replace(" ", "")
                                    if (cardNo == "") {

                                        val dialog = OptionDialog(context,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            "Bank offer can not be empty",
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {},
                                            negativeClick = {})
                                        dialog.show()

                                    } else if (cardNo.length != 16) {
                                        val dialog = OptionDialog(context,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            "Enter valid card number.",
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {},
                                            negativeClick = {})
                                        dialog.show()
                                    } else {

                                        listner.bankItemApply(
                                            offerId,
                                            cardNo,
                                            binding.checkBox,
                                            binding.imageView64,
                                            binding.bankApply,
                                            binding.banksCancel,
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

                                    override fun onTextChanged(s: CharSequence, st: Int, be: Int, count: Int) {
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
                                                i, i + 4
                                            ) + space else numbersOnly.substring(i)
                                            i += 4
                                        }
                                        binding.bankEdit.setText(formatted)
                                        binding.bankEdit.setSelection(binding.bankEdit.text.toString().length)

                                    }

                                    override fun beforeTextChanged(
                                        s: CharSequence, start: Int, count: Int, after: Int
                                    ) {
                                    }

                                    override fun afterTextChanged(e: Editable) {}
                                })

                                //remove
                                binding.banksCancel.setOnClickListener {
                                    cardNo = binding.bankEdit.text.toString().replace(" ", "")

                                    binding.bankEdit.isClickable = true
                                    binding.bankEdit.isFocusable = true
                                    binding.bankEdit.isEnabled = true
                                    binding.bankEdit.isFocusableInTouchMode = true

                                    listner.bankItemRemove(
                                        offerId,
                                        cardNo,
                                        binding.checkBox,
                                        binding.imageView64,
                                        binding.bankApply,
                                        binding.banksCancel,
                                        binding.bankEdit,
                                        binding.editTextTextPersonName2,
                                        binding.knet,
                                        binding.textView158,
                                        binding.textView157,
                                        binding.offerEditText)

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
                                            "$clickName can not be empty",
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {},
                                            negativeClick = {})
                                        dialog.show()
                                    } else {
                                        listner.onVoucherApply(
                                            this,
                                            offerCode,
                                            clickName,
                                            clickId,
                                            binding.offerEditText,
                                            binding.textView157,
                                            binding.checkBox2,
                                            binding.imageView66
                                        )
                                    }
                                }
                                //remove voucher
                                binding.imageView66.setOnClickListener {
                                    val offerCode = binding.offerEditText.text.toString()
                                    listner.onGiftCardItemRemove(
                                        this,
                                        offerCode,
                                        clickName,
                                        clickId,
                                        binding.offerEditText,
                                        binding.textView157,
                                        binding.checkBox2,
                                        binding.imageView66
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

                                binding.textView158.text = context.getString(R.string.wallet_balance) + this.respPayModes[0].balance

                                // apply
                                binding.textView159.setOnClickListener {
                                    listner.walletItemApply(this)
//                                    binding.cancelBtn.show()
//                                    binding.textView159.hide()

//                                    creditCardClick = false
//                                    knetClick = false
//                                    Glide.with(context).load(this.respPayModes[1].imageUrl).into(binding.imageCreditCard)
//                                    Glide.with(context).load(this.respPayModes[0].imageUrl).into(binding.imageKnet)

//                                    binding.imageCreditCard.setColorFilter(ContextCompat.getColor(context, R.color.white))
//                                    binding.imageKnet.setColorFilter(ContextCompat.getColor(context, R.color.white))
//
//                                    binding.textKnetName.setTextColor(context.getColor(R.color.white))
//                                    binding.textCreditCardName.setTextColor(context.getColor(R.color.white))

//                                    listner.onCreditCardItemClick(this, cardNo, creditCardClick, knetClick)
//                                    listner.onKnitItemClick(this, creditCardClick, knetClick)
//                                    notifyDataSetChanged()

                                }

                                binding.cancelBtn.setOnClickListener {
                                    binding.cancelBtn.hide()
                                    binding.textView159.show()

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
                                    creditCardClick = true
                                    knetClick = false

//                                  Glide.with(context).load(this.respPayModes[1].activeImageUrl).into(binding.imageCreditCard)
//                                  Glide.with(context).load(this.respPayModes[0].imageUrl).into(binding.imageKnet)

                                    binding.imageCreditCard.setColorFilter(ContextCompat.getColor(context, R.color.red))
                                    binding.imageKnet.setColorFilter(ContextCompat.getColor(context, R.color.white))

                                    binding.textKnetName.setTextColor(context.getColor(R.color.white))
                                    binding.textCreditCardName.setTextColor(context.getColor(R.color.red))
                                    listner.onCreditCardItemClick(this, cardNo, creditCardClick, knetClick)

                                }
                                binding.knet.setOnClickListener {
                                    knetClick = true
                                    creditCardClick = false
//                                    Glide.with(context).load(this.respPayModes[0].activeImageUrl).into(binding.imageKnet)
//                                    Glide.with(context).load(this.respPayModes[1].imageUrl).into(binding.imageCreditCard)

                                    binding.imageCreditCard.setColorFilter(ContextCompat.getColor(context, R.color.white))
                                    binding.imageKnet.setColorFilter(ContextCompat.getColor(context, R.color.red))

                                    binding.textKnetName.setTextColor(context.getColor(R.color.red))
                                    binding.textCreditCardName.setTextColor(context.getColor(R.color.white))

                                    listner.onKnitItemClick(this, creditCardClick, knetClick)
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
            banksCancel: TextView,
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
            banksCancel: TextView,
            bankEdit: EditText,
            editTextTextPersonName2: TextView,
            knet: LinearLayout,
            textView158: TextView,
            textView157: TextView,
            offerEditText: EditText
        )

        fun onSimilarItemClick(view: GetMovieResponse.Output.Similar)
        fun onCreditCardItemClick(
            view: PaymentListResponse.Output.PayMode,
            cardNo: String,
            creditCardClick: Boolean,
            knetClick: Boolean
        )

        fun onKnitItemClick(
            view: PaymentListResponse.Output.PayMode, creditCardClick: Boolean, knetClick: Boolean
        )

        fun onVoucherApply(
            view: PaymentListResponse.Output.PayMode,
            offerCode: String,
            clickName: String,
            clickId: String,
            offerEditText: EditText,
            textView157: TextView,
            checkBox2: ImageView,
            imageView66: ImageView
        )

        fun onGiftCardItemRemove(
            view: PaymentListResponse.Output.PayMode,
            offerCode: String,
            clickName: String,
            clickId: String,
            offerEditText: EditText,
            textView157: TextView,
            checkBox2: ImageView,
            imageView66: ImageView
        )
    }

    override fun giftCardClick(view: PaymentListResponse.Output.PayMode.RespPayMode) {
        clickName = view.name
        clickId = view.id.toString()
        notifyDataSetChanged()
    }
}