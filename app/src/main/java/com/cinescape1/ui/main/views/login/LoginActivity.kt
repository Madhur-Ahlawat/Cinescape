package com.cinescape1.ui.main.views.login

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.os.Bundle
import android.text.*
import android.text.InputFilter.LengthFilter
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.WindowManager.LayoutParams
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.cinescape1.databinding.ActivityLoginBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.login.viewModel.LoginViewModel
import com.cinescape1.ui.main.views.*
import com.cinescape1.ui.main.views.activeWallet.ActivateWalletActivity
import com.cinescape1.ui.main.views.adapters.CountryCodeAdapter
import com.cinescape1.ui.main.views.details.ShowTimesActivity
import com.cinescape1.ui.main.views.home.HomeActivity
import com.cinescape1.ui.main.views.login.guest.ContinueGuestActivity
import com.cinescape1.ui.main.views.login.resetPassword.ResetPasswordActivity
import com.cinescape1.ui.main.views.payment.PaymentWebActivity
import com.cinescape1.ui.main.views.prefrence.UserPreferencesActivity
import com.cinescape1.ui.main.views.seatLayout.SeatScreenMainActivity
import com.cinescape1.utils.*
import com.cinescape1.utils.Constant.Companion.SUCCESS_CODE
import com.facebook.*
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookSdk.addLoggingBehavior
import com.facebook.FacebookSdk.sdkInitialize
import com.facebook.FacebookSdk.setIsDebugEnabled
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.android.support.DaggerAppCompatActivity
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS as FLAG_TRANSLUCENT_STATUS1


class LoginActivity : DaggerAppCompatActivity(), CountryCodeAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var loader: LoaderDialog? = null

    @Inject
    lateinit var preferences: AppPreferences
    private val loginViewModel: LoginViewModel by viewModels { viewModelFactory }
    private var binding: ActivityLoginBinding? = null
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
    private var email: String? = null
    private var facebook_uid: String? = null
    private var id: String? = null
    private var first_name: String? = null
    private var last_name: String? = null
    private var social_id: String? = null
    private var name: String? = null
    private var picture: String? = null

    private companion object {
        private const val RC_SIGN_IN = 120
    }

    private var gAuth: FirebaseAuth? = null
    private lateinit var googleSignInClient: GoogleSignInClient
    private var fAuth: FirebaseAuth? = null
    private var callbackManager: CallbackManager? = null

    private var from = ""
    private var BOOKING = ""

    var access_token: AccessToken? = null
    var request: GraphRequest? = null

    private var gender: String = ""
    private var receiveMail: Boolean = false
    private var receiveMobileNot: Boolean = false
    private var receivedResNote: Boolean = false
    var dialog: Dialog? = null
    private var countryCodeList = ArrayList<CountryCodeResponse.Output>()
    private var mAdapter: CountryCodeAdapter? = null
    var countryCode: String = ""
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
//                preferences.putString(Constant.IntentKey.LANGUAGE, "ar")

                binding?.imageSwitcher?.isChecked = false
                println("getLocalLanguage--->${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}")
            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(this, "en")
//                preferences.putString(Constant.IntentKey.LANGUAGE, "en")
                binding?.imageSwitcher?.isChecked = true
            }
            else -> {
                binding?.imageSwitcher?.isChecked = true
            }
        }
        setContentView(view)

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

        if (intent.hasExtra("from")) {
            println("Details--->1231")

            ttType = intent.getStringExtra("type").toString()
            MovieId = intent.getStringExtra("movieId").toString()
            from = intent.getStringExtra("from").toString()

        }
        if (intent.hasExtra("Payment")) {
            BOOKING = intent.getStringExtra("BOOKING").toString()
            from = intent.getStringExtra("FROM").toString()

        }
        if (intent.hasExtra("BOOKING")) {
            BOOKING = intent.getStringExtra("BOOKING").toString()
            println("FromCome--->${BOOKING}")
        }

        //AppBar Hide
        window.apply {
            clearFlags(FLAG_TRANSLUCENT_STATUS1)
            addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        FirebaseApp.initializeApp(this@LoginActivity)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("405271387976-7jsu83n4m4647tb4lt58g45rpnjqculg.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        gAuth = FirebaseAuth.getInstance()
        fAuth = FirebaseAuth.getInstance()
        callbackManager = create()
        broadcastReceiver = MyReceiver()
        broadcastIntent()

        movedNext()
        countryCodeLoad()
        conditionals()
    }

    private fun continueGuest(
        email: String,
        name: String,
        socialId: String,
        type: String,
        idToken: String
    ) {
        preferences.putBoolean(Constant.IS_LOGIN, true)
        preferences.putString(Constant.FIRST_NAME, name)
        preferences.putString(Constant.TYPE_LOGIN, type)
        preferences.putString(Constant.USER_EMAIL, email)
        preferences.putString(Constant.ID_TOKEN, socialId)
        val intent =
            Intent(
                this@LoginActivity,
                ContinueGuestActivity::class.java
            )
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun broadcastIntent() {
        @Suppress("DEPRECATION")
        registerReceiver(broadcastReceiver, IntentFilter(CONNECTIVITY_ACTION))
    }

    private fun movedNext() {
        val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
        val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)

        //terms Condition
        binding?.textTermsConditions?.setOnClickListener {
            val intent = Intent(
                this,
                PaymentWebActivity::class.java
            )
            intent.putExtra("From", "login")
            intent.putExtra("PAY_URL", Constant.termsCondition)
            startActivity(intent)
        }

        binding?.textView81?.setOnClickListener {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        //signingUi
        binding?.textView2?.setOnClickListener {
            binding?.textView2?.typeface = bold
            binding?.textView108?.typeface = regular

            binding?.signupUi?.hide()
            binding?.signInUi?.show()

            binding?.textView2?.setBackgroundResource(R.drawable.signin_bt_ui)
            binding?.textView108?.setBackgroundResource(R.drawable.signup_ui_trans)

        }
        //signUpUi
        binding?.textView108?.setOnClickListener {

            binding?.textView2?.typeface = regular
            binding?.textView108?.typeface = bold

            binding?.signInUi?.hide()
            binding?.signupUi?.show()

            binding?.textView2?.setBackgroundResource(R.drawable.signin_bt_ui_trans)
            binding?.textView108?.setBackgroundResource(R.drawable.signup_ui)

        }

        binding?.txtForgotPassword?.setOnClickListener {
            val intent = Intent(this@LoginActivity, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        binding?.imageGoogle?.setOnClickListener {
            googleSignIn()
        }

        binding?.imageSwitcher?.setOnCheckedChangeListener { compoundButton, b ->
            println("compoundButton---$b")
            if (b) {
                Constant.IntentKey.LANGUAGE_SELECT = "en"
                LocaleHelper.setLocale(this, "en")
                preferences.putString(Constant.IntentKey.SELECT_LANGUAGE, "en")
                preferences.putString(Constant.IntentKey.LANGUAGE, "en")

                val intent = intent
                overridePendingTransition(0, 0)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)

            } else {

                Constant.IntentKey.LANGUAGE_SELECT = "ar"
                LocaleHelper.setLocale(this, "ar")
                preferences.putString(Constant.IntentKey.SELECT_LANGUAGE, "ar")
                preferences.putString(Constant.IntentKey.LANGUAGE, "ar")

                val intent = intent
                overridePendingTransition(0, 0)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
            }
        }

        binding?.imageFacebook?.setOnClickListener {
            facebookLogin()
        }

        binding?.textClubActivate?.setOnClickListener {
            val intent = Intent(this@LoginActivity, ActivateWalletActivity::class.java)
            startActivity(intent)
        }



        binding?.txtContinueGuestUi?.setOnClickListener {
            val intent = Intent(this@LoginActivity, ContinueGuestActivity::class.java)
            startActivity(intent)
        }

        binding?.textProceedUi?.setOnClickListener {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding?.btnSignIn?.setOnClickListener {
            val username = binding?.enterUsername?.text.toString()
            val password = binding?.enterpassword?.text.toString()
            when {
                username.trim() == "" -> {

                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        resources.getString(R.string.emailEmpty),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
                password.trim() == "" -> {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        resources.getString(R.string.passwordEmpty),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
                else -> {
                    signingUsingApi(username, password)
                    Constant().hideKeyboard(this)
                }
            }
        }

        //SignUp Clicks

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
                } else {
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

            } else if (firstName.trim() == "") {
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
            } else if (confPassword.trim() == "") {
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
            } else if (password != confPassword) {
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
            } else if (confPassword != password) {
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
            } else if (checkDob.trim() == "") {
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

            } else if (checkGender.trim() == "") {
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
            } else {
                resisters(
                    email, firstName, lastName, checkGender, password, checkDob, phone,
                    receiveMail, receiveMobileNot, receivedResNote
                )
                Constant().hideKeyboard(this)
            }

        }

    }

    private fun facebookLogin() {
        sdkInitialize(this.applicationContext)
        if (BuildConfig.DEBUG) {
            setIsDebugEnabled(true)
            addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
        }
        LoginManager.getInstance().logInWithReadPermissions(
            (this@LoginActivity as Activity?)!!,
            Arrays.asList("email", "public_profile")
        )
        callbackManager = create()
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("response Success", "Login")
                    access_token = loginResult.accessToken
                    Log.d("response access_token", access_token.toString())
                    request =
                        GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), object :
                            GraphRequest.GraphJSONObjectCallback {
                            override fun onCompleted(
                                `object`: JSONObject?,
                                response: GraphResponse?
                            ) {
                                val json = response!!.getJSONObject()
                                try {
                                    if (json != null) {
                                        Log.d("response", json.toString())
                                        try {
                                            email = json.getString("email")
                                        } catch (e: java.lang.Exception) {
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "Sorry!!! Your email is not verified on facebook.",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            return
                                        }
                                        facebook_uid = json.getString("id")
                                        social_id = json.getString("id")
                                        first_name = json.getString("first_name")
                                        last_name = json.getString("last_name")
                                        name = json.getString("name")
                                        picture =
                                            "https://graph.facebook.com/$facebook_uid/picture?type=large"
                                        Log.d("response", " picture$picture")
//                                        Picasso.with(context).load(picture)
//                                            .placeholder(R.mipmap.ic_launcher).into(userIv)
//                                        mPb.setVisibility(View.GONE)
                                        continueGuest(
                                            email.toString(),
                                            first_name.toString() + " " + last_name,
                                            social_id.toString(),
                                            "Google",
                                            facebook_uid.toString()
                                        )
                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                    Log.d("response problem", "problem" + e.message)
                                }
                            }
                        })

                    val parameters = Bundle()
                    parameters.putString(
                        "fields",
                        "id,name,first_name,last_name,link,email,picture"
                    )
                    request!!.parameters
                    request!!.executeAsync()
                }

                override fun onCancel() {
                    Toast.makeText(this@LoginActivity, "Login Cancel", Toast.LENGTH_LONG).show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun signingUsingApi(name: String, password: String) {
        loginViewModel.userLogin(this, name, password)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            if (Constant.status == it.data?.data?.result && SUCCESS_CODE == it.data.data.code) {
                                preferences.putString(Constant.USER_ID, it.data.data.output.userId)
                                preferences.putBoolean(
                                    Constant.IS_LOGIN, true
                                )
                                preferences.putString(
                                    Constant.USER_NAME,
                                    it.data.data.output.userName
                                )
                                preferences.putString(
                                    Constant.FIRST_NAME,
                                    it.data.data.output.firstName
                                )
                                preferences.putString(
                                    Constant.LAST_NAME,
                                    it.data.data.output.lastName
                                )
                                preferences.putString(Constant.USER_DOB, it.data.data.output.dob)
                                preferences.putString(
                                    Constant.MOBILE,
                                    it.data.data.output.mobilePhone
                                )
                                preferences.putString(
                                    Constant.USER_EMAIL,
                                    it.data.data.output.email
                                )
                                preferences.putString(
                                    Constant.USER_GENDER,
                                    it.data.data.output.gender
                                )
                                preferences.putString(
                                    Constant.USER_CITY,
                                    it.data.data.output.mobilePhone
                                )
                                preferences.putString(
                                    Constant.COUNTRY_CODE,
                                    it.data.data.output.countryCode
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
                                } else if (BOOKING == "FOOD") {
                                    val intent = Intent(
                                        this,
                                        HomeActivity::class.java
                                    ).putExtra("BOOKING", "FOOD")
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(intent)
                                    finish()
                                } else if (from == "Details") {
                                    println("Details--->123")
                                    val intent =
                                        Intent(this@LoginActivity, ShowTimesActivity::class.java)
                                            .putExtra("type", ttType)
                                            .putExtra("from", show_pos)
                                            .putExtra("movieId", MovieId)

                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()

                                } else {
                                    val intent =
                                        Intent(this@LoginActivity, HomeActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
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

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if (fAuth != null) {
            val currentUser = fAuth!!.currentUser
            updateUI(currentUser)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        try {
            println("userInfo ---> ${user!!.email}")
            println("userInfo1 ---> ${user.displayName}")
            println("userInfo2 ---> ${user.uid}")
        } catch (e: Exception) {
            println("error---->${e.message}")
        }
    }

    // google
    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("Login", "firebaseAuthWithGoogle:" + account.id)
                    first_name = account.displayName
                    email = account.email
                    social_id = account.id
//                    id=account.idToken
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("Login", "Google sign in failed", e)
                }

            } else {
                Toast.makeText(this, "" + exception, Toast.LENGTH_LONG).show()
                println("ExceptionMsg--->${exception}")
                Log.w("Login", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        gAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    continueGuest(
                        email.toString(),
                        first_name.toString(),
                        social_id.toString(),
                        "Google",
                        idToken
                    )
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithCredential:failure", task.exception)
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
        loginViewModel.resisters(
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
        loginViewModel.countryCode(this)
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

                                    val maxLengthEditText = it.data.output[0].phoneLength
                                    binding?.editTextPhone?.filters = arrayOf<InputFilter>(LengthFilter(maxLengthEditText))

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
        loginViewModel.mVerify(otpVerifyRequest)
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
                                    if (from == "Details") {
                                        println("Details--->1234")

                                        val intent =
                                            Intent(
                                                this@LoginActivity,
                                                ShowTimesActivity::class.java
                                            )
                                                .putExtra("type", ttType)
                                                .putExtra("from", show_pos)
                                                .putExtra("movieId", MovieId)

                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()

                                    }




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
                                        val intent = Intent(
                                            this@LoginActivity,
                                            UserPreferencesActivity::class.java
                                        )
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
        println("PhoneLength--->${view.phoneLength}")
        countryCode = view.isdCode
        val maxLengthEditText = view.phoneLength
        binding?.editTextPhone?.filters = arrayOf<InputFilter>(LengthFilter(maxLengthEditText))

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
