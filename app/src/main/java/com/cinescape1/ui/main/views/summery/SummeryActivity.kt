package com.cinescape1.ui.main.views.summery

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.cinescape1.data.models.responseModel.TicketSummaryResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityCheckoutWithFoodBinding
import com.cinescape1.ui.main.views.finalTicket.FinalTicketActivity
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.summery.viewModel.SummeryViewModel
import com.cinescape1.ui.main.views.login.LoginActivity
import com.cinescape1.ui.main.views.payment.PaymentWebActivity
import com.cinescape1.ui.main.views.adapters.SummerySeatListAdapter
import com.cinescape1.ui.main.views.adapters.checkoutAdapter.AdapterCheckoutFoodItem
import com.cinescape1.utils.*
import com.cinescape1.utils.Constant.IntentKey.Companion.USER_ID
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.threatmetrix.TrustDefender.*
import com.threatmetrix.TrustDefender.TMXProfilingConnections.TMXProfilingConnections
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_checkout_with_food.*
import kotlinx.android.synthetic.main.cancel_dialog.*
import kotlinx.android.synthetic.main.cancel_dialog.view.*
import kotlinx.android.synthetic.main.checkout_creditcart_payment_alert.*
import kotlinx.android.synthetic.main.checkout_layout_ticket_include.*
import kotlinx.android.synthetic.main.food_review_pay_include.*
import org.json.JSONArray
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class SummeryActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private var loader: LoaderDialog? = null
    private val summeryViewModel: SummeryViewModel by viewModels { viewModelFactory }
    private var binding: ActivityCheckoutWithFoodBinding? = null
    private var up = true
    private var click = false
    private var payType = ""
    private var bookingId = ""
    private var bookType = ""
    private var transId = ""
    private var broadcastReceiver: BroadcastReceiver? = null
    private var timeCount: Long = 0
    private var m_sessionID = ""
    private var refId = ""
    private var VISA_PREFIX = "4"
    private var mContext: Activity = this
    private var MASTERCARD_PREFIX = "51,52,53,54,55,2222,22"
    private var DISCOVER_PREFIX = "6011,65"
    private var AMEX_PREFIX = "34,37,"
    private var JCB = "2131,1800"
    private var MAESTRO = "5033,5868,6759,5641"
    private var UATP = "1354"
    private var type = ""
    private var from = ""
    private var paidPrice = ""
    private var totalPrice = ""
    private var cardinal = Cardinal.getInstance()

    private var secondLeft: Long = 0
    private var timeExtandClick: Boolean = false
    private var dialogShow: Long = 60
    private var countDownTimerPrimary: CountDownTimer? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutWithFoodBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(this, "en")
            }
            else -> {
                LocaleHelper.setLocale(this, "en")
            }
        }
        setContentView(view)
        from = intent.getStringExtra("From").toString()
        bookType = intent.getStringExtra("BOOKING").toString()
        type = intent.getStringExtra("TYPE").toString()
        //AppBar Hide
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        println("bookingType---->${type}")
        if (type == "0") {
            textView111?.show()
            textView112?.show()
            resendTimer()
        }
        if (!preferences.getBoolean(Constant.IS_LOGIN)) {
            val intent = Intent(this, LoginActivity::class.java)
                .putExtra("BOOKING", bookType)
                .putExtra("FROM", "Payment")
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        tckSummary(
            TicketSummaryRequest(
                intent.getStringExtra("TRANS_ID").toString(),
                USER_ID,
                preferences.getString(Constant.USER_ID).toString()
            )
        )
        broadcastReceiver = MyReceiver()
        broadcastIntent()
        movedNext()
    }

    private fun broadcastIntent() {
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun movedNext() {
        binding?.viewProceed?.setOnClickListener {
            if (click) {
                when (payType) {
                    "WALLET" -> {

                        walletPay(
                            HmacKnetRequest(
                                bookingId,
                                bookType,
                                transId,
                                preferences.getString(Constant.USER_ID).toString()
                            )
                        )
//                        startActivity(
//                            Intent(
//                                this,
//                                PaymentWebActivity::class.java
//                            ).putExtra("PAY_URL", "")
//                        )
                    }
                    "KNET" -> {
                        paymentHmac(
                            HmacKnetRequest(
                                bookingId,
                                bookType,
                                transId,
                                preferences.getString(Constant.USER_ID).toString()
                            )
                        )

                    }
                    "CC" -> {
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
                            LayoutInflater.from(this)
                                .inflate(R.layout.checkout_creditcart_payment_alert, null)
                        val mBuilder = AlertDialog.Builder(this)
                            .setView(mDialogView)
                        val proceedAlertDialog = mBuilder.show()
                        proceedAlertDialog.show()
//                        " "+getString(R.string.price_kd)+" "+
                        proceedAlertDialog?.kd_to_pay?.text = " $totalPrice"

                        proceedAlertDialog.cardNumberTextInputEditText.addTextChangedListener(object :
                            TextWatcher {
                            override fun beforeTextChanged(
                                charSequence: CharSequence,
                                i: Int,
                                i1: Int,
                                i2: Int
                            ) {
                                try {
                                    if (!proceedAlertDialog.cardNumberTextInputEditText.text
                                            .toString().isEmpty()
                                    ) {
                                        proceedAlertDialog.image_american_express_card.visibility =
                                            View.VISIBLE
                                        if (VISA_PREFIX.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString().substring(0, 1)
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.visa_card
                                                )
                                            )
                                        } else if (MASTERCARD_PREFIX.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString()
                                                    .substring(0, 2) + ","
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.mastercard_card
                                                )
                                            )
                                        } else if (AMEX_PREFIX.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString()
                                                    .substring(0, 2) + ","
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.amerian_card
                                                )
                                            )
                                        } else if (DISCOVER_PREFIX.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString().substring(0, 4)
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.amerian_card
                                                )
                                            )
                                        } else if (JCB.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString().substring(0, 1)
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.amerian_card
                                                )
                                            )
                                        } else if (MAESTRO.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString().substring(0, 2)
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.amerian_card
                                                )
                                            )
                                        } else {
                                            proceedAlertDialog.image_american_express_card.visibility =
                                                View.GONE
                                        }
                                    } else {
                                        proceedAlertDialog.image_american_express_card.visibility =
                                            View.GONE
                                    }
                                } catch (e: java.lang.Exception) {
                                }
                            }

                            override fun onTextChanged(
                                charSequence: CharSequence,
                                i: Int,
                                i1: Int,
                                i2: Int
                            ) {
                                try {
                                    if (!proceedAlertDialog.cardNumberTextInputEditText.text
                                            .toString().isEmpty()
                                    ) {
                                        proceedAlertDialog.image_american_express_card.visibility =
                                            View.VISIBLE
                                        if (VISA_PREFIX.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString().substring(0, 1)
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.visa_card
                                                )
                                            )
                                        } else if (MASTERCARD_PREFIX.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString()
                                                    .substring(0, 2) + ","
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.mastercard_card
                                                )
                                            )
                                        } else if (AMEX_PREFIX.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString()
                                                    .substring(0, 2) + ","
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.amerian_card
                                                )
                                            )
                                        } else if (DISCOVER_PREFIX.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString().substring(0, 4)
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.disover_card
                                                )
                                            )
                                        } else if (JCB.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString().substring(0, 4)
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.jcb_card
                                                )
                                            )
                                        } else if (MAESTRO.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString().substring(0, 4)
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.maestro_card
                                                )
                                            )
                                        } else if (UATP.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString().substring(0, 4)
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.uatp
                                                )
                                            )
                                        } else {
                                            proceedAlertDialog.image_american_express_card.visibility =
                                                View.GONE
                                        }
                                    } else {
                                        proceedAlertDialog.image_american_express_card.visibility =
                                            View.GONE
                                    }
                                } catch (e: java.lang.Exception) {
                                }
                            }

                            override fun afterTextChanged(editable: Editable) {
                                try {
                                    if (!proceedAlertDialog.cardNumberTextInputEditText.text
                                            .toString().isEmpty()
                                    ) {
                                        proceedAlertDialog.image_american_express_card.visibility =
                                            View.VISIBLE
                                        if (VISA_PREFIX.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString().substring(0, 1)
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.visa_card
                                                )
                                            )
                                        } else if (MASTERCARD_PREFIX.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString()
                                                    .substring(0, 2) + ","
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.mastercard_card
                                                )
                                            )
                                        } else if (AMEX_PREFIX.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString()
                                                    .substring(0, 2) + ","
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.amerian_card
                                                )
                                            )
                                        } else if (DISCOVER_PREFIX.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString().substring(0, 4)
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.disover_card
                                                )
                                            )
                                        } else if (JCB.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString().substring(0, 4)
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.jcb_card
                                                )
                                            )
                                        } else if (MAESTRO.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString().substring(0, 4)
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.maestro_card
                                                )
                                            )
                                        } else if (UATP.contains(
                                                proceedAlertDialog.cardNumberTextInputEditText.text
                                                    .toString().substring(0, 4)
                                            ) && !proceedAlertDialog.cardNumberTextInputEditText.text
                                                .toString().isEmpty()
                                        ) {
                                            proceedAlertDialog.image_american_express_card.setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    mContext,
                                                    R.drawable.uatp
                                                )
                                            )
                                        } else {
                                            proceedAlertDialog.image_american_express_card.visibility =
                                                View.GONE
                                        }
                                    } else {
                                        proceedAlertDialog.image_american_express_card.visibility =
                                            View.GONE
                                    }
                                } catch (e: java.lang.Exception) {
                                }
                            }
                        })

                        proceedAlertDialog.text_cancel_go_back.setOnClickListener {
                            proceedAlertDialog.dismiss()

                        }
                        var lastInput = ""

                        proceedAlertDialog.expireDateTextInputEditText.addTextChangedListener(object :
                            TextWatcher {
                            override fun afterTextChanged(p0: Editable?) {}

                            override fun beforeTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {
                            }

                            @SuppressLint("SetTextI18n")
                            override fun onTextChanged(
                                p0: CharSequence?,
                                start: Int,
                                removed: Int,
                                added: Int
                            ) {
                                val input = p0.toString()
                                val formatter = SimpleDateFormat("MM/yy", Locale.GERMANY)
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
                                    lastInput =
                                        proceedAlertDialog.expireDateTextInputEditText.text.toString()
                                    //because not valid so code exits here
                                    return
                                }
                            }
                        })

                        proceedAlertDialog.btn_pay.setOnClickListener {
                            if (validateFields(proceedAlertDialog))
                                try {
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
                                bookingId,
                                bookType,
                                transId,
                                preferences.getString(Constant.USER_ID).toString()
                            )
                        )
                    }
                }
            } else {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.payment_method),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            }
        }

        binding?.viewCancel?.setOnClickListener {
            cancelDialog()
        }
        imgs_back.setOnClickListener {
//            onBackPressed()
            cancelDialog()

        }

        binding?.viewFood?.setOnClickListener {
            if (!up) {
                up = true
                icon_down_arrow.setImageResource(R.drawable.ic_icons_arrow_down)
                recyclerview_food_chekout.visibility = View.GONE
            } else {
                up = false
                icon_down_arrow.setImageResource(R.drawable.arrow_up)
                recyclerview_food_chekout.visibility = View.VISIBLE
            }
        }
    }

    private fun walletPay(request: HmacKnetRequest) {
        summeryViewModel.paymentWallet(request)
            .observe(this) {
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
                                            applicationContext,
                                            FinalTicketActivity::class.java
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
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
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
                                positiveClick = {
                                },
                                negativeClick = {
                                })
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

    private fun cancelDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.cancel_dialog)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
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

    private fun creditCardInit(request: HmacKnetRequest) {
        summeryViewModel.creditCardInit(request)
            .observe(this) {
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
                                                20,
                                                TimeUnit.SECONDS
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
                                            it.data.output.deviceSessionId,
                                            it.data.output.merchantId
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
                                                validateResponse: ValidateResponse?,
                                                serverJwt: String?
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
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
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
                                positiveClick = {
                                },
                                negativeClick = {
                                })
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

    private fun postCardData(request: PostCardRequest) {
        summeryViewModel.postCardData(request)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        if (it.data.output.redirect == "0") {
                                            cardinal.cca_continue(
                                                it.data.output.authTransId,
                                                it.data.output.pares,
                                                this
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
                                                                    m_sessionID,
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
                                                positiveClick = {
                                                },
                                                negativeClick = {
                                                })
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
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
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
                                positiveClick = {
                                },
                                negativeClick = {
                                })
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
        summeryViewModel.validateJWT(s)
            .observe(this) {
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
                                                applicationContext,
                                                FinalTicketActivity::class.java
                                            )
                                            intent.putExtra(
                                                Constant.IntentKey.TRANSACTION_ID,
                                                transId
                                            )
                                            intent.putExtra(
                                                Constant.IntentKey.BOOKING_ID,
                                                bookingId
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
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
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
                                positiveClick = {
                                },
                                negativeClick = {
                                })
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

    // Ticket Summary
    private fun tckSummary(request: TicketSummaryRequest) {
        summeryViewModel.tckSummary(request)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        bookType = it.data.output.bookingType
                                        bookingId = it.data.output.bookingId
                                        transId = request.transid
                                        retrieveSummaryResponse(it.data.output)
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
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
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
                                positiveClick = {
                                },
                                negativeClick = {
                                })
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

    // Hmac Request
    private fun paymentHmac(request: HmacKnetRequest) {
        summeryViewModel.paymentKnetHmac(request)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
//                                        startActivity(Intent(this, PaymentWebActivity::class.java).putExtra("PAY_URL",it.data.output.callingUrl?:""))
                                        val intent = Intent(
                                            applicationContext,
                                            PaymentWebActivity::class.java
                                        )
                                        intent.putExtra("PAY_URL", it.data.output.callingUrl)
                                        intent.putExtra(
                                            Constant.IntentKey.TRANSACTION_ID,
                                            transId.toString()
                                        )
                                        intent.putExtra(Constant.IntentKey.BOOKING_ID, bookingId)
//                                        intent.putExtra(Constant.PCBackStackActivity.OPEN_ACTIVITY_NAME, Constant.PCBackStackActivity.SOUND_LIKE_PLANE_ACTIVITY)
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
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
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
                                positiveClick = {
                                },
                                negativeClick = {
                                })
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

    @SuppressLint("SetTextI18n")
    private fun retrieveSummaryResponse(output: TicketSummaryResponse.Output) {
        binding?.uiCheckout?.show()
        binding?.constraintLayout6?.show()
        println("BookingType--->${output.totalPrice}")
        binding?.textTimeToLeft?.text = output.totalPrice
        if (output.totalPrice.isNullOrEmpty()) {
            binding?.textTimeLeft?.hide()
        } else {
            binding?.textTimeLeft?.show()

        }
        binding?.textWalletKd?.text = output.balance
        binding?.textView117?.text = output.balance

        if (output.clubEnable) {
            binding?.viewWallet?.isEnabled = true
            binding?.viewWallet?.isClickable = true
        } else {
            binding?.viewWallet?.isEnabled = false
            binding?.viewWallet?.isClickable = false
        }

        if (output.bookingType == "FOOD") {
            setCheckoutOnlyFoodItemAdapter(output.concessionFoods)
            ticketPage.hide()
            priceView.hide()
            totalPrice=output.totalTicketPrice

            binding?.priceUi?.hide()
            binding?.textTimeLeft?.hide()
            binding?.textTimeToLeft?.hide()
            checkout_food_include.show()
            binding?.view1Line1?.hide()
            foodViewCheck.hide()
            binding?.imageView6?.setImageResource(R.mipmap.food_checkout)

        } else {
//            binding?.priceUi?.show()
            ticketPage.show()
            priceView.show()
            foodViewCheck.show()
            binding?.view1Line1?.show()
            binding?.priceUi?.hide()
            checkout_food_include.hide()
            Glide.with(this).load(output.posterhori).placeholder(R.drawable.bombshell)
                .into(binding?.imageView6!!)
            summary_name_movie.text = output.moviename
            txt_screen.text = output.screenId.toString()
            text_location_names.text = output.cinemaname
            text_times2.text = output.experience
            text_date_name.text = output.showDate
            textView19.text = output.showTime

            val layoutManager = FlexboxLayoutManager(this)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager.alignItems = AlignItems.STRETCH
//            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
            val adapter = SummerySeatListAdapter(output.seatsArr)
            seatList.layoutManager = layoutManager
            seatList.adapter = adapter


            text_kds.text = output.ticketPrice
            totalPrice=output.totalTicketPrice
            text_kd_total.text = output.totalTicketPrice
            paidPrice = output.totalPrice
            binding?.textTimeToLeft?.text = output.totalPrice
            binding?.textView118?.text = output.totalPrice
            binding?.textView116?.text = output.totalPrice

            binding?.textKdFood?.text = output.totalPrice

            text_qty_number.text = output.numofseats.toString()
            binding?.textView119?.text = output.foodTotal

            setCheckoutFoodItemAdapter(output.concessionFoods)

            if (output.mcensor == "") {
                summary_censor.hide()
            } else {
                summary_censor.show()
                summary_censor.text = output.mcensor
            }
            when (output.mcensor) {
                "PG" -> {
                    summary_censor.setBackgroundResource(R.color.grey)
                }
                "G" -> {
                    summary_censor.setBackgroundResource(R.color.green)
                }
                "18+" -> {
                    summary_censor.setBackgroundResource(R.color.red)
                }
                "13+" -> {
                    summary_censor.setBackgroundResource(R.color.yellow)
                }
                "E" -> {
                    summary_censor.setBackgroundResource(R.color.wowOrange)
                }
                "T" -> {
                    summary_censor.setBackgroundResource(R.color.tabIndicater)
                }
                else -> {
                    summary_censor.setBackgroundResource(R.color.blue)
                }
            }
        }
        payModeList()
    }

    private fun payModeList() {
        binding?.viewWallet?.setOnClickListener {
            binding?.imageWallet?.setBackgroundColor(getColor(R.color.text_alert_color_red))
            binding?.textWalletName?.setTextColor(getColor(R.color.white))
            binding?.textWalletKd?.setTextColor(getColor(R.color.white))

            binding?.imageKnet?.setBackgroundColor(getColor(R.color.transparent))
            binding?.textKnetName?.setTextColor(getColor(R.color.hint_color))

            binding?.imageCreditCard?.setBackgroundColor(getColor(R.color.transparent))
            binding?.textCreditCardName?.setTextColor(getColor(R.color.hint_color))
            click = true
            payType = "WALLET"
        }

        binding?.viewKnet?.setOnClickListener {
            binding?.imageWallet?.setBackgroundColor(getColor(R.color.transparent))
            binding?.textWalletName?.setTextColor(getColor(R.color.hint_color))
            binding?.textWalletKd?.setTextColor(getColor(R.color.hint_color))

            binding?.imageKnet?.setBackgroundColor(getColor(R.color.text_alert_color_red))
            binding?.textKnetName?.setTextColor(getColor(R.color.white))

            binding?.imageCreditCard?.setBackgroundColor(getColor(R.color.transparent))
            binding?.textCreditCardName?.setTextColor(getColor(R.color.hint_color))
            payType = "KNET"
            click = true
        }

        binding?.viewCreditCard?.setOnClickListener {
            binding?.imageWallet?.setBackgroundColor(getColor(R.color.transparent))
            binding?.textWalletName?.setTextColor(getColor(R.color.hint_color))
            binding?.textWalletKd?.setTextColor(getColor(R.color.hint_color))

            binding?.imageKnet?.setBackgroundColor(getColor(R.color.transparent))
            binding?.textKnetName?.setTextColor(getColor(R.color.hint_color))

            binding?.imageCreditCard?.setBackgroundColor(getColor(R.color.text_alert_color_red))
            binding?.textCreditCardName?.setTextColor(getColor(R.color.white))
            payType = "CC"
            click = true

        }
    }

    private fun setCheckoutFoodItemAdapter(concessionFoods: List<TicketSummaryResponse.ConcessionFood>) {
        println("foodViewCheck----2>${concessionFoods}")
        if (concessionFoods.isNullOrEmpty()) {
            binding?.foodViewCheck?.hide()
            binding?.view?.hide()
        } else {
            binding?.foodViewCheck?.show()
            binding?.view?.show()

            val gridLayout = GridLayoutManager(
                this@SummeryActivity,
                1,
                GridLayoutManager.VERTICAL,
                false
            )
            recyclerview_food_chekout.layoutManager = LinearLayoutManager(this)
            val adapter = AdapterCheckoutFoodItem(this@SummeryActivity, concessionFoods)
            recyclerview_food_chekout.layoutManager = gridLayout
            recyclerview_food_chekout.adapter = adapter
            adapter.loadNewData(concessionFoods)
        }

    }

    private fun setCheckoutOnlyFoodItemAdapter(concessionFoods: List<TicketSummaryResponse.ConcessionFood>) {
        val gridLayout =
            GridLayoutManager(this@SummeryActivity, 1, GridLayoutManager.VERTICAL, false)
        recyclerview_food_preview.layoutManager = LinearLayoutManager(this)
        val adapter = AdapterCheckoutFoodItem(this@SummeryActivity, concessionFoods)
        recyclerview_food_preview.layoutManager = gridLayout
        recyclerview_food_preview.adapter = adapter
        adapter.loadNewData(concessionFoods)
    }

    private fun resendTimer() {
        countDownTimerPrimary =
            object : CountDownTimer((Constant.IntentKey.TimerTime * 1000), 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    val second = millisUntilFinished / 1000 % 60
                    val minutes = millisUntilFinished / (1000 * 60) % 60
                    textView111?.text = "$minutes:$second"
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
                                    textView111?.text = "$minutes:$second"
                                    Constant.IntentKey.TimerExtand = minutes * 60 + second
                                }

                                override fun onFinish() {
                                    val dialog = OptionDialog(this@SummeryActivity,
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
                                        negativeClick = {
                                        })
                                    dialog.show()

                                }
                            }.start()
                        } else if (Constant.IntentKey.TimerExtandCheck && Constant.IntentKey.TimerExtand < 0) {
                            val dialog = OptionDialog(this@SummeryActivity,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                getString(R.string.timeOut),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                },
                                negativeClick = {
                                })
                            dialog.show()
                        }
                    }
                }

                override fun onFinish() {
                    if (!timeExtandClick){
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
            timeExtandClick=true
            Constant.IntentKey.TimerExtand =90+secondLeft
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

    override fun onBackPressed() {
        super.onBackPressed()
        cancelDialog()

    }

    private fun cancelTrans(cancelTransRequest: CancelTransRequest) {
        summeryViewModel.cancelTrans(cancelTransRequest)
            .observe(this) {
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
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
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
                                positiveClick = {
                                },
                                negativeClick = {
                                })
                            dialog.show()
                        }
                        Status.LOADING -> {
                        }
                    }
                }
            }
    }

    fun doProfile(sessions1: String, merchent: String) {
        val list: List<String> = ArrayList()
        //        list.add("attribute 1");
//        list.add("attribute 2");
        val options =
            TMXProfilingOptions().setCustomAttributes(list) // Fire off the profiling request. We could use a more complex request,
        options.setSessionID(merchent + sessions1)
        val profilingHandle = TMXProfiling.getInstance().profile(options, CompletionNotifier())
        // Session id can be collected here
        Log.d("TAG", "Session id = " + profilingHandle.sessionID)
        /*
         * profilingHandle can also be used to cancel this profile if needed *
         * profilingHandle.cancel();
         * */m_sessionID = sessions1
    }

    private class CompletionNotifier : TMXEndNotifier {
        override fun complete(result: TMXProfilingHandle.Result) {
//            m_sessionID = result.getSessionID();
            println("SessionId-->${SummeryActivity().m_sessionID}")
            Log.d("ProfilingResults-", "SessionID:" + result.sessionID + "Status:" + result.status)
        }
    }

    private fun validateFields(proceedAlertDialog: AlertDialog): Boolean {
        return if (proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                .isEmpty() && proceedAlertDialog.cardNumberTextInputEditText.text
                .toString().length != 16 && !CreditCardUtils.isValid(
                proceedAlertDialog.cardNumberTextInputEditText.text.toString().replace(" ", "")
            )
        ) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.valid_card),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                },
                negativeClick = {
                })
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
                positiveClick = {
                },
                negativeClick = {
                })
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
                positiveClick = {
                },
                negativeClick = {
                })
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
                positiveClick = {
                },
                negativeClick = {
                })
            dialog.show()
            false
        } else {
            true
        }
    }

}