package com.cinescape1.ui.main.views.payment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.lifecycle.ViewModelProvider
import com.cinescape1.R
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityPaymentWebBinding
import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.views.finalTicket.FinalTicketActivity
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.payment.paymentFaield.PaymentFailedActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


@ActivityScoped
class PaymentWebActivity : DaggerAppCompatActivity() {
    private var fnb = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var binding: ActivityPaymentWebBinding? = null

    @Inject
    lateinit var preferences: AppPreferences

    //    private val splashViewModel: SplashViewModel by viewModels { viewModelFactory }
    private var payUrl = ""
    var bookingId = ""
    var transId = ""
    var from = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityPaymentWebBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        payUrl = intent.getStringExtra("PAY_URL").toString()
        bookingId = intent.getStringExtra(Constant.IntentKey.BOOKING_ID).toString()
        transId = intent.getStringExtra(Constant.IntentKey.TRANSACTION_ID).toString()
        from = intent.getStringExtra("From").toString()
        if (from == "login") {
            binding?.textView109?.text = resources.getString(R.string.terms_amp_condition)
            binding?.viewBack?.show()
            binding?.textView109?.show()
            binding?.imageView47?.show()
        } else {
            binding?.viewBack?.hide()
            binding?.textView109?.hide()
            binding?.imageView47?.hide()
        }

        binding?.imageView47?.setOnClickListener {
            finish()
        }

        println("pay_url--->$payUrl")
        println("transId--->$transId")
        println("bookingId--->$bookingId")
        loadWebView(payUrl)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView(url: String) {
        try {
            binding?.paymentView?.visibility = View.VISIBLE
            binding?.paymentView?.loadUrl(url)
            val webSettings = binding?.paymentView?.settings
            webSettings?.javaScriptEnabled = true
            webSettings?.domStorageEnabled = true
            binding?.paymentView?.clearFormData()
            binding?.paymentView?.addJavascriptInterface(
                JavaScriptInterface(
                    this
                ), "Android"
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        binding?.paymentView?.webViewClient = object : WebViewClient() {

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                println("url response123--->$url")

                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
//                Utility.dismissDialog()
                println("url---$url")
                super.onPageFinished(view, url)

                if (url.contains("knetmobresp")) {
                    val uri: Uri = Uri.parse(url)
                    val args = uri.queryParameterNames
                    println("args---$args")
                    if (uri.getQueryParameter("result") == "success") {

                        if (from == "recharge") {
                            Constant.IntentKey.OPEN_FROM = 1
                            finish()
                        } else {
                            val intent = Intent(applicationContext, FinalTicketActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra(Constant.IntentKey.TRANSACTION_ID, transId)
                            intent.putExtra(Constant.IntentKey.BOOKING_ID, bookingId)
                            startActivity(intent)
//                        finish()
                        }
                    } else {
                        val intent = Intent(applicationContext, PaymentFailedActivity::class.java)
                        intent.putExtra(Constant.IntentKey.TRANSACTION_ID, transId)
                        intent.putExtra(Constant.IntentKey.BOOKING_ID, bookingId)
                        startActivity(intent)
                        val dialog = OptionDialog(this@PaymentWebActivity,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            uri.getQueryParameter("error").toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                                onBackPressed()
                            },
                            negativeClick = {
                            })
                        dialog.show()
                    }
//
                }


            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
//                Utility.dismissDialog()
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedHttpError(
                view: WebView,
                request: WebResourceRequest,
                errorResponse: WebResourceResponse
            ) {
//                Utility.dismissDialog()
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
//                Utility.dismissDialog()
                super.onReceivedSslError(view, handler, error)
            }
        }
    }

    class JavaScriptInterface internal constructor(var mContext: Activity) {
        @JavascriptInterface
        fun paymentResponse(response: String, pagename: String) {
//            timer.cancel()
            Log.e("Tag", "Payment Complete=$response-pagename-$pagename")
//            tinyDB.putString(App_constants.BOOK_ENCRYPT_ID, response)
//            val intent = Intent(mContext, FinalTicketActivity::class.java)
//            intent.putExtra(Constant.IntentKey.TRANSACTION_ID, transId)
//            intent.putExtra(Constant.IntentKey.BOOKING_ID, bookingId)
//            mContext.startActivity(intent)
//            mContext.finish()
        }
    }

    override fun onBackPressed() {
        if (from == "login") {
            finish()
        } else {
            if (fnb == Constant.FNB) {
                finish()
            } else {

//            val intent = Intent(this, CinemaSessionActivity::class.java)
////            intent.putExtra(Constant.IntentKey.TICKET_BOOKING_DETAILS, paymentIntentData)
//            intent.putExtra(Constant.IntentKey.TRANSACTION_ID, transId)
//            intent.putExtra(Constant.IntentKey.BOOKING_ID, bookingId)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//            finish()

//            Constant().confirmDialog(
//                this,
//                cinemaID,
//                transactionId,
//                paymentType,
//                paymentIntentData.bookingID.toString(),
//                paymentViewModel,
//                preferences.getString(Constant.USER_ID).toString()
//            )
            }
        }


    }

}

