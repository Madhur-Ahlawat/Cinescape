package com.cinescape1.ui.main.views.payment.paymentList.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.InputType
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
import kotlinx.android.synthetic.main.account_preference_layout.*
import kotlinx.android.synthetic.main.activity_checkout_with_food.*
import java.util.*

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
    private var knetClicked = false
    private var creditCardClicked = false
    val list: ArrayList<PaymentListResponse.Output.PayMode.RespPayMode.PayModeBank> =
        ArrayList()
    private var bankClicked = true
    private var giftCardClicked = true
    private var walletClicked = true
    private var walletApplied = false

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
                binding.ivDropdown.show()
                //wallet
                if (this.payType == "GATEWAY") {
                    binding.ivDropdown.hide()
                    binding.knetCcUi.show()
                    Glide.with(context)
                        .load(this.respPayModes[1].imageUrl)
                        .into(binding.imageCreditCard)

                    Glide.with(context)
                        .load(this.respPayModes[0].imageUrl)
                        .into(binding.imageKnet)
                    binding?.view105.hide()
                    if (creditCardClicked) {
                        holder.binding.apply {
                            imageCreditCard.setColorFilter(
                                ContextCompat.getColor(
                                    context,
                                    R.color.red
                                )
                            )
                            textCreditCardName.setTextColor(context.getColor(R.color.red))
                            imageKnet.setColorFilter(ContextCompat.getColor(context, R.color.gray))
                            textKnetName.setTextColor(context.getColor(R.color.gray))
                        }
                    }
                    if (knetClicked) {
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
                    if (walletApplied) {
                        holder.binding?.apply {
                            cancelBtn.show()
                            textviewBtWalletApply.hide()
                        imageKnet.setColorFilter(ContextCompat.getColor(context, R.color.white))
                        textKnetName.setTextColor(context.getColor(R.color.white))
                        }
                    }

                    if (viewModel.selectedPaymentMethod == PaymentMethodSealedClass.GIFT_CARD_COMPLETE || viewModel.selectedPaymentMethod == PaymentMethodSealedClass.GIFT_CARD_PARTIAL) {
                        binding.knet.isClickable = false
                        binding.knet.isEnabled = false

                        holder.binding.apply {
                            imageCreditCard.setColorFilter(
                                ContextCompat.getColor(
                                    context,
                                    R.color.red
                                )
                            )
                            textCreditCardName.setTextColor(context.getColor(R.color.red))
                            imageKnet.setColorFilter(ContextCompat.getColor(context, R.color.gray))
                            textKnetName.setTextColor(context.getColor(R.color.gray))
                            imageKnet.isClickable = false
                            imageCreditCard.isClickable = false
                        }
                    } else {
                        binding.knet.isClickable = true
                        binding.knet.isEnabled = true
                        binding.creditCard.isClickable = true
                        binding.creditCard.isEnabled = true
                    }
                }
                else if (this.payType == "BANK") {
                    if (!PaymentListActivity.spinnerClickable) {
                        binding.spinnerCardOptions?.apply {
                            setEnabled(false);
                            setClickable(false);
                        }
                    }
                    else {
                        binding.spinnerCardOptions?.apply {
                            setEnabled(true);
                            setClickable(true);
                        }
                    }
                    list?.clear()
                    list.add(
                        PaymentListResponse.Output.PayMode.RespPayMode.PayModeBank(
                            0,
                            "Available Bank Offers"
                        )
                    )
                    list.addAll(this.respPayModes[0].payModeBanks)
                    binding.spinnerCardOptions.setBackgroundColor(
                        context.resources.getColor(
                            R.color.dropDownColor
                        )
                    )
                    binding.spinnerCardOptions.adapter = customAdapter
                    customAdapter = BankOfferAdapter(context, list)
                    binding.spinnerCardOptions.adapter=customAdapter
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
                                    binding.tvBankApply.hide()
                                } else {
                                    binding?.apply {
                                        offerEditText?.apply {
                                            etEnterCardNumber.text?.clear()
                                            etEnterCardNumber.isClickable = true
                                            etEnterCardNumber.isFocusable = true
                                            etEnterCardNumber.isEnabled = true
                                            etEnterCardNumber.isFocusableInTouchMode = true
                                            etEnterCardNumber.inputType =
                                                InputType.TYPE_CLASS_NUMBER
                                        }
                                        clEnterCardNumber.show()
                                        tvBankApply.show()
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
                                    }

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
                    if (viewModel.selectedPaymentMethod == PaymentMethodSealedClass.GIFT_CARD_COMPLETE || viewModel.selectedPaymentMethod == PaymentMethodSealedClass.GIFT_CARD_PARTIAL) {
                        //if gift card applied then hide the spinner
                        binding?.apply {
                            ivDropdown.setImageResource(R.drawable.arrow_down)
                            bankOfferChooseCards.hide()
                            walletUi.hide()
                            giftCardUi.hide()
                            knetCcUi.hide()
                            binding?.bankOfferChooseCards.hide()
                            headerOfferType.setTextColor(context.getColor(R.color.gray))
                        }
                    } else {
                        if (bankClicked) {
                            binding?.apply {
                                clChooseCards.show()
                                spinnerCardOptions.show()
                                clEnterCardNumber.show()
                                offerEditText.hide()
                                tvBankApply.show()
                                banksCancel.hide()
                                bankOfferChooseCards.show()
                                headerOfferType.setTextColor(context.getColor(R.color.white))
                                ivDropdown.setImageResource(R.drawable.arrow_down)
                            }
                        } else {
                            binding?.apply {
                                ivDropdown.setImageResource(R.drawable.arrow_up)
                                bankOfferChooseCards.hide()
                            }

                        }
                    }
                }
                else if (this.payType == "OFFER") {
                    if (giftCardClicked) {
                        clickName = this.respPayModes[0].name
                        binding?.apply {
                            ivDropdown.setImageResource(R.drawable.arrow_up)
                            bankOfferChooseCards.hide()
                            walletUi.hide()
                            giftCardUi.show()
                            offerEditText.hint =
                                context.resources.getString(R.string.enter_gift_card)
                        }


                        binding.giftCard.setOnClickListener {
                            binding?.apply {
                                bankOfferChooseCards.hide()
                                walletUi.hide()
                                giftCardUi.show()
                            }
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
                        binding.ivDropdown.setImageResource(R.drawable.arrow_down)
                        binding.bankOfferChooseCards.hide()
                        binding.walletUi.hide()
                        binding.giftCardUi.hide()
                    }
                }
                else if (this.payType == "WALLET") {
                    if (walletClicked == true) {
                        binding?.apply {
                            walletUi.show()
                            textViewWalletBalance.text =
                                context.getString(R.string.wallet_balance) + respPayModes[0].balance
                            bankOfferChooseCards.hide()
                            giftCardUi.hide()
                            ivDropdown.setImageResource(R.drawable.arrow_up)
                            if (walletApplied) {
                                textviewBtWalletApply.hide()
                                cancelBtn.show()
                            } else {
                                textviewBtWalletApply.show()
                                cancelBtn.hide()
                            }
                        }
                    } else {
                        binding?.apply {
                            ivDropdown.setImageResource(R.drawable.arrow_down)
                            bankOfferChooseCards.hide()
                            walletUi.hide()
                            giftCardUi.hide()
                            knetCcUi.hide()
                        }
                    }
                }
                else {
                    binding.headerOfferType.setTextColor(context.getColor(R.color.white))
                    binding.knetCcUi.hide()
                }
                //Card Click
                binding.creditCard.setOnClickListener {
                    creditCardClicked = true
                    knetClicked = false
                    viewModel.setPaymentMethodSelection(PaymentMethodSealedClass.NONE)
                    walletApplied = false
                    Constant.CARD_NO = cardNo
                    notifyDataSetChanged()
                }

                //Knet Click
                binding.knet.setOnClickListener {
                    Constant.CARD_NO = ""
                    walletApplied = false
                    knetClicked = true
                    viewModel.setPaymentMethodSelection(PaymentMethodSealedClass.NONE)
                    creditCardClicked = false
                    notifyDataSetChanged()
                }
                // apply
                binding.textviewBtWalletApply.setOnClickListener {
                    Constant.CARD_NO = ""
                    walletApplied = true
                    knetClicked = false
                    viewModel.setPaymentMethodSelection(PaymentMethodSealedClass.NONE)
                    creditCardClicked = false
                    notifyDataSetChanged()
                }

//                wallet cancel
                binding.cancelBtn.setOnClickListener {
                    viewModel.setPaymentMethodSelection(PaymentMethodSealedClass.NONE)
                    Constant.CARD_NO = ""
//                    context.toast("cancel Wallet")
                    walletApplied = false
                    notifyDataSetChanged()
                }
                //show Hide
                binding.clHeaderLabelAndDropdown.setOnClickListener {
                    println("payType---->${this.payType}")
                    when (this.payType) {
                        "BANK" -> {
                            if (bankClicked == true) {
                                bankClicked = false
                            } else {
                                bankClicked = true
                            }
                        }
                        "OFFER" -> {

                            if (giftCardClicked) {
                                giftCardClicked = false
                            } else {
                                giftCardClicked = true
                            }
                        }
                        "WALLET" -> {
                            if (walletClicked == true) {
                                walletClicked = false
                            } else {
                                walletClicked = true
                            }
                        }
                        "GATEWAY" -> {
                            binding.ivDropdown.setImageResource(R.drawable.arrow_up)
                            binding.knetCcUi.show()
                            binding.bankOfferChooseCards.hide()
                            binding.walletUi.hide()
                            binding.giftCardUi.hide()
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