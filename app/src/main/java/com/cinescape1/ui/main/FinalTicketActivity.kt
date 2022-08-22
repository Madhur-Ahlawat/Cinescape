package com.cinescape1.ui.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.FinalTicketRequest
import com.cinescape1.data.models.requestModel.MySingleTicketRequest
import com.cinescape1.data.models.requestModel.ResendRequest
import com.cinescape1.data.models.responseModel.TicketSummaryResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityFinalTicketBinding
import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.viewModels.FinalTicketViewModel
import com.cinescape1.ui.main.views.FoodActivity
import com.cinescape1.ui.main.views.HomeActivity
import com.cinescape1.ui.main.views.adapters.SeatListAdapter
import com.cinescape1.ui.main.views.adapters.checkoutAdapter.AdapterCheckoutConFirmFoodDetail
import com.cinescape1.ui.main.views.adapters.sliderAdapter.SliderFoodConfirmViewPgweAdapter
import com.cinescape1.utils.*
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.cancel_dialog.view.*
import kotlinx.android.synthetic.main.checkout_booking_confirm_alert2_include.*
import kotlinx.android.synthetic.main.checkout_booking_confirm_alert_include.*
import javax.inject.Inject

@ActivityScoped
class FinalTicketActivity : DaggerAppCompatActivity() {
    private var loader: LoaderDialog? = null

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory
    private var binding: ActivityFinalTicketBinding? = null

    @Inject
    lateinit var preferences: AppPreferences
    private val finalTicketViewModel: FinalTicketViewModel by viewModels { viewmodelFactory }
    var bookingId = ""
    var transId = ""
    var transId1 = 0
    var qrData = ""
    private var qrBitmap: Bitmap? = null
    private var bookType: String = ""
    private var from: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityFinalTicketBinding.inflate(layoutInflater, null, false)
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
        //AppBar Hide
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
        bookingId = intent.getStringExtra(Constant.IntentKey.BOOKING_ID).toString()
        transId = intent.getStringExtra(Constant.IntentKey.TRANSACTION_ID).toString()
        bookType = intent.getStringExtra(Constant.IntentKey.BOOK_TYPE).toString()

        println("TransactionId--->${transId}")
        if (transId.isBlank() ||
            transId.isEmpty() ||
            transId.isEmpty() ||
            !transId.isDigitsOnly() ||
            transId.toIntOrNull() == null
        ) {
            println("transId-------${transId}")
        } else {
            transId1 = transId.toInt()
            println("transId1-------${transId1}")
        }

        from = intent.getStringExtra("FROM").toString()

        if (from == "MTicket") {
            setMySingleTicket()
        } else {
            printTicket(FinalTicketRequest(bookingId, transId.toInt()))
        }

        binding?.imagShare?.setColorFilter(this.getColor(R.color.white))
        binding?.tvShare?.setTextColor(this.getColor(R.color.white))
        movedNext()
    }

    private fun movedNext() {
        val someText = "Share Data"
        binding?.view29?.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, someText)
            startActivity(Intent.createChooser(shareIntent, "Share using"))
        }
        binding?.imageView8?.setOnClickListener {
            val intent = Intent(this@FinalTicketActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun printTicket(request: FinalTicketRequest) {
        finalTicketViewModel.tckBooked(request)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        retrieveBookedResponse(it.data.output)
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

    @SuppressLint("SetTextI18n")
    private fun retrieveBookedResponse(output: TicketSummaryResponse.Output) {

        // Make Qr data
        qrData = output.qr
        qrBitmap = Constant().createQrCode(output.qr)
        binding?.imageQrCode?.setImageBitmap(qrBitmap)

        println("imageUrl---->${output.posterhori}")
        //Set Header image
        binding?.imageHead?.let {
            Glide.with(this)
                .load(output.posterhori)
                .placeholder(R.drawable.cinema)
                .into(it)
        }
        binding?.imageView49?.let {
            Glide.with(this)
                .load(output.posterhori)
                .placeholder(R.drawable.cinema)
                .into(it)

        }

        binding?.uiFinalTaket?.show()

        val viewPager: ViewPager = findViewById(R.id.viewPager_slider_layout)
        val layouts: ArrayList<Int> = ArrayList()
        viewPager.offscreenPageLimit = 1
        if (output.bookingType == "BOOKING") {
            if (output.concessionFoods.isNotEmpty()) {
                layouts.add(R.layout.checkout_booking_confirm_alert_include)
                layouts.add(R.layout.checkout_booking_confirm_alert2_include)
            } else {
                layouts.add(R.layout.checkout_booking_confirm_alert_include)
            }
        } else {
            layouts.add(R.layout.checkout_booking_confirm_alert2_include)
        }

        val myViewPagerAdapter = SliderFoodConfirmViewPgweAdapter(layouts, this)
        viewPager.adapter = myViewPagerAdapter


        if (output.bookingType == "BOOKING") {
            try {
                binding?.layoutDots?.show()
                if (output.concessionFoods.isNotEmpty()) {
                    binding?.layoutDots?.setupWithViewPager(binding?.viewPagerSliderLayout, true)
                }
                text_bookin_id_no.text = output.kioskId
                text_name_movie.text = output.moviename
                text_location_names.text = output.cinemaname
                txt_date.isSelected = true
                txt_date.text = output.showDate + " " + output.showTime
                text_wallet.text = output.payDone

                if (output.category.isNullOrEmpty()) {
                    tv_category_title.invisible()
                } else {
                    tv_category_title.show()
                    categoryName.isSelected = true
                    categoryName.text = output.category
                }

                text_kd_total_ticket_price.text = output.totalTicketPrice
                text_types.text = output.mcensor

                val layoutManager = FlexboxLayoutManager(this)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager.alignItems = AlignItems.STRETCH
                val adapter = SeatListAdapter(output.seatsArr)

                if (!output.foodPickup){
                    text_how_picup.hide()
                }else{
                    text_how_picup.hide()
                }
                seats_list.setHasFixedSize(true)
                seats_list.layoutManager = layoutManager
                seats_list.adapter = adapter
                text_how_picup.paintFlags = text_how_picup.paintFlags or Paint.UNDERLINE_TEXT_FLAG

                text_how_picup.setOnClickListener {
                    val mDialogView =
                        LayoutInflater.from(this).inflate(R.layout.food_pickup_dialog, null)
                    val mBuilder = AlertDialog.Builder(this, R.style.NewDialog).setView(mDialogView)
                    val mAlertDialog = mBuilder.show()
                    mAlertDialog.show()
                    mAlertDialog.window?.setBackgroundDrawableResource(R.color.black70)
                    val closeDialog = mDialogView.findViewById<TextView>(R.id.close_dialog)
                    val text = mAlertDialog.findViewById<TextView>(R.id.textView105)

                    text?.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(output.pickupInfo, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(output.pickupInfo)
                    }
                    closeDialog.setOnClickListener {
                        mAlertDialog.dismiss()
                    }

                }

                if (!output.cancelReserve) {
//                    textView48.show()
                    textView48.invisible()
                } else {
                    textView48.invisible()
                }

                if (!output.addFood) {
                    textView49.show()
                } else {
                    textView49.invisible()
                }

                //Cancel Reservation
                textView48.setOnClickListener {
                    cancelReservationDialog()
                }

                //Add Food
                textView49.setOnClickListener {
                    startActivity(
                        Intent(this, FoodActivity::class.java)
                            .putExtra("CINEMA_ID", output.cinemacode)
                            .putExtra("BOOKING", "FOOD")
                            .putExtra("type", "0")

                    )

                }

                //Send Mail
                textView50.setOnClickListener {
                    resendMail(
                        ResendRequest(
                            bookingId,
                            output.bookingType,
                            transId,
                            preferences.getString(Constant.USER_ID).toString()
                        )
                    )
                }

                //Resend Mail
                imageView28.setOnClickListener {
                    resendMail(
                        ResendRequest(
                            bookingId,
                            output.bookingType,
                            transId,
                            preferences.getString(Constant.USER_ID).toString()
                        )
                    )
                }
            } catch (e: Exception) {
                println("exception---->${e.printStackTrace()}")
            }
        } else {
            try {
//FoodCase
                if (!output.cancelReserve) {
                    imageView31.hide()
//                    imageView31.show()
                } else {
                    imageView31.invisible()
                }

                if (!output.addFood) {
                    imageView32.hide()
//                    imageView32.show()
                } else {
                    imageView32.invisible()
                }

                //Cancel Food
                imageView31.setOnClickListener {
                    cancelReservationDialog()
                }

            } catch (e: Exception) {
                println("exception2--->${e.message}")
            }
        }


        // Set Food
        if (output.concessionFoods.isNotEmpty()) {
            if (output.bookingType == "BOOKING") {
                binding?.layoutDots?.show()
            } else {
                binding?.layoutDots?.hide()
            }
            textView51.text = getString(R.string.price_kd) + output.ticketPrice
            text_bookin_id_no1.text = output.kioskId
            text_wallet1.text = output.payDone

            if(!output.foodPickup){
                textFoodPicUp.hide()
            }else{
                textFoodPicUp.show()
            }
            text_kd_total_ticket_price1.text = output.totalPrice
            textFoodPicUp.paintFlags = textFoodPicUp.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            textFoodPicUp.setOnClickListener {
                val mDialogView =
                    LayoutInflater.from(this).inflate(R.layout.food_pickup_dialog, null)
                val mBuilder = AlertDialog.Builder(this, R.style.NewDialog).setView(mDialogView)
                val mAlertDialog = mBuilder.show()
                mAlertDialog.show()
                mAlertDialog.window?.setBackgroundDrawableResource(R.color.black70)
                val closeDialog = mDialogView.findViewById<TextView>(R.id.close_dialog)
                val text = mAlertDialog.findViewById<TextView>(R.id.textView105)

                text?.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(output.pickupInfo, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(output.pickupInfo)
                }
                closeDialog.setOnClickListener {
                    mAlertDialog.dismiss()
                }

            }

            setFinalFoodItemAdapter(output.concessionFoods)
        }

        //Resend mail
        imageView32.setOnClickListener {
            resendMail(
                ResendRequest(
                    bookingId,
                    output.bookingType,
                    transId,
                    preferences.getString(Constant.USER_ID).toString()
                )
            )
        }

    }

    private fun cancelReservationDialog() {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView: View =
            LayoutInflater.from(this).inflate(R.layout.cancel_dialog, viewGroup, false)
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setView(dialogView)
        val alertDialog: android.app.AlertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        alertDialog.show()

        dialogView.consSure?.setOnClickListener {
            alertDialog.dismiss()
            cancelReservation(FinalTicketRequest(bookingId, transId.toInt()))
        }

        dialogView.negative_btn?.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun resendMail(resendRequest: ResendRequest) {
        finalTicketViewModel.resendMail(resendRequest)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        val dialog = OptionDialog(this,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            it.data.msg,
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {
                                            },
                                            negativeClick = {
                                            })
                                        dialog.show()
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

    private fun cancelReservation(finalTicketRequest: FinalTicketRequest) {
        finalTicketViewModel.cancelReservation(finalTicketRequest)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        val dialog = OptionDialog(this,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            it.data.msg,
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {
                                            },
                                            negativeClick = {
                                            })
                                        dialog.show()
                                    } catch (e: Exception) {
                                        val dialog = OptionDialog(this,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            it.data.msg.toString(),
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {
                                            },
                                            negativeClick = {
                                            })
                                        dialog.show()
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

    private fun setFinalFoodItemAdapter(concessionFoods: List<TicketSummaryResponse.ConcessionFood>) {
        val gridLayout =
            GridLayoutManager(this@FinalTicketActivity, 1, GridLayoutManager.VERTICAL, false)
        recyclerview_food_details.layoutManager = LinearLayoutManager(this)
        val adapter = AdapterCheckoutConFirmFoodDetail(this@FinalTicketActivity, concessionFoods)
        recyclerview_food_details.layoutManager = gridLayout
        recyclerview_food_details.adapter = adapter
        adapter.loadNewData(concessionFoods)
    }

    private fun setMySingleTicket() {
        mySingleTicket(
            MySingleTicketRequest(
                bookingId,
                bookType,
                transId1,
                preferences.getString(Constant.USER_ID)!!
            )
        )
    }

    private fun mySingleTicket(request: MySingleTicketRequest) {
        finalTicketViewModel.getMySingleTicketData(request)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        retrieveBookedResponse(it.data.output)
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}


