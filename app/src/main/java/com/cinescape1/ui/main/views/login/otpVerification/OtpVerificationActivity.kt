package com.cinescape1.ui.main.views.login.otpVerification

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.OtpVerifyRequest
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityOtpVerificationBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.login.otpVerification.viewModel.OtpVerificationViewModel
import com.cinescape1.ui.main.views.prefrence.UserPreferencesActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.Status
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

@Suppress("DEPRECATION")
class OtpVerificationActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var loader: LoaderDialog? = null
    @Inject
    lateinit var preferences: AppPreferences
    private val otpVerificationViewModel: OtpVerificationViewModel by viewModels { viewModelFactory }
    private var binding: ActivityOtpVerificationBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        //AppBar Hide
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        movedNext()
    }

    private fun movedNext() {
        binding?.imageView59?.setOnClickListener {
            onBackPressed()
        }

        binding?.textView150?.setOnClickListener {
            val otpEmail = binding?.textView146?.getStringFromFields()
            val otpMobile = binding?.textView149?.getStringFromFields()
            if (otpEmail == "null") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.enterEmailOtp),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            }else if (otpMobile=="null"){
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.enterMobileOtp),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            }else{
                otpVerify(OtpVerifyRequest(otpEmail.toString(), otpMobile.toString(), intent.getStringExtra("userId").toString()))
            }
        }


    }

    private fun otpVerify(otpVerifyRequest: OtpVerifyRequest) {
        otpVerificationViewModel.mVerify(otpVerifyRequest)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    preferences.putString(Constant.USER_ID, it.data.output.userId)
                                    preferences.putBoolean(
                                        Constant.IS_LOGIN, true
                                    )
                                    preferences.putString(
                                        Constant.USER_NAME,
                                        it.data.output.userName
                                    )
                                    preferences.putString(
                                        Constant.FIRST_NAME,
                                        it.data.output.firstName
                                    )
                                    preferences.putString(
                                        Constant.LAST_NAME,
                                        it.data.output.lastName
                                    )
                                    preferences.putString(Constant.USER_DOB, it.data.output.dob)
                                    preferences.putString(
                                        Constant.MOBILE,
                                        it.data.output.mobilePhone
                                    )
                                    preferences.putString(
                                        Constant.USER_EMAIL,
                                        it.data.output.email
                                    )
                                    preferences.putString(
                                        Constant.USER_GENDER,
                                        it.data.output.gender
                                    )
                                    preferences.putString(
                                        Constant.USER_CITY,
                                        it.data.output.mobilePhone
                                    )
                                    preferences.putString(
                                        Constant.COUNTRY_CODE,
                                        it.data.output.countryCode
                                    )
                                    val intent = Intent(
                                        this@OtpVerificationActivity,
                                        UserPreferencesActivity::class.java
                                    )
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(intent)
                                    finish()

                                }else{
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