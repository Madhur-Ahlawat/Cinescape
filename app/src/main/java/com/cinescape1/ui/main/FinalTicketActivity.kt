package com.cinescape1.ui.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.LayoutInflater
import android.view.View
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
import com.cinescape1.data.models.responseModel.TicketSummaryResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityFinalTicketBinding
import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.viewModels.FinalTicketViewModel
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
import kotlinx.android.synthetic.main.checkout_booking_confirm_alert2_include.recyclerview_food_details
import kotlinx.android.synthetic.main.checkout_booking_confirm_alert3_include.*
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
                                        binding?.uiFinalTaket?.show()

//                                        binding?.cardUi?.show()
                                        binding?.successConstraintLayout?.show()
                                        binding?.uiFinalTaket?.show()
                                        val runnable = Runnable {
                                            binding?.successConstraintLayout?.hide()
                                            binding?.imageQrCode?.show()
                                            binding?.cardUi?.show()
                                        }
                                        val handler = Handler(Looper.getMainLooper())
                                        handler.postDelayed(runnable, 3000)


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

        val runnable = Runnable {
            binding?.successConstraintLayout?.hide()
            binding?.imageQrCode?.show()
            binding?.cardUi?.show()
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(runnable, 3000)
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
//        viewPager.offscreenPageLimit = 1
        if (output.bookingType == "BOOKING") {
            if (output.concessionFoods.isNotEmpty()) {
                println("checkCase--->1---New")
                layouts.add(R.layout.checkout_booking_confirm_alert_include)
                layouts.add(R.layout.checkout_booking_confirm_alert2_include)
                layouts.add(R.layout.checkout_booking_confirm_alert3_include)

            } else {
                println("checkCase--->2---New")
                layouts.add(R.layout.checkout_booking_confirm_alert_include)
            }
        } else {
            println("checkCase--->3---New")
            layouts.add(R.layout.checkout_booking_confirm_alert2_include)
            layouts.add(R.layout.checkout_booking_confirm_alert3_include)
        }

        val myViewPagerAdapter = SliderFoodConfirmViewPgweAdapter(layouts, this)
        viewPager.adapter = myViewPagerAdapter
        viewPager.currentItem = 0
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                println("onPageSelected----"+position)
                if (output.bookingType == "BOOKING") {
                    if (output.concessionFoods.isNotEmpty()) {
                        if (position==2) {
                            binding?.layoutDots?.setupWithViewPager(viewPager, true)
                            textView135.text = output.bookingId
                            //ticket
                            textView125.text = output.bookingId
                            //food
                            textView128.text =
                                output.concessionFoods.size.toString() + getString(R.string.item)
                            //referenceId
                            textView131.text = output.referenceId
                            //track No
                            textView133.text = output.trackId
                            //transaction Date Time
                            textView135.text = output.showTime
                            //payDone
                            textView136.text = output.payDone
                            //payTotal
                            textView137.text = output.totalPrice
                        }

//                //include 2
//                // Set Food
//                    if (output.bookingType == "BOOKING") {
//                        binding?.layoutDots?.show()
//                    } else {
//                        binding?.layoutDots?.hide()
//                    }

                        if (position==1) {
                            textView51.text = getString(R.string.price_kd) + output.ticketPrice
                            text_wallet1.text = output.payDone
                            text_bookin_id_no1.text = output.bookingId

                            if (!output.foodPickup) {
                                textFoodPicUp.hide()
                            } else {
                                textFoodPicUp.show()
                            }
                            text_kd_total_ticket_price1.text = output.totalPrice
                            textFoodPicUp.paintFlags =
                                textFoodPicUp.paintFlags or Paint.UNDERLINE_TEXT_FLAG

                            textFoodPicUp.setOnClickListener {
                                val mDialogView =
                                    LayoutInflater.from(this@FinalTicketActivity)
                                        .inflate(R.layout.food_pickup_dialog, null)
                                val mBuilder =
                                    AlertDialog.Builder(this@FinalTicketActivity, R.style.NewDialog)
                                        .setView(mDialogView)
                                val mAlertDialog = mBuilder.show()
                                mAlertDialog.show()
                                mAlertDialog.window?.setBackgroundDrawableResource(R.color.black70)
                                val closeDialog =
                                    mDialogView.findViewById<TextView>(R.id.close_dialog)
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

                            val gridLayout =
                                GridLayoutManager(
                                    this@FinalTicketActivity,
                                    1,
                                    GridLayoutManager.VERTICAL,
                                    false
                                )
                            recyclerview_food_details.layoutManager = LinearLayoutManager(this@FinalTicketActivity)
                            val adapter2 =
                                AdapterCheckoutConFirmFoodDetail(
                                    this@FinalTicketActivity,
                                    output.concessionFoods
                                )
                            recyclerview_food_details.layoutManager = gridLayout
                            recyclerview_food_details.adapter = adapter2
                            adapter2.loadNewData(output.concessionFoods)
                        }

                        //include 1
                        if (position==0) {
                            if (!output.foodPickup) {
                                text_how_picup.hide()
                            } else {
                                text_how_picup.hide()
                            }
                            categoryName.text = output.category
                            binding?.layoutDots?.show()
                            text_bookin_id_no.text = output.kioskId
                            text_name_movie.text = output.moviename
                            text_location_names.text = output.cinemaname
                            text_types.text = output.mcensor
                            txt_scrrens.text = output.screenId
                            tv_screenx.text = output.experience
                            txt_date.isSelected = true
                            txt_date.text = output.showDate + " " + output.showTime
                            text_wallet.text = output.payDone
                            text_kd_total_ticket_price.text = output.totalTicketPrice
                            //Recycler Seat
                            val layoutManager = FlexboxLayoutManager(this@FinalTicketActivity)
                            layoutManager.flexDirection = FlexDirection.ROW
                            layoutManager.justifyContent = JustifyContent.FLEX_START
                            layoutManager.alignItems = AlignItems.STRETCH
                            val adapter = SeatListAdapter(output.seatsArr)
                            seats_list.setHasFixedSize(true)
                            seats_list.layoutManager = layoutManager
                            seats_list.adapter = adapter
                        }

                    } else {

                        println("checkCase--->2")
                        //include 1
                        if (!output.foodPickup) {
                            text_how_picup.hide()
                        } else {
                            text_how_picup.hide()
                        }
                        binding?.layoutDots?.show()
                        text_bookin_id_no.text = output.kioskId
                        text_name_movie.text = output.moviename
                        text_location_names.text = output.cinemaname
                        txt_scrrens.text = output.screenId
                        tv_screenx.text = output.experience
                        txt_date.isSelected = true
                        txt_date.text = output.showDate + " " + output.showTime
                        text_wallet.text = output.payDone
                        text_kd_total_ticket_price.text = output.totalTicketPrice
                        //Recycler Seat
                        val layoutManager = FlexboxLayoutManager(this@FinalTicketActivity)
                        layoutManager.flexDirection = FlexDirection.ROW
                        layoutManager.justifyContent = JustifyContent.FLEX_START
                        layoutManager.alignItems = AlignItems.STRETCH
                        val adapter = SeatListAdapter(output.seatsArr)
                        seats_list.setHasFixedSize(true)
                        seats_list.layoutManager = layoutManager
                        seats_list.adapter = adapter

                        println("checkCase--->3")
                        //include 2
                        // Set Food

                    }
                }else{
                    textView51.text = getString(R.string.price_kd) + output.ticketPrice
                    text_wallet1.text = output.payDone

                    if (!output.foodPickup) {
                        textFoodPicUp.hide()
                    } else {
                        textFoodPicUp.show()
                    }
                    text_kd_total_ticket_price1.text = output.totalPrice
                    textFoodPicUp.paintFlags = textFoodPicUp.paintFlags or Paint.UNDERLINE_TEXT_FLAG

                    textFoodPicUp.setOnClickListener {
                        val mDialogView =
                            LayoutInflater.from(this@FinalTicketActivity).inflate(R.layout.food_pickup_dialog, null)
                        val mBuilder =
                            AlertDialog.Builder(this@FinalTicketActivity, R.style.NewDialog).setView(mDialogView)
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
                    val gridLayout =
                        GridLayoutManager(this@FinalTicketActivity, 1, GridLayoutManager.VERTICAL, false)
                    recyclerview_food_details.layoutManager = LinearLayoutManager(this@FinalTicketActivity)
                    val adapter =
                        AdapterCheckoutConFirmFoodDetail(this@FinalTicketActivity, output.concessionFoods)
                    recyclerview_food_details.layoutManager = gridLayout
                    recyclerview_food_details.adapter = adapter
                    adapter.loadNewData(output.concessionFoods)

                    //Include 3
                    textView135.text = output.bookingId
                    //ticket
                    textView125.text = output.bookingId
                    //food
                    textView128.text = output.concessionFoods.size.toString() + getString(R.string.item)
                    //referenceId
                    textView131.text = output.referenceId
                    //track No
                    textView133.text = output.trackId
                    //transaction Date Time
                    textView135.text = output.showTime
                    //payDone
                    textView136.text = output.payDone
                    //payTotal
                    textView137.text = output.totalPrice

                }
            }
        })

        if (output.bookingType == "BOOKING") {
            if (output.concessionFoods.isNotEmpty()) {
                if (viewPager.currentItem == 2) {
                    binding?.layoutDots?.setupWithViewPager(viewPager, true)
                    textView135.text = output.bookingId
                    //ticket
                    textView125.text = output.bookingId
                    //food
                    textView128.text =
                        output.concessionFoods.size.toString() + getString(R.string.item)
                    //referenceId
                    textView131.text = output.referenceId
                    //track No
                    textView133.text = output.trackId
                    //transaction Date Time
                    textView135.text = output.showTime
                    //payDone
                    textView136.text = output.payDone
                    //payTotal
                    textView137.text = output.totalPrice
                }

//                //include 2
//                // Set Food
//                    if (output.bookingType == "BOOKING") {
//                        binding?.layoutDots?.show()
//                    } else {
//                        binding?.layoutDots?.hide()
//                    }

                if (viewPager.currentItem==1) {
                    textView51.text = getString(R.string.price_kd) + output.ticketPrice
                    text_wallet1.text = output.payDone
                    text_bookin_id_no1.text = output.bookingId

                    if (!output.foodPickup) {
                        textFoodPicUp.hide()
                    } else {
                        textFoodPicUp.show()
                    }
                    text_kd_total_ticket_price1.text = output.totalPrice
                    textFoodPicUp.paintFlags =
                        textFoodPicUp.paintFlags or Paint.UNDERLINE_TEXT_FLAG

                    textFoodPicUp.setOnClickListener {
                        val mDialogView =
                            LayoutInflater.from(this@FinalTicketActivity)
                                .inflate(R.layout.food_pickup_dialog, null)
                        val mBuilder =
                            AlertDialog.Builder(this@FinalTicketActivity, R.style.NewDialog)
                                .setView(mDialogView)
                        val mAlertDialog = mBuilder.show()
                        mAlertDialog.show()
                        mAlertDialog.window?.setBackgroundDrawableResource(R.color.black70)
                        val closeDialog =
                            mDialogView.findViewById<TextView>(R.id.close_dialog)
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

                    val gridLayout =
                        GridLayoutManager(
                            this@FinalTicketActivity,
                            1,
                            GridLayoutManager.VERTICAL,
                            false
                        )
                    recyclerview_food_details.layoutManager = LinearLayoutManager(this@FinalTicketActivity)
                    val adapter2 =
                        AdapterCheckoutConFirmFoodDetail(
                            this@FinalTicketActivity,
                            output.concessionFoods
                        )
                    recyclerview_food_details.layoutManager = gridLayout
                    recyclerview_food_details.adapter = adapter2
                    adapter2.loadNewData(output.concessionFoods)
                }

                //include 1
                if (viewPager.currentItem==0) {
                    if (!output.foodPickup) {
                        text_how_picup.hide()
                    } else {
                        text_how_picup.hide()
                    }
                    categoryName.text = output.category
                    binding?.layoutDots?.show()
                    text_bookin_id_no.text = output.kioskId
                    text_name_movie.text = output.moviename
                    text_location_names.text = output.cinemaname
                    text_types.text = output.mcensor
                    txt_scrrens.text = output.screenId
                    tv_screenx.text = output.experience
                    txt_date.isSelected = true
                    txt_date.text = output.showDate + " " + output.showTime
                    text_wallet.text = output.payDone
                    text_kd_total_ticket_price.text = output.totalTicketPrice
                    //Recycler Seat
                    val layoutManager = FlexboxLayoutManager(this@FinalTicketActivity)
                    layoutManager.flexDirection = FlexDirection.ROW
                    layoutManager.justifyContent = JustifyContent.FLEX_START
                    layoutManager.alignItems = AlignItems.STRETCH
                    val adapter = SeatListAdapter(output.seatsArr)
                    seats_list.setHasFixedSize(true)
                    seats_list.layoutManager = layoutManager
                    seats_list.adapter = adapter
                }

            } else {

                println("checkCase--->2")
                //include 1
                if (!output.foodPickup) {
                    text_how_picup.hide()
                } else {
                    text_how_picup.hide()
                }
                binding?.layoutDots?.show()
                text_bookin_id_no.text = output.kioskId
                text_name_movie.text = output.moviename
                text_location_names.text = output.cinemaname
                txt_scrrens.text = output.screenId
                tv_screenx.text = output.experience
                txt_date.isSelected = true
                txt_date.text = output.showDate + " " + output.showTime
                text_wallet.text = output.payDone
                text_kd_total_ticket_price.text = output.totalTicketPrice
                //Recycler Seat
                val layoutManager = FlexboxLayoutManager(this@FinalTicketActivity)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager.alignItems = AlignItems.STRETCH
                val adapter = SeatListAdapter(output.seatsArr)
                seats_list.setHasFixedSize(true)
                seats_list.layoutManager = layoutManager
                seats_list.adapter = adapter

                println("checkCase--->3")
                //include 2
                // Set Food

            }
        }else{
            textView51.text = getString(R.string.price_kd) + output.ticketPrice
            text_wallet1.text = output.payDone

            if (!output.foodPickup) {
                textFoodPicUp.hide()
            } else {
                textFoodPicUp.show()
            }
            text_kd_total_ticket_price1.text = output.totalPrice
            textFoodPicUp.paintFlags = textFoodPicUp.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            textFoodPicUp.setOnClickListener {
                val mDialogView =
                    LayoutInflater.from(this@FinalTicketActivity).inflate(R.layout.food_pickup_dialog, null)
                val mBuilder =
                    AlertDialog.Builder(this@FinalTicketActivity, R.style.NewDialog).setView(mDialogView)
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
            val gridLayout =
                GridLayoutManager(this@FinalTicketActivity, 1, GridLayoutManager.VERTICAL, false)
            recyclerview_food_details.layoutManager = LinearLayoutManager(this@FinalTicketActivity)
            val adapter =
                AdapterCheckoutConFirmFoodDetail(this@FinalTicketActivity, output.concessionFoods)
            recyclerview_food_details.layoutManager = gridLayout
            recyclerview_food_details.adapter = adapter
            adapter.loadNewData(output.concessionFoods)

            //Include 3
            textView135.text = output.bookingId
            //ticket
            textView125.text = output.bookingId
            //food
            textView128.text = output.concessionFoods.size.toString() + getString(R.string.item)
            //referenceId
            textView131.text = output.referenceId
            //track No
            textView133.text = output.trackId
            //transaction Date Time
            textView135.text = output.showTime
            //payDone
            textView136.text = output.payDone
            //payTotal
            textView137.text = output.totalPrice

        }



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
                                            binding?.imageQrCode?.show()
                                            binding?.cardUi?.show()
                                            binding?.uiFinalTaket?.show()
//                                        val runnable = Runnable {
//                                            binding?.successConstraintLayout?.hide()
//                                            binding?.imageQrCode?.show()
//                                            binding?.cardUi?.show()
//                                        }
//                                        val handler = Handler(Looper.getMainLooper())
//                                        handler.postDelayed(runnable, 3000)

                                            retrieveBookedResponse(it.data.output)
                                        } catch (e: Exception) {
                                            e.printStackTrace()
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


