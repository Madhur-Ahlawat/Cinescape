package com.cinescape1.ui.main.views.finalTicket

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.*
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.*
import com.cinescape1.data.models.responseModel.TicketSummaryResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityFinalTicketBinding
import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.finalTicket.adapter.FinalTicketParentAdapter
import com.cinescape1.ui.main.views.finalTicket.model.FinalTicketLocalModel
import com.cinescape1.ui.main.views.finalTicket.viewModel.FinalTicketViewModel
import com.cinescape1.ui.main.views.food.FoodActivity
import com.cinescape1.ui.main.views.home.HomeActivity
import com.cinescape1.utils.*
import com.cinescape1.utils.Constant.IntentKey.Companion.BACKFinlTicket
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.cancel_dialog.*
import java.security.MessageDigest
import javax.inject.Inject

@Suppress("DEPRECATION")
@ActivityScoped
class FinalTicketActivity : DaggerAppCompatActivity(),
    FinalTicketParentAdapter.TypeFaceFinalTicket0ne,
    FinalTicketParentAdapter.TypeFaceFinalTicketTwo,
    FinalTicketParentAdapter.TypeFaceFinalTicketThree {

    private var loader: LoaderDialog? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var binding: ActivityFinalTicketBinding? = null

    @Inject
    lateinit var preferences: AppPreferences
    private val finalTicketViewModel: FinalTicketViewModel by viewModels { viewModelFactory }
    private var bookingId = ""
    private var transId = ""
    private var transId1 = 0
    private var qrData = ""
    private var qrBitmap: Bitmap? = null
    private var bookType: String = ""
    private var from: String = ""
    private val finalTicketLocalModel = ArrayList<FinalTicketLocalModel>()
    private var oneBookingId1: TextView? = null
    private var oneTitle1: TextView? = null
    private var oneRating1: TextView? = null
    private var oneLocation1: TextView? = null
    private var oneDateTime1: TextView? = null
    private var oneScreen1: TextView? = null
    private var oneCategoryName1: TextView? = null
    private var onePayMode1: TextView? = null
    private var onePrice1: TextView? = null

    private var twoBookingId1: TextView? = null
    private var twoPickupInfo1: TextView? = null
    private var twoPayMode1: TextView? = null
    private var twoPayPrice1: TextView? = null

    private var threeBookingId1: TextView? = null
    private var threeTicket1: TextView? = null
    private var threeTicketPrice1: TextView? = null
    private var threeFood1: TextView? = null
    private var threeFoodPrice1: TextView? = null
    private var threeReferenceId1: TextView? = null
    private var threeReferenceTxt1: TextView? = null
    private var threeTrackId1: TextView? = null
    private var threeDateTime1: TextView? = null
    private var threePayMode1: TextView? = null

    private var prepareBtn = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityFinalTicketBinding.inflate(layoutInflater, null, false)
        val view = binding?.root

        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
                val regular = ResourcesCompat.getFont(this, R.font.montserrat_regular)
                val bold = ResourcesCompat.getFont(this, R.font.montserrat_bold)
//                val medium = ResourcesCompat.getFont(this, R.font.gess_medium)

                binding?.textBookingAddedWallet?.typeface = regular
                binding?.textBookingSuccess?.typeface = regular
                binding?.tvShare?.typeface = regular
                binding?.textView46?.typeface = regular
                binding?.textView120?.typeface = bold

                oneBookingId1?.typeface = bold
                oneTitle1?.typeface = bold  // heavy
                oneRating1?.typeface = bold  // heavy
                oneLocation1?.typeface = bold
                oneDateTime1?.typeface = bold
                oneScreen1?.typeface = bold
                oneCategoryName1?.typeface = bold
                onePayMode1?.typeface = bold
                onePrice1?.typeface = bold

                twoBookingId1?.typeface = bold
                twoPickupInfo1?.typeface = bold
                twoPayMode1?.typeface = bold
                twoPayPrice1?.typeface = bold

                threeBookingId1?.typeface = bold
                threeTicket1?.typeface = bold
                threeTicketPrice1?.typeface = bold
                threeFood1?.typeface = bold
                threeFoodPrice1?.typeface = bold
                threeReferenceId1?.typeface = bold
                threeReferenceTxt1?.typeface = bold
                threeTrackId1?.typeface = bold
                threeDateTime1?.typeface = bold
                threePayMode1?.typeface = bold
            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(this, "en")
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(this, R.font.sf_pro_text_heavy)
//                val medium = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)

                binding?.textBookingAddedWallet?.typeface = regular
                binding?.textBookingSuccess?.typeface = regular
                binding?.tvShare?.typeface = regular
                binding?.textView46?.typeface = regular
                binding?.textView120?.typeface = bold

                oneBookingId1?.typeface = bold
                oneTitle1?.typeface = heavy  // heavy
                oneRating1?.typeface = heavy  // heavy
                oneLocation1?.typeface = bold
                oneDateTime1?.typeface = bold
                oneScreen1?.typeface = bold
                oneCategoryName1?.typeface = bold
                onePayMode1?.typeface = bold
                onePrice1?.typeface = bold

                twoBookingId1?.typeface = bold
                twoPickupInfo1?.typeface = bold
                twoPayMode1?.typeface = bold
                twoPayPrice1?.typeface = bold

                threeBookingId1?.typeface = bold
                threeTicket1?.typeface = bold
                threeTicketPrice1?.typeface = bold
                threeFood1?.typeface = bold
                threeFoodPrice1?.typeface = bold
                threeReferenceId1?.typeface = bold
                threeReferenceTxt1?.typeface = bold
                threeTrackId1?.typeface = bold
                threeDateTime1?.typeface = bold
                threePayMode1?.typeface = bold

            }
            else -> {
                LocaleHelper.setLocale(this, "en")
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(this, R.font.sf_pro_text_heavy)
//                val medium = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)

                binding?.textBookingAddedWallet?.typeface = regular
                binding?.textBookingSuccess?.typeface = regular
                binding?.tvShare?.typeface = regular
                binding?.textView46?.typeface = regular
                binding?.textView120?.typeface = bold

                oneBookingId1?.typeface = bold
                oneTitle1?.typeface = heavy  // heavy
                oneRating1?.typeface = heavy  // heavy
                oneLocation1?.typeface = bold
                oneDateTime1?.typeface = bold
                oneScreen1?.typeface = bold
                oneCategoryName1?.typeface = bold
                onePayMode1?.typeface = bold
                onePrice1?.typeface = bold

                twoBookingId1?.typeface = bold
                twoPickupInfo1?.typeface = bold
                twoPayMode1?.typeface = bold
                twoPayPrice1?.typeface = bold

                threeBookingId1?.typeface = bold
                threeTicket1?.typeface = bold
                threeTicketPrice1?.typeface = bold
                threeFood1?.typeface = bold
                threeFoodPrice1?.typeface = bold
                threeReferenceId1?.typeface = bold
                threeReferenceTxt1?.typeface = bold
                threeTrackId1?.typeface = bold
                threeDateTime1?.typeface = bold
                threePayMode1?.typeface = bold
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

        if (transId.isBlank() || transId.isEmpty() || transId.isEmpty() || !transId.isDigitsOnly() || transId.toIntOrNull() == null) {
            println("transId-------${transId}")
        } else {
            transId1 = transId.toInt()
            println("transId1-------${transId1}")
        }

        from = intent.getStringExtra("FROM").toString()

        if (from == "MTicket") {
            setMySingleTicket()
        } else {
            printTicket(FinalTicketRequest(bookingId, transId))
        }

        binding?.imagShare?.setColorFilter(this.getColor(R.color.white))
        binding?.tvShare?.setTextColor(this.getColor(R.color.white))
        movedNext()
    }

    private fun movedNext() {
        val someText = "Share Data"
        binding?.share?.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, someText)
            startActivity(Intent.createChooser(shareIntent, "Share using"))
        }

        binding?.imageView8?.setOnClickListener {
            if (intent.getStringExtra("FROM_ACCOUNT") == "account") {
//                onBackPressed()
//                val intent = Intent(this@FinalTicketActivity, HomeActivity::class.java)
                Constant.IntentKey.OPEN_FROM = 1
//                startActivity(intent)
                finish()
            } else {
                Constant.IntentKey.DialogShow = true
                val intent = Intent(this@FinalTicketActivity, HomeActivity::class.java)
                Constant.IntentKey.OPEN_FROM = 0
                startActivity(intent)
                finish()
            }
        }
    }

    private fun printTicket(request: FinalTicketRequest) {
        finalTicketViewModel.tckBooked(request).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                try {
                                    binding?.uiFinalTaket?.show()
                                    binding?.successConstraintLayout?.show()
                                    binding?.uiFinalTaket?.show()
                                    val runnable = Runnable {
                                        binding?.successConstraintLayout?.hide()
                                        binding?.imageQrCode?.show()
                                        binding?.cardPaymentOptionsUi?.show()
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

    @SuppressLint("SetTextI18n")
    private fun retrieveBookedResponse(output: TicketSummaryResponse.Output) {

//bookingId
        binding?.textBookinIdNo?.text = output.kioskId
        //pickupNumber
        binding?.textView167?.text = output.pickUpNumber

        binding?.btnPreparedFood?.setOnClickListener {
            prepareBtn = 1
            foodPickup(
                FoodPrepareRequest(
                    output.bookingId, "", 0, preferences.getString(Constant.USER_ID).toString()
                )
            )
        }

        when (output.food) {
            0 -> {
                binding?.btnPreparedFood?.hide()
                binding?.btnAddFood?.hide()
                binding?.consFoodPickupId?.hide()
            }
            1 -> {
                binding?.btnPreparedFood?.hide()
                binding?.btnAddFood?.show()
                binding?.consFoodPickupId?.hide()
            }
            2 -> {
                binding?.btnPreparedFood?.show()
                binding?.btnAddFood?.hide()
                binding?.consFoodPickupId?.hide()
                binding?.btnPreparedFood?.background =
                    ContextCompat.getDrawable(this@FinalTicketActivity, R.drawable.food_pickup_bg)
                binding?.btnPreparedFood?.isClickable = false
            }
            3 -> {
                binding?.btnPreparedFood?.show()
                binding?.btnAddFood?.hide()
                binding?.consFoodPickupId?.hide()
                binding?.btnPreparedFood?.isClickable = true
            }
            4 -> {
                binding?.btnPreparedFood?.hide()
                binding?.btnAddFood?.hide()
                binding?.consFoodPickupId?.show()
            }
        }

        //ALert Dialog
        binding?.textView166?.setOnClickListener {
            val mDialogView = LayoutInflater.from(this@FinalTicketActivity)
                .inflate(R.layout.food_pickup_dialog, null)
            val mBuilder = AlertDialog.Builder(this@FinalTicketActivity, R.style.NewDialog)
                .setView(mDialogView)
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

//foodAdd Button
        binding?.btnAddFood?.setOnClickListener {
            val intent = Intent(this@FinalTicketActivity, FoodActivity::class.java).putExtra(
                "CINEMA_ID", output.cinemacode
            ).putExtra("BOOKING", "ADDFOOD").putExtra("type", "FOOD")
                .putExtra("typeSkip", "SkipButtonHide")
            startActivity(intent)
        }


        val runnable = Runnable {
            binding?.successConstraintLayout?.hide()
            binding?.imageQrCode?.show()
            binding?.cardPaymentOptionsUi?.show()
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(runnable, 3000)
        // Make Qr data
        qrData = output.qr
        qrBitmap = Constant().createQrCode(output.qr)
        binding?.imageQrCode?.setImageBitmap(qrBitmap)

        //Set Header image
        binding?.imageHead?.let {
            Glide.with(this).load(output.posterhori).placeholder(R.drawable.app_icon).into(it)
        }
        if (output.bookingType == "BOOKING") {
            binding?.imageView49?.let {
                Glide.with(this)
                    .load(output.posterhori)
                    .transform(CutOffLogo())
                    .placeholder(R.drawable.app_icon)
                    .into(it)
            }
        } else {
            binding?.imageView49?.let {
                Glide.with(this)
                    .load(output.posterhori)
                    .transform(CutOffLogo())
                    .placeholder(R.drawable.food_final_icon)
                    .into(it)
            }
        }

        binding?.uiFinalTaket?.show()

        if (output.bookingType == "BOOKING") {
            if (output.concessionFoods.isNotEmpty()) {
                println("checkCase--->1---New")
                finalTicketLocalModel.add(FinalTicketLocalModel(output.bookingType, 1))
                finalTicketLocalModel.add(FinalTicketLocalModel(output.bookingType, 2))
                finalTicketLocalModel.add(FinalTicketLocalModel(output.bookingType, 3))

            } else {
                println("checkCase--->2---New")
                finalTicketLocalModel.add(FinalTicketLocalModel(output.bookingType, 1))
                finalTicketLocalModel.add(FinalTicketLocalModel(output.bookingType, 3))
            }
        } else {

            finalTicketLocalModel.add(FinalTicketLocalModel(output.bookingType, 2))
//          finalTicketLocalModel.add(FinalTicketLocalModel(output.bookingType, 3))
            println("checkCase--->3---New")
        }

        val gridLayout =
            GridLayoutManager(this@FinalTicketActivity, 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerViewFinalTicket?.layoutManager =
            LinearLayoutManager(this@FinalTicketActivity)

        if (prepareBtn == 0) {
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding?.recyclerViewFinalTicket)

            val finalTicketParentAdapter = FinalTicketParentAdapter(
                this@FinalTicketActivity, finalTicketLocalModel, output, this, this, this
            )
            binding?.recyclerViewFinalTicket?.layoutManager = gridLayout
            binding?.recyclerViewFinalTicket?.adapter = finalTicketParentAdapter
            binding?.layoutDots?.attachToRecyclerView(binding?.recyclerViewFinalTicket!!)

        } else {
            prepareBtn = 0
        }


    }

    private fun setMySingleTicket() {
        mySingleTicket(
            MySingleTicketRequest(
                bookingId, bookType, transId1, preferences.getString(Constant.USER_ID)!!
            )
        )
    }

    private fun mySingleTicket(request: MySingleTicketRequest) {
        finalTicketViewModel.getMySingleTicketData(request).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                try {
                                    binding?.imageQrCode?.show()
                                    binding?.cardPaymentOptionsUi?.show()
                                    binding?.uiFinalTaket?.show()
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        BACKFinlTicket = 0
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onTypeFaceFinalTicketOne(

        oneTitle: TextView,
        oneRating: TextView,
        oneLocation: TextView,
        oneDateTime: TextView,
        oneScreen: TextView,
        oneCategoryName: TextView,
        onePayMode: TextView,
        onePrice: TextView
    ) {
        oneTitle1 = oneTitle
        oneRating1 = oneRating
        oneLocation1 = oneLocation
        oneDateTime1 = oneDateTime
        oneScreen1 = oneScreen
        oneCategoryName1 = oneCategoryName
        onePayMode1 = onePayMode
        onePrice1 = onePrice
    }

    override fun cancelReserv(foodSelctedItem: TicketSummaryResponse.Output) {
        cancelDialog()
    }

    override fun onTypeFaceFinalTicketTwo(
       twoPayMode: TextView, twoPayPrice: TextView
    ) {
        twoPayMode1 = twoPayMode
        twoPayPrice1 = twoPayPrice

    }

    override fun onTypeFaceFinalTicketThree(
        threeTicket: TextView,
        threeTicketPrice: TextView,
        threeFood: TextView,
        threeFoodPrice: TextView,
        threeReferenceId: TextView,
        threeReferenceTxt: TextView,
        threeTrackId: TextView,
        threeDateTime: TextView,
        threePayMode: TextView
    ) {
        threeTicket1 = threeTicket
        threeTicketPrice1 = threeTicketPrice
        threeFood1 = threeFood
        threeFoodPrice1 = threeFoodPrice
        threeReferenceId1 = threeReferenceId
        threeReferenceTxt1 = threeReferenceTxt
        threeTrackId1 = threeTrackId
        threeDateTime1 = threeDateTime
        threePayMode1 = threePayMode

    }


    private fun cancelReservation(finalTicketRequest: FinalTicketRequest) {
        finalTicketViewModel.cancelReservation(finalTicketRequest).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                try {
                                    val dialog = OptionDialog(this@FinalTicketActivity,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        it.data.msg,
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {},
                                        negativeClick = {})
                                    dialog.show()

                                } catch (e: Exception) {
                                    val dialog = OptionDialog(this@FinalTicketActivity,
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
                                val dialog = OptionDialog(this@FinalTicketActivity,
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
                        val dialog = OptionDialog(this@FinalTicketActivity,
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
                        loader?.show(this@FinalTicketActivity.supportFragmentManager, null)
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
            cancelReservation(FinalTicketRequest(bookingId, transId))
            finish()
        }

        dialog.negative_btn?.setOnClickListener {
            dialog.dismiss()
        }

    }


    // Food Pickup
    private fun foodPickup(request: FoodPrepareRequest) {
        finalTicketViewModel.foodPickup(request).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                try {

//                                    finish()
//                                    overridePendingTransition( 0, 0)
//                                    Intent.FLAG_ACTIVITY_NO_ANIMATION
//                                    startActivity(getIntent())
//                                    overridePendingTransition( 0, 0)

                                    if (from == "MTicket") {
                                        setMySingleTicket()
                                        finish()
                                        startActivity(intent)
                                    } else {
                                        printTicket(FinalTicketRequest(bookingId, transId))
                                    }

                                    println("FoodPrepareSuccess ---> Success")
                                } catch (e: Exception) {
                                    println("FoodPrepareError ---> ${e.message}")
                                }
                            }

                        }
                    }
                    Status.ERROR -> {
                        loader?.dismiss()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.message ?: "Something went wrong",
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {

                            },
                            negativeClick = {})
                        dialog.show()
                    }
                    Status.LOADING -> {
//                            loader = LoaderDialog(R.string.pleasewait)
//                            loader?.show(this@FinalTicketActivity.supportFragmentManager, null)
                    }
                }
            }
        }
    }
    class CutOffLogo : BitmapTransformation() {
        override fun transform(
            pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int
        ): Bitmap = Bitmap.createBitmap(
            toTransform, 0, 0, toTransform.width, toTransform.height - 250   // number of pixels
        )

        override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
    }


}


