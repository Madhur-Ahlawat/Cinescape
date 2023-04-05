package com.cinescape1.ui.main.views.payment.paymentList.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.GetMovieResponse
import com.cinescape1.databinding.ItemPaymentListBinding
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.payment.PaymentMethodSealedClass
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity
import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse
import com.cinescape1.ui.main.views.summery.viewModel.SummeryViewModel
import com.cinescape1.utils.Constant
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import com.cinescape1.utils.toast
import kotlinx.android.synthetic.main.account_preference_layout.*
import kotlinx.android.synthetic.main.activity_checkout_with_food.*
import java.util.*
import java.util.regex.Pattern

class PaymentListAdapter(
    private val context: Activity,
    private val payMode: ArrayList<PaymentListResponse.Output.PayMode>,
    private val listner: RecycleViewItemClickListener, private val viewModel: SummeryViewModel
) : RecyclerView.Adapter<PaymentListAdapter.ViewHolder>(),
    GiftCardAdapter.RecycleViewItemClickListener {
    public var customAdapter: BankOfferAdapter? = null
    private var clickName = ""
    private var clickId = ""
    private var offerId = ""
    private var cardNo = ""
    private var knetClick = false
    private var creditCardClick = false

    private var cartBank = true
    private var cartGift = true
    private var cartWallet = true
    private var cancelWallet = false

    inner class ViewHolder(val binding: ItemPaymentListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPaymentListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(payMode[position]) {

                //title
                binding.headerOfferType.text = this.name

                //wallet
                if (this.payType == "GATEWAY") {
                    binding.ivDropdown.setImageResource(R.drawable.arrow_up)
                    binding.cardPaymentOptionsUi.show()
                    Glide.with(context)
                        .load(this.respPayModes[1].imageUrl)
                        .into(binding.imageCreditCard)

                    Glide.with(context)
                        .load(this.respPayModes[0].imageUrl)
                        .into(binding.imageKnet)

                } else {
                    binding.cardPaymentOptionsUi.hide()
                }

                if (PaymentListActivity.giftApplied) {
                    binding.clHeaderLabelAndDropdown.isClickable = false
                    binding.clHeaderLabelAndDropdown.isEnabled = false
                    binding.knet.isClickable = false
                    binding.knet.isEnabled = false

                    //offer Click
                    binding.tvApplyCardOffer.isClickable = false
                    binding.tvApplyCardOffer.isEnabled = false
                    //wallet
                    binding.textviewBtWalletApply.isClickable = false
                    binding.textviewBtWalletApply.isEnabled = false
                    //gift Cart
                    binding.offerEditText.isClickable = false
                    binding.offerEditText.isEnabled = false
                    binding.offerEditText.isFocusable = false

                } else {
                    binding.knet.isClickable = true
                    binding.knet.isEnabled = true
                    binding.clHeaderLabelAndDropdown.isClickable = true
                    binding.clHeaderLabelAndDropdown.isEnabled = true
                    //offer Click
                    binding.tvApplyCardOffer.isClickable = true
                    binding.tvApplyCardOffer.isEnabled = true
                    //wallet
                    binding.textviewBtWalletApply.isClickable = true
                    binding.textviewBtWalletApply.isEnabled = true
                    //gift Cart
                    binding.offerEditText.isClickable = true
                    binding.offerEditText.isEnabled = true
                    binding.offerEditText.isFocusable = true
                }

                if (PaymentListActivity.offerApplied) {
                    binding.clHeaderLabelAndDropdown.isClickable = false
                    binding.clHeaderLabelAndDropdown.isEnabled = false
                    binding.knet.isClickable = false
                    binding.knet.isEnabled = false
                    //offer Click
                    binding.tvApplyCardOffer.isClickable = false
                    binding.tvApplyCardOffer.isEnabled = false
                    binding.tvApplyCardOffer.hide()

                    //wallet
                    binding.textviewBtWalletApply.isClickable = false
                    binding.textviewBtWalletApply.isEnabled = false
                    //gift Cart
                    binding.offerEditText.isClickable = false
                    binding.offerEditText.isEnabled = false
                    binding.offerEditText.isFocusable = false
                } else {
                    binding.knet.isClickable = true
                    binding.knet.isEnabled = true
                    binding.clHeaderLabelAndDropdown.isClickable = true
                    binding.clHeaderLabelAndDropdown.isEnabled = true
                    //offer Click
                    binding.tvApplyCardOffer.isClickable = true
                    binding.tvApplyCardOffer.isEnabled = true
                    //wallet
                    binding.textviewBtWalletApply.isClickable = true
                    binding.textviewBtWalletApply.isEnabled = true
                    //gift Cart
                    binding.offerEditText.isClickable = true
                    binding.offerEditText.isEnabled = true
                    binding.offerEditText.isFocusable = true
                }


                //Card Click
                binding.creditCard.setOnClickListener {
                    creditCardClick = true
                    knetClick = false
                    context.toast("$cancelWallet")
                    Constant.CARD_NO = cardNo
                    viewModel.setPaymentMethodSelection(PaymentMethodSealedClass.CREDIT_CARD)
                    notifyDataSetChanged()
                }

                //Knet Click
                binding.knet.setOnClickListener {
                    Constant.CARD_NO = ""
                    cancelWallet = false
                    viewModel.setPaymentMethodSelection(PaymentMethodSealedClass.KNET)
                    knetClick = true
                    creditCardClick = false
                    notifyDataSetChanged()
                }
                // apply
                binding.textviewBtWalletApply.setOnClickListener {
                    viewModel.setPaymentMethodSelection(PaymentMethodSealedClass.WALLET)
                    context.toast("apply")
                    Constant.CARD_NO = ""
                    cancelWallet = true
                    knetClick = false
                    creditCardClick = false
                    notifyDataSetChanged()
                }

//                wallet cancel
                binding.cancelBtn.setOnClickListener {
                    Constant.CARD_NO = ""
                    context.toast("cancel Wallet")
                    cancelWallet = true
                    knetClick = false
                    creditCardClick = false
                    viewModel.setPaymentMethodSelection(PaymentMethodSealedClass.NONE)
                    notifyDataSetChanged()
                }

                //show Hide
                binding.clHeaderLabelAndDropdown.setOnClickListener {
                    println("payType---->${this.payType}")
                    when (this.payType) {
                        "BANK" -> {
                            if (cartBank == true) {
                                cartBank = false
                                binding.ivDropdown.setImageResource(R.drawable.arrow_up)
                                binding.bankOffer.show()
                                binding.wallet.hide()
                                binding.giftCardUi.hide()
                                val list: ArrayList<PaymentListResponse.Output.PayMode.RespPayMode.PayModeBank> =
                                    ArrayList()
                                list.add(
                                    PaymentListResponse.Output.PayMode.RespPayMode.PayModeBank(
                                        0,
                                        "Available Bank Offers"
                                    )
                                )
                                list.addAll(this.respPayModes[0].payModeBanks)

                                customAdapter = BankOfferAdapter(context, list)
                                binding.spinnerCardOptions.adapter = customAdapter
                                binding.spinnerCardOptions.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parent: AdapterView<*>,
                                            view: View,
                                            position: Int,
                                            id: Long
                                        ) {

                                            println("customAdapterPosition------>${position}")
                                            offerId = list[position].id.toString()
                                            if (position == 0) {
                                                binding.clEnterCardNumber.hide()
                                            } else {
                                                binding.clEnterCardNumber.show()
                                            }

                                            val value =
                                                parent.getItemAtPosition(position) as PaymentListResponse.Output.PayMode.RespPayMode.PayModeBank
                                            view.setBackgroundColor(Color.parseColor("#000000"))
                                            if (value == list[0]) {
                                                (view as ConstraintLayout).findViewById<TextView>(R.id.textView21)
                                                    .setTextColor(Color.parseColor("#ADADAD"))
                                            }

                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {
//                                            view.setBackgroundColor(Color.parseColor("#000000"))
                                        }
                                    }

                                binding.tvBankApply.setOnClickListener {
                                    cardNo =
                                        binding.etEnterCardNumber.text.toString().replace(" ", "")
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
                                            binding.ivCrossCancel,
                                            binding.tvBankApply,
                                            binding.banksCancel,
                                            binding.etEnterCardNumber,
                                            binding.tvOfferAppliedForNTickets,
                                            binding.knet,
                                            binding.textViewWalletBalance,
                                            binding.tvApplyCardOffer,
                                            binding.offerEditText
                                        )

                                    }
                                }

//                                binding.bankEdit.addTextChangedListener(
//                                    FourDigitCardFormatWatcher()
//                                )


                                binding.etEnterCardNumber.addTextChangedListener(object :
                                    TextWatcher {
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
                                        val currentText: String =
                                            binding.etEnterCardNumber.text.toString()
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
                                        binding.etEnterCardNumber.setText(formatted)
                                        binding.etEnterCardNumber.setSelection(binding.etEnterCardNumber.text.toString().length)

                                    }

                                    override fun beforeTextChanged(
                                        s: CharSequence, start: Int, count: Int, after: Int
                                    ) {
                                    }

                                    override fun afterTextChanged(e: Editable) {}
                                })

                                //remove
                                binding.banksCancel.setOnClickListener {
                                    cardNo =
                                        binding.etEnterCardNumber.text.toString().replace(" ", "")
                                    binding.etEnterCardNumber.isClickable = true
                                    binding.etEnterCardNumber.isFocusable = true
                                    binding.etEnterCardNumber.isEnabled = true
                                    binding.etEnterCardNumber.isFocusableInTouchMode = true

                                    listner.bankItemRemove(
                                        offerId,
                                        cardNo,
                                        binding.checkBox,
                                        binding.ivCrossCancel,
                                        binding.tvBankApply,
                                        binding.banksCancel,
                                        binding.etEnterCardNumber,
                                        binding.tvOfferAppliedForNTickets,
                                        binding.knet,
                                        binding.textViewWalletBalance,
                                        binding.tvApplyCardOffer,
                                        binding.offerEditText
                                    )

                                }

                            } else {
                                cartBank = true

                                println("cartBankTrue------->${cartBank}")
                                binding.ivDropdown.setImageResource(R.drawable.arrow_down)
                                binding.bankOffer.hide()
                                binding.wallet.hide()
                                binding.giftCardUi.hide()
                            }
                        }
                        "OFFER" -> {

                            if (cartGift == true) {
                                println("------>${this.respPayModes}")
                                cartGift = false
                                clickName = this.respPayModes[0].name
                                binding.ivDropdown.setImageResource(R.drawable.arrow_up)
                                binding.bankOffer.hide()
                                binding.wallet.hide()
                                binding.giftCardUi.show()

                                binding.offerEditText.hint = clickName

                                binding.giftCard.setOnClickListener {
                                    binding.bankOffer.hide()
                                    binding.wallet.hide()
                                    binding.giftCardUi.show()
                                    notifyDataSetChanged()
                                }

                                val adapter = GiftCardAdapter(
                                    context, this.respPayModes, this@PaymentListAdapter
                                )
                                binding.recyclerOffer.layoutManager = LinearLayoutManager(
                                    context, LinearLayoutManager.HORIZONTAL, false
                                )
                                binding.recyclerOffer.adapter = adapter

                                binding.tvApplyCardOffer.setOnClickListener {
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
                                            binding.tvApplyCardOffer,
                                            binding.checkBox2,
                                            binding.imageView66,
                                            binding.textCancelBtn,
                                        )
                                    }
                                }

                                // Cancel btn
                                binding.textCancelBtn.setOnClickListener {
                                    binding.textCancelBtn.hide()
                                    binding.tvApplyCardOffer.show()
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
                                        binding.tvApplyCardOffer,
                                        binding.checkBox2,
                                        binding.imageView66
                                    )

                                }
                            } else {
                                cartGift = true
                                binding.ivDropdown.setImageResource(R.drawable.arrow_down)
                                binding.bankOffer.hide()
                                binding.wallet.hide()
                                binding.giftCardUi.hide()
                            }
                        }
                        "WALLET" -> {
                            if (cartWallet == true) {

                                cartWallet = false
                                binding.bankOffer.hide()
                                binding.wallet.show()
                                binding.giftCardUi.hide()

                                binding.ivDropdown.setImageResource(R.drawable.arrow_up)
                                binding.textViewWalletBalance.text =
                                    context.getString(R.string.wallet_balance) + this.respPayModes[0].balance
//
//                                // apply
//                                binding.textviewBtWalletApply.setOnClickListener {
//
//                                    cancelWallet= false
//                                    applyCheck = 1
//                                    binding.cancelBtn.show()
//                                    binding.textviewBtWalletApply.hide()
//                                }
//
//                                binding.cancelBtn.setOnClickListener {
//                                    cancelWallet= true
//                                    applyCheck = 2
//                                    binding.cancelBtn.hide()
//                                    binding.textviewBtWalletApply.show()
//                                }

                            } else {
                                cartWallet = true
                                binding.ivDropdown.setImageResource(R.drawable.arrow_down)
                                binding.bankOffer.hide()
                                binding.wallet.hide()
                                binding.giftCardUi.hide()
                            }
                        }
                        "GATEWAY" -> {
                            binding.ivDropdown.setImageResource(R.drawable.arrow_up)
                            binding.cardPaymentOptionsUi.show()
                            binding.bankOffer.hide()
                            binding.wallet.hide()
                            binding.giftCardUi.hide()

                            binding.creditCard.setOnClickListener {
                                creditCardClick = true
                                knetClick = false
                                cancelWallet = true
                                viewModel.setPaymentMethodSelection(PaymentMethodSealedClass.CREDIT_CARD)
                                notifyDataSetChanged()

                            }
                            binding.knet.setOnClickListener {
                                knetClick = true
                                creditCardClick = false
                                cancelWallet = true
                                viewModel.setPaymentMethodSelection(PaymentMethodSealedClass.KNET)
//                                    Glide.with(context).load(this.respPayModes[0].activeImageUrl).into(binding.imageKnet)
//                                    Glide.with(context).load(this.respPayModes[1].imageUrl).into(binding.imageCreditCard)

                                notifyDataSetChanged()
                            }

                        }
                    }
                    notifyDataSetChanged()
                }

            }
        }
        when (viewModel.selectedPaymentMethod) {
            PaymentMethodSealedClass.WALLET -> {
                holder.binding?.apply {
                    cancelBtn.show()
                    textviewBtWalletApply.hide()
                    imageCreditCard.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                    imageKnet.setColorFilter(ContextCompat.getColor(context, R.color.white))
                    textKnetName.setTextColor(context.getColor(R.color.white))
                    textCreditCardName.setTextColor(context.getColor(R.color.white))
                }
            }
            PaymentMethodSealedClass.KNET -> {
                holder.binding.apply {
                    textKnetName.setTextColor(context.getColor(R.color.red))
                    textCreditCardName.setTextColor(context.getColor(R.color.white))
                    cancelBtn.hide()
                    textviewBtWalletApply.show()
                    imageCreditCard.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                    imageKnet.setColorFilter(ContextCompat.getColor(context, R.color.red))
                    textKnetName.setTextColor(context.getColor(R.color.red))
                    textCreditCardName.setTextColor(context.getColor(R.color.white))
                }
            }
            PaymentMethodSealedClass.CREDIT_CARD -> {
                holder.binding.apply {
                    cancelBtn.hide()
                    textviewBtWalletApply.show()
                    imageCreditCard.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.red
                        )
                    )
                    imageKnet.setColorFilter(ContextCompat.getColor(context, R.color.white))
                    imageCreditCard.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.red
                        )
                    )
                    textKnetName.setTextColor(context.getColor(R.color.white))
                    textCreditCardName.setTextColor(context.getColor(R.color.red))
                }
            }
            PaymentMethodSealedClass.NONE -> {
                holder.binding.apply {
                    cancelBtn.hide()
                    textviewBtWalletApply.show()
                    imageCreditCard.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                    imageKnet.setColorFilter(ContextCompat.getColor(context, R.color.white))
                    textKnetName.setTextColor(context.getColor(R.color.white))
                    textCreditCardName.setTextColor(context.getColor(R.color.white))
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
            tvApply: TextView,
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
            tvApply: TextView,
            offerEditText: EditText
        )

        fun onSimilarItemClick(view: GetMovieResponse.Output.Similar)

        fun onVoucherApply(
            view: PaymentListResponse.Output.PayMode,
            offerCode: String,
            clickName: String,
            clickId: String,
            offerEditText: EditText,
            tvApply: TextView,
            checkBox2: ImageView,
            imageView66: ImageView,
            textCancelBtn: TextView
        )

        fun onGiftCardItemRemove(
            view: PaymentListResponse.Output.PayMode,
            offerCode: String,
            clickName: String,
            clickId: String,
            offerEditText: EditText,
            tvApply: TextView,
            checkBox2: ImageView,
            imageView66: ImageView
        )
    }

    override fun giftCardClick(view: PaymentListResponse.Output.PayMode.RespPayMode) {
        clickName = view.name
        clickId = view.id.toString()
        notifyDataSetChanged()
    }

    fun Activity.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}