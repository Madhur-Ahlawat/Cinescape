package com.cinescape1.ui.main.views.summery

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.*
import com.cinescape1.data.models.responseModel.TicketSummaryResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityCheckoutWithFoodBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.adapters.SummerySeatListAdapter
import com.cinescape1.ui.main.views.adapters.checkoutAdapter.AdapterCheckoutFoodItem
import com.cinescape1.ui.main.views.home.HomeActivity
import com.cinescape1.ui.main.views.login.LoginActivity
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity
import com.cinescape1.ui.main.views.summery.viewModel.SummeryViewModel
import com.cinescape1.utils.*
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.threatmetrix.TrustDefender.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_checkout_with_food.*
import kotlinx.android.synthetic.main.cancel_dialog.*
import kotlinx.android.synthetic.main.cancel_dialog.view.*
import kotlinx.android.synthetic.main.checkout_creditcart_payment_alert.*
import kotlinx.android.synthetic.main.checkout_layout_ticket_include.*
import kotlinx.android.synthetic.main.food_review_pay_include.*
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
class SummeryActivity : DaggerAppCompatActivity(), SummerySeatListAdapter.TypeFaceSeatLists {

    private var mDialog: Dialog? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private var loader: Dialog? = null
    private val summeryViewModel: SummeryViewModel by viewModels { viewModelFactory }
    private var binding: ActivityCheckoutWithFoodBinding? = null
    private var up = true
    private var click = false
    private var payType = ""
    private var bookingId = ""
    private var bookingIdNEw = ""
    private var bookType = ""
    private var transId = ""
    private var broadcastReceiver: BroadcastReceiver? = null
    private var timeCount: Long = 0
    private var type = ""
    private var from = ""
    private var image = ""
    private var paidPrice = ""
    private var totalPrice = ""
    private var cinemaId = ""
    private var sessionId = ""
    private var secondLeft: Long = 0
    private var timeExtendClick: Boolean = false
    private var dialogShow: Long = 60
    private var countDownTimerPrimary: CountDownTimer? = null
    private var seatNumber1: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutWithFoodBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
                val regular = ResourcesCompat.getFont(this, R.font.montserrat_regular)
                val bold = ResourcesCompat.getFont(this, R.font.montserrat_bold)
                val medium = ResourcesCompat.getFont(this, R.font.montserrat_medium)

                binding?.checkoutTicketInclude?.textView112?.typeface = regular
//                binding?.checkoutTicketInclude?.textView111?.typeface = bold   // heavy
                binding?.checkoutTicketInclude?.summaryNameMovie?.typeface = bold   // heavy
                binding?.checkoutTicketInclude?.summaryCensor?.typeface = regular
                binding?.checkoutTicketInclude?.textLocations?.typeface = regular
                binding?.checkoutTicketInclude?.textLocationNames?.typeface = regular
                binding?.checkoutTicketInclude?.txtDates?.typeface = regular
                binding?.checkoutTicketInclude?.txtScreen?.typeface = regular
                binding?.checkoutTicketInclude?.textsTime?.typeface = regular
                binding?.checkoutTicketInclude?.textTimes2?.typeface = regular
                binding?.checkoutTicketInclude?.textDates?.typeface = regular
                binding?.checkoutTicketInclude?.textDateName?.typeface = regular
                binding?.checkoutTicketInclude?.textTimess?.typeface = regular
                binding?.checkoutTicketInclude?.textView19?.typeface = regular
                binding?.checkoutTicketInclude?.textSeat?.typeface = regular
                binding?.checkoutTicketInclude?.textTicketPrice?.typeface = regular
                binding?.checkoutTicketInclude?.textKds?.typeface = regular
                binding?.checkoutTicketInclude?.textQuntity?.typeface = regular
                binding?.checkoutTicketInclude?.textQtyNumber?.typeface = regular
                binding?.checkoutTicketInclude?.textTotal?.typeface = regular
                binding?.checkoutTicketInclude?.textKdTotal?.typeface = regular
                seatNumber1?.typeface = regular

                binding?.textFoodTitle?.typeface = bold
                binding?.textView152?.typeface = regular
                binding?.textView153?.typeface = regular
                binding?.textView154?.typeface = regular
                binding?.textView151?.typeface = regular
                binding?.textSelectPaymentMethod?.typeface = bold
                binding?.textCreditCardName?.typeface = regular
                binding?.textKnetName?.typeface = regular
                binding?.textWalletName?.typeface = regular
                binding?.textWalletKd?.typeface = regular
                binding?.textView113?.typeface = regular
                binding?.textView114?.typeface = regular
                binding?.textView115?.typeface = bold
                binding?.textView116?.typeface = regular
                binding?.textView118?.typeface = bold

                binding?.textCancel?.typeface = regular
                binding?.textTimeLeft?.typeface = regular
                binding?.textTimeToLeft?.typeface = regular
                binding?.txtProceed?.typeface = bold

            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(this, "en")
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(this, R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)

                binding?.checkoutTicketInclude?.textView112?.typeface = regular
//                binding?.checkoutTicketInclude?.textView111?.typeface = heavy   // heavy
                binding?.checkoutTicketInclude?.summaryNameMovie?.typeface = heavy   // heavy
                binding?.checkoutTicketInclude?.summaryCensor?.typeface = regular
                binding?.checkoutTicketInclude?.textLocations?.typeface = regular
                binding?.checkoutTicketInclude?.textLocationNames?.typeface = regular
                binding?.checkoutTicketInclude?.txtDates?.typeface = regular
                binding?.checkoutTicketInclude?.txtScreen?.typeface = regular
                binding?.checkoutTicketInclude?.textsTime?.typeface = regular
                binding?.checkoutTicketInclude?.textTimes2?.typeface = regular
                binding?.checkoutTicketInclude?.textDates?.typeface = regular
                binding?.checkoutTicketInclude?.textDateName?.typeface = regular
                binding?.checkoutTicketInclude?.textTimess?.typeface = regular
                binding?.checkoutTicketInclude?.textView19?.typeface = regular
                binding?.checkoutTicketInclude?.textSeat?.typeface = regular
                binding?.checkoutTicketInclude?.textTicketPrice?.typeface = regular
                binding?.checkoutTicketInclude?.textKds?.typeface = regular
                binding?.checkoutTicketInclude?.textQuntity?.typeface = regular
                binding?.checkoutTicketInclude?.textQtyNumber?.typeface = regular
                binding?.checkoutTicketInclude?.textTotal?.typeface = regular
                binding?.checkoutTicketInclude?.textKdTotal?.typeface = regular
                seatNumber1?.typeface = regular

                binding?.textFoodTitle?.typeface = bold
                binding?.textView152?.typeface = regular
                binding?.textView153?.typeface = regular
                binding?.textView154?.typeface = regular
                binding?.textView151?.typeface = regular
                binding?.textSelectPaymentMethod?.typeface = bold
                binding?.textCreditCardName?.typeface = regular
                binding?.textKnetName?.typeface = regular
                binding?.textWalletName?.typeface = regular
                binding?.textWalletKd?.typeface = regular
                binding?.textView113?.typeface = regular
                binding?.textView114?.typeface = regular
                binding?.textView115?.typeface = bold
                binding?.textView116?.typeface = regular
                binding?.textView118?.typeface = bold

                binding?.textCancel?.typeface = regular
                binding?.textTimeLeft?.typeface = regular
                binding?.textTimeToLeft?.typeface = regular
                binding?.txtProceed?.typeface = bold


            }
            else -> {
                LocaleHelper.setLocale(this, "en")
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(this, R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)

                binding?.checkoutTicketInclude?.textView112?.typeface = regular
//                binding?.checkoutTicketInclude?.textView111?.typeface = heavy   // heavy
                binding?.checkoutTicketInclude?.summaryNameMovie?.typeface = heavy   // heavy
                binding?.checkoutTicketInclude?.summaryCensor?.typeface = regular
                binding?.checkoutTicketInclude?.textLocations?.typeface = regular
                binding?.checkoutTicketInclude?.textLocationNames?.typeface = regular
                binding?.checkoutTicketInclude?.txtDates?.typeface = regular
                binding?.checkoutTicketInclude?.txtScreen?.typeface = regular
                binding?.checkoutTicketInclude?.textsTime?.typeface = regular
                binding?.checkoutTicketInclude?.textTimes2?.typeface = regular
                binding?.checkoutTicketInclude?.textDates?.typeface = regular
                binding?.checkoutTicketInclude?.textDateName?.typeface = regular
                binding?.checkoutTicketInclude?.textTimess?.typeface = regular
                binding?.checkoutTicketInclude?.textView19?.typeface = regular
                binding?.checkoutTicketInclude?.textSeat?.typeface = regular
                binding?.checkoutTicketInclude?.textTicketPrice?.typeface = regular
                binding?.checkoutTicketInclude?.textKds?.typeface = regular
                binding?.checkoutTicketInclude?.textQuntity?.typeface = regular
                binding?.checkoutTicketInclude?.textQtyNumber?.typeface = regular
                binding?.checkoutTicketInclude?.textTotal?.typeface = regular
                binding?.checkoutTicketInclude?.textKdTotal?.typeface = regular
                seatNumber1?.typeface = regular

                binding?.textFoodTitle?.typeface = bold
                binding?.textView152?.typeface = regular
                binding?.textView153?.typeface = regular
                binding?.textView154?.typeface = regular
                binding?.textView151?.typeface = regular
                binding?.textSelectPaymentMethod?.typeface = bold
                binding?.textCreditCardName?.typeface = regular
                binding?.textKnetName?.typeface = regular
                binding?.textWalletName?.typeface = regular
                binding?.textWalletKd?.typeface = regular
                binding?.textView113?.typeface = regular
                binding?.textView114?.typeface = regular
                binding?.textView115?.typeface = bold
                binding?.textView116?.typeface = regular
                binding?.textView118?.typeface = bold

                binding?.textCancel?.typeface = regular
                binding?.textTimeLeft?.typeface = regular
                binding?.textTimeToLeft?.typeface = regular
                binding?.txtProceed?.typeface = bold

            }
        }
        setContentView(view)

        //AppBar Hide
        Constant().appBarHide(this@SummeryActivity)

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
                bookingId,
                preferences.getString(Constant.USER_ID).toString()
            )
        )

        try {
            from = intent.getStringExtra("From").toString()
            bookType = intent.getStringExtra("BOOKING").toString()
            type = intent.getStringExtra("TYPE").toString()

            sessionId = intent.getStringExtra("SESSION_ID").toString()
            cinemaId = intent.getStringExtra("CINEMA_ID").toString()
            transId = intent.getStringExtra("TRANS_ID").toString()
            broadcastReceiver = MyReceiver()

        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (bookType == "BOOKING") {
            println("SummeryPageIntentResultBooking21-----yes")
            textView111?.show()
            textView112?.show()
            resendTimer()
        } else {
            println("SummeryPageIntentResultFood21-----yes")
            textView111?.invisible()
            textView112?.invisible()
        }

        broadcastIntent()
        movedNext()
    }

    private fun broadcastIntent() {
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun cancelDialog() {
        mDialog = Dialog(this).also {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
            it.setContentView(R.layout.cancel_dialog)
            it.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            it.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.window!!.attributes.windowAnimations = R.style.DialogAnimation
            it.window!!.setGravity(Gravity.BOTTOM)
            it.show()
            it.consSure?.setOnClickListener {
                Constant.IntentKey.TimerExtandCheck = true
                Constant.IntentKey.TimerExtand = 90
                Constant.IntentKey.TimerTime = 360
                cancelTrans(CancelTransRequest(bookingId, transId))
                if (bookType == "FOOD") {
                    Constant.IntentKey.DialogShow = true
                    val intent = Intent(this, HomeActivity::class.java)
                    Constant.IntentKey.OPEN_FROM = 0
                    startActivity(intent)
                }
                finish()
            }
            it.negative_btn?.setOnClickListener { it2 ->
                it.dismiss()
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
                                        bookingIdNEw = it.data.output.bookingId
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
                            loader = LoaderDialog.getInstance(this,layoutInflater)
                            loader?.show()
                        }
                    }
                }
            }
    }

    private fun movedNext() {
        binding?.txtProceed?.setOnClickListener {
            try {
                val intent = Intent(this, PaymentListActivity::class.java)
                intent.putExtra("CINEMA_ID", cinemaId)
                intent.putExtra("SESSION_ID", sessionId)
                intent.putExtra("TRANS_ID", transId)
                intent.putExtra("bookingId", bookingIdNEw)
                intent.putExtra("BOOKING", bookType)
                intent.putExtra("image", image)
                intent.putExtra("paidPrice", paidPrice)
                Constant.IntentKey.TimerTime = timeCount
                startActivity(intent)
                finish()

                println(
                    "ListOfIdIntent-------->${cinemaId},--${sessionId}" +
                            ",--${transId},---${bookingIdNEw},-----${bookType}," +
                            "-----${image},----${paidPrice},--------${timeCount}"
                )

            } catch (e: Exception) {
                println("ListOfIdIntentError---->${e.message}")
            }

        }

        binding?.viewCancel?.setOnClickListener {
            cancelDialog()
        }

        imgs_back.setOnClickListener {
            cancelDialog()
        }

        binding?.viewFood?.setOnClickListener {

            if (recyclerview_food_chekout.visibility == View.GONE) {
                icon_down_arrow.setImageResource(R.drawable.arrow_up)
                recyclerview_food_chekout.visibility = View.VISIBLE

            } else {

                icon_down_arrow.setImageResource(R.drawable.arrow_down)
                recyclerview_food_chekout.visibility = View.GONE
            }

//            if (!up) {
//                up = true
//                icon_down_arrow.setImageResource(R.drawable.ic_icons_arrow_down)
//                recyclerview_food_chekout.visibility = View.GONE
//            } else {
//                up = false
//                icon_down_arrow.setImageResource(R.drawable.arrow_up)
//                recyclerview_food_chekout.visibility = View.VISIBLE
//            }


        }
    }

    @SuppressLint("SetTextI18n")
    private fun retrieveSummaryResponse(output: TicketSummaryResponse.Output) {
        payModeList()
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
            Glide.with(this)
                .load(output.posterhori)
                .placeholder(R.drawable.food_final_icon)
                .into(binding?.imageView6!!)

            ticketPage.hide()
            vie109.invisible()
            priceView.hide()
            binding?.view?.visibility = View.INVISIBLE
            totalPrice = output.totalTicketPrice
            binding?.priceUi?.hide()
            checkout_food_include.show()
            binding?.view1Line1?.hide()
            foodViewCheck.hide()

        } else {
            vie109.show()

            ticketPage.show()
            priceView.show()
            foodViewCheck.show()
            binding?.view1Line1?.show()
            binding?.priceUi?.hide()
            checkout_food_include.hide()
            image = output.posterhori
            Glide.with(this).load(output.posterhori).placeholder(R.drawable.bombshell)
                .into(binding?.imageView6!!)
            summary_name_movie.text = output.moviename
            txt_screen.text = output.screenId
            text_location_names.text = output.cinemaname
            text_times2.text = output.experience
            text_date_name.text = output.showDate
            textView19.text = output.showTime

            val layoutManager = FlexboxLayoutManager(this)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager.alignItems = AlignItems.STRETCH
            val adapter = SummerySeatListAdapter(output.seatsArr, this)
            seatList.layoutManager = layoutManager
            seatList.adapter = adapter


            text_kds.text = output.ticketPrice
            totalPrice = output.totalTicketPrice
            text_kd_total.text = output.totalTicketPrice
            paidPrice = output.totalPrice
            totalPrice = output.totalPrice
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

            val ratingColor = output.ratingColor
            summary_censor.setBackgroundColor(Color.parseColor(ratingColor))
        }

    }

    override fun onTypeFaceSeatList(seatNumber: TextView) {
        seatNumber1 = seatNumber
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

        binding?.textFood?.hide()
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
                false)

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
                                    if (!this@SummeryActivity.isFinishing) {
                                        //show dialog
                                        dialog.show()
                                    }

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
        try {

            alertDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

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


//    private fun paymentDialog() {
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.paymnet_info_dialog)
//        dialog.window?.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
//        dialog.window?.setGravity(Gravity.BOTTOM)
//        dialog.show()
//        val imageView71 = dialog.findViewById<ImageView>(R.id.imageView71)
//        val recyclerTicketPrice = dialog.findViewById<RecyclerView>(R.id.recyclerTicketPrice)
//
//        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
//        val adapter = ItemInfoPopupAdapter(this, outputlist!!)
//        recyclerTicketPrice.layoutManager = gridLayout
//        recyclerTicketPrice.adapter = adapter
//
//        imageView71.setOnClickListener {
//            dialog.dismiss()
//        }
//    }

}