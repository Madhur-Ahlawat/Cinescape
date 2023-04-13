package com.cinescape1.ui.main.views.payment.paymentList.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.GetMovieResponse
import com.cinescape1.databinding.ItemBankOfferBinding
import com.cinescape1.databinding.ItemGatewayUiBinding
import com.cinescape1.databinding.ItemGiftCardBinding
import com.cinescape1.databinding.ItemWalletUiBinding
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.payment.PaymentMethodSealedClass
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.bankApplied
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.bankClicked
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.cardNo
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.clickId
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.clickName
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.creditCardClicked
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.giftCardApplied
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.giftCardClicked
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.giftCardNumber
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.knetClicked
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.offerId
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.selectedCardType
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.walletApplied
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.walletClicked
import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse
import com.cinescape1.ui.main.views.summery.viewModel.SummeryViewModel
import com.cinescape1.utils.Constant
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import kotlinx.android.synthetic.main.account_preference_layout.*
import kotlinx.android.synthetic.main.activity_checkout_with_food.*
import java.util.*
import kotlin.collections.ArrayList


class PaymentListAdapter(
    private val context: Activity,
    private val payMode: ArrayList<PaymentListResponse.Output.PayMode>,
    private val listner: RecycleViewItemClickListener, private val viewModel: SummeryViewModel
) : RecyclerView.Adapter<PaymentListAdapter.PaymentListViewHolder>(),
    GiftCardAdapter.RecycleViewItemClickListener {
    private var pos: Int = -1
    public var customAdapter: BankOfferAdapter? = null
    val list: ArrayList<PaymentListResponse.Output.PayMode.RespPayMode.PayModeBank> =
        ArrayList()

    init {
        list?.clear()
    }

    private fun initSpinnerAdapter(respPayModes: ArrayList<PaymentListResponse.Output.PayMode.RespPayMode>) {
        list?.clear()
        list.add(
            PaymentListResponse.Output.PayMode.RespPayMode.PayModeBank(
                0,
                "Available Bank Offers"
            )
        )
        list.addAll(respPayModes[0].payModeBanks)
        customAdapter = BankOfferAdapter(context, list)
    }

    inner class PaymentListViewHolder(val binding: ViewDataBinding?, var viewType: Int) :
        ViewHolder(binding!!.root) {

    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentListViewHolder {
        val inflater = LayoutInflater.from(parent.getContext())
        var binding: ViewDataBinding
        when (viewType) {
            0 -> {
                binding = DataBindingUtil.inflate(inflater, R.layout.item_bank_offer, parent, false)
                return PaymentListViewHolder(binding as ItemBankOfferBinding, viewType)
            }

            1 -> {
                binding = DataBindingUtil.inflate(inflater, R.layout.item_gift_card, parent, false)
                return PaymentListViewHolder(binding as ItemGiftCardBinding, viewType)
            }

            2 -> {
                binding = DataBindingUtil.inflate(inflater, R.layout.item_wallet_ui, parent, false)
                return PaymentListViewHolder(binding as ItemWalletUiBinding, viewType)
            }

            3 -> {
                binding = DataBindingUtil.inflate(inflater, R.layout.item_gateway_ui, parent, false)
                return PaymentListViewHolder(binding as ItemGatewayUiBinding, viewType)

            }

            else -> {
                binding = DataBindingUtil.inflate(inflater, R.layout.item_bank_offer, parent, false)
                return PaymentListViewHolder(binding as ItemBankOfferBinding, viewType)
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return 0
        } else if (position == 1) {
            return 1
        } else if (position == 2) {
            return 2
        } else if (position == 3) {
            return 3
        } else {
            return 0
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(
        holder: PaymentListViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        pos = -1
        if (holder.viewType == 0) {
            var binding = holder.binding as ItemBankOfferBinding
            binding.textviewApplyBankOffer.setOnClickListener {
                cardNo =
                    binding.etEnterBankOfferCardNumber.text.toString()
                        .replace(" ", "")
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

                }
                else if (cardNo.length != 16) {
                    val dialog = OptionDialog(context,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        "Enter valid card number.",
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                else {
                    listner.bankItemApply(
                        offerId,
                        cardNo
                    )

                }
            }
            binding?.textviewCancelBankOffer.setOnClickListener {
                pos=position
                val offerCode = binding.etEnterBankOfferCardNumber.text.toString()
                listner.bankItemRemove(
                    offerId,
                    offerCode
                )
            }
            with(payMode[position]) {
                //title
                initSpinnerAdapter(respPayModes)
                binding!!.headerOfferType.text = this.name
                binding?.apply {
                    headerUi.setOnClickListener {
                        pos = position
                        if (bankClicked) {
                            bankClicked = false
                        } else {
                            bankClicked = true
                        }
                        notifyItemChanged(pos)
                    }
                }

                if (!PaymentListActivity.spinnerClickable) {
                    binding.spinnerCardOptions?.apply {
                        setEnabled(false);
                        setClickable(false);
                    }
                } else {
                    binding.spinnerCardOptions?.apply {
                        setEnabled(true);
                        setClickable(true);
                    }
                }
                binding.spinnerCardOptions.setBackgroundColor(
                    context.resources.getColor(
                        R.color.dropDownColor
                    )
                )
                if(binding?.spinnerCardOptions.adapter == null || !(binding?.spinnerCardOptions.adapter is BankOfferAdapter)){
                    binding.spinnerCardOptions.adapter = customAdapter
                }
                binding.spinnerCardOptions.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            selectedCardType = position
                            println("customAdapterPosition------>${position}")
                            offerId = list[position].id.toString()
                            if (position == 0) {

                                binding?.apply {
                                    clEnterCardNumber.hide()
                                    textviewApplyBankOffer.hide()
                                    textviewCancelBankOffer.hide()
                                }
                            } else {
                                binding?.apply {
                                    etEnterBankOfferCardNumber.isEnabled = true
                                    clEnterCardNumber.show()
                                    textviewApplyBankOffer.show()
                                    textviewCancelBankOffer.hide()
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
                binding?.apply {
                    if (bankApplied) {
                        textviewCancelBankOffer.show()
                        textviewApplyBankOffer.hide()
                        etEnterBankOfferCardNumber.isEnabled = false
                        tvOfferAppliedForNTickets.show()
                    } else {
                        textviewCancelBankOffer.hide()
                        textviewApplyBankOffer.show()
                        etEnterBankOfferCardNumber.isEnabled = true
                    }
                }
                if (bankClicked) {
                    binding?.apply {
                        bankOfferUi.show()
                        headerOfferType.setTextColor(context.getColor(R.color.white))
                        ivDropdown.setImageResource(R.drawable.arrow_up)
                    }
                } else {
                    binding?.apply {
                        ivDropdown.setImageResource(R.drawable.arrow_down)
                        bankOfferUi.hide()
                    }
                }

            }
        } else if (holder.viewType == 1) {
            with(payMode[position]) {
                var binding = holder.binding as ItemGiftCardBinding
                binding!!.headerOfferType.text = this.name

                binding?.apply {
                    headerUi.setOnClickListener {
                        pos = position
                        if (giftCardClicked) {
                            giftCardClicked = false
                        } else {
                            giftCardClicked = true
                        }
                        notifyItemChanged(pos)
                    }
                    giftCardUi.show()
                }
                if (giftCardClicked) {
                    clickName = this.respPayModes[0].name
                    binding?.apply {
                        ivDropdown.setImageResource(R.drawable.arrow_up)
                        giftCardUi.show()
                        editTextGiftCard.hint =
                            context.resources.getString(R.string.enter_gift_card)
                    }
                    if (viewModel.selectedPaymentMethod == PaymentMethodSealedClass.GIFT_CARD_COMPLETE || viewModel.selectedPaymentMethod == PaymentMethodSealedClass.GIFT_CARD_PARTIAL) {
                        binding?.apply {
                            editTextGiftCard.isEnabled = false
                            tvApplyGiftCard.hide()
                            textviewCancelGiftCard.show()
                            editTextGiftCard.setText(giftCardNumber)
                        }
                    } else {
                        binding?.apply {
                            editTextGiftCard.isEnabled = true
                            tvApplyGiftCard.show()
                            textviewCancelGiftCard.hide()
                            editTextGiftCard.setText(giftCardNumber)
                        }
                    }


//                        mBinding.giftCard.setOnClickListener {
//                            binding?.apply {
//                                bankOfferChooseCards.hide()
//                                walletUi.hide()
//                                giftCardUi.show()
//                            }
//                            notifyDataSetChanged()
//                        }

                    val adapter = GiftCardAdapter(
                        context, this.respPayModes, this@PaymentListAdapter
                    )
                    binding.recyclerOffer.layoutManager = LinearLayoutManager(
                        context, LinearLayoutManager.HORIZONTAL, false
                    )
                    binding.recyclerOffer.adapter = adapter
                    binding.tvApplyGiftCard.setOnClickListener {
                        val offerCode = binding.editTextGiftCard.text.toString()
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
                                clickId
                            )
                        }
                    }
                    // Cancel btn
//                        mBinding.textCancelBtn.setOnClickListener {
//                            mBinding.textCancelBtn.hide()
//                            mBinding.tvApplyCardOffer.show()
//                        }

                    //remove voucher
                    binding.textviewCancelGiftCard.setOnClickListener {
                        val offerCode = binding.editTextGiftCard.text.toString()
                        listner.onGiftCardItemRemove(
                            this,
                            offerCode,
                            clickName,
                            clickId,
                        )

                    }
                } else {
                    binding.ivDropdown.setImageResource(R.drawable.arrow_down)
                    binding.giftCardUi.hide()
                }
            }
        } else if (holder.viewType == 2) {
            with(payMode[position]) {
                var binding = holder.binding as ItemWalletUiBinding
                binding!!.headerOfferType.text = this.name
                binding?.apply {
                    headerUi.setOnClickListener {
                        pos = position
                        if (walletClicked) {
                            walletClicked = false
                        } else {
                            walletClicked = true
                        }
                        notifyItemChanged(pos)
                    }
                    walletUi.show()
                }
                if (walletClicked) {
                    binding?.apply {
                        walletUi.show()
                        textViewWalletBalance.text =
                            context.getString(R.string.wallet_balance) + respPayModes[0].balance
                        ivDropdown.setImageResource(R.drawable.arrow_up)
                        if (walletApplied) {
                            textviewBtWalletApply.hide()
                            textviewBtWalletCancel.show()
                        } else {
                            textviewBtWalletApply.show()
                            textviewBtWalletCancel.hide()
                        }
                    }
                } else {
                    binding?.apply {
                        ivDropdown.setImageResource(R.drawable.arrow_down)
                        walletUi.hide()
                    }
                }
                binding.textviewBtWalletApply.setOnClickListener {
                    pos = position
                    Constant.CARD_NO = ""
                    walletApplied = true
                    knetClicked = false
                    viewModel.setPaymentMethodSelection(PaymentMethodSealedClass.NONE)
                    creditCardClicked = false
                    notifyItemChanged(pos)
                }

//                wallet cancel
                binding.textviewBtWalletCancel.setOnClickListener {
                    pos = position
                    viewModel.setPaymentMethodSelection(PaymentMethodSealedClass.NONE)
                    Constant.CARD_NO = ""
//                    context.toast("cancel Wallet")
                    walletApplied = false
                    notifyItemChanged(pos)
                }
            }
        } else if (holder.viewType == 3) {
            with(payMode[position]) {
                var binding = holder.binding as ItemGatewayUiBinding
                binding!!.headerOfferType.text = this.name
                binding?.apply {
                    ivDropdown.hide()
                    knetCcUi.show()
                }
                if (creditCardClicked) {
                    holder.binding.apply {
                        imageCreditCard.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.red
                            )
                        )
                        textCreditCardName.setTextColor(context.getColor(R.color.red))
                    }
                } else {
                    holder.binding.apply {
                        imageCreditCard.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        )
                        textCreditCardName.setTextColor(context.getColor(R.color.white))
                    }
                }
                if (knetClicked) {
                    binding.apply {
                        textKnetName.setTextColor(context.getColor(R.color.red))
                        imageKnet.setColorFilter(ContextCompat.getColor(context, R.color.red))
                    }
                } else {
                    binding.apply {
                        imageKnet.setColorFilter(ContextCompat.getColor(context, R.color.white))
                        textKnetName.setTextColor(context.getColor(R.color.white))
                    }
                }

                Glide.with(context)
                    .load(this.respPayModes[1].imageUrl)
                    .into(binding.imageCreditCard)

                Glide.with(context)
                    .load(this.respPayModes[0].imageUrl)
                    .into(binding.imageKnet)

                if (viewModel.selectedPaymentMethod == PaymentMethodSealedClass.GIFT_CARD_COMPLETE || viewModel.selectedPaymentMethod == PaymentMethodSealedClass.GIFT_CARD_PARTIAL) {
                    binding.apply {
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
                }
                binding?.apply {
                    imageKnet.setOnClickListener {
                        pos = position
                        if (knetClicked) {
                            knetClicked = false
                        } else {
                            giftCardApplied = false
                            creditCardClicked = false
                            knetClicked = true
                        }
                        notifyItemChanged(pos)
                    }
                }
                binding?.apply {
                    creditCard.setOnClickListener {
                        pos = position
                        if (creditCardClicked) {
                            creditCardClicked = false
                        } else {
                            giftCardApplied = false
                            knetClicked = false
                            creditCardClicked = true
                        }
                        notifyItemChanged(pos)
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
        fun bankItemApply(
            view: String,
            cardNo: String
        )

        fun bankItemRemove(
            view: String,
            cardNo: String
        )

        fun onSimilarItemClick(view: GetMovieResponse.Output.Similar)

        fun onVoucherApply(
            view: PaymentListResponse.Output.PayMode,
            offerCode: String,
            clickName: String,
            clickId: String
        )

        fun onGiftCardItemRemove(
            item: PaymentListResponse.Output.PayMode,
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

    fun Activity.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}