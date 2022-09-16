package com.cinescape1.ui.main.views.finalTicket

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.cinescape1.ui.main.views.finalTicket.viewModel.FinalTicketViewModel
import com.cinescape1.ui.main.views.home.HomeActivity
import com.cinescape1.ui.main.views.finalTicket.adapter.FinalTicketParentAdapter
import com.cinescape1.ui.main.views.finalTicket.model.FinalTicketLocalModel
import com.cinescape1.utils.*
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

@ActivityScoped
class FinalTicketActivity : DaggerAppCompatActivity() {

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
                finalTicketLocalModel.add(FinalTicketLocalModel("BOOKING", 3))

            } else {
                println("checkCase--->2---New")
                finalTicketLocalModel.add(FinalTicketLocalModel("BOOKING", 1))
            }
        } else {
            finalTicketLocalModel.add(FinalTicketLocalModel("BOOKING", 2))
            println("checkCase--->3---New")
        }


        val gridLayout =
            GridLayoutManager(this@FinalTicketActivity, 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerViewFinalTicket?.layoutManager =
            LinearLayoutManager(this@FinalTicketActivity)
        val finalTicketParentAdapter =
            FinalTicketParentAdapter(this@FinalTicketActivity, finalTicketLocalModel, output)
        binding?.recyclerViewFinalTicket?.layoutManager = gridLayout
        binding?.recyclerViewFinalTicket?.adapter = finalTicketParentAdapter
        binding?.layoutDots?.attachToRecyclerView( binding?.recyclerViewFinalTicket!!)

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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}


