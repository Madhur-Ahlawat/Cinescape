package com.cinescape1.ui.main.views.payment.paymentFaield

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.FinalTicketRequest
import com.cinescape1.databinding.ActivityPaymentFailedBinding
import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.home.HomeActivity
import com.cinescape1.ui.main.views.payment.paymentFaield.reponse.PaymentFailedResponse
import com.cinescape1.ui.main.views.payment.paymentFaield.viewModel.PaymentFailedViewModel
import com.cinescape1.utils.Constant
import com.cinescape1.utils.Status
import com.cinescape1.utils.show
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

@ActivityScoped

class PaymentFailedActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var binding: ActivityPaymentFailedBinding? = null
    private val paymentFailedViewModel: PaymentFailedViewModel by viewModels { viewModelFactory }
    private var loader: Dialog? = null

    private var bookingId = ""
    private var transId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityPaymentFailedBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        try {
            bookingId = intent.getStringExtra(Constant.IntentKey.BOOKING_ID).toString()
            transId = intent.getStringExtra(Constant.IntentKey.TRANSACTION_ID).toString()
            println("FailedTransandBookingId----->${bookingId}------>${transId}")
            printTicket(FinalTicketRequest(bookingId, transId))
        }catch (e:Exception){
            println("finalError------->${e.message}")
        }

    }

    private fun printTicket(request: FinalTicketRequest) {
        paymentFailedViewModel.paymentFailed(request)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        retrieveData(it.data.output)
                                    } catch (e: Exception) {
                                        val dialog = OptionDialog(this,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            it.data.msg,
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {
                                                finish()
                                            },
                                            negativeClick = {
                                            })
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

    private fun retrieveData(output: PaymentFailedResponse.Output) {
        binding?.ui?.show()
        binding?.textView141?.text = output.referenceId
        binding?.textView142?.text = output.trackId
        binding?.textView143?.text = output.bookingTime

        binding?.linearLayout4?.setOnClickListener {
            val intent = Intent(this@PaymentFailedActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@PaymentFailedActivity, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
       Constant.IntentKey.OPEN_FROM = 0
        startActivity(intent)
        finish()

    }

}