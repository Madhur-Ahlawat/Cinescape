package com.cinescape1.ui.main.views.login.guest

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.GuestRequest
import com.cinescape1.data.models.responseModel.CountryCodeResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityContinueGuestBinding
import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.login.guest.viewModel.ContinueGuestViewModel
import com.cinescape1.ui.main.views.adapters.CountryCodeAdapter
import com.cinescape1.ui.main.views.home.HomeActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.InputTextValidator
import com.cinescape1.utils.LocaleHelper
import com.cinescape1.utils.Status
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
@ActivityScoped
class ContinueGuestActivity : DaggerAppCompatActivity(),
    CountryCodeAdapter.RecycleViewItemClickListener {
    private var loader: LoaderDialog? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private val continueGuestActivity: ContinueGuestViewModel by viewModels { viewModelFactory }
    private var binding: ActivityContinueGuestBinding? = null
    var dialog: Dialog? = null
    private var countryCodeList = ArrayList<CountryCodeResponse.Output>()
    private var mAdapter: CountryCodeAdapter? = null
    var countryCode: String = ""
    var name: String = ""
    var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityContinueGuestBinding.inflate(layoutInflater, null, false)
        val view = binding?.root

        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
                val regular = ResourcesCompat.getFont(this, R.font.gess_light)
                val bold = ResourcesCompat.getFont(this, R.font.gess_bold)
                val medium = ResourcesCompat.getFont(this, R.font.gess_medium)

                // guest sign in
                binding?.textSignup?.typeface = bold
                // continue btn
                binding?.textView73?.typeface = bold

                println("getLocalLanguage--->${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}")
            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(this, "en")
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(this, R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)

                // guest sign in
                binding?.textSignup?.typeface = heavy
                // continue btn
                binding?.textView73?.typeface = heavy

            }
            else -> {
//                LocaleHelper.setLocale(this, "en")
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(this, R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)

                // guest sign in
                binding?.textSignup?.typeface = heavy
                // continue btn
                binding?.textView73?.typeface = heavy

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

        name = preferences.getString(Constant.FIRST_NAME).toString()
        email = preferences.getString(Constant.USER_EMAIL).toString()
        binding?.GuestName?.setText(name)
        binding?.GuestEmail?.setText(email)


        countryCodeLoad()
        movedNext()
    }

    private fun movedNext() {
        binding?.imageView58?.setOnClickListener {
            onBackPressed()
        }
        binding?.textView73?.setOnClickListener {
            val username = binding?.GuestName?.text.toString()
            val password = binding?.GuestEmail?.text.toString()
            val mobile = binding?.editTextPhone?.text.toString()
            if (username.trim() == ""){
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.enterName),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            }else if (password.trim() == ""){
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.emailOnlyEmpty),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            }else if (password.trim() != ""){
                if (!InputTextValidator.validateEmail(binding?.GuestEmail!!)) {
                    if (binding?.GuestEmail?.text.toString().trim { it <= ' ' }.isEmpty()) {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            resources.getString(R.string.emailOnlyEmpty),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                            },
                            negativeClick = {
                            })
                        dialog.show()
                    } else{
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            resources.getString(R.string.email_msg_invalid),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                            },
                            negativeClick = {
                            })
                        dialog.show()
                    }

                }
                else if(mobile.trim() == "" ){
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        resources.getString(R.string.enterMo),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
                else{
                    continueGuest(
                        GuestRequest(
                            countryCode,
                            "",
                            binding?.GuestEmail?.text.toString(),
                            binding?.GuestName?.text.toString(),
                            "",
                            "",
                            binding?.editTextPhone?.text.toString(),
                            "",
                            promoEmail = true,
                            promoMobile = true,
                            reserveNotification = "",
                            socialId = "",
                            socialType = "",
                            userName = ""
                        )
                    )
                    Constant().hideKeyboard(this)
                }

            }


        }

    }

    private fun continueGuest(guestRequest: GuestRequest) {
        continueGuestActivity.continueGuest(guestRequest)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {
                                    try {
                                        preferences.putBoolean(
                                            Constant.IS_LOGIN, true
                                        )
                                        preferences.putString(
                                            Constant.USER_ID,
                                            it.data.output.userId
                                        )
                                        preferences.putString(
                                            Constant.TYPE_LOGIN,
                                            "GUEST"
                                        )
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
                                        val intent =
                                            Intent(
                                                this@ContinueGuestActivity,
                                                HomeActivity::class.java
                                            )
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
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

    private fun countryCodeLoad() {
        continueGuestActivity.countryCode(this)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    println("LocationResponse--->${it.data.output}")
                                    countryCodeList = it.data.output
                                    binding?.mobileCode?.setText(it.data.output[0].isdCode)
                                    countryCode = it.data.output[0].isdCode
                                    val maxLengthEditText = it.data.output[0].phoneLength
                                    binding?.editTextPhone?.filters = arrayOf<InputFilter>(
                                        InputFilter.LengthFilter(
                                            maxLengthEditText
                                        )
                                    )
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

    private fun retrieveCountryList(output: ArrayList<CountryCodeResponse.Output>) {
        binding?.mobileCode?.setOnClickListener {
            bottomDialog(output)
        }
    }

    private fun bottomDialog(countryList: ArrayList<CountryCodeResponse.Output>) {
        val mDialogView = layoutInflater.inflate(R.layout.countrycode_new_dialog, null)
        val mBuilder = AlertDialog.Builder(this, R.style.MyDialogTransparent)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.setCancelable(false)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        val recyclerView = mDialogView.findViewById<RecyclerView>(R.id.NewCountryRecycler)
        val cancel = mDialogView.findViewById<View>(R.id.view73)
        val edSearch = mDialogView.findViewById<EditText>(R.id.searchData)
        val proceed = mDialogView.findViewById<View>(R.id.textView57)
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
                countryCodeList = ArrayList()
                countryCodeList = if (cs == "") {
                    countryList
                } else {
                    ArrayList()
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
            binding?.mobileCode?.setText(countryCode)
        }
    }

    override fun onItemClick(view: CountryCodeResponse.Output, check : Boolean) {
        countryCode = view.isdCode

        val maxLengthEditText = view.phoneLength
        binding?.editTextPhone?.filters = arrayOf<InputFilter>(
            InputFilter.LengthFilter(
                maxLengthEditText
            )
        )

        println("CountryCode--->${view.isdCode}")
//        dialog?.dismiss()
    }
}