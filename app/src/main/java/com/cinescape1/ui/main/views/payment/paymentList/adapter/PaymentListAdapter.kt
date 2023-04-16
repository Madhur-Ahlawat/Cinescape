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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.cinescape1.ui.main.views.payment.PaymentMethodSealedClass
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
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.walletClicked
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.walletEnabled
import com.cinescape1.ui.main.views.payment.paymentList.RecycleViewItemClickListener
import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse
import com.cinescape1.ui.main.views.summery.viewModel.SummeryViewModel
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
            binding?.etEnterBankOfferCardNumber.addTextChangedListener(object : TextWatcher {
                val space = ' '
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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
                binding?.apply {
                    headerUi.isClickable = true
                    headerUi.isEnabled = true
                    headerUi.isFocusable = true
                }
            } else {
                bankClicked = false
                bankApplied = false
                binding?.apply {
                    headerUi.isClickable = false
                    headerUi.isEnabled = false
                    headerUi.isFocusable = false
                }
            }
            with(payMode[position]) {
                binding?.apply {
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
                    headerOfferType.text = name
                    headerUi.show()
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
                binding.spinnerCardOptions.setBackgroundColor(
                    context.resources.getColor(
                        R.color.dropDownColor
                    )
                )
                if (binding?.spinnerCardOptions.adapter == null || !(binding?.spinnerCardOptions.adapter is BankOfferAdapter)) {
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
                        tvOfferAppliedForNTickets.hide()
                    }
                }
                if (bankClicked) {
                    binding?.apply {
                        bankOfferUi.show()
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
                    clickName="Gift Card"
                    offerCode = binding.editTextGiftCard.text.toString()
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
                if (giftCardAppliedFull) {
                    binding?.apply {
                        headerUi.isEnabled = false
                        headerUi.isFocusable = false
                        headerUi.isClickable = false
                        editTextGiftCard?.apply {
                            isClickable = false
                            isEnabled = false
                            isFocusable = false
                        }
                        textviewCancelGiftCard.show()
                        tvApplyGiftCard.hide()
                    }
                }
                if(giftCardApplied){
                    binding?.apply {
                        headerUi.isEnabled = false
                        headerUi.isFocusable = false
                        headerUi.isClickable = false
                        editTextGiftCard?.apply {
                            isEnabled = false
                            isFocusable = false
                            isClickable = false
                        }
                        textviewCancelGiftCard.show()
                        tvApplyGiftCard.hide()
                    }
                }
                if(!giftCardApplied && !giftCardAppliedFull){
                    binding?.apply {
                        headerUi.isEnabled = true
                        headerUi.isFocusable = true
                        headerUi.isClickable = true
                        editTextGiftCard?.apply {
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
                        headerUi.isClickable = true
                        headerUi.isEnabled = true
                        headerUi.isFocusable = true
                    }
                } else {
                    giftCardClicked = false
                    giftCardApplied = false
                    giftCardAppliedFull = false
                    binding?.apply {
                        headerUi.isClickable = false
                        headerUi.isEnabled = false
                        headerUi.isFocusable = false
                    }
                }
                if (giftCardClicked) {
                    binding?.apply {
                        headerUi.show()
                        giftCardUi.show()
                    }
                } else {
                    binding?.apply {
                        headerUi.show()
                        giftCardUi.hide()
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
                        notifyDataSetChanged()
                    }
                    textviewBtWalletApply.setOnClickListener {
                        pos = position
                        walletApplied = true
                        knetSelected = false
                        creditCardSelected = false
                        giftCardApplied = false
                        notifyDataSetChanged()
                    }
                    textviewBtWalletCancel.setOnClickListener {
                        pos = position
                        walletApplied = false
                        notifyDataSetChanged()                    }
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
                        headerUi.isClickable = true
                        headerUi.isEnabled = true
                        headerUi.isFocusable = true
                    }
                }
                else {
                    walletClicked = false
                    walletApplied = false
                    binding?.apply {
                        headerUi.isClickable = false
                        headerUi.isEnabled = false
                        headerUi.isFocusable = false
                    }
                }
                if (walletClicked) {
                    knetSelected = false
                    creditCardSelected = false
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
                    headerOfferType.text = name
                    headerUi.show()
                    headerOfferType.text = name
                    ivDropdown.hide()
                    knetCcUi.show()

                    knet.setOnClickListener {
                        knetSelected=true
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
                        notifyDataSetChanged()                    }
                }
                if (knetEnabled) {
                    binding?.apply {
                        knet.apply {
                            isClickable = true
                            isFocusable = true
                            isEnabled = true
                        }
                    }
                } else {
                    binding?.apply {
                        knet.apply {
                            isClickable = false
                            isFocusable = false
                            isEnabled = false
                        }
                    }
                }
                if (creditCardEnabled) {
                    binding?.apply {
                        creditCard?.apply {
                            isClickable = true
                            isFocusable = true
                            isEnabled = true
                        }
                    }
                } else {
                    binding?.apply {
                        creditCard.apply {
                            isClickable = false
                            isFocusable = false
                            isEnabled = false
                        }
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

}