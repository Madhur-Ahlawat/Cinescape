package com.cinescape1.ui.main.views.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.CountryCodeResponse
import com.cinescape1.data.models.responseModel.MoreTabResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.FragmentMorePageBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.viewModels.MoreInfoViewModel
import com.cinescape1.ui.main.views.LoginActivity
import com.cinescape1.ui.main.views.adapters.*
import com.cinescape1.utils.*
import com.cinescape1.utils.Constant.IntentKey.Companion.OPEN_FROM
import com.google.firebase.auth.FirebaseAuth
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_more_page.*
import kotlinx.android.synthetic.main.item_contactus.*
import kotlinx.android.synthetic.main.language_ui.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


class MorePageFragment : DaggerFragment(), CountryCodeAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private var binding: FragmentMorePageBinding? = null
    private var loader: LoaderDialog? = null
    private val moreInfoViewModel: MoreInfoViewModel by viewModels { viewModelFactory }
    private var responseData: MoreTabResponse? = null
    private var twitter_user_name: String? = "Cinescapekuwait"
    private var countryCodeList = ArrayList<CountryCodeResponse.Output>()
    private var mAdapter: CountryCodeAdapter? = null
    var countryCode: String = ""
    var dialog: Dialog? = null
    private var broadcastReceiver: BroadcastReceiver? = null
    private var mobile: String = ""
    private var frontPhoto: MultipartBody.Part? = null

    private val SELECT_PICTURE = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMorePageBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        return view!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //AppBar Hide
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requireActivity().window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE
        requireActivity().window.statusBarColor = Color.BLACK
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(requireActivity(), "ar")
                image_switcher?.isChecked = false
                println("getLocalLanguage--->${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}")

            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(requireActivity(), "en")
                image_switcher?.isChecked = true
            }
            else -> {
                image_switcher?.isChecked = true
            }
        }
        mobile = preferences.getString(Constant.MOBILE).toString()
        contactEmail.setText(preferences.getString(Constant.USER_EMAIL))
        enterUsername.setText(preferences.getString(Constant.USER_NAME))
        enter_mobile_numbers.setText(preferences.getString(Constant.MOBILE))
        image_switcher?.setOnCheckedChangeListener { _, b ->
            if (b) {
                Constant.IntentKey.LANGUAGE_SELECT = "en"
                LocaleHelper.setLocale(requireActivity(), "en")
                preferences.putString(Constant.IntentKey.SELECT_LANGUAGE, "en")
                OPEN_FROM = 1

//                preferences.putString(Constant.IntentKey.OPEN_FROM,"More")

//                OPEN_FROM=1
                val intent = requireActivity().intent
                requireActivity().overridePendingTransition(0, 0)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                requireActivity().finish()
                requireActivity().overridePendingTransition(0, 0)
                startActivity(intent)
            } else {
                Constant.IntentKey.LANGUAGE_SELECT = "ar"
                LocaleHelper.setLocale(requireActivity(), "ar")
                preferences.putString(Constant.IntentKey.SELECT_LANGUAGE, "ar")
                OPEN_FROM = 1

//                preferences.putString( Constant.IntentKey.OPEN_FROM,"More")
                val intent = requireActivity().intent
                requireActivity().overridePendingTransition(0, 0)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                requireActivity().finish()
                requireActivity().overridePendingTransition(0, 0)
                startActivity(intent)
            }
        }

        //location
        binding?.viewProfile?.setOnClickListener {
            binding?.imageUserProfile?.setColorFilter(requireActivity().getColor(R.color.white))
            binding?.textProfileTitle?.setTextColor(requireActivity().getColor(R.color.white))

            binding?.imageBooking?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textBookingTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imagePreference?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textPreference?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageRecharageWallet?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRecharageWallet?.setTextColor(requireActivity().getColor(R.color.text_color))

//            binding?.imageRefundCoin?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRefund?.setTextColor(requireActivity().getColor(R.color.text_color))


//            binding?.imageHistory?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textHistoery?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.textView35?.show()
            binding?.recyclerMore?.show()
            binding?.scrollNested?.hide()
            location(responseData?.output?.cinemas!!)
        }

        //faq
        binding?.viewBooking?.setOnClickListener {
            binding?.imageUserProfile?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textProfileTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageBooking?.setColorFilter(requireActivity().getColor(R.color.white))
            binding?.textBookingTitle?.setTextColor(requireActivity().getColor(R.color.white))

            binding?.imagePreference?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textPreference?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageRecharageWallet?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRecharageWallet?.setTextColor(requireActivity().getColor(R.color.text_color))

//            binding?.imageRefundCoin?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRefund?.setTextColor(requireActivity().getColor(R.color.text_color))

//            binding?.imageHistory?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textHistoery?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.textView35?.hide()
            binding?.scrollNested?.hide()
            binding?.recyclerMore?.show()
            faq(responseData?.output?.faqs!!)
        }

        //contact
        binding?.viewPreference?.setOnClickListener {
            binding?.imageUserProfile?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textProfileTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageBooking?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textBookingTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imagePreference?.setColorFilter(requireActivity().getColor(R.color.white))
            binding?.textPreference?.setTextColor(requireActivity().getColor(R.color.white))


            binding?.imageRecharageWallet?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRecharageWallet?.setTextColor(requireActivity().getColor(R.color.text_color))

//            binding?.imageRefundCoin?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRefund?.setTextColor(requireActivity().getColor(R.color.text_color))

//            binding?.imageHistory?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textHistoery?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.textView35?.hide()
            binding?.recyclerMore?.hide()
            binding?.scrollNested?.show()
        }

        //ageRating
        binding?.viewRecharge?.setOnClickListener {
            binding?.imageUserProfile?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textProfileTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageBooking?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textBookingTitle?.setTextColor(requireActivity().getColor(R.color.text_color))


            binding?.imagePreference?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textPreference?.setTextColor(requireActivity().getColor(R.color.text_color))


            binding?.imageRecharageWallet?.setColorFilter(requireActivity().getColor(R.color.white))
            binding?.textRecharageWallet?.setTextColor(requireActivity().getColor(R.color.white))


//            binding?.imageRefundCoin?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRefund?.setTextColor(requireActivity().getColor(R.color.text_color))


//            binding?.imageHistory?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textHistoery?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.textView35?.hide()
            binding?.recyclerMore?.show()
            binding?.scrollNested?.hide()
            ageRating(responseData?.output?.ratings!!)

        }

        //termsAndCondition
        binding?.viewRefund?.setOnClickListener {
            binding?.imageUserProfile?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textProfileTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageBooking?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textBookingTitle?.setTextColor(requireActivity().getColor(R.color.text_color))


            binding?.imagePreference?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textPreference?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageRecharageWallet?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRecharageWallet?.setTextColor(requireActivity().getColor(R.color.text_color))

//            binding?.imageRefundCoin?.setColorFilter(requireActivity().getColor(R.color.white))
            binding?.textRefund?.setTextColor(requireActivity().getColor(R.color.white))

//            binding?.imageHistory?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textHistoery?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.textView35?.hide()
            binding?.recyclerMore?.show()
            binding?.scrollNested?.hide()
            termsAndCondition(responseData?.output?.tncs!!)
        }

        //privacyPolicy
        binding?.viewHistorys?.setOnClickListener {
            binding?.imageUserProfile?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textProfileTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageBooking?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textBookingTitle?.setTextColor(requireActivity().getColor(R.color.text_color))


            binding?.imagePreference?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textPreference?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageRecharageWallet?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRecharageWallet?.setTextColor(requireActivity().getColor(R.color.text_color))

//            binding?.imageRefundCoin?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRefund?.setTextColor(requireActivity().getColor(R.color.text_color))

//            binding?.imageHistory?.setColorFilter(requireActivity().getColor(R.color.white))
            binding?.textHistoery?.setTextColor(requireActivity().getColor(R.color.white))


            scrollNested?.hide()
            binding?.textView35?.hide()
            binding?.recyclerMore?.show()
            privacyPolicy(responseData?.output?.privacy!!)
        }

        //Facebook
        imageView17.setOnClickListener {
            launchFacebook()
        }

        //Instagram
        imageView18.setOnClickListener {
            launchInsta()
        }

        //Twitter
        imageView19.setOnClickListener {
            openTwitter()
        }

        //youtube
        imageView20.setOnClickListener {
            youtube()
        }

        textView29.setOnClickListener {
//            if (requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions( arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), myGalleryPermissionCode)
//            } else {
//
//                val i = Intent()
//                i.type = "image/*"
//                i.action = Intent.ACTION_GET_CONTENT
//                startActivityForResult(Intent.createChooser(i, "Select Picture"), selectPicture)
//            }

            handlePermission()
        }

        textView18.setOnClickListener {
            val username = enterUsername.text.toString()
            val email = contactEmail.text.toString()
            val mobile = enter_mobile_numbers.text.toString()
            val name = editTextTextPersonName.text.toString()


            if (username.isNullOrEmpty()) {
                val dialog = OptionDialog(requireContext(),
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
            } else if (!InputTextValidator.validateEmail(contactEmail)) {
                if (contactEmail.text.toString().trim { it <= ' ' }.isEmpty()) {
                    val dialog = OptionDialog(requireContext(),
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
                    val dialog = OptionDialog(requireContext(),
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


            } else if (mobile.isNullOrEmpty()) {
                val dialog = OptionDialog(requireContext(),
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
            } else if (name.isNullOrEmpty()) {
                val dialog = OptionDialog(requireContext(),
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.enterMSG),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            } else if (frontPhoto == null) {
                val dialog = OptionDialog(requireContext(),
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.enterPhoto),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            } else {
                contactUs(email, name, mobile, username, frontPhoto)
                Constant().hideKeyboard(requireActivity())
            }

        }

        println("Mobile--->${mobile}")
        if (mobile == "") {
            text_signout.text = getString(R.string.sign_in)
            view_first.setOnClickListener {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                requireActivity().finish()
            }
        } else {
            text_signout.text = getString(R.string.sign_out)
            view_first.setOnClickListener {
                val dialog = OptionDialog(requireContext(),
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    it.resources.getString(R.string.signout),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                        FirebaseAuth.getInstance().signOut()
                        preferences.clearData()
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        requireActivity().finish()
                    },
                    negativeClick = {
                    })
                dialog.show()
            }
        }

        broadcastReceiver = MyReceiver()
        broadcastIntent()
        countryCodeLoad()
        moreTabs()
        contact()
    }

    /* Choose an image from Gallery */
    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun handlePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //ask for permission
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                SELECT_PICTURE
            )
        } else {
            openImageChooser()
        }
    }

    private fun broadcastIntent() {
        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    private fun openTwitter() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name=$twitter_user_name")
                )
            )
        } catch (e: java.lang.Exception) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/#!/$twitter_user_name")
                )
            )
        }
    }

    private fun launchFacebook() {
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.facebook.com/CinescapeKuwait/?view_public_for=84586906921")
            )
            startActivity(intent)
        } catch (e: java.lang.Exception) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://www.facebook.com/CinescapeKuwait")
                )
            )
        }
    }

    private fun youtube() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/c/CinescapeKuwait")
            )
        )

    }

    private fun launchInsta() {
        val uriForApp: Uri = Uri.parse("http://instagram.com/_u/cinescapekuwait/")
        val forApp = Intent(Intent.ACTION_VIEW, uriForApp)

        val uriForBrowser: Uri = Uri.parse("http://instagram.com/cinescapekuwait/")
        val forBrowser = Intent(Intent.ACTION_VIEW, uriForBrowser)

        forApp.component = ComponentName(
            "com.instagram.android",
            "com.instagram.android.activity.UrlHandlerActivity"
        )

        try {
            startActivity(forApp)
        } catch (e: ActivityNotFoundException) {
            startActivity(forBrowser)
        }
    }

    private fun moreTabs() {
        moreInfoViewModel.moreTabs()
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    println("SomethingWrong--->${it.data.msg}")
                                    try {
                                        binding?.moreLayout?.show()
                                        responseData = it.data
                                        location(responseData?.output?.cinemas!!)
                                    } catch (e: Exception) {
                                        println("exceptionMsg--->${e.message}")
                                    }

                                } else {
                                    println("Something Wrong")
                                }
                            }
                        }
                        Status.ERROR -> {
                            loader?.dismiss()
                            val dialog = OptionDialog(requireActivity(),
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
                            if (isAdded) {
                                loader = LoaderDialog(R.string.pleasewait)
                                loader?.show(requireActivity().supportFragmentManager, null)
                            }
                        }
                    }
                }
            }
    }

    private fun contactUs(
        email: String,
        name: String,
        mobile: String,
        username: String,
        Photo: MultipartBody.Part?,
    ) {
        moreInfoViewModel.contactUs(email, name, mobile, username, Photo)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    println("SomethingWrong--->${it.data.msg}")
                                    val dialog = OptionDialog(requireActivity(),
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        it.data.msg,
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                            contactEmail.text?.clear()
                                            enter_mobile_numbers.text?.clear()
                                            editTextTextPersonName.text?.clear()
                                            enterUsername.text?.clear()
                                            frontPhoto = null
                                        },
                                        negativeClick = {
                                        })
                                    dialog.show()
                                } else {
                                    println("Something Wrong")
                                }
                            }
                        }
                        Status.ERROR -> {
                            loader?.dismiss()
                            val dialog = OptionDialog(requireActivity(),
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
                            loader?.show(requireActivity().supportFragmentManager, null)
                        }
                    }
                }
            }
    }

    private fun contact() {

        imageView3.setOnClickListener {
            val phone = "1803456"
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }

        imageView4.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=7800049994"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    private fun faq(faqs: ArrayList<MoreTabResponse.Faq>) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = FaqAdapter(faqs, requireActivity())
        binding?.recyclerMore?.setHasFixedSize(true)
        binding?.recyclerMore?.layoutManager = layoutManager
        binding?.recyclerMore?.adapter = adapter
    }

    private fun privacyPolicy(privacy: ArrayList<MoreTabResponse.Privacy>) {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.recyclerMore?.setHasFixedSize(true)
        val adapter = PrivacyAdapter(privacy, requireContext())
        binding?.recyclerMore?.layoutManager = layoutManager
        binding?.recyclerMore?.adapter = adapter
    }

    private fun ageRating(ratings: ArrayList<MoreTabResponse.Rating>) {
        println("ageRating--->${ratings}")
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.recyclerMore?.setHasFixedSize(true)
        val adapter = AgeRatingAdapter(ratings, requireContext())
        binding?.recyclerMore?.layoutManager = layoutManager
        binding?.recyclerMore?.adapter = adapter
    }

    private fun termsAndCondition(tunics: ArrayList<MoreTabResponse.Tnc>) {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.recyclerMore?.setHasFixedSize(true)
        val adapter = TermsConditionAdapter(tunics, requireContext())
        binding?.recyclerMore?.layoutManager = layoutManager
        binding?.recyclerMore?.adapter = adapter

    }

    private fun location(location: ArrayList<MoreTabResponse.Cinema>) {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.recyclerMore?.setHasFixedSize(true)
        val adapter = LocationAdapter(location, requireContext())
        binding?.recyclerMore?.layoutManager = layoutManager
        binding?.recyclerMore?.adapter = adapter

    }

    private fun countryCodeLoad() {
        moreInfoViewModel.countryCode(requireActivity())
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    println("LocationResponse--->${it.data.output}")
                                    countryCodeList = it.data.output
                                    enter_mobile_code.text = it.data.output[0].isdCode
                                    val maxLengthEditText = it.data.output[0].phoneLength
                                    enter_mobile_numbers.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLengthEditText ))

                                    retrieveCountryList(it.data.output)

                                } else {
                                    println("Something Wrong")
                                }
                            }
                        }
                        Status.ERROR -> {
                            loader?.dismiss()
                            val dialog = OptionDialog(requireActivity(),
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
                        }
                    }
                }
            }
    }

    private fun retrieveCountryList(output: ArrayList<CountryCodeResponse.Output>) {
        enter_mobile_code.setOnClickListener {
            bottomDialog(output)
        }
    }

    private fun bottomDialog(countryList: ArrayList<CountryCodeResponse.Output>) {
        val mDialogView = layoutInflater.inflate(R.layout.countrycode_new_dialog, null)
        val mBuilder = AlertDialog.Builder(requireActivity(), R.style.MyDialogTransparent)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.setCancelable(false)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        val recyclerView = mDialogView.findViewById<RecyclerView>(R.id.NewCountryRecycler)
        val cancel = mDialogView.findViewById<View>(R.id.view73)
        val edSearch = mDialogView.findViewById<EditText>(R.id.searchData)
        val proceed = mDialogView.findViewById<View>(R.id.textView57)
        mAdapter = CountryCodeAdapter(countryCodeList, this, requireActivity())
        recyclerView.adapter = mAdapter
        //Recycler
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
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
            enter_mobile_code.text = countryCode
        }

    }

    override fun onItemClick(view: CountryCodeResponse.Output) {
        println("PhoneLength--->${view.phoneLength}")
        countryCode = view.isdCode
        val maxLengthEditText = view.phoneLength
        enter_mobile_numbers.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLengthEditText ))

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            SELECT_PICTURE -> {
                var i = 0
                while (i < permissions.size) {
                    val permission = permissions[i]
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        val showRationale =
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                requireActivity(),
                                permission
                            )
                        if (showRationale) {
                            textView29.performClick()
                            //  Show your own message here
                        } else {
                            showSettingsAlert()
                        }
                    }
                    i++
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showSettingsAlert() {
        val alertDialog = android.app.AlertDialog.Builder(requireActivity()).create()
        alertDialog.setTitle("Alert")
        alertDialog.setMessage("App needs to access the Camera.")
        alertDialog.setButton(
            android.app.AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW"
        ) { dialog, _ ->
            dialog.dismiss()
            //finish();
        }
        alertDialog.setButton(
            android.app.AlertDialog.BUTTON_POSITIVE, "SETTINGS"
        ) { dialog, _ ->
            dialog.dismiss()
            openAppSettings(requireActivity())
        }
        alertDialog.show()
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Thread {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                if (requestCode == SELECT_PICTURE) {
                    // Get the url from data
                    val selectedImageUri = data!!.data
                    if (null != selectedImageUri) {
                        // Get the path from the Uri
//                        val path = getPathFromURI(selectedImageUri)

                        val mFileTemp =
                            File(ImageFilePath.getFilePath(requireContext(), selectedImageUri))
                        val requestFile: RequestBody =
                            mFileTemp.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        val file =
                            MultipartBody.Part.createFormData("file", mFileTemp.name, requestFile)
//                        updateImage(file)
                        frontPhoto = file

                    }
                }
            }
        }.start()
    }

    companion object {
        fun openAppSettings(context: Activity?) {
            if (context == null) {
                return
            }
            val i = Intent()
            i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            i.addCategory(Intent.CATEGORY_DEFAULT)
            i.data = Uri.parse("package:" + context.packageName)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            context.startActivity(i)
        }
    }

}