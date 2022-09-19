package com.cinescape1.ui.main.views.payment.paymentFaield

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.FinalTicketRequest
import com.cinescape1.databinding.ActivityPaymentFailedBinding
import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.payment.paymentFaield.viewModel.PaymentFailedViewModel
import com.cinescape1.utils.Constant
import com.cinescape1.utils.Status
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

@ActivityScoped

class PaymentFailedActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var binding: ActivityPaymentFailedBinding? = null
    private val paymentFailedViewModel: PaymentFailedViewModel by viewModels { viewModelFactory }
    private var loader: LoaderDialog? = null

    private var bookingId = ""
    private var transId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityPaymentFailedBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        bookingId = intent.getStringExtra(Constant.IntentKey.BOOKING_ID).toString()
        transId = intent.getStringExtra(Constant.IntentKey.TRANSACTION_ID).toString()
        printTicket(FinalTicketRequest(bookingId, transId.toInt()))

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

                                    } catch (e: Exception) {

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

}