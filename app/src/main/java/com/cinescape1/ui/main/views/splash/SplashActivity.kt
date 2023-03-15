package com.cinescape1.ui.main.views.splash

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.cinescape1.R
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.player.viewModel.SplashViewModel
import com.cinescape1.ui.main.views.home.HomeActivity
import com.cinescape1.ui.main.views.login.LoginActivity
import com.cinescape1.ui.main.views.login.otpVerification.OtpVerificationActivity
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity
import com.cinescape1.ui.main.views.prefrence.UserPreferencesActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.LocaleHelper
import com.cinescape1.utils.Status
import dagger.android.support.DaggerAppCompatActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
@ActivityScoped
class SplashActivity : DaggerAppCompatActivity() {
    private var loader: LoaderDialog? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferences: AppPreferences
    private val splashViewModel: SplashViewModel by viewModels { viewModelFactory }
    private var networkDialog:Dialog? = null

    @SuppressLint("PackageManagerGetSignatures")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
                Constant.LANGUAGE = "${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}"
                println("getLocalLanguage--->${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}--->$Constant.LANGUAGE")
            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(this, "en")
                Constant.LANGUAGE = "${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}"
            }
            else -> {
                LocaleHelper.setLocale(this, "en")
                Constant.LANGUAGE = "${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}"
            }
        }
        setContentView(R.layout.activity_splash)
        try {
            val info = packageManager.getPackageInfo(
                "com.cinescape.android",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        if (isConnected()) {
            getSplashText()
        } else {
            networkDialog()
        }
    }
    private fun isConnected(): Boolean {
        var connected = false
        try {
            val cm =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: Exception) {
            Log.e("Connectivity Exception", e.message!!)
        }
        return connected
    }


    private fun getSplashText() {
        splashViewModel.getSplashText()
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {
                                    try {
                                        openMovieForYou()
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
                                  finish()
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

    private fun openMovieForYou() {
        Constant.IntentKey.BACKFinlTicket +=0
        val runnable = Runnable {
            if (preferences.getBoolean(Constant.IS_LOGIN)) {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(runnable, 3000)
    }


    private fun networkDialog() {
        networkDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        networkDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        networkDialog?.setContentView(R.layout.internet_dialog)
        networkDialog?.show()
        val window: Window? = networkDialog?.window
        val wlp: WindowManager.LayoutParams? = window?.attributes
        wlp?.gravity = Gravity.CENTER
        wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv())
        window?.attributes = wlp
        networkDialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        val restart = networkDialog?.findViewById<View>(R.id.view49)
        val network = networkDialog?.findViewById<View>(R.id.view48)
        network?.setOnClickListener { v: View? ->
            val intent2 = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(intent2)
        }
        restart?.setOnClickListener { v: View? ->
            recreate()
        }
    }
}