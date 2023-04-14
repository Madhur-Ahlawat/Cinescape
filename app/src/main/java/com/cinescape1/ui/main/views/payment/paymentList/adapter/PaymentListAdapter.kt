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
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.cardNo
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.clickId
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.clickName
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.creditCardSelected
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.giftCardApplied
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.giftCardClicked
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.knetSelected
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.offerCode
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.offerId
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.selectedCardType
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.walletApplied
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity.Companion.walletClicked
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
            binding?.etEnterBankOfferCardNumber.addTextChangedListener(object:TextWatcher{
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
                    if(s.toString().length<19){
                        binding?.apply {
                            textviewApplyBankOffer.hide()
                        }
                    }
                    else{
                        binding?.apply {
                            textviewApplyBankOffer.show()
                        }
                    }
                }
            })

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
            binding?.textviewCancelBankOffer.setOnClickListener {
                pos = position
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
                if (bankApplied) {
                    giftCardApplied = false
                    giftCardClicked = false
                    binding?.apply {
                        headerUi.isClickable = false
                        headerUi.isEnabled = false
                        headerUi.isFocusable = false

                        giftCardUi.isClickable = false
                        giftCardUi.isEnabled = false
                        giftCardUi.isFocusable = false
                    }
                } else {
                    binding?.apply {
                        headerUi.isClickable = true
                        headerUi.isEnabled = true
                        headerUi.isFocusable = true

                        giftCardUi.isClickable = true
                        giftCardUi.isEnabled = true
                        giftCardUi.isFocusable = true
                    }
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
                    }
                    if (giftCardClicked) {
                        clickName = this.respPayModes[0].name
                        binding?.apply {
                            ivDropdown.setImageResource(R.drawable.arrow_up)
                            giftCardUi.show()
                        }

                        val adapter = GiftCardAdapter(
                            context, this.respPayModes, this@PaymentListAdapter
                        )
                        binding.recyclerOffer.layoutManager = LinearLayoutManager(
                            context, LinearLayoutManager.HORIZONTAL, false
                        )
                        binding.recyclerOffer.adapter = adapter
                        binding.tvApplyGiftCard.setOnClickListener {
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
                    if (giftCardApplied) {
                        binding?.apply {
                            editTextGiftCard.isEnabled = false
                            tvApplyGiftCard.hide()
                            textviewCancelGiftCard.show()
                        }
                    } else {
                        binding?.apply {
                            editTextGiftCard.isEnabled = true
                            tvApplyGiftCard.show()
                            textviewCancelGiftCard.hide()
                        }
                    }
                }
            }
        } else if (holder.viewType == 2) {
            with(payMode[position]) {
                var binding = holder.binding as ItemWalletUiBinding
                if (bankApplied) {
                    walletApplied = false
                    walletClicked = false
                    binding?.apply {
                        headerUi.isClickable = false
                        headerUi.isEnabled = false
                        headerUi.isFocusable = false

                        walletUi.isClickable = false
                        walletUi.isEnabled = false
                        walletUi.isFocusable = false
                    }
                } else {
                    binding?.apply {
                        headerUi.isClickable = true
                        headerUi.isEnabled = true
                        headerUi.isFocusable = true

                        walletUi.isClickable = true
                        walletUi.isEnabled = true
                        walletUi.isFocusable = true
                    }
                }

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
                    knetSelected=false
                    creditCardSelected=false
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
                binding.textviewBtWalletApply.setOnClickListener {
                    pos = position
                    walletApplied = true
                    viewModel.setPaymentMethodSelection(PaymentMethodSealedClass.NONE)
                    notifyItemChanged(pos)
                }

//                wallet cancel
                binding.textviewBtWalletCancel.setOnClickListener {
                    pos = position
                    viewModel.setPaymentMethodSelection(PaymentMethodSealedClass.NONE)
                    walletApplied = false
                    notifyItemChanged(pos)
                }
            }
        }
        else if (holder.viewType == 3) {
            with(payMode[position]) {
                var binding = holder.binding as ItemGatewayUiBinding
                binding!!.headerOfferType.text = this.name
                if (bankApplied) {
                    creditCardSelected = true
                    knetSelected = false
                    binding.apply {
                        knet.isClickable = false
                        knet.isFocusable = false
                        knet.isEnabled = false
                        creditCard.isClickable = false
                        creditCard.isFocusable = false
                        creditCard.isEnabled = false
                    }
                } else {
                    binding?.apply {
                        knet.isClickable = true
                        knet.isFocusable = true
                        knet.isEnabled = true
                        creditCard.isClickable = true
                        creditCard.isFocusable = true
                        creditCard.isEnabled = true
                    }
                }
                binding?.apply {
                    ivDropdown.hide()
                    knetCcUi.show()
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
                            Log.e(TAG, "onLoadFailed")
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
                            Log.e(TAG, "onLoadFailed")
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
                binding?.apply {
                    knet.setOnClickListener {
                        creditCardSelected = false
                        knetSelected = true
                        walletApplied=false
                        pos = position
                        notifyItemChanged(pos)
                    }
                }
                binding?.apply {
                    creditCard.setOnClickListener {
                        knetSelected = false
                        creditCardSelected = true
                        walletApplied=false
                        pos = position
                        notifyItemChanged(pos)
                    }
                }
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