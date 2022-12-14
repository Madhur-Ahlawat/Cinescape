package com.cinescape1.ui.main.views.finalTicket

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.CancelTransRequest
import com.cinescape1.data.models.requestModel.FinalTicketRequest
import com.cinescape1.data.models.requestModel.MySingleTicketRequest
import com.cinescape1.data.models.responseModel.TicketSummaryResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityFinalTicketBinding
import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.finalTicket.adapter.FinalTicketParentAdapter
import com.cinescape1.ui.main.views.finalTicket.model.FinalTicketLocalModel
import com.cinescape1.ui.main.views.finalTicket.viewModel.FinalTicketViewModel
import com.cinescape1.ui.main.views.home.HomeActivity
import com.cinescape1.utils.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.cancel_dialog.*
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
    private var oneType1: TextView? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityFinalTicketBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
                val regular = ResourcesCompat.getFont(this, R.font.gess_light)
                val bold = ResourcesCompat.getFont(this, R.font.gess_bold)
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
                oneType1?.typeface = bold
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
                oneType1?.typeface = bold
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
                oneType1?.typeface = bold
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
            printTicket(FinalTicketRequest(bookingId, transId))
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
//
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
            finalTicketLocalModel.add(FinalTicketLocalModel(output.bookingType, 3))

            println("checkCase--->3---New")
        }

        val gridLayout =
            GridLayoutManager(this@FinalTicketActivity, 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerViewFinalTicket?.layoutManager =
            LinearLayoutManager(this@FinalTicketActivity)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding?.recyclerViewFinalTicket)
        val finalTicketParentAdapter =
            FinalTicketParentAdapter(
                this@FinalTicketActivity,
                finalTicketLocalModel,
                output,
                this,
                this,
                this
            )
        binding?.recyclerViewFinalTicket?.layoutManager = gridLayout
        binding?.recyclerViewFinalTicket?.adapter = finalTicketParentAdapter
        binding?.layoutDots?.attachToRecyclerView(binding?.recyclerViewFinalTicket!!)

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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onTypeFaceFinalTicketOne(
        oneBookingId: TextView,
        oneTitle: TextView,
        oneRating: TextView,
        oneLocation: TextView,
        oneDateTime: TextView,
        oneScreen: TextView,
        oneType: TextView,
        oneCategoryName: TextView,
        onePayMode: TextView,
        onePrice: TextView
    ) {
        oneBookingId1 = oneBookingId
        oneTitle1 = oneTitle
        oneRating1 = oneRating
        oneLocation1 = oneLocation
        oneDateTime1 = oneDateTime
        oneScreen1 = oneScreen
        oneType1 = oneType
        oneCategoryName1 = oneCategoryName
        onePayMode1 = onePayMode
        onePrice1 = onePrice
    }

    override fun cancelReserv(foodSelctedItem: TicketSummaryResponse.Output) {
        cancelDialog()


    }

    override fun onTypeFaceFinalTicketTwo(
        twoBookingId: TextView,
        twoPickupInfo: TextView,
        twoPayMode: TextView,
        twoPayPrice: TextView
    ) {
        twoBookingId1 = twoBookingId
        twoPickupInfo1 = twoPickupInfo
        twoPayMode1 = twoPayMode
        twoPayPrice1 = twoPayPrice

    }

    override fun onTypeFaceFinalTicketThree(
        threeBookingId: TextView,
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
        threeBookingId1 = threeBookingId
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
            cancelReservation(FinalTicketRequest(bookingId, transId))
            finish()
        }

        dialog.negative_btn?.setOnClickListener {
            dialog.dismiss()
        }
    }

}


