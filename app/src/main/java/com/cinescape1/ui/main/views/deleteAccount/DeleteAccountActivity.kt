package com.cinescape1.ui.main.views.deleteAccount

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.cinescape1.R
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.DeleteAccountLayoutBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.deleteAccount.viewModel.DeleteAccountViewModel
import com.cinescape1.ui.main.views.login.LoginActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.InputTextValidator
import com.cinescape1.utils.Status
import com.cinescape1.utils.toast
import com.google.firebase.auth.FirebaseAuth
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.account_profile_layout.*
import javax.inject.Inject


@Suppress("DEPRECATION")
class DeleteAccountActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private val deleteAccountViewModel: DeleteAccountViewModel by viewModels { viewModelFactory }
    private var binding: DeleteAccountLayoutBinding? = null
    private var loader: LoaderDialog? = null

    private var message = ""
    private var userId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DeleteAccountLayoutBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        binding?.imgBackBtn?.setOnClickListener {
            onBackPressed()
        }


        // This overrides the radiogroup onCheckListener
        // This overrides the radiogroup onCheckListener
        binding?.radioGroup?.setOnCheckedChangeListener { arg0, id ->
            val checkedRadioButton = arg0.findViewById<View>(id) as RadioButton
            when (id) {
                R.id.radio1 -> {
                    if (id == R.id.radio1) {
//                        toast("select  id--->${id}---->${R.id.radio1}")
                        checkedRadioButton.buttonTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this, R.color.text_alert_color_red
                            )
                        )
                    } else {
                        toast("unselect  id--->${id}---->${R.id.radio1}")
                        checkedRadioButton.buttonTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this, R.color.text_color
                            )
                        )
                    }
                }
                R.id.radio2 -> {
                    if (id == R.id.radio2) {
                        checkedRadioButton.buttonTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.text_alert_color_red))
                    } else {
                        checkedRadioButton.buttonTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.text_color))
                    }

                }

                R.id.radio3 -> {
                    if (id == R.id.radio3) {
                        checkedRadioButton.buttonTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this, R.color.text_alert_color_red))
                    } else {
                        checkedRadioButton.buttonTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.text_color))
                    }
                }
            }
        }

        manageFunction()
    }

    private fun manageFunction() {
        binding?.edit?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                if (s.toString() != "") {
                    binding?.radio1?.isChecked = false
                    binding?.radio2?.isChecked = false
                    binding?.radio3?.isChecked = false
                }

            }
        })

        binding?.btnConfirmDeletion?.setOnClickListener {
            val msgEdit = binding?.edit?.text.toString()
            if (binding?.radio1?.isChecked == true) {
                message = binding?.radio1?.text.toString()
            } else if (binding?.radio2?.isChecked == true) {
                message = binding?.radio2?.text.toString()
            } else if (binding?.radio3?.isChecked == true) {
                message = binding?.radio3?.text.toString()
            } else if (msgEdit != "") {
                message = msgEdit
            }


            if (message==""){
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.deleteMsg),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            }else if (!InputTextValidator.validateEmail(binding?.contactEmail!!)) {
                if (binding?.contactEmail?.text.toString().trim { it <= ' ' }.isEmpty()) {
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
                        resources.getString(R.string.email_msg_invalid),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
            } else {
                otpVerify(
                    DeleteAccountRequest(
                        message, preferences.getString(Constant.USER_ID).toString()
                    )
                )
            }
        }
    }

    private fun otpVerify(otpVerifyRequest: DeleteAccountRequest) {
        deleteAccountViewModel.deleteAccount(otpVerifyRequest).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                FirebaseAuth.getInstance().signOut()
                                preferences.clearData()
                                Constant.IntentKey.BOOKINGClick = 0
                                Constant.IntentKey.BACKFinlTicket = 0
                                Constant.IntentKey.NextBookingsResponse = null

                                val intent =
                                    Intent(this@DeleteAccountActivity, LoginActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
                                finish()

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

}