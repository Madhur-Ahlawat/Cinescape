package com.cinescape1.ui.main.views.login.activeWallet

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import android.app.Dialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.ActivatwWalletRequest
import com.cinescape1.data.models.responseModel.CountryCodeResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityActivateWalletBinding
import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.adapters.CountryCodeAdapter
import com.cinescape1.ui.main.views.login.activeWallet.repsonse.ActivateWalletResponse
import com.cinescape1.ui.main.views.login.activeWallet.viewModel.ActivateWalletViewModel
import com.cinescape1.ui.main.views.login.otpVerification.OtpVerificationActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.InputTextValidator
import com.cinescape1.utils.LocaleHelper
import com.cinescape1.utils.Status
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


@SuppressLint("CustomSplashScreen")
@ActivityScoped
class ActivateWalletActivity : DaggerAppCompatActivity(),
    CountryCodeAdapter.RecycleViewItemClickListener {
    private var loader: Dialog? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private val activateWalletViewModel: ActivateWalletViewModel by viewModels { viewModelFactory }
    private var binding: ActivityActivateWalletBinding? = null
    private var countryCode: String = ""
    private var mAdapter: CountryCodeAdapter? = null
    private var languageCheck: String = "en"
    private var countryCodeList = ArrayList<CountryCodeResponse.Output>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityActivateWalletBinding.inflate(layoutInflater, null, false)
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
                println("getLocalLanguage--->${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}")
                languageCheck = "ar"
                val regular = ResourcesCompat.getFont(this, R.font.montserrat_regular)
                val bold = ResourcesCompat.getFont(this, R.font.montserrat_bold)
                val medium = ResourcesCompat.getFont(this, R.font.montserrat_medium)

                binding?.textView62?.typeface = bold
                binding?.textView63?.typeface = regular
                binding?.textView64?.typeface = bold
                binding?.textView68?.typeface = regular
                binding?.textView69?.typeface = regular
                binding?.textView65?.typeface = bold
                binding?.textView66?.typeface = regular
                binding?.textView67?.typeface = medium

            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(this, "en")
                languageCheck = "en"
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(this, R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)

                binding?.textView62?.typeface = bold
                binding?.textView63?.typeface = regular
                binding?.textView64?.typeface = bold
                binding?.textView68?.typeface = regular
                binding?.textView69?.typeface = regular
                binding?.textView65?.typeface = bold
                binding?.textView66?.typeface = regular
                binding?.textView67?.typeface = medium
            }
            else -> {
                LocaleHelper.setLocale(this, "en")
                languageCheck = "en"
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(this, R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)

                binding?.textView62?.typeface = bold
                binding?.textView63?.typeface = regular
                binding?.textView64?.typeface = bold
                binding?.textView68?.typeface = regular
                binding?.textView69?.typeface = regular
                binding?.textView65?.typeface = bold
                binding?.textView66?.typeface = regular
                binding?.textView67?.typeface = medium
            }
        }

        setContentView(binding?.root)



        manageFunctions()
    }

    private fun manageFunctions() {
        movedNext()
        countryCodeLoad()
    }

    private fun movedNext() {
        //back
        binding?.imageView37?.setOnClickListener {
            finish()
        }

        binding?.textView67?.setOnClickListener {
            val cardNumber = binding?.editText?.text.toString()
            val email = binding?.editText1?.text.toString()
            val password = binding?.editText2?.text.toString()
            val confPassword = binding?.editText3?.text.toString()
            val mobile = binding?.editText6?.text.toString()

            println("details---->card${cardNumber}-->email${email}--pass--->${password}--->confPass--->${confPassword}--->mob--->${mobile}")
            if (cardNumber.isNullOrEmpty()) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.cardNoEmpty),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (!InputTextValidator.validateEmail(binding?.editText1!!)) {
                if (binding?.editText1?.text.toString().trim { it <= ' ' }.isEmpty()) {
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
            } else if (password.isNullOrEmpty()) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.passwordEmpty),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (confPassword.isNullOrEmpty()) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.passwordEmpty),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (password != confPassword) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.passNotMatch),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (mobile.isNullOrEmpty()) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.enterMo),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (countryCode.isNullOrEmpty()) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.countryEmpty),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else {
                activateCard(
                    ActivatwWalletRequest(
                        cardNumber,
                        countryCode,
                        "",
                        email,
                        "",
                        "",
                        "",
                        mobile,
                        password,
                        promoEmail = true,
                        promoMobile = true,
                        reserveNotification = true,
                        socialId = "",
                        socialType = ""
                    )
                )
            }

        }

        binding?.textView64?.setOnClickListener {
            finish()
        }
        binding?.cancelAndGoBack?.setOnClickListener {
            finish()
        }
    }

    private fun activateCard(activateWalletRequest: ActivatwWalletRequest) {
        activateWalletViewModel.activateCard(activateWalletRequest).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.code == Constant.SUCCESS_CODE) {
                                retrieveData(it.data.output)
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
                        loader = LoaderDialog.getInstance(this,layoutInflater)
                        loader?.show()
                    }
                }
            }
        }
    }

    private fun retrieveData(output: ActivateWalletResponse.Output) {
        val intent = Intent(this, OtpVerificationActivity::class.java)
        intent.putExtra("userId", output.userid)
        intent.putExtra("type", "login")
        intent.putExtra("verifyType", output.otp_require)
        startActivity(intent)

    }


    //////////////////////////////   Country Code Manage /////////////////////////

    private fun countryCodeLoad() {
        activateWalletViewModel.countryCode(this).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                println("LocationResponse--->${it.data.output}")

                                countryCodeList = it.data.output
                                binding?.editText4?.setText(" " + it.data.output[0].isdCode)
                                countryCode = it.data.output[0].isdCode
                                retrieveCountryList(it.data.output)
                            } else {
                                println("Something Wrong")
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
                        loader = LoaderDialog.getInstance(this,layoutInflater)
                        loader?.show()
                    }
                }
            }
        }
    }

    private fun retrieveCountryList(output: ArrayList<CountryCodeResponse.Output>) {
        binding?.editText4?.setOnClickListener {
            bottomDialog(output)
        }
    }

    private fun bottomDialog(countryList: java.util.ArrayList<CountryCodeResponse.Output>) {
        val mDialogView = layoutInflater.inflate(R.layout.countrycode_new_dialog, null)
        val mBuilder = AlertDialog.Builder(this, R.style.MyDialogTransparent).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.setCancelable(false)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        val recyclerView = mDialogView.findViewById<RecyclerView>(R.id.NewCountryRecycler)
        val cancel = mDialogView.findViewById<View>(R.id.view73)
        val edSearch = mDialogView.findViewById<EditText>(R.id.searchData)
        val proceed = mDialogView.findViewById<TextView>(R.id.textView57)
        val textCancel = mDialogView.findViewById<TextView>(R.id.textView59)
        val textGoBack = mDialogView.findViewById<TextView>(R.id.textView60)

        if (languageCheck == "ar") {
            val regular = ResourcesCompat.getFont(this, R.font.montserrat_regular)
            val bold = ResourcesCompat.getFont(this, R.font.montserrat_bold)
            proceed.typeface = bold
            textCancel.typeface = bold
            textGoBack.typeface = regular

        } else {
            val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
            val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
            proceed.typeface = bold
            textCancel.typeface = bold
            textGoBack.typeface = regular
        }


        mAdapter = CountryCodeAdapter(countryCodeList, this, this)
        recyclerView.adapter = mAdapter
        //Recycler
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        recyclerView.isNestedScrollingEnabled = true
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        countryCodeList = countryList

        edSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {

            }

            override fun onTextChanged(
                cs: CharSequence, start: Int, before: Int, count: Int
            ) {
                countryCodeList = java.util.ArrayList()
                countryCodeList = if (cs == "") {
                    countryList
                } else {
                    java.util.ArrayList()
                }
                for (i in 0 until countryList.size) {
                    try {
                        if (countryList[i].countryName.lowercase()
                                .contains(cs.toString().lowercase())
                        ) {
                            countryCodeList.add(countryList[i])
                        }

                    } catch (e: Exception) {
                        println("SearchMsg--->${e.message}")
                    }
                }
                println("comingSoonSize--->${countryCodeList.size}")

                mAdapter?.updateList(countryCodeList)
//                field1.setText("")
            }
        })

        cancel.setOnClickListener {
            mAlertDialog?.dismiss()
        }

        proceed.setOnClickListener {
            mAlertDialog?.dismiss()
            edSearch.text.clear()
            binding?.editText4?.setText(countryCode)
        }
    }

    override fun onItemClick(view: CountryCodeResponse.Output, check: Boolean) {
        countryCode = view.isdCode
        val maxLengthEditText = view.phoneLength
        binding?.editText6?.filters = arrayOf<InputFilter>(
            InputFilter.LengthFilter(
                maxLengthEditText
            )
        )
    }


}