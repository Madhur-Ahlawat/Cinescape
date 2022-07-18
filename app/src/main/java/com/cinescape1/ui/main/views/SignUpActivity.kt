package com.cinescape1.ui.main.views

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.OtpVerifyRequest
import com.cinescape1.data.models.responseModel.CountryCodeResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivitySignUpBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.viewModels.SignupViewModel
import com.cinescape1.ui.main.views.adapters.CountryCodeAdapter
import com.cinescape1.utils.Constant
import com.cinescape1.utils.InputTextValidator
import com.cinescape1.utils.MyReceiver
import com.cinescape1.utils.Status
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SignUpActivity : DaggerAppCompatActivity(), CountryCodeAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var loader: LoaderDialog? = null

    @Inject
    lateinit var preferences: AppPreferences
    private val signupViewModel: SignupViewModel by viewModels { viewModelFactory }
    private var binding: ActivitySignUpBinding? = null
    private var gender: String = ""
    private var receiveMail: Boolean = false
    private var receiveMobileNot: Boolean = false
    private var receivedResNote: Boolean = false
    var dialog: Dialog? = null
    private var countryCodeList = ArrayList<CountryCodeResponse.Output>()
    private var mAdapter: CountryCodeAdapter? = null
    var countryCode: String = ""
    private var broadcastReceiver: BroadcastReceiver? = null
    private var areaCode = ""
    private var cinemaName = ""
    private var seatCat = ""
    private var seatType = ""
    private var ttType = ""
    private var DatePosition = ""
    private var date_pos = 0
    private var show_pos = 0
    private var cinema_pos = 0
    private var seatQuanitity = 0
    private var dateTime = ""
    private var MovieId = ""
    private var CinemaID = ""
    private var SessionID = ""
    private var dt = ""
    private var from = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        //AppBar Hide
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        if (intent.hasExtra("FROM")) {
            areaCode = intent.getStringExtra("AREA_CODE").toString()
            from = intent.getStringExtra("FROM").toString()
            cinemaName = intent.getStringExtra("CINEMA").toString()
            seatCat = intent.getStringExtra("SEAT_CAT").toString()
            ttType = intent.getStringExtra("TT_TYPE").toString()
            seatType = intent.getStringExtra("SEAT_TYPE").toString()
            date_pos = intent.getIntExtra("DATE_POS", 0)
            show_pos = intent.getIntExtra("SHOW_POS", 0)
            cinema_pos = intent.getIntExtra("CINEMA_POS", 0)
            seatQuanitity = intent.getIntExtra("SEAT_POS", 0)


            dateTime = intent.getStringExtra("DateTime").toString()
            MovieId = intent.getStringExtra("MovieId").toString()
            CinemaID = intent.getStringExtra("CinemaID").toString()
            SessionID = intent.getStringExtra("SessionID").toString()
            DatePosition = intent.getStringExtra("DatePosition").toString()
            dt = intent.getStringExtra("dt").toString()
        }


        broadcastIntent()
        movedNext()
        countryCodeLoad()
        conditionals()
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

    @SuppressLint("ShowToast")
    private fun movedNext() {

        binding?.imageView44?.setOnClickListener {
            onBackPressed()
        }

        binding?.textHaveAccountSignUi?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        binding?.textSignup?.setOnClickListener {
            val intent = Intent(this@SignUpActivity, UserPreferencesActivity::class.java)
            startActivity(intent)
        }

        binding?.enterDateBirths?.setOnClickListener {
            val mcurrentDate = Calendar.getInstance()
            var mYear = mcurrentDate[Calendar.YEAR]
            var mMonth = mcurrentDate[Calendar.MONTH]
            var mDay = mcurrentDate[Calendar.DAY_OF_MONTH]
            val mDatePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val myCalendar = Calendar.getInstance()
                    myCalendar[Calendar.YEAR] = selectedYear
                    myCalendar[Calendar.MONTH] = selectedMonth
                    myCalendar[Calendar.DAY_OF_MONTH] = selectedDay
                    val myFormat = "dd-MM-yy" //Change as you need
                    val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
                    binding?.enterDateBirths?.setText(sdf.format(myCalendar.time))
                    mDay = selectedDay
                    mMonth = selectedMonth
                    mYear = selectedYear
                }, mYear, mMonth, mDay
            )
            mDatePicker.datePicker.maxDate = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 0)
            mDatePicker.show()
        }

        binding?.imageReceiveSwitcherEmail?.setOnCheckedChangeListener { _, isChecked ->
            receiveMobileNot = when (isChecked) {
                true -> {
                    true
                }
                false -> {
                    false
                }
            }
        }

        binding?.imageMoNotification?.setOnCheckedChangeListener { _, isChecked ->
            receiveMail = when (isChecked) {
                true -> {
                    true
                }
                false -> {
                    false
                }
            }
        }

        binding?.imageView5?.setOnCheckedChangeListener { _, isChecked ->
            receivedResNote = when (isChecked) {
                true -> {
                    true
                }
                false -> {
                    false
                }
            }
        }

        binding?.radioGroup?.setOnCheckedChangeListener { _, _ ->
            if (binding?.male?.isChecked == true) {
//                binding?.male?.setTextColor(
//                    ContextCompat.getColorStateList(
//                        this,
//                        R.color.text_alert_color_red
//                    )
//                )

                binding?.male?.buttonTintList =
                    ColorStateList.valueOf(getColor(R.color.text_alert_color_red))

                binding?.female?.buttonTintList =
                    ColorStateList.valueOf(getColor(R.color.text_color))

//                binding?.female?.setTextColor(
//                    ContextCompat.getColorStateList(
//                        this,
//                        R.color.text_color
//                    )
//                )

                gender = "Male"
            } else if (binding?.female?.isChecked == true) {
//                binding?.male?.setTextColor(
//                    ContextCompat.getColorStateList(
//                        this,
//                        R.color.text_color
//                    )
//                )
                binding?.male?.buttonTintList = ColorStateList.valueOf(getColor(R.color.text_color))
                binding?.female?.buttonTintList =
                    ColorStateList.valueOf(getColor(R.color.text_alert_color_red))
//                binding?.female?.setTextColor(
//                    ContextCompat.getColorStateList(
//                        this,
//                        R.color.text_alert_color_red
//                    )
//                )
                gender = "Female"
            }
        }

        binding?.signupBtn?.setOnClickListener {
            val email = binding?.enterEmails?.text.toString()
            val firstName = binding?.enterFirstNames?.text.toString()
            val lastName = binding?.enterLastNames?.text.toString()
            val checkGender = gender
            val password = binding?.enterPasswords?.text.toString()
            val confPassword = binding?.enterConfirmPasswords?.text.toString()
            val checkDob = binding?.enterDateBirths?.text.toString()
            val phone = binding?.editTextPhone?.text.toString()

            if (!InputTextValidator.validateEmail(binding?.enterEmails!!)) {
                if (binding?.enterEmails?.text.toString().trim { it <= ' ' }.isEmpty()) {
//                    binding?.enterEmails?.error = resources.getString(R.string.emailOnlyEmpty)
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
            else if (firstName.trim() == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.firstName),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            } else if (lastName.trim() == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.lastName),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            } else if (password.trim() == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.enterPass),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            }
            else if (confPassword.trim() == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.enterConfPass),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            }
            else  if (password != confPassword) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.passNotMatch),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            }else if (confPassword!=password){
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.passNotMatch),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            } else if (phone.trim() == "") {
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
            else if (checkDob.trim() == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.enterDob),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()

            }else if (checkGender.trim() == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.selectGender),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            }else{
                resisters(
                    email, firstName, lastName, checkGender, password, checkDob, phone,
                    receiveMail, receiveMobileNot, receivedResNote
                )
                Constant().hideKeyboard(this)
            }

        }
    }

    private fun resisters(
        email: String,
        firstname: String,
        lastName: String,
        gender: String,
        password: String,
        dob: String,
        phNum: String,
        receiveMail: Boolean,
        receiveMobileNot: Boolean,
        receiveResNot: Boolean,
    ) {
        signupViewModel.resisters(
            this,
            email,
            countryCode,
            firstname,
            lastName,
            gender,
            password,
            dob,
            phNum,
            receiveMail,
            receiveMobileNot,
            receiveResNot
        )
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            if (Constant.status == it.data?.data?.result && Constant.SUCCESS_CODE == it.data.data.code) {
                                OtpDialog(it.data.data.output.userid)
                            } else {
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    it.data?.data?.msg.toString(),
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {
                                    },
                                    negativeClick = {
                                    })
                                dialog.show()
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

    private fun conditionals() {
        try {
            val termConditionAndPrivacy = TextUtils.concat(
                Constant().getSpanableText(
                    ForegroundColorSpan(getColor(R.color.text_color)),
                    ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)!!,
                    0,
                    30,
                    1.1f,
                    SpannableString(getString(R.string.by_signing_up_i_agree_to_the))
                ),
                Constant().getSpanableText1(
                    ForegroundColorSpan(getColor(R.color.white)),
                    ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)!!,
                    UnderlineSpan(),
                    0,
                    18,
                    1.1f,
                    SpannableString(getString(R.string.terms_amp_conditions))
                )

            )

            text_terms_conditions.text = termConditionAndPrivacy

        } catch (e: Exception) {
            println("termsCondition--->${e.message}")

        }

    }

    @SuppressLint("SimpleDateFormat")
    fun data(date: String): Int {
        if (!TextUtils.isEmpty(date)) {
            val cal = Calendar.getInstance()
            val dateInString = SimpleDateFormat("dd MMM, yyyy")
                .format(cal.time)
            val formatter = SimpleDateFormat("dd MMM, yyyy")
            var parsedDate: Date? = null
            var date1: Date? = null
            try {
                parsedDate = formatter.parse(dateInString)
                date1 = formatter.parse(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (parsedDate != null && date1 != null) return getDiffYears(date1, parsedDate)
        }
        return 0
    }

    private fun getDiffYears(first: Date, last: Date): Int {
        val a = getCalendar(first)
        val b = getCalendar(last)
        var diff = b[Calendar.YEAR] - a[Calendar.YEAR]
        if (a[Calendar.MONTH] > b[Calendar.MONTH] ||
            a[Calendar.MONTH] == b[Calendar.MONTH] && a[Calendar.DATE] > b[Calendar.DATE]
        ) {
            diff--
        }
        return diff
    }

    private fun getCalendar(date: Date): Calendar {
        val cal = Calendar.getInstance(Locale.US)
        cal.time = date
        return cal
    }

    @SuppressLint("SetTextI18n")
    private fun countryCodeLoad() {
        signupViewModel.countryCode(this)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    println("LocationResponse--->${it.data.output}")
                                    countryCodeList = it.data.output
                                    binding?.mobileCode?.setText(resources.getString(R.string.mobile) + "    " + it.data.output[0].isdCode)
                                    countryCode = it.data.output[0].isdCode
                                    retriveCountryList(it.data.output)
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

    private fun OtpVerify(otpVerifyRequest: OtpVerifyRequest) {
        signupViewModel.mVerify(otpVerifyRequest)
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
                                    if (from == "seat") {
                                        val intent = Intent(
                                            this,
                                            SeatScreenMainActivity::class.java
                                        ).putExtra("AREA_CODE", areaCode)
                                            .putExtra("TT_TYPE", ttType)
                                            .putExtra("CINEMA", cinemaName)
                                            .putExtra("SEAT_CAT", seatCat)
                                            .putExtra("SEAT_TYPE", seatType)
                                            .putExtra("DATE_POS", date_pos)
                                            .putExtra("SEAT_POS", seatQuanitity)
                                            .putExtra("DateTime", dateTime)
                                            .putExtra("MovieId", MovieId)
                                            .putExtra("CinemaID", CinemaID)
                                            .putExtra("DatePosition", DatePosition)
                                            .putExtra("dt", dt)
                                            .putExtra("FROM", "seat")
                                            .putExtra("SessionID", SessionID)
                                            .putExtra("SHOW_POS", show_pos)
                                            .putExtra("CINEMA_POS", cinema_pos)

                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        startActivity(intent)
                                        finish()
                                    } else {
//                                        val intent =Intent(this@SignUpActivity, HomeActivity::class.java)
                                        val intent =Intent(this@SignUpActivity, UserPreferencesActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        startActivity(intent)
                                        finish()
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

    private fun retriveCountryList(output: ArrayList<CountryCodeResponse.Output>) {
        binding?.mobileCode?.setOnClickListener {
            bottomDialog(output)
        }
    }

    override fun onItemClick(view: CountryCodeResponse.Output) {
        countryCode = view.isdCode
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
            binding?.mobileCode?.setText(resources.getString(R.string.mobile) + " " + countryCode)
        }
    }

    @SuppressLint("CutPasteId")
    private fun OtpDialog(userid: String) {
        val mDialogView = layoutInflater.inflate(R.layout.otp_dialog, null)
        val mBuilder = AlertDialog.Builder(this, R.style.MyDialogTransparent)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.setCancelable(false)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        val back = mDialogView.findViewById<ImageView>(R.id.imageView43)
        val submit = mDialogView.findViewById<TextView>(R.id.button2)
        back.setOnClickListener {
            mAlertDialog.dismiss()
        }
        submit.setOnClickListener {
            val phone = mDialogView.findViewById<TextInputEditText>(R.id.mobileCode).text.toString()
            val email = mDialogView.findViewById<TextInputEditText>(R.id.emailCode).text.toString()
            when {
                phone.trim() == "" -> {
                    mDialogView.findViewById<TextInputEditText>(R.id.mobileCode)?.error = ""
                }
                email.trim() == "" -> {
                    mDialogView.findViewById<TextInputEditText>(R.id.emailCode)?.error = ""
                }
                else -> {
                    mAlertDialog.dismiss()
                    Constant().hideKeyboard(this)
                    OtpVerify(OtpVerifyRequest(email, phone, userid))
                }
            }
        }
    }
}