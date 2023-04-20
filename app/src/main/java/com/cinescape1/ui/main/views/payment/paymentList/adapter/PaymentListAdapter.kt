package com.cinescape1.ui.main.views.payment.paymentList.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cinescape1.R
import com.cinescape1.databinding.ItemBankOfferBinding
import com.cinescape1.databinding.ItemGatewayUiBinding
import com.cinescape1.databinding.ItemGiftCardBinding
import com.cinescape1.databinding.ItemWalletUiBinding
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.bankApplied
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.bankClicked
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.bankEnabled
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.cardNo
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.clickId
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.clickName
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.creditCardEnabled
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.creditCardSelected
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.giftCardApplied
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.giftCardAppliedFull
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.giftCardClicked
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.giftCardEnabled
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.knetEnabled
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.knetSelected
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.offerCode
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.offerId
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.selectedCardType
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.walletApplied
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.walletAppliedFull
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.walletClicked
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.walletEnabled
import com.cinescape1.ui.main.views.payment.paymentList.RecycleViewItemClickListener
import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse
import com.cinescape1.ui.main.views.summery.viewModel.SummeryViewModel
import com.cinescape1.utils.CustomSpinner
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import kotlinx.android.synthetic.main.account_preference_layout.*
import kotlinx.android.synthetic.main.activity_checkout_with_food.*
import java.util.*


class PaymentListAdapter(
    private val context: Activity,
    private val payMode: ArrayList<PaymentListResponse.Output.PayMode>,
    private val listner: RecycleViewItemClickListener, private val viewModel: SummeryViewModel
) : RecyclerView.Adapter<PaymentListAdapter.PaymentListViewHolder>(),
    GiftCardAdapter.RecycleViewItemClickListener, CustomSpinner.OnSpinnerEventsListener {

    private var binding: ViewDataBinding? = null
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
                context.resources.getString(R.string.available_bank_offers)
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
            with(payMode[position]) {
                binding?.apply {
                    headerUi.show()
                    headerOfferType.text = name
                    headerUi.setOnClickListener {
                        pos = position
                        if (bankClicked) {
                            bankClicked = false
                        } else {
                            bankClicked = true
                        }
                        notifyItemChanged(pos)
                    }
                    textviewCancelBankOffer.setOnClickListener {
                        pos = position
                        val offerCode = binding.etEnterBankOfferCardNumber.text.toString()
                        listner.bankItemRemove(
                            offerId,
                            offerCode
                        )
                    }
                    textviewApplyBankOffer.setOnClickListener {
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
                                cardNo
                            )

                        }
                    }
                    etEnterBankOfferCardNumber.addTextChangedListener(object : TextWatcher {
                        val space = ' '
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                        }

                        override fun afterTextChanged(s: Editable?) {
                            // Remove spacing char
                            if (s!!.length > 0 && s!!.length % 5 == 0) {
                                val c = s[s.length - 1]
                                if (space == c) {
                                    s.delete(s.length - 1, s.length)
                                }
                            }
                            // Insert char where needed.
                            if (s.length > 0 && s!!.length % 5 == 0) {
                                val c = s[s.length - 1]
                                // Only if its a digit where there should be a space we insert a space
                                if (Character.isDigit(c) && TextUtils.split(
                                        s.toString(),
                                        space.toString()
                                    ).size <= 3
                                ) {
                                    s.insert(s.length - 1, space.toString())
                                }
                            }
                            if (s.toString().length < 19) {
                                binding?.apply {
                                    textviewApplyBankOffer.hide()
                                }
                            } else {
                                binding?.apply {
                                    textviewApplyBankOffer.show()
                                }
                            }
                        }
                    })
                    if (bankEnabled) {
                        headerUi.isClickable = true
                        headerUi.isEnabled = true
                        headerUi.isFocusable = true
                        headerOfferType.setTextColor(context.resources.getColor(R.color.white))
                        ivDropdown.setColorFilter(context.resources.getColor(R.color.white))
                    } else {
                        bankClicked = false
                        bankApplied = false
                        headerOfferType.setTextColor(context.resources.getColor(R.color.gray))
                        ivDropdown.setColorFilter(context.resources.getColor(R.color.gray))
                        headerUi.isClickable = false
                        headerUi.isEnabled = false
                        headerUi.isFocusable = false

                    }
                }
                //title
                initSpinnerAdapter(respPayModes)

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
                binding?.apply {
                    headerOfferType.text = name
                    spinnerCardOptions?.apply {
                        prompt = context.resources.getString(R.string.available_bank_offers)
                        setBackgroundColor(
                            context.resources.getColor(
                                R.color.white
                            )
                        )
                        if (adapter == null || !(adapter is BankOfferAdapter)) {
                            adapter = customAdapter
                        }
                        setListenerCallback(this@PaymentListAdapter)
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
                                            textviewApplyBankOffer.hide()
                                            textviewCancelBankOffer.hide()
                                        }
                                    } else {
                                        binding?.apply {
                                            etEnterBankOfferCardNumber.isEnabled = true
                                            clEnterCardNumber.show()
//                                    textviewApplyBankOffer.show()
//                                    textviewCancelBankOffer.hide()
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

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    TODO("Not yet implemented")
                                }
                            }
                        if (bankApplied) {
                            spinnerCardOptions?.apply {
                                isClickable = false
                                isEnabled = false
                                isFocusable = false
                            }
                            textviewCancelBankOffer.show()
                            textviewApplyBankOffer.hide()
                            etEnterBankOfferCardNumber.isEnabled = false
                            tvOfferAppliedForNTickets.show()
                        } else {
                            spinnerCardOptions?.apply {
                                isClickable = true
                                isEnabled = true
                                isFocusable = true
                            }
                            textviewCancelBankOffer.hide()
                            textviewApplyBankOffer.show()
                            etEnterBankOfferCardNumber.isEnabled = true
                            tvOfferAppliedForNTickets.hide()
                        }

                        if (bankClicked) {
                            ivDropdown.setImageResource(R.drawable.arrow_up)
                            bankOfferUi.show()
                            if (!bankApplied) {
                                textviewCancelBankOffer.hide()
                                if (etEnterBankOfferCardNumber.isVisible && etEnterBankOfferCardNumber.text.toString().length >= 19) {
                                    textviewApplyBankOffer.show()
                                } else {
                                    textviewApplyBankOffer.hide()
                                }
                            } else {
                                textviewApplyBankOffer.hide()
                                textviewCancelBankOffer.show()

                            }
                        } else {
                            ivDropdown.setImageResource(R.drawable.arrow_down)
                            bankOfferUi.hide()
                        }
                    }

                }
            }
        }
            else if (holder.viewType == 1) {
            with(payMode[position]) {
                var binding = holder.binding as ItemGiftCardBinding
                binding?.apply {
                    headerOfferType.text = name
                    headerUi.show()
                    headerUi.setOnClickListener {
                        pos = position
                        if (giftCardClicked) {
                            giftCardClicked = false
                        } else {
                            giftCardClicked = true
                        }
                        notifyItemChanged(pos)
                    }
                }
                binding.tvApplyGiftCard.setOnClickListener {
                    clickName = "Gift Card"
                    offerCode = binding.etEnterGiftCardNumber!!.text.toString()
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
                            offerCode!!,
                            clickName,
                            clickId
                        )
                    }
                }
                binding.textviewCancelGiftCard.setOnClickListener {
                    clickName = "Gift Card"
                    if (giftCardAppliedFull) {
                        giftCardApplied = false
                        giftCardAppliedFull = false
                        bankEnabled = true
                        walletEnabled = true
                        knetEnabled = true
                        creditCardEnabled = true
                        notifyDataSetChanged()
                    } else if (giftCardApplied) {
                        offerCode = binding.etEnterGiftCardNumber!!.text.toString()
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
                            listner.onGiftCardItemRemove(
                                this,
                                offerCode!!,
                                clickName,
                                clickId
                            )
                        }
                    }
                }
                if (giftCardAppliedFull) {
                    binding?.apply {
                        headerUi.isEnabled = true
                        headerUi.isFocusable = true
                        headerUi.isClickable = true
                        etEnterGiftCardNumber?.apply {
                            isClickable = false
                            isEnabled = false
                            isFocusable = false
                        }
                        textviewCancelGiftCard.show()
                        tvApplyGiftCard.hide()
                    }
                } else if (giftCardApplied) {
                    binding?.apply {
                        headerUi.isEnabled = true
                        headerUi.isFocusable = true
                        headerUi.isClickable = true
                        etEnterGiftCardNumber?.apply {
                            isEnabled = false
                            isFocusable = false
                            isClickable = false
                        }
                        textviewCancelGiftCard.show()
                        tvApplyGiftCard.hide()
                    }
                } else {
                    binding?.apply {
                        headerUi.isEnabled = true
                        headerUi.isFocusable = true
                        headerUi.isClickable = true
                        etEnterGiftCardNumber?.apply {
                            isEnabled = true
                            isFocusable = true
                            isClickable = true
                        }
                        textviewCancelGiftCard.hide()
                        tvApplyGiftCard.show()
                    }
                }
                if (giftCardEnabled) {
                    binding?.apply {
                        headerOfferType.setTextColor(context.resources.getColor(R.color.white))
                        ivDropdown.setColorFilter(context.resources.getColor(R.color.white))
                        headerUi.isClickable = true
                        headerUi.isEnabled = true
                        headerUi.isFocusable = true
                    }
                } else {
                    giftCardClicked = false
                    giftCardApplied = false
                    giftCardAppliedFull = false
                    binding?.apply {
                        headerOfferType.setTextColor(context.resources.getColor(R.color.gray))
                        ivDropdown.setColorFilter(context.resources.getColor(R.color.gray))
                        headerUi.isClickable = false
                        headerUi.isEnabled = false
                        headerUi.isFocusable = false
                    }
                }
                if (giftCardClicked) {
                    binding?.apply {
                        headerUi.show()
                        giftCardUi.show()
                        ivDropdown.setImageResource(R.drawable.arrow_up)
                    }
                } else {
                    binding?.apply {
                        headerUi.show()
                        giftCardUi.hide()
                        ivDropdown.setImageResource(R.drawable.arrow_down)

                    }
                }
            }
        } else if (holder.viewType == 2) {
            with(payMode[position]) {
                var binding = holder.binding as ItemWalletUiBinding
                binding?.apply {
                    headerOfferType.text = name
                    headerUi.show()
                    walletUi.show()
                    headerUi.setOnClickListener {
                        pos = position
                        if (walletClicked) {
                            walletClicked = false
                        } else {
                            walletClicked = true
                        }
                        notifyItemChanged(pos)
                    }
                    textviewBtWalletApply.setOnClickListener {
                        listner.newWalletApplyApply(payFull = false)
                    }
                    textviewBtWalletCancel.setOnClickListener {
                        if (walletAppliedFull) {
                            pos = position
                            bankEnabled = true
                            giftCardEnabled = true
                            knetSelected = false
                            creditCardSelected = false
                            creditCardEnabled = true
                            knetEnabled = true
                            walletApplied = false
                            walletAppliedFull = false
                            notifyDataSetChanged()
                        } else {
                            listner.onNewWalletRemove()
                        }
                    }
                }
                if (walletApplied) {
                    binding?.apply {
                        textviewBtWalletApply.hide()
                        textviewBtWalletCancel.show()
                    }

                } else {
                    binding?.apply {
                        textviewBtWalletApply.show()
                        textviewBtWalletCancel.hide()
                    }
                }
                if (walletEnabled) {
                    binding?.apply {
                        headerOfferType.setTextColor(context.resources.getColor(R.color.white))
                        ivDropdown.setColorFilter(context.resources.getColor(R.color.white))

                        headerUi.isClickable = true
                        headerUi.isEnabled = true
                        headerUi.isFocusable = true
                    }
                } else {
                    walletClicked = false
                    walletApplied = false
                    binding?.apply {
                        headerOfferType.setTextColor(context.resources.getColor(R.color.gray))
                        ivDropdown.setColorFilter(context.resources.getColor(R.color.gray))
                        headerUi.isClickable = false
                        headerUi.isEnabled = false
                        headerUi.isFocusable = false
                    }
                }
                if (walletClicked) {
                    binding?.apply {
                        walletUi.show()
                        textViewWalletBalance.text =
                            context.getString(R.string.wallet_balance) + respPayModes[0].balance
                        ivDropdown.setImageResource(R.drawable.arrow_up)
                    }
                } else {
                    binding?.apply {
                        ivDropdown.setImageResource(R.drawable.arrow_down)
                        walletUi.hide()
                    }
                }
            }
        } else if (holder.viewType == 3) {
            with(payMode[position]) {
                var binding = holder.binding as ItemGatewayUiBinding
                binding?.apply {
                    ivDropdown.hide()
                    headerOfferType.text = name
                    headerUi.show()
                    knetCcUi.show()

                    knet.setOnClickListener {
                        knetSelected = true
                        creditCardSelected = false
                        walletApplied = false
                        pos = position
                        notifyDataSetChanged()
                    }
                    creditCard.setOnClickListener {
                        knetSelected = false
                        creditCardSelected = true
                        walletApplied = false
                        pos = position
                        notifyDataSetChanged()
                    }
                }
                if (knetEnabled) {
                    binding?.apply {
                        knet.apply {
                            isClickable = true
                            isFocusable = true
                            isEnabled = true
                        }
                        imageKnet.setColorFilter(context.resources.getColor(R.color.white))
                    }
                } else {
                    binding?.apply {
                        knet.apply {
                            isClickable = false
                            isFocusable = false
                            isEnabled = false
                        }
                        imageKnet.setColorFilter(context.resources.getColor(R.color.gray))
                    }
                }
                if (creditCardEnabled) {

                    binding?.apply {
                        creditCard?.apply {
                            isClickable = true
                            isFocusable = true
                            isEnabled = true
                        }
                        imageCreditCard.setColorFilter(context.resources.getColor(R.color.white))
                    }
                } else {
                    binding?.apply {
                        creditCard.apply {
                            isClickable = false
                            isFocusable = false
                            isEnabled = false
                        }
                        imageCreditCard.setColorFilter(context.resources.getColor(R.color.gray))
                    }
                }
                if (!(knetEnabled && creditCardEnabled)) {
                    binding?.apply {
                        headerOfferType.setTextColor(context.resources.getColor(R.color.gray))
                    }
                } else {
                    binding?.apply {
                        headerOfferType.setTextColor(context.resources.getColor(R.color.white))
                    }
                }
                Glide.with(context)
                    .load(this.respPayModes[1].imageUrl)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            if (creditCardSelected) {
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
                            //do something if error loading
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d(TAG, "OnResourceReady")
                            if (creditCardSelected) {
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
                                if (!creditCardEnabled) {
                                    binding.apply {
                                        imageCreditCard.setColorFilter(
                                            ContextCompat.getColor(
                                                context,
                                                R.color.gray
                                            )
                                        )
                                        textCreditCardName.setTextColor(context.getColor(R.color.gray))
                                    }
                                } else {
                                    binding.apply {
                                        imageCreditCard.setColorFilter(
                                            ContextCompat.getColor(
                                                context,
                                                R.color.white
                                            )
                                        )
                                        textCreditCardName.setTextColor(context.getColor(R.color.white))
                                    }
                                }
                            }
                            return false
                        }
                    })
                    .into(binding.imageCreditCard)

                Glide.with(context)
                    .load(this.respPayModes[0].imageUrl)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            if (knetSelected) {
                                binding.apply {
                                    textKnetName.setTextColor(context.getColor(R.color.red))
                                    imageKnet.setColorFilter(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.red
                                        )
                                    )
                                }
                            } else {
                                if (!knetEnabled) {
                                    binding.apply {
                                        imageKnet.setColorFilter(
                                            ContextCompat.getColor(
                                                context,
                                                R.color.gray
                                            )
                                        )
                                        textKnetName.setTextColor(context.getColor(R.color.gray))
                                    }
                                } else {
                                    binding.apply {
                                        imageKnet.setColorFilter(
                                            ContextCompat.getColor(
                                                context,
                                                R.color.white
                                            )
                                        )
                                        textKnetName.setTextColor(context.getColor(R.color.white))
                                    }
                                }
                            }
                            //do something if error loading
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d(TAG, "OnResourceReady")
                            if (knetSelected) {
                                binding.apply {
                                    textKnetName.setTextColor(context.getColor(R.color.red))
                                    imageKnet.setColorFilter(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.red
                                        )
                                    )
                                }
                            } else {
                                if (!knetEnabled) {
                                    binding.apply {
                                        imageKnet.setColorFilter(
                                            ContextCompat.getColor(
                                                context,
                                                R.color.gray
                                            )
                                        )
                                        textKnetName.setTextColor(context.getColor(R.color.gray))
                                    }
                                } else {
                                    binding.apply {
                                        imageKnet.setColorFilter(
                                            ContextCompat.getColor(
                                                context,
                                                R.color.white
                                            )
                                        )
                                        textKnetName.setTextColor(context.getColor(R.color.white))
                                    }
                                }

                            }
                            return false
                        }
                    })
                    .into(binding.imageKnet)
            }

        }
    }

    override fun getItemCount(): Int {
        return payMode.size
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

    override fun onSpinnerOpened(spinner: AppCompatSpinner?) {
        Glide.with(context).load(context.resources.getDrawable(R.drawable.arrow_up)).into((binding as ItemBankOfferBinding).ivDropdownSpinner)
    }
    override fun onSpinnerClosed(spinner: AppCompatSpinner?) {
        Glide.with(context).load(context.resources.getDrawable(R.drawable.arrow_down)).into((binding as ItemBankOfferBinding).ivDropdownSpinner)
    }

}