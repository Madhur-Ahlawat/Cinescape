package com.cinescape1.ui.main.views.login

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.*
import android.content.res.ColorStateList
import android.graphics.Paint
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.os.Bundle
import android.text.*
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CountryCodeResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityLoginBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.*
import com.cinescape1.ui.main.views.login.activeWallet.ActivateWalletActivity
import com.cinescape1.ui.main.views.adapters.CountryCodeAdapter
import com.cinescape1.ui.main.views.details.nowShowing.ShowTimesActivity
import com.cinescape1.ui.main.views.home.HomeActivity
import com.cinescape1.ui.main.views.login.guest.ContinueGuestActivity
import com.cinescape1.ui.main.views.login.otpVerification.OtpVerificationActivity
import com.cinescape1.ui.main.views.login.reponse.LoginResponse
import com.cinescape1.ui.main.views.login.resetPassword.ResetPasswordActivity
import com.cinescape1.ui.main.views.login.viewModel.LoginViewModel
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


@Suppress("DEPRECATION")
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
    private var datePosition = ""
    private var datePos = 0
    private var showPos = 0
    private var cinemaPos = 0
    private var seatQuantity = 0
    private var dateTime = ""
    private var movieId = ""
    private var cinemaID = ""
    private var sessionID = ""
    private var dt = ""
    private var email: String? = null
    private var facebookUid: String? = null
    private var firstName: String? = null
    private var lastName: String? = null
    private var socialId: String? = null
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
    private var booking = ""

    var accessToken: AccessToken? = null
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


    private val MyPREFERENCES = "MyPrefs"
    private var sharedpreferences: SharedPreferences? = null
    private val OnBoardingClick = "Name"
    private var clickOnBoarding: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
                binding?.imageSwitcher?.isChecked = false
                val regular = ResourcesCompat.getFont(this, R.font.gess_light)
                val bold = ResourcesCompat.getFont(this, R.font.gess_bold)
                val medium = ResourcesCompat.getFont(this, R.font.gess_medium)

                //Login
                //skip
                binding?.textView81?.typeface = bold
                //sign in
                binding?.textView2?.typeface = bold
                //sign up
                binding?.textView108?.typeface = regular
                //forgot password
                binding?.txtForgotPassword?.typeface = bold
                //btn Sign in
                binding?.btnSignIn?.typeface = bold
                //Continue in
                binding?.txtContinueGuest?.typeface = regular
                //guest
                binding?.guest?.typeface = bold
                //or
                binding?.textOr?.typeface = regular
                // club Card
                binding?.clubCard?.typeface = regular
                //Activate
                binding?.activate?.typeface = bold
                //English
                binding?.textSwitcher?.typeface = regular

                //signup
                binding?.textReceivePromotionEmail?.typeface = regular
                binding?.txtRNotification?.typeface = regular
                binding?.textReservationNotification?.typeface = regular
                binding?.textRecomNotification?.typeface = regular
                binding?.textTermsConditions?.typeface = regular



                println("getLocalLanguage--->${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}")
            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(this, "en")
                binding?.imageSwitcher?.isChecked = true

                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val medium = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)

                //skip
                binding?.textView81?.typeface = bold
                //sign in
                binding?.textView2?.typeface = bold
                //sign up
                binding?.textView108?.typeface = regular
                //forgot password
                binding?.txtForgotPassword?.typeface = bold
                //btn Sign in
                binding?.btnSignIn?.typeface = bold
                //Continue in
                binding?.txtContinueGuest?.typeface = regular
                //guest
                binding?.guest?.typeface = bold
                //or
                binding?.textOr?.typeface = regular
                // club Card
                binding?.clubCard?.typeface = regular
                //Activate
                binding?.activate?.typeface = bold
                //English
                binding?.textSwitcher?.typeface = regular

                //signup
                binding?.textReceivePromotionEmail?.typeface = regular
                binding?.txtRNotification?.typeface = regular
                binding?.textReservationNotification?.typeface = regular
                binding?.textRecomNotification?.typeface = regular
                binding?.textTermsConditions?.typeface = regular

            }
            else -> {
                binding?.imageSwitcher?.isChecked = true

                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val medium = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)

                //skip
                binding?.textView81?.typeface = bold
                //sign in
                binding?.textView2?.typeface = bold
                //sign up
                binding?.textView108?.typeface = regular
                //forgot password
                binding?.txtForgotPassword?.typeface = bold
                //btn Sign in
                binding?.btnSignIn?.typeface = bold
                //Continue in
                binding?.txtContinueGuest?.typeface = regular
                //guest
                binding?.guest?.typeface = bold
                //or
                binding?.textOr?.typeface = regular
                // club Card
                binding?.clubCard?.typeface = regular
                //Activate
                binding?.activate?.typeface = bold
                //English
                binding?.textSwitcher?.typeface = regular

                //signup
                binding?.textReceivePromotionEmail?.typeface = regular
                binding?.txtRNotification?.typeface = regular
                binding?.textReservationNotification?.typeface = regular
                binding?.textRecomNotification?.typeface = regular
                binding?.textTermsConditions?.typeface = regular

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
            datePos = intent.getIntExtra("DATE_POS", 0)
            showPos = intent.getIntExtra("SHOW_POS", 0)
            cinemaPos = intent.getIntExtra("CINEMA_POS", 0)
            seatQuantity = intent.getIntExtra("SEAT_POS", 0)


            dateTime = intent.getStringExtra("DateTime").toString()
            movieId = intent.getStringExtra("MovieId").toString()
            cinemaID = intent.getStringExtra("CinemaID").toString()
            sessionID = intent.getStringExtra("SessionID").toString()
            datePosition = intent.getStringExtra("DatePosition").toString()
            dt = intent.getStringExtra("dt").toString()
        }

        if (intent.hasExtra("from")) {
            println("Details--->1231")

            ttType = intent.getStringExtra("type").toString()
            movieId = intent.getStringExtra("movieId").toString()
            from = intent.getStringExtra("from").toString()

        }

        if (intent.hasExtra("Payment")) {
            booking = intent.getStringExtra("BOOKING").toString()
            from = intent.getStringExtra("FROM").toString()

        }

        if (intent.hasExtra("BOOKING")) {
            booking = intent.getStringExtra("BOOKING").toString()
        }

        //AppBar Hide
        Constant().hideKeyboard(this)

        //Preference Check Open
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        clickOnBoarding = sharedpreferences?.getBoolean(OnBoardingClick, false)!!

        callbackManager = create()
        broadcastReceiver = MyReceiver()

        socialCredential()
        broadcastIntent()
        movedNext()
        countryCodeLoad()
    }

    private fun socialCredential() {
        FirebaseApp.initializeApp(this@LoginActivity)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("405271387976-7jsu83n4m4647tb4lt58g45rpnjqculg.apps.googleusercontent.com")
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        gAuth = FirebaseAuth.getInstance()
        fAuth = FirebaseAuth.getInstance()

    }


    private fun broadcastIntent() {
        @Suppress("DEPRECATION") registerReceiver(
            broadcastReceiver, IntentFilter(CONNECTIVITY_ACTION)
        )
    }


    private fun movedNext() {
        val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
        val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)

        binding?.textTermsConditions1?.paintFlags =
            binding?.textTermsConditions1?.paintFlags?.or(Paint.UNDERLINE_TEXT_FLAG)!!

        //terms Condition
        binding?.textTermsConditions1?.setOnClickListener {
            val intent = Intent(
                this, PaymentWebActivity::class.java
            )
            intent.putExtra("From", "login")
            intent.putExtra("PAY_URL", Constant.termsConditions)
            startActivity(intent)
        }

        binding?.textView81?.setOnClickListener {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        //signingUi
        binding?.textView2?.setOnClickListener {
            Constant().appBarHide(this)
            binding?.textView2?.typeface = bold
            binding?.textView108?.typeface = regular

            binding?.signupUi?.hide()
            binding?.signInUi?.show()

            binding?.textView2?.setBackgroundResource(R.drawable.signin_bt_ui)
            binding?.textView108?.setBackgroundResource(R.drawable.signup_ui_trans)

        }
        //signUpUi
        binding?.textView108?.setOnClickListener {

            Constant().appBarHide(this)
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

        binding?.imageSwitcher?.setOnCheckedChangeListener { _, b ->
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
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                password.trim() == "" -> {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        resources.getString(R.string.passwordEmpty),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
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
            val mCurrentDate = Calendar.getInstance()
            var mYear = mCurrentDate[Calendar.YEAR]
            var mMonth = mCurrentDate[Calendar.MONTH]
            var mDay = mCurrentDate[Calendar.DAY_OF_MONTH]
            val mDatePicker = DatePickerDialog(
                this, { _, selectedYear, selectedMonth, selectedDay ->
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
            } else if (firstName.trim() == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.firstName),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (lastName.trim() == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.lastName),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (password.trim() == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.enterPass),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (confPassword.trim() == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.enterConfPass),
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
            } else if (confPassword != password) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.passNotMatch),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (phone.trim() == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.enterMo),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (checkDob.trim() == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.enterDob),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()

            } else if (checkGender.trim() == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.selectGender),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else {
                resisters(
                    email,
                    firstName,
                    lastName,
                    checkGender,
                    password,
                    checkDob,
                    phone,
                    receiveMail,
                    receiveMobileNot,
                    receivedResNote
                )
                Constant().hideKeyboard(this)
            }
        }
    }

/////////////////////////////////    Log In   /////////////////////////////////

    private fun signingUsingApi(name: String, password: String) {
        loginViewModel.userLogin(this, name, password).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        if (Constant.status == it.data?.data?.result && SUCCESS_CODE == it.data.data.code) {
                            when (it.data.data.output.otp_require) {
                                "BOTH" -> {
                                    val intent = Intent(this, OtpVerificationActivity::class.java)
                                    intent.putExtra("userId", it.data.data.output.userId)
                                    intent.putExtra("type", "login")
                                    intent.putExtra("verifyType", it.data.data.output.otp_require)
                                    startActivity(intent)
                                }
                                "MOBILE" -> {
                                    val intent = Intent(this, OtpVerificationActivity::class.java)
                                    intent.putExtra("userId", it.data.data.output.userId)
                                    intent.putExtra("type", "login")
                                    intent.putExtra("verifyType", it.data.data.output.otp_require)
                                    startActivity(intent)
                                }
                                "EMAIL" -> {
                                    val intent = Intent(this, OtpVerificationActivity::class.java)
                                    intent.putExtra("userId", it.data.data.output.userId)
                                    intent.putExtra("type", "login")
                                    intent.putExtra("verifyType", it.data.data.output.otp_require)
                                    startActivity(intent)
                                }
                                else -> {
                                    retrieveLoginData(it.data.data.output)
                                }
                            }
                        } else {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data?.data?.msg.toString(),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
                                negativeClick = {})
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

    private fun retrieveLoginData(output: LoginResponse.Output) {
        saveDataInPreference(output)

        if (from == "seat") {
            val intent = Intent(
                this, SeatScreenMainActivity::class.java
            ).putExtra("AREA_CODE", areaCode).putExtra("TT_TYPE", ttType)
                .putExtra("CINEMA", cinemaName).putExtra("SEAT_CAT", seatCat)
                .putExtra("SEAT_TYPE", seatType).putExtra("DATE_POS", datePos)
                .putExtra("SEAT_POS", seatQuantity).putExtra("DateTime", dateTime)
                .putExtra("MovieId", movieId).putExtra("CinemaID", cinemaID)
                .putExtra("DatePosition", datePosition).putExtra("dt", dt).putExtra("FROM", "seat")
                .putExtra("SessionID", sessionID).putExtra("SHOW_POS", showPos)
                .putExtra("CINEMA_POS", cinemaPos)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        } else if (booking == "FOOD") {
            val intent = Intent(this, HomeActivity::class.java).putExtra("BOOKING", "FOOD")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        } else if (from == "Details") {
            val intent =
                Intent(this@LoginActivity, ShowTimesActivity::class.java).putExtra("type", ttType)
                    .putExtra("from", showPos).putExtra("movieId", movieId)
                    .putExtra("Home", "homeBack")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()

        } else {
            if (!clickOnBoarding) {
                val intent = Intent(this@LoginActivity, UserPreferencesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

    }

    private fun saveDataInPreference(output: LoginResponse.Output) {
        preferences.putString(Constant.USER_ID, output.userId)
        preferences.putBoolean(
            Constant.IS_LOGIN, true
        )
        preferences.putString(
            Constant.USER_NAME, output.user.userName
        )
        preferences.putString(
            Constant.FIRST_NAME, output.user.firstName
        )
        preferences.putString(
            Constant.LAST_NAME, output.user.lastName
        )
        preferences.putString(Constant.USER_DOB, output.user.dob)
        preferences.putString(
            Constant.MOBILE, output.user.mobilePhone
        )
        preferences.putString(
            Constant.USER_EMAIL, output.user.email
        )
        preferences.putString(
            Constant.USER_GENDER, output.user.gender
        )
        preferences.putString(
            Constant.USER_CITY, output.user.mobilePhone
        )
        preferences.putString(
            Constant.COUNTRY_CODE, output.user.countryCode
        )
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


    ////////////////////////////////   Resister Data//////////////////////////////

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
        ).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        if (Constant.status == it.data?.data?.result && SUCCESS_CODE == it.data.data.code) {
                            preferences.putString(Constant.USER_ID,it.data.data.output.userId)
                            val intent = Intent(
                                this, OtpVerificationActivity::class.java)
                            intent.putExtra("userId", it.data.data.output.userId)
                            intent.putExtra("type", "signUp")
                            intent.putExtra("verifyType","")
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)

                            finish()
                        } else {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data?.data?.msg.toString(),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
                                negativeClick = {})
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


    @SuppressLint("SimpleDateFormat")
    fun data(date: String): Int {
        if (!TextUtils.isEmpty(date)) {
            val cal = Calendar.getInstance()
            val dateInString = SimpleDateFormat("dd MMM, yyyy").format(cal.time)
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
        if (a[Calendar.MONTH] > b[Calendar.MONTH] || a[Calendar.MONTH] == b[Calendar.MONTH] && a[Calendar.DATE] > b[Calendar.DATE]) {
            diff--
        }
        return diff
    }

    private fun getCalendar(date: Date): Calendar {
        val cal = Calendar.getInstance(Locale.US)
        cal.time = date
        return cal
    }

    //////////////////////////////  Country Code //////////////////////////

    @SuppressLint("SetTextI18n")
    private fun countryCodeLoad() {
        loginViewModel.countryCode(this).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == SUCCESS_CODE) {
                                countryCodeList = it.data.output
                                binding?.mobileCode?.setText(resources.getString(R.string.mobile) + "    " + it.data.output[0].isdCode)
                                countryCode = it.data.output[0].isdCode

                                val maxLengthEditText = it.data.output[0].phoneLength
                                binding?.editTextPhone?.filters =
                                    arrayOf<InputFilter>(LengthFilter(maxLengthEditText))

                                retrieveCountryList(it.data.output)
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

    private fun retrieveCountryList(output: ArrayList<CountryCodeResponse.Output>) {
        binding?.mobileCode?.setOnClickListener {
            bottomDialog(output)
        }
    }

    override fun onItemClick(view: CountryCodeResponse.Output, check: Boolean) {
        countryCode = view.isdCode
        val maxLengthEditText = view.phoneLength
        binding?.editTextPhone?.filters = arrayOf<InputFilter>(LengthFilter(maxLengthEditText))
    }

    private fun bottomDialog(countryList: ArrayList<CountryCodeResponse.Output>) {
        val mDialogView = layoutInflater.inflate(R.layout.countrycode_new_dialog, null)
        val mBuilder = AlertDialog.Builder(this, R.style.MyDialogTransparent).setView(mDialogView)
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


    ///////////////////////////////  Social Login  ///////////////////////////


    // google Login
    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    //faceBookLogin
    private fun facebookLogin() {
        sdkInitialize(this.applicationContext)
        if (BuildConfig.DEBUG) {
            setIsDebugEnabled(true)
            addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
        }
        LoginManager.getInstance().logInWithReadPermissions(
            (this@LoginActivity as Activity?)!!, listOf("email", "public_profile")
        )
        callbackManager = create()
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    accessToken = result.accessToken
                    println("access_token---->${accessToken}")
                    request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        object : GraphRequest.GraphJSONObjectCallback {
                            override fun onCompleted(
                                obj: JSONObject?, response: GraphResponse?
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
                                        facebookUid = json.getString("id")
                                        socialId = json.getString("id")
                                        firstName = json.getString("first_name")
                                        lastName = json.getString("last_name")
                                        name = json.getString("name")
                                        picture =
                                            "https://graph.facebook.com/$facebookUid/picture?type=large"

                                        continueGuest(
                                            email.toString(),
                                            firstName.toString() + " " + lastName,
                                            socialId.toString(),
                                            "Google"
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
                        "fields", "id,name,first_name,last_name,link,email,picture"
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
                    firstName = account.displayName
                    email = account.email
                    socialId = account.id
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
        gAuth!!.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                continueGuest(
                    email.toString(), firstName.toString(), socialId.toString(), "Google"
                )
            } else {
                println("login--->${task.exception}")
            }
        }
    }


    private fun continueGuest(
        email: String, name: String, socialId: String, type: String
    ) {
        preferences.putBoolean(Constant.IS_LOGIN, true)
        preferences.putString(Constant.FIRST_NAME, name)
        preferences.putString(Constant.TYPE_LOGIN, type)
        preferences.putString(Constant.USER_EMAIL, email)
        preferences.putString(Constant.ID_TOKEN, socialId)
        val intent = Intent(
            this@LoginActivity, ContinueGuestActivity::class.java
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if (fAuth != null) {
            val currentUser = fAuth!!.currentUser
            updateUI(currentUser)
        }
    }
}

