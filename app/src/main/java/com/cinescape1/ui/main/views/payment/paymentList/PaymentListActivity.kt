package com.cinescape1.ui.main.views.payment.paymentList

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalEnvironment
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalRenderType
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalUiType
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalActionCode
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalConfigurationParameters
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalInitService
import com.cardinalcommerce.shared.userinterfaces.UiCustomization
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.*
import com.cinescape1.data.models.responseModel.GetMovieResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityPaymentListBinding
import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.finalTicket.FinalTicketActivity
import com.cinescape1.ui.main.views.payment.PaymentWebActivity
import com.cinescape1.ui.main.views.payment.paymentList.adapter.PaymentListAdapter
import com.cinescape1.ui.main.views.payment.paymentList.response.GiftCardRemove
import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse
import com.cinescape1.ui.main.views.summery.response.GiftCardResponse
import com.cinescape1.ui.main.views.summery.viewModel.SummeryViewModel
import com.cinescape1.utils.*
import com.cinescape1.utils.Constant.Companion.bankOfferClick
import com.threatmetrix.TrustDefender.*
import com.threatmetrix.TrustDefender.TMXProfilingConnections.TMXProfilingConnections
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.booking_alert2_item.*
import kotlinx.android.synthetic.main.cancel_dialog.*
import kotlinx.android.synthetic.main.cancel_dialog.view.*
import kotlinx.android.synthetic.main.checkout_creditcart_payment_alert.*
import kotlinx.android.synthetic.main.checkout_layout_ticket_include.*
import kotlinx.android.synthetic.main.item_payment_list.*
import org.json.JSONArray
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ActivityScoped
class PaymentListActivity : DaggerAppCompatActivity(),
    PaymentListAdapter.RecycleViewItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        var offerApplied: Boolean = false
        var giftApplied: Boolean = false
    }

    @Inject
    lateinit var preferences: AppPreferences
    private val summeryViewModel: SummeryViewModel by viewModels { viewModelFactory }


    private var binding: ActivityPaymentListBinding? = null
    private var loader: LoaderDialog? = null
    private var cardinal = Cardinal.getInstance()
    private var bookingId = ""
    private var transId = ""
    private var bookType = ""
    private var totalPrice = ""
    private var mSessionid = ""
    private var refId = ""
    private var VISA_PREFIX = "4"
    private var mContext: Activity = this
    private var MASTERCARD_PREFIX = "51,52,53,54,55,2222,22"
    private var DISCOVER_PREFIX = "6011,65"
    private var AMEX_PREFIX = "34,37,"
    private var JCB = "2131,1800"
    private var MAESTRO = "5033,5868,6759,5641"
    private var UATP = "1354"
    private var image = ""
    private var from = ""
    private var paidPrice = ""
    private var timeCount: Long = 0
    private var secondLeft: Long = 0
    private var dialogShow: Long = 60
    private var timeExtendClick: Boolean = false
    private var countDownTimerPrimary: CountDownTimer? = null
    private var adapter: PaymentListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityPaymentListBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        bookingId = intent.getStringExtra("bookingId").toString()
        bookType = intent.getStringExtra("BOOKING").toString()
        transId = intent.getStringExtra("TRANS_ID").toString()
        image = intent.getStringExtra("image").toString()
        paidPrice = intent.getStringExtra("paidPrice").toString()

        Glide.with(this).load(image).placeholder(R.drawable.bombshell).into(binding?.imageView6!!)

        println("book--->${bookingId}--type->${bookType}---transId--->${transId}>")

        if (bookType == "BOOKING") {
            textView111?.show()
            textView112?.show()
            resendTimer()
        } else {
            textView111?.invisible()
            textView112?.invisible()
        }
        ticketList(
            TicketSummaryRequest(
                transId, bookingId, preferences.getString(Constant.USER_ID).toString()
            )
        )

        binding?.viewCancel?.setOnClickListener {
            cancelDialog()
        }
    }

    private fun ticketList(request: TicketSummaryRequest) {
        summeryViewModel.paymentList(request).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                try {
                                    retrieveData(it.data.output)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        it.data.msg,
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {},
                                        negativeClick = {})
                                    dialog.show()
                                }

                            } else {
                                loader?.dismiss()
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    it.data?.msg.toString(),
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()
                            }

                        }
                    }
                    Status.ERROR -> {
                        loader?.dismiss()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.message.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                    Status.LOADING -> {
                        loader = LoaderDialog(R.string.pleasewait)
                        loader?.show(supportFragmentManager, null)
                    }
                }
            }
        }
    }

    private fun retrieveData(output: PaymentListResponse.Output) {
        binding?.paymentLayout?.show()
        binding?.textTimeToLeft?.text = output.amount
        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        adapter = PaymentListAdapter(this, output.payMode, this)
        binding?.recyclerPayMode?.layoutManager = gridLayout
        binding?.recyclerPayMode?.adapter = adapter
    }

    override fun walletItemApply(view: PaymentListResponse.Output.PayMode) {
        walletPay(
            HmacKnetRequest(
                bookingId, bookType, transId, preferences.getString(Constant.USER_ID).toString()
            )
        )
    }

    override fun bankItemApply(
        offerId: String,
        cardNo: String,
        check: ImageView,
        close: ImageView,
        apply: TextView,
        bankEdit: EditText,
        msg: TextView,
        knet: LinearLayout,
        walletApply: TextView,
        offerApply: TextView,
        offerEditText: EditText
    ) {
        bankOfferApply(
            BankOfferRequest(
                bookingId,
                cardNo,
                offerId,
                transId,
                preferences.getString(Constant.USER_ID).toString()
            ), check, close, apply, bankEdit, msg, knet, walletApply, offerApply, offerEditText
        )
    }

    private fun bankOfferApply(
        bankOfferRequest: BankOfferRequest,
        chekbox: ImageView,
        close: ImageView,
        apply: TextView,
        bankEdit: EditText,
        msg: TextView,
        knet: LinearLayout,
        walletApply: TextView,
        offerApply: TextView,
        offerEditText: EditText
    ) {
        summeryViewModel.bankApply(bankOfferRequest).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                binding?.textTimeToLeft?.text = it.data.output.amount
                                msg.show()
                                msg.text = it.data.output.MSG
                                apply.hide()
                                close.show()
                                chekbox.show()
                                offerApplied = true
                                adapter?.notifyDataSetChanged()
//                             bank  Clickable false
                                bankEdit.isClickable = false
                                bankEdit.isEnabled = false
                                bankEdit.isFocusable = false

//                          //knet
                                knet.isClickable = false
                                knet.isEnabled = false
                                knet.isFocusable = false

//                          //wallet
                                walletApply.isClickable = false
                                walletApply.isEnabled = false
                                walletApply.isFocusable = false

//                          //offer
                                offerApply.isClickable = false
                                offerApply.isEnabled = false
                                offerApply.isFocusable = false

//                          offer EditText
                                offerEditText.isClickable = false
                                offerEditText.isEnabled = false
                                offerEditText.isFocusable = false
                                bankEdit.isFocusableInTouchMode = false
                            }

                        }
                    }
                    Status.ERROR -> {
                        loader?.dismiss()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.message.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                    Status.LOADING -> {
                        loader = LoaderDialog(R.string.pleasewait)
                        loader?.show(supportFragmentManager, null)
                    }
                }
            }
        }
    }

    override fun bankItemRemove(
        offerId: String,
        cardNo: String,
        check: ImageView,
        close: ImageView,
        apply: TextView,
        bankEdit: EditText,
        msg: TextView,
        knet: LinearLayout,
        walletApply: TextView,
        offerApply: TextView,
        offerEditText: EditText
    ) {
        bankOfferRemove(
            BankOfferRequest(
                bookingId,
                cardNo,
                offerId,
                transId,
                preferences.getString(Constant.USER_ID).toString()
            ), check, close, apply, bankEdit, msg, knet, walletApply, offerApply, offerEditText
        )
    }

    private fun bankOfferRemove(
        bankOfferRequest: BankOfferRequest,
        checkbox: ImageView,
        close: ImageView,
        apply: TextView,
        bankEdit: EditText,
        msg: TextView,
        knet: LinearLayout,
        walletApply: TextView,
        offerApply: TextView,
        offerEditText: EditText
    ) {
        summeryViewModel.bankRemove(bankOfferRequest).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                msg.hide()
                                bankOfferClick = false
                                //bank
                                apply.show()
                                close.hide()
                                checkbox.hide()
                                offerApplied = false
                                adapter?.notifyDataSetChanged()
                                bankEdit.text.clear()
                                bankEdit.isClickable = true
                                bankEdit.isFocusable = true
                                bankEdit.isEnabled = true
                                bankEdit.isFocusableInTouchMode = true
                                bankEdit.inputType = InputType.TYPE_NULL


                                //knet
                                knet.isClickable = true
                                knet.isEnabled = true
                                knet.isFocusable = true

//                          //wallet
                                walletApply.isClickable = true
                                walletApply.isEnabled = true
                                walletApply.isFocusable = true

//                          //offer
                                offerApply.isClickable = true
                                offerApply.isEnabled = true
                                offerApply.isFocusable = true

                                //offer EditText
                                offerEditText.isClickable = true
                                offerEditText.isEnabled = true
                                offerEditText.isFocusable = true
                                offerEditText.isFocusableInTouchMode = true
                                offerEditText.inputType = InputType.TYPE_NULL
                            }
                        }
                    }
                    Status.ERROR -> {
                        loader?.dismiss()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.message.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                    Status.LOADING -> {
                        loader = LoaderDialog(R.string.pleasewait)
                        loader?.show(supportFragmentManager, null)
                    }
                }
            }
        }

    }


    private fun walletPay(request: HmacKnetRequest) {
        summeryViewModel.paymentWallet(request).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                try {
                                    Constant.IntentKey.TimerExtandCheck = true
                                    Constant.IntentKey.TimerExtand = 90
                                    Constant.IntentKey.TimerTime = 360
                                    val intent = Intent(
                                        applicationContext, FinalTicketActivity::class.java
                                    )
                                    intent.putExtra(
                                        Constant.IntentKey.TRANSACTION_ID, transId
                                    )
                                    intent.putExtra(Constant.IntentKey.BOOKING_ID, bookingId)
                                    startActivity(intent)
                                } catch (e: Exception) {
                                    println("updateUiCinemaSession ---> ${e.message}")
                                }

                            } else {
                                loader?.dismiss()
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    it.data?.msg.toString(),
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()
                            }

                        }
                    }
                    Status.ERROR -> {
                        loader?.dismiss()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.message.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                    Status.LOADING -> {
                        loader = LoaderDialog(R.string.pleasewait)
                        loader?.show(supportFragmentManager, null)
                    }
                }
            }
        }

    }

    override fun onSimilarItemClick(view: GetMovieResponse.Output.Similar) {

    }

    override fun onCreditCardItemClick(
        view: PaymentListResponse.Output.PayMode,
        cardNo: String,
        creditCardClick1: Boolean,
        knetClick1: Boolean
    ) {
        val knetClick = knetClick1
        val creditCardClick = creditCardClick1
        paymentOptionClick(view, knetClick, creditCardClick, cardNo)
    }

    private fun paymentOptionClick(
        view: PaymentListResponse.Output.PayMode,
        knetClick: Boolean,
        creditCardClick: Boolean,
        cardNo: String) {

        binding?.txtProceed?.setOnClickListener {
            if (!knetClick && !creditCardClick) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.select_payment_methods),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (creditCardClick) {
                creditCardDialog(cardNo)
            } else if (knetClick) {
                paymentHmac(
                    HmacKnetRequest(
                        bookingId,
                        bookType,
                        transId,
                        preferences.getString(Constant.USER_ID).toString()
                    )
                )
            }
        }

    }

    private fun creditCardDialog(cardNo: String) {

        val cardinalConfigurationParameters = CardinalConfigurationParameters()
        cardinalConfigurationParameters.environment = CardinalEnvironment.STAGING
        cardinalConfigurationParameters.requestTimeout = 8000
        cardinalConfigurationParameters.challengeTimeout = 5

        val rType = JSONArray()
        rType.put(CardinalRenderType.OTP)
        rType.put(CardinalRenderType.SINGLE_SELECT)
        rType.put(CardinalRenderType.MULTI_SELECT)
        rType.put(CardinalRenderType.OOB)
        rType.put(CardinalRenderType.HTML)
        cardinalConfigurationParameters.renderType = rType

        cardinalConfigurationParameters.uiType = CardinalUiType.BOTH

        val yourUICustomizationObject = UiCustomization()
        cardinalConfigurationParameters.uiCustomization = yourUICustomizationObject

        cardinal.configure(this, cardinalConfigurationParameters)
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.checkout_creditcart_payment_alert, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val proceedAlertDialog = mBuilder.show()
        proceedAlertDialog.show()
        proceedAlertDialog?.kd_to_pay?.text = " $totalPrice"
        proceedAlertDialog?.cardNumberTextInputEditText?.setText(cardNo)
        if (cardNo == "") {
            proceedAlertDialog?.cardNumberTextInputEditText?.isClickable = true
            proceedAlertDialog?.cardNumberTextInputEditText?.isEnabled = true
            proceedAlertDialog?.cardNumberTextInputEditText?.isFocusable = true
        } else {
            proceedAlertDialog?.cardNumberTextInputEditText?.isClickable = false
            proceedAlertDialog?.cardNumberTextInputEditText?.isEnabled = false
            proceedAlertDialog?.cardNumberTextInputEditText?.isFocusable = false

        }
        proceedAlertDialog.cardNumberTextInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence, i: Int, i1: Int, i2: Int
            ) {
                try {
                    if (!proceedAlertDialog.cardNumberTextInputEditText.text.toString().isEmpty()) {
                        proceedAlertDialog.image_american_express_card.visibility = View.VISIBLE
                        if (VISA_PREFIX.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 1)
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.visa_card
                                )
                            )

                        } else if (MASTERCARD_PREFIX.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 2) + ","
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.mastercard_card
                                )
                            )
                        } else if (AMEX_PREFIX.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 2) + ","
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.amerian_card
                                )
                            )
                        } else if (DISCOVER_PREFIX.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 4)
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.amerian_card
                                )
                            )
                        } else if (JCB.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 1)
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.amerian_card
                                )
                            )
                        } else if (MAESTRO.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 2)
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.amerian_card
                                )
                            )
                        } else {
                            proceedAlertDialog.image_american_express_card.visibility = View.GONE
                        }
                    } else {
                        proceedAlertDialog.image_american_express_card.visibility = View.GONE
                    }
                } catch (e: java.lang.Exception) {

                }
            }

            override fun onTextChanged(
                charSequence: CharSequence, i: Int, i1: Int, i2: Int
            ) {
                try {
                    if (!proceedAlertDialog.cardNumberTextInputEditText.text.toString().isEmpty()) {
                        proceedAlertDialog.image_american_express_card.visibility = View.VISIBLE
                        if (VISA_PREFIX.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 1)
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.visa_card
                                )
                            )
                        } else if (MASTERCARD_PREFIX.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 2) + ","
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.mastercard_card
                                )
                            )
                        } else if (AMEX_PREFIX.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 2) + ","
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.amerian_card
                                )
                            )
                        } else if (DISCOVER_PREFIX.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 4)
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.disover_card
                                )
                            )
                        } else if (JCB.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 4)
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.jcb_card
                                )
                            )
                        } else if (MAESTRO.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 4)
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.maestro_card
                                )
                            )
                        } else if (UATP.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 4)
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.uatp
                                )
                            )
                        } else {
                            proceedAlertDialog.image_american_express_card.visibility = View.GONE
                        }
                    } else {
                        proceedAlertDialog.image_american_express_card.visibility = View.GONE
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun afterTextChanged(editable: Editable) {
                try {
                    if (!proceedAlertDialog.cardNumberTextInputEditText.text.toString().isEmpty()) {
                        proceedAlertDialog.imageView52.visibility = View.VISIBLE
                        if (VISA_PREFIX.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 1)
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.imageView52.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.visa_card
                                )
                            )
                        } else if (MASTERCARD_PREFIX.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 2) + ","
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.imageView52.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.mastercard_card
                                )
                            )
                        } else if (AMEX_PREFIX.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 2) + ","
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.imageView52.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.amerian_card
                                )
                            )
                        } else if (DISCOVER_PREFIX.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 4)
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.imageView52.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.disover_card
                                )
                            )
                        } else if (JCB.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 4)
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.imageView52.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.jcb_card
                                )
                            )
                        } else if (MAESTRO.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 4)
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.imageView52.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.maestro_card
                                )
                            )
                        } else if (UATP.contains(
                                proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                    .substring(0, 4)
                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                                .isEmpty()
                        ) {
                            proceedAlertDialog.imageView52.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PaymentListActivity, R.drawable.uatp
                                )
                            )
                        } else {

                        }
                    } else {

                    }
                } catch (e: java.lang.Exception) {
                }
            }
        })

        proceedAlertDialog.text_cancel_go_back.setOnClickListener {
            proceedAlertDialog.dismiss()

        }
        var lastInput = ""

        proceedAlertDialog.expireDateTextInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(
                p0: CharSequence?, p1: Int, p2: Int, p3: Int
            ) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(
                p0: CharSequence?, start: Int, removed: Int, added: Int
            ) {
                val input = p0.toString()
                val formatter = SimpleDateFormat("MM/YY", Locale.GERMANY)
                val expiryDateDate = Calendar.getInstance()
                try {
                    expiryDateDate.time = formatter.parse(input)
                } catch (e: ParseException) {
                    if (p0?.length == 2 && !lastInput.endsWith("/")) {
                        val month = Integer.parseInt(input)
                        if (month <= 12) {
                            proceedAlertDialog.expireDateTextInputEditText.setText(
                                proceedAlertDialog.expireDateTextInputEditText.text.toString() + "/"
                            )
                            proceedAlertDialog.expireDateTextInputEditText.setSelection(
                                3
                            )
                        }
                    } else if (p0?.length == 2 && lastInput.endsWith("/")) {
                        val month = Integer.parseInt(input)
                        if (month <= 12) {
                            proceedAlertDialog.expireDateTextInputEditText.setText(
                                proceedAlertDialog.expireDateTextInputEditText.text.toString()
                                    .substring(0, 1)
                            )
                        }
                    }
                    lastInput = proceedAlertDialog.expireDateTextInputEditText.text.toString()
                    //because not valid so code exits here
                    return
                }
            }
        })

        proceedAlertDialog.btn_pay.setOnClickListener {
            if (validateFields(proceedAlertDialog)) try {
                postCardData(
                    PostCardRequest(
                        bookingId,
                        proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                            .replace(" ".toRegex(), "").trim(),
                        proceedAlertDialog.ccvTextInputEditText.text.toString(),
                        proceedAlertDialog.expireDateTextInputEditText.text.toString()
                            .split("/")[0],
                        proceedAlertDialog.expireDateTextInputEditText.text.toString()
                            .split("/")[1],
                        refId
                    )
                )
            } catch (e: Exception) {
                println("exception--->${e.message}")
            }


        }


        creditCardInit(
            HmacKnetRequest(
                bookingId, bookType, transId, preferences.getString(Constant.USER_ID).toString()
            )
        )
    }

    private fun postCardData(request: PostCardRequest) {
        summeryViewModel.postCardData(request).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                try {
                                    if (it.data.output.redirect == "0") {
                                        cardinal.cca_continue(
                                            it.data.output.authTransId, it.data.output.pares, this
                                        ) { context, validateResponse, s ->
                                            println("consumerSessionId12-->" + validateResponse.actionCode + "----" + validateResponse.errorDescription)
                                            if (validateResponse.actionCode == CardinalActionCode.CANCEL) {
                                                toast("Transaction Cancelled!")
                                            } else if (validateResponse.actionCode == CardinalActionCode.ERROR) {
                                                toast(validateResponse.errorDescription)
                                            } else if (validateResponse.actionCode == CardinalActionCode.SUCCESS) {
                                                if (s != null) {
                                                    runOnUiThread {
                                                        validateJWT(
                                                            ValidateJWTRequest(
                                                                bookingId,
                                                                request.cardNumber,
                                                                request.cvNumber,
                                                                request.expirationMonth,
                                                                request.expirationYear,
                                                                s,
                                                                mSessionid,
                                                                ""
                                                            )
                                                        )
                                                    }
                                                } else {
                                                    toast("Transaction Failed!")
                                                }
                                            } else {
                                                toast(validateResponse.errorDescription)
                                            }
                                        }
                                    } else {
                                        loader?.dismiss()
                                        val dialog = OptionDialog(this,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            it.data.output.errorDescription,
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {},
                                            negativeClick = {})
                                        dialog.show()
                                    }
                                } catch (e: Exception) {
                                    println("updateUiCinemaSession ---> ${e.message}")
                                }

                            } else {
                                loader?.dismiss()
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    it.data?.msg.toString(),
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()
                            }

                        }
                    }
                    Status.ERROR -> {
                        loader?.dismiss()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.message.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                    Status.LOADING -> {
                        loader = LoaderDialog(R.string.pleasewait)
                        loader?.show(supportFragmentManager, null)
                    }
                }
            }
        }
    }

    private fun validateJWT(s: ValidateJWTRequest) {
        summeryViewModel.validateJWT(s).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                try {
                                    if (from == "recharge") {
                                        Constant.IntentKey.OPEN_FROM = 1
                                        finish()
                                    } else {
                                        val intent = Intent(
                                            applicationContext, FinalTicketActivity::class.java
                                        )
                                        intent.putExtra(
                                            Constant.IntentKey.TRANSACTION_ID, transId
                                        )
                                        intent.putExtra(
                                            Constant.IntentKey.BOOKING_ID, bookingId
                                        )
                                        startActivity(intent)
                                    }
                                } catch (e: Exception) {
                                    println("updateUiCinemaSession ---> ${e.message}")
                                }

                            } else {
                                loader?.dismiss()
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    it.data?.msg.toString(),
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()
                            }

                        }
                    }
                    Status.ERROR -> {
                        loader?.dismiss()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.message.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                    Status.LOADING -> {
                        loader = LoaderDialog(R.string.pleasewait)
                        loader?.show(supportFragmentManager, null)
                    }
                }
            }
        }
    }

    private fun creditCardInit(request: HmacKnetRequest) {
        summeryViewModel.creditCardInit(request).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                try {
                                    cardinal = Cardinal.getInstance()
                                    val serverJwt: String = it.data.output.jwtToken

                                    val profilingConnections: TMXProfilingConnectionsInterface =
                                        TMXProfilingConnections().setConnectionTimeout(
                                            20, TimeUnit.SECONDS
                                        ).setRetryTimes(3)


                                    val config = TMXConfig() // (REQUIRED) Organisation ID
                                        .setOrgId(it.data.output.orgId)
                                        .setFPServer("h.online-metrix.net")
                                        .setContext(applicationContext)
                                        .setProfilingConnections(profilingConnections)
                                        .setProfileTimeout(20, TimeUnit.SECONDS)
                                        .setRegisterForLocationServices(true)
//
                                    TMXProfiling.getInstance().init(config)

                                    doProfile(
                                        it.data.output.deviceSessionId, it.data.output.merchantId
                                    )
                                    cardinal.init(serverJwt, object : CardinalInitService {
                                        /**
                                         * You may have your Submit button disabled on page load. Once you are set up
                                         * for CCA, you may then enable it. This will prevent users from submitting
                                         * their order before CCA is ready.
                                         */
                                        override fun onSetupCompleted(consumerSessionId: String) {
                                            refId = consumerSessionId
                                            println("consumerSessionId-->$consumerSessionId")
                                        }

                                        override fun onValidated(
                                            validateResponse: ValidateResponse?, serverJwt: String?
                                        ) {
                                            println("consumerSessionId-->" + validateResponse?.actionCode + "----" + serverJwt)
                                        }
                                    })

                                } catch (e: Exception) {
                                    println("updateUiCinemaSession ---> ${e.message}")
                                }

                            } else {
                                loader?.dismiss()
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    it.data?.msg.toString(),
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()
                            }

                        }
                    }
                    Status.ERROR -> {
                        loader?.dismiss()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.message.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                    Status.LOADING -> {
                        loader = LoaderDialog(R.string.pleasewait)
                        loader?.show(supportFragmentManager, null)
                    }
                }
            }
        }
    }

    private fun doProfile(sessions1: String, merchent: String) {
        val list: List<String> = ArrayList()
        val options =
            TMXProfilingOptions().setCustomAttributes(list) // Fire off the profiling request. We could use a more complex request,
        options.setSessionID(merchent + sessions1)
        val profilingHandle = TMXProfiling.getInstance().profile(
            options, CompletionNotifier()
        )
        // Session id can be collected here
        Log.d("TAG", "Session id = " + profilingHandle.sessionID)
        /*
         * profilingHandle can also be used to cancel this profile if needed *
         * profilingHandle.cancel();
         * */
        mSessionid = sessions1
    }

    private class CompletionNotifier : TMXEndNotifier {
        override fun complete(result: TMXProfilingHandle.Result) {
//            m_sessionID = result.getSessionID();
            println("SessionId-->${PaymentListActivity().mSessionid}")
            Log.d("ProfilingResults-", "SessionID:" + result.sessionID + "Status:" + result.status)
        }
    }

    private fun validateFields(proceedAlertDialog: AlertDialog): Boolean {
        return if (proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                .isEmpty() && proceedAlertDialog.cardNumberTextInputEditText.text.toString().length != 16 && !CreditCardUtils.isValid(
                proceedAlertDialog.cardNumberTextInputEditText.text.toString().replace(" ", "")
            )
        ) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.valid_card),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
            false
        } else if (proceedAlertDialog.expireDateTextInputEditText.text.toString()
                .isEmpty() || proceedAlertDialog.expireDateTextInputEditText.text.toString().length < 5
        ) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.valid_expiry),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
            false
        } else if (proceedAlertDialog.expireDateTextInputEditText.text.toString()
                .isEmpty() || proceedAlertDialog.expireDateTextInputEditText.text.toString()
                .split("/")
                .toTypedArray()[0].toInt() > 12 || proceedAlertDialog.expireDateTextInputEditText.text.toString().length < 5
        ) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.valid_expiry),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
            false
        } else if (proceedAlertDialog.ccvTextInputEditText.text.toString()
                .isEmpty() && proceedAlertDialog.ccvTextInputEditText.length() != 3
        ) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.valid_cvv),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
            false
        } else {
            true
        }
    }

    override fun onKnitItemClick(
        view: PaymentListResponse.Output.PayMode, creditCardClick1: Boolean, knetClick1: Boolean
    ) {

        val knetClick = knetClick1
        val creditCardClick = creditCardClick1
        paymentOptionClick(view, knetClick, creditCardClick, "")


    }

    override fun onVoucherApply(
        view: PaymentListResponse.Output.PayMode,
        offerCode: String,
        clickName: String,
        clickId: String,
        offerEditText: EditText,
        textView157: TextView,
        checkBox2: ImageView,
        imageView66: ImageView
    ) {
        if (clickName == "Gift Card") {
            giftCardApply(
                GiftCardRequest(
                    bookingId,
                    bookType,
                    offerCode,
                    transId,
                    preferences.getString(Constant.USER_ID).toString()
                ), offerEditText, textView157, checkBox2, imageView66
            )
        } else if (clickName == "Voucher") {
            voucherApply(
                GiftCardRequest(
                    bookingId,
                    bookType,
                    offerCode,
                    transId,
                    preferences.getString(Constant.USER_ID).toString()
                ), offerEditText, textView157, checkBox2, imageView66
            )
        }
    }

    override fun onGiftCardItemRemove(
        view: PaymentListResponse.Output.PayMode,
        offerCode: String,
        clickName: String,
        clickId: String,
        offerEditText: EditText,
        textView157: TextView,
        checkBox2: ImageView,
        imageView66: ImageView
    ) {
        if (clickName == "Gift Card") {
            giftCardRemove(
                GiftCardRequest(
                    bookingId,
                    bookType,
                    offerCode,
                    transId,
                    preferences.getString(Constant.USER_ID).toString()
                ), offerEditText, textView157, checkBox2, imageView66
            )
        } else if (clickName == "Voucher") {

        }
    }

    private fun voucherApply(
        request: GiftCardRequest,
        offerEditText: EditText,
        textView157: TextView,
        checkBox2: ImageView,
        imageView66: ImageView
    ) {
        summeryViewModel.voucherApply(request).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                try {
                                    Constant.IntentKey.TimerExtandCheck = true
                                    Constant.IntentKey.TimerExtand = 90
                                    Constant.IntentKey.TimerTime = 360
                                    val intent = Intent(
                                        applicationContext, FinalTicketActivity::class.java
                                    )
                                    intent.putExtra(Constant.IntentKey.TRANSACTION_ID, transId)
                                    intent.putExtra(Constant.IntentKey.BOOKING_ID, bookingId)
                                    startActivity(intent)
                                } catch (e: Exception) {
                                    println("updateUiCinemaSession ---> ${e.message}")
                                }

                            } else {
                                loader?.dismiss()
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    it.data?.msg.toString(),
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()
                            }

                        }
                    }
                    Status.ERROR -> {
                        loader?.dismiss()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.message.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                    Status.LOADING -> {
                        loader = LoaderDialog(R.string.pleasewait)
                        loader?.show(supportFragmentManager, null)
                    }
                }
            }
        }

    }

    private fun giftCardRemove(
        request: GiftCardRequest,
        offerEditText: EditText,
        apply: TextView,
        imageCheck: ImageView,
        remove: ImageView
    ) {
        summeryViewModel.giftCardRemove(request).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                try {
                                    retriveRemoveGiftCard(it.data.output, offerEditText, apply, imageCheck, remove)
                                } catch (e: Exception) {
                                    println("updateUiCinemaSession ---> ${e.message}")
                                }

                            } else {
                                loader?.dismiss()
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    it.data?.msg.toString(),
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()
                            }

                        }
                    }
                    Status.ERROR -> {
                        loader?.dismiss()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.message.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                    Status.LOADING -> {
                        loader = LoaderDialog(R.string.pleasewait)
                        loader?.show(supportFragmentManager, null)
                    }
                }
            }
        }
    }

    private fun retriveRemoveGiftCard(
        output: GiftCardRemove.Output,
        offerEditText: EditText,
        apply: TextView,
        imageCheck: ImageView,
        remove: ImageView
    ) {
        binding?.textTimeToLeft?.text = output.amount
        apply.show()
        imageCheck.hide()
        remove.hide()

        giftApplied= false
        adapter?.notifyDataSetChanged()

        offerEditText.text.clear()
        offerEditText.isClickable = true
        offerEditText.isEnabled = true
        offerEditText.isFocusable = true
        bankEdit.isFocusableInTouchMode = true

    }

    private fun giftCardApply(
        request: GiftCardRequest,
        offerEditText: EditText,
        apply: TextView,
        imageCheck: ImageView,
        remove: ImageView
    ) {
        summeryViewModel.giftCardApply(request).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                try {
                                    retrieveDataGiftCard(
                                        it.data.output, offerEditText, apply, imageCheck, remove
                                    )
                                } catch (e: Exception) {
                                    println("updateUiCinemaSession ---> ${e.message}")
                                }

                            } else {
                                loader?.dismiss()
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    it.data?.msg.toString(),
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()
                            }

                        }
                    }
                    Status.ERROR -> {
                        loader?.dismiss()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.message.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                    Status.LOADING -> {
                        loader = LoaderDialog(R.string.pleasewait)
                        loader?.show(supportFragmentManager, null)
                    }
                }
            }
        }

    }

    private fun retrieveDataGiftCard(
        output: GiftCardResponse.Output,
        offerEditText: EditText,
        apply: TextView,
        imageCheck: ImageView,
        remove: ImageView
    ) {
        if (output.PAID == "NO") {
            binding?.textTimeToLeft?.text = output.amount
            giftApplied=true
            offerEditText.isClickable = false
            offerEditText.isEnabled = false
            offerEditText.isFocusable = false
            bankEdit.isFocusableInTouchMode = false
            adapter?.notifyDataSetChanged()

            apply.hide()
            imageCheck.show()
            remove.show()
        } else {
            Constant.IntentKey.TimerExtandCheck = true
            Constant.IntentKey.TimerExtand = 90
            Constant.IntentKey.TimerTime = 360
            val intent = Intent(
                applicationContext, FinalTicketActivity::class.java
            )
            intent.putExtra(Constant.IntentKey.TRANSACTION_ID, transId)
            intent.putExtra(Constant.IntentKey.BOOKING_ID, bookingId)
            startActivity(intent)
        }
    }

    // hmac Request
    private fun paymentHmac(request: HmacKnetRequest) {
        summeryViewModel.paymentKnetHmac(request).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                try {

                                    val intent = Intent(applicationContext, PaymentWebActivity::class.java)
                                    intent.putExtra("PAY_URL", it.data.output.callingUrl)
                                    intent.putExtra(Constant.IntentKey.TRANSACTION_ID, transId)
                                    intent.putExtra(Constant.IntentKey.BOOKING_ID, bookingId)
                                    startActivity(intent)
                                    finish()

                                } catch (e: Exception) {
                                    println("updateUiCinemaSession ---> ${e.message}")
                                }

                            } else {
                                loader?.dismiss()
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    it.data?.msg.toString(),
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()
                            }

                        }
                    }
                    Status.ERROR -> {
                        loader?.dismiss()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.message.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                    Status.LOADING -> {
//                            loader = LoaderDialog(R.string.pleasewait)
//                            loader?.show(supportFragmentManager, null)
                    }
                }
            }
        }
    }

    private fun resendTimer() {
        countDownTimerPrimary =
            object : CountDownTimer((Constant.IntentKey.TimerTime * 1000), 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    val second = millisUntilFinished / 1000 % 60
                    val minutes = millisUntilFinished / (1000 * 60) % 60
                    val display = java.lang.String.format("%02d:%02d", minutes, second)

                    textView111?.text = display
                    timeCount = minutes * 60 + second
                    secondLeft = second

                    if (timeCount == dialogShow) {

                        println("FinishTimer-=-->${Constant.IntentKey.TimerExtandCheck},Time--->${Constant.IntentKey.TimerExtand}")
                        if (!Constant.IntentKey.TimerExtandCheck) {
                            extendTime()
                        } else if (Constant.IntentKey.TimerExtandCheck && Constant.IntentKey.TimerExtand > 0) {
                            object : CountDownTimer((Constant.IntentKey.TimerExtand * 1000), 1000) {
                                @SuppressLint("SetTextI18n")
                                override fun onTick(millisUntilFinished: Long) {
                                    val second = millisUntilFinished / 1000 % 60
                                    val minutes = millisUntilFinished / (1000 * 60) % 60
                                    val display =
                                        java.lang.String.format("%02d:%02d", minutes, second)

                                    textView111?.text = display
                                    Constant.IntentKey.TimerExtand = minutes * 60 + second
                                }

                                override fun onFinish() {
                                    val dialog = OptionDialog(this@PaymentListActivity,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        getString(R.string.timeOut),
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                            Constant.IntentKey.TimerTime = 360
                                            Constant.IntentKey.TimerExtand = 90
                                            finish()
                                        },
                                        negativeClick = {})
                                    dialog.show()

                                }
                            }.start()
                        } else if (Constant.IntentKey.TimerExtandCheck && Constant.IntentKey.TimerExtand < 0) {
                            val dialog = OptionDialog(this@PaymentListActivity,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                getString(R.string.timeOut),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
                                negativeClick = {})
                            dialog.show()
                        }
                    }
                }

                override fun onFinish() {
                    if (!timeExtendClick) {
                        Constant.IntentKey.TimerExtand = 90
                        Constant.IntentKey.TimerTime = 360
                        finish()
                    }

                }
            }.start()
    }

    private fun extendTime() {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView: View =
            LayoutInflater.from(this).inflate(R.layout.cancel_dialog, viewGroup, false)
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val alertDialog: android.app.AlertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        alertDialog.show()

        dialogView.title.text = getString(R.string.app_name)
        dialogView.subtitle.text = getString(R.string.stillHere)
        dialogView.consSure?.setOnClickListener {
            countDownTimerPrimary?.cancel()
            timeExtendClick = true
            Constant.IntentKey.TimerExtand = 90 + secondLeft
            Constant.IntentKey.TimerExtandCheck = true
            alertDialog.dismiss()
            object : CountDownTimer((Constant.IntentKey.TimerExtand * 1000), 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    val second = millisUntilFinished / 1000 % 60
                    val minutes = millisUntilFinished / (1000 * 60) % 60
                    textView111.text = "$minutes:$second"
                    Constant.IntentKey.TimerExtand = minutes * 60 + second

                }

                override fun onFinish() {
                    Constant.IntentKey.TimerExtand = 90
                    Constant.IntentKey.TimerTime = 360
                    finish()
                }
            }.start()
        }

        dialogView.negative_btn?.setOnClickListener {
            Constant.IntentKey.TimerExtand = 90
            finish()
        }
    }

    private fun cancelDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.cancel_dialog)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.show()

        dialog.consSure?.setOnClickListener {
            Constant.IntentKey.TimerExtandCheck = true
            Constant.IntentKey.TimerExtand = 90
            Constant.IntentKey.TimerTime = 360
            cancelTrans(CancelTransRequest(bookingId, transId))
            finish()
        }

        dialog.negative_btn?.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun cancelTrans(cancelTransRequest: CancelTransRequest) {
        summeryViewModel.cancelTrans(cancelTransRequest).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                try {
                                    println("cancelTrans ---> ${it.data.output}")
                                } catch (e: Exception) {
                                    println("updateUiCinemaSession ---> ${e.message}")
                                }

                            } else {
                                loader?.dismiss()
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    it.data?.msg.toString(),
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()
                            }

                        }
                    }
                    Status.ERROR -> {
                        loader?.dismiss()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.message.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                    Status.LOADING -> {
                    }
                }
            }
        }
    }

}