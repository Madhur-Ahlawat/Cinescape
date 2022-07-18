package com.cinescape1.ui.main.views

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.OfferDetailsResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityOfferDetailsBinding
import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.viewModels.OfferDetailsViewModel
import com.cinescape1.utils.Constant
import com.cinescape1.utils.MyReceiver
import com.cinescape1.utils.Status
import com.cinescape1.utils.show
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


@SuppressLint("CustomSplashScreen")
@ActivityScoped
class OfferDetailsActivity : DaggerAppCompatActivity() {
    private var loader: LoaderDialog? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private val offerDetailsViewModel: OfferDetailsViewModel by viewModels { viewModelFactory }
    private var binding: ActivityOfferDetailsBinding? = null
    private var idCheck: String? = "1"
    private var broadcastReceiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferDetailsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        //AppBar Hide
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        idCheck = intent.getStringExtra(Constant.IntentKey.Offer_ID).toString()
        println("DetailsCheck--->${idCheck}")
        binding?.imageView25?.setOnClickListener {
            finish()
        }
        offerDetails()
        broadcastReceiver = MyReceiver()
        broadcastIntent()
    }

    private fun broadcastIntent() {
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }

    private fun offerDetails() {
        offerDetailsViewModel.offerDetails(idCheck)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {
                                    try {
                                        retriveData(it.data.output)
                                    } catch (e: Exception) {
                                        println("exception--->${e.message}")
                                    }
                                } else {
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        it.data?.msg.toString(),
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                            finish()
                                        },
                                        negativeClick = {
                                            finish()
                                        })
                                    dialog.show()
                                }
                            }
                        }
                        Status.ERROR -> {
                        }
                        Status.LOADING -> {
                            loader = LoaderDialog(R.string.pleasewait)
                            loader?.show(supportFragmentManager, null)
                        }
                    }
                }
            }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun retriveData(output: OfferDetailsResponse.Output) {
        binding?.offerDetailsUi?.show()
        Glide.with(this)
            .load(output.appImageUrl)
            .placeholder(R.drawable.bombshell)
            .into(binding?.imageView24!!)

        binding?.webView?.webViewClient = WebViewClient()
        // this will load the url of the website

        // this will enable the javascript settings
        binding?.webView?.settings?.javaScriptEnabled = true
        binding?.webView?.settings?.javaScriptEnabled = true
        binding?.webView?.loadDataWithBaseURL(null, output.description, "text/html", "utf-8", null)
        // if you want to enable zoom feature
        binding?.webView?.settings?.setSupportZoom(true)
    }
}

