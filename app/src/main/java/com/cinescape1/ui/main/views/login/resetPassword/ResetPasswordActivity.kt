package com.cinescape1.ui.main.views.login.resetPassword

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.ForgotPasswordRequest
import com.cinescape1.data.models.requestModel.UpdatePasswordRequest
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityResetpassWordBinding
import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.login.LoginActivity
import com.cinescape1.ui.main.views.login.resetPassword.viewModel.ResetPasswordViewModel
import com.cinescape1.utils.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.forgot_otp_verify.*
import kotlinx.android.synthetic.main.forgot_otp_verify.view.*
import kotlinx.android.synthetic.main.reset_password.*
import kotlinx.android.synthetic.main.set_password.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
@ActivityScoped
class ResetPasswordActivity : DaggerAppCompatActivity() {
    private var loader: LoaderDialog? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferences: AppPreferences
    private val splashViewModel: ResetPasswordViewModel by viewModels { viewModelFactory }
    private var binding: ActivityResetpassWordBinding? = null
    private var otpEmail: String = ""
    private var userId: String = ""
    private var emailId: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityResetpassWordBinding.inflate(layoutInflater, null, false)
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
                println("getLocalLanguage--->${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}")
            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(this, "en")
            }
            else -> {
                LocaleHelper.setLocale(this, "en")
            }
        }
        setContentView(binding?.root)

        manageFunctions()

    }

    private fun manageFunctions() {

        //AppBar Hide
        Constant().appBarHide(this)

        //backClick
        binding?.imageView70?.setOnClickListener {
            finish()
        }

        movedNext()
    }


    @SuppressLint("ClickableViewAccessibility", "ShowToast")
    private fun movedNext() {
//        //Edit
        resendEmailED.setOnTouchListener(OnTouchListener { _, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= resendEmailED.right - resendEmailED.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    // your action here
                    binding?.sendOtpForgotUi?.show()
                    binding?.ForgotOtpVerify?.hide()
                    return@OnTouchListener true
                }
            }
            false
        })

        //Resend Otp
        textView76.setOnClickListener {
            forgotPassword(ForgotPasswordRequest("", resendEmailED.text.toString()))
        }

        //SendOtp
        sendOtpForgot.setOnClickListener {
            emailId = enterEmailForPass.text.toString()
            if (!InputTextValidator.validateEmail(enterEmailForPass!!)) {
                if (enterEmailForPass.text.toString().trim { it <= ' ' }.isEmpty()) {
//                    binding?.enterEmails?.error = resources.getString(R.string.emailOnlyEmpty)
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        resources.getString(R.string.emailOnlyEmpty),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                } else {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        resources.getString(R.string.email_msg_invalid1),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }

            } else {
                forgotPassword(ForgotPasswordRequest("", enterEmailForPass.text.toString()))
            }

        }

        verifyOtpForgot.setOnClickListener {
            otpEmail = otpView.value
            if (otpEmail.contains("null")) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.otp_msg),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else {
                binding?.textSignup?.text = resources.getString(R.string.resetPassword)
                binding?.textHaveAccountSign?.text = ""
                binding?.updatePassword?.show()
            }
        }

        //Update Password
        updatePasswordForgot.setOnClickListener {
            val pass = enterForgeUpdatePass.text.toString()
            val confPass = enterConfPasswords.text.toString()
            when {
                pass.trim() == "" -> {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        resources.getString(R.string.enterPass),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                confPass.trim() == "" -> {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        resources.getString(R.string.enterConfPass),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()

                }
                pass != confPass -> {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        resources.getString(R.string.passNotMatch),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                else -> {
                    updatePassword(
                        UpdatePasswordRequest(
                            otpEmail, enterConfPasswords.text.toString(), userId
                        )
                    )
                }
            }
        }
    }

    //////////////////////////// Forgot Password ////////////////////////////////

    private fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest) {
        splashViewModel.forgotPassword(forgotPasswordRequest).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.code == Constant.SUCCESS_CODE) {
                                try {
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        it.data.msg.toString(),
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {},
                                        negativeClick = {})
                                    dialog.show()

                                    userId = it.data.output.userid
                                    binding?.sendOtpForgotUi?.hide()
                                    binding?.textSignup?.text =
                                        resources.getString(R.string.password_recovery)
                                    binding?.textHaveAccountSign?.text =
                                        resources.getString(R.string.recovery_msg_two)
                                    binding?.ForgotOtpVerify?.show()
                                    resendEmailED.setText(emailId)
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
                            it.message ?: "Oops its not you, its Us.",
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

    //////////////////////////// Update Password ////////////////////////////////

    private fun updatePassword(updatePasswordRequest: UpdatePasswordRequest) {
        splashViewModel.updatePassword(updatePasswordRequest).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.code == Constant.SUCCESS_CODE) {
                                try {
                                    val intent = Intent(
                                        this@ResetPasswordActivity, LoginActivity::class.java
                                    )
                                    startActivity(intent)
                                    finish()
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
                            it.message ?: "Oops its not you, its Us.",
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
}