package com.cinescape1.ui.main.views.home.fragments.account

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalEnvironment
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalRenderType
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalUiType
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalActionCode
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalConfigurationParameters
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalInitService
import com.cardinalcommerce.shared.userinterfaces.UiCustomization
import com.cinescape1.R
import com.cinescape1.data.models.ModelPreferenceAgeRating
import com.cinescape1.data.models.ModelPreferenceCategory
import com.cinescape1.data.models.ModelPreferenceExperience
import com.cinescape1.data.models.ModelPreferenceType
import com.cinescape1.data.models.requestModel.*
import com.cinescape1.data.models.responseModel.*
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.FragmentAccountPageBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.adapters.CountryCodeAdapter
import com.cinescape1.ui.main.views.adapters.ExperienceAdapter
import com.cinescape1.ui.main.views.adapters.accountPageAdapters.AdapterBookingHistory
import com.cinescape1.ui.main.views.adapters.accountPageAdapters.UpcomingBookingAdapter
import com.cinescape1.ui.main.views.home.adapter.CustomSpinnerAdapter
import com.cinescape1.ui.main.views.home.fragments.account.adapter.RechargeSpinnerAdapter
import com.cinescape1.ui.main.views.home.fragments.account.response.RechargeAmountResponse
import com.cinescape1.ui.main.views.home.fragments.account.viewModel.AccountFragViewModel
import com.cinescape1.ui.main.views.login.LoginActivity
import com.cinescape1.ui.main.views.payment.PaymentWebActivity
import com.cinescape1.utils.*
import com.cinescape1.utils.Constant.IntentKey.Companion.OPEN_FROM
import com.google.android.flexbox.FlexboxLayout
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.threatmetrix.TrustDefender.TMXConfig
import com.threatmetrix.TrustDefender.TMXProfiling
import com.threatmetrix.TrustDefender.TMXProfilingConnections.TMXProfilingConnections
import com.threatmetrix.TrustDefender.TMXProfilingConnectionsInterface
import com.threatmetrix.TrustDefender.TMXProfilingOptions
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.account_history_layout.*
import kotlinx.android.synthetic.main.account_preference_layout.*
import kotlinx.android.synthetic.main.account_profile_layout.*
import kotlinx.android.synthetic.main.account_recharge_card_layout.*
import kotlinx.android.synthetic.main.account_refund_layout.*
import kotlinx.android.synthetic.main.cancel_dialog.*
import kotlinx.android.synthetic.main.cancel_dialog.view.*
import kotlinx.android.synthetic.main.checkout_creditcart_payment_alert.*
import kotlinx.android.synthetic.main.fragment_account_page.*
import kotlinx.android.synthetic.main.seat_category_item.*
import org.json.JSONArray
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Suppress("DEPRECATION")
class AccountPageFragment : DaggerFragment(), CountryCodeAdapter.RecycleViewItemClickListener,
    ExperienceAdapter.RecycleViewItemClickListener,
    UpcomingBookingAdapter.RecycleViewItemClickListener,
    UpcomingBookingAdapter.ReesendMailItemClickListener, AdapterBookingHistory.typeFaceItem {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private var binding: FragmentAccountPageBinding? = null
    private var loader: LoaderDialog? = null
    private var profileList: ArrayList<ProfileResponse.Output.Experience>? = null
    private val accountFragViewModel: AccountFragViewModel by viewModels { viewModelFactory }
    private val list: ArrayList<ModelPreferenceExperience> = arrayListOf(
        ModelPreferenceExperience(R.drawable.img_vip, 0),
        ModelPreferenceExperience(R.drawable.img_imax, 0),
        ModelPreferenceExperience(R.drawable.img_4dx, 0),
        ModelPreferenceExperience(R.drawable.img_dolby, 0),
        ModelPreferenceExperience(R.drawable.img_3d, 0),
        ModelPreferenceExperience(R.drawable.img_eleven, 0),
        ModelPreferenceExperience(R.drawable.img_2d, 0),
        ModelPreferenceExperience(R.drawable.img_screenx, 0)
    )

    private var userName: String = ""
    private var firstName: String = ""
    private var lastName: String = ""
    private var email: String = ""
    private var mobile: String = ""
    private var countryCode: String = ""
    private var dob: String = ""
    private var gender: String = ""
    private var type: String = ""
    private var rechargeAmount: String = ""
    private var countryCodeList = ArrayList<CountryCodeResponse.Output>()
    private var getAmountList = ArrayList<RechargeAmountResponse.Output.Amount>()
    private var mAdapter: CountryCodeAdapter? = null
    private var seatAbility: Int = 0
    private var bookingText: String = ""
    private var broadcastReceiver: BroadcastReceiver? = null
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var latitude: String = ""
    private var longitude: String = ""

    private var cinema: String = ""
    private var arbic: Boolean = false

    private var cinemaId = ""
    private var rechargeType = ""

    private var seatCategory: String = ""
    private var seatType: String = ""
    private val experience: ArrayList<String> = ArrayList()
    private var ageRating: ArrayList<String> = ArrayList()

    private var locationlist = ArrayList<FoodResponse.Output.Cinema>()

    private var clickEnable: Int = 0


    //CC
    private var m_sessionID = ""
    private var refId = ""
    private var VISA_PREFIX = "4"
    private var MASTERCARD_PREFIX = "51,52,53,54,55,2222,22"
    private var DISCOVER_PREFIX = "6011,65"
    private var AMEX_PREFIX = "34,37,"
    private var JCB = "2131,1800"
    private var MAESTRO = "5033,5868,6759,5641"
    private var UATP = "1354"
    private var from = ""
    private var cardinal = Cardinal.getInstance()
    private var ccCardNo = ""
    private var ccCardName = ""
    private var ccCardCvv = ""
    private var ccCardExpiryYear = ""
    private var ccCardExpiryMonth = ""
    var proceedAlertDialog: AlertDialog? = null

    // typeFace
    var textBookingHistoryTitle1: TextView?= null
    var textBookingHistoryDate1: TextView?= null
    var textBookingHistoryTime1: TextView?= null

    var textAddress1: TextView?= null
    var textviewScreenNumber1: TextView?= null
    var textviewDateInfo1: TextView?= null

    var textviewTimeInfo1: TextView?= null
    var textviewExperienceName1: TextView?= null
    var textviewSeatName1: TextView?= null

    var textKdTicketPrice1: TextView?= null
    var payDone1: TextView?= null
    var rechargeTime1: TextView?= null
    var rechargePrice1: TextView?= null
    var rechargeDate1: TextView?= null
    var paidBy1: TextView?= null
    var foodTotalPrice1: TextView?= null
    var foodPaidby1: TextView?= null

    var categoryName: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountPageBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        return view!!
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Show status bar
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        requireActivity().window.statusBarColor = Color.BLACK

        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(requireActivity(), "ar")
                binding?.imageSwitcherLang?.isChecked = false
                arbic = true
                println("getLocalLanguage--->${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}")

                val regular = ResourcesCompat.getFont(requireActivity(), R.font.gess_light)
                val bold = ResourcesCompat.getFont(requireActivity(), R.font.gess_bold)
                val medium = ResourcesCompat.getFont(requireActivity(), R.font.gess_medium)

                binding?.textSwitcher?.typeface = regular
                binding?.textArabic?.typeface = regular
                binding?.textUserAccountName?.typeface = bold
                binding?.textWalletUserId?.typeface = regular
                binding?.textUserWalletKd?.typeface = regular
                binding?.textProfileTitle?.typeface = regular
                binding?.textBookingTitle?.typeface = regular
                binding?.textPreference?.typeface = regular
                binding?.textRecharageWallet?.typeface = regular
                binding?.textSignout?.typeface = regular
                binding?.textUpcomingBooking?.typeface = bold

                // preferences include text
                text_preferences.typeface = bold
                text_locations.typeface = bold
                text_choose_preferencess.typeface = regular
                text_find_near_location.typeface = regular
                text_seat_category_preference.typeface = bold
                text_seat_type_preference.typeface = bold
                text_age_rating_preference.typeface = bold

                categoryName?.typeface = regular

                // include profile layout text
                text_profile_titles.typeface = bold
                text_make_editable.typeface = bold
                text_user_name_title.typeface = regular
                text_first_name.typeface = regular
                text_last_name.typeface = regular
                textConfPassword.typeface = regular
                text_email.typeface = regular
                text_mobile_title.typeface = regular
                text_date_birth.typeface = regular
                text_gender.typeface = regular
                text_city.typeface = regular
                enter_city.typeface = regular
                text_receive_promotion_email.typeface = regular
                txtRNotification.typeface = regular
                textRecomNotification.typeface = regular
                UpdateAccount.typeface = bold

                // Adapter item text
                textBookingHistoryTitle1?.typeface = bold
                textBookingHistoryDate1?.typeface = regular
                textBookingHistoryTime1?.typeface = regular

                textAddress1?.typeface = regular
                textviewScreenNumber1?.typeface = regular
                textviewDateInfo1?.typeface = regular
                textviewTimeInfo1?.typeface = regular
                textviewExperienceName1?.typeface = regular
                textviewSeatName1?.typeface = regular
                textKdTicketPrice1?.typeface = regular
                payDone1?.typeface = regular
                rechargeTime1?.typeface = regular
                rechargePrice1?.typeface = regular
                rechargeDate1?.typeface = regular
                paidBy1?.typeface = regular
                foodTotalPrice1?.typeface = regular
                foodPaidby1?.typeface = regular


                //  Recharge wallet include text
                text_recharge_wallet?.typeface = bold
                text_amount?.typeface = bold
                text_choose_preference_amount?.typeface = regular
                text_payment_method?.typeface = bold
                text_choose_payment_method?.typeface = regular
                text_gift_card?.typeface = regular
                text_kent?.typeface = regular
                text_credit_card?.typeface = regular
                tv_proceed_btn?.typeface = bold

                // Refund include layout item
                text_refunds.typeface = bold

                // account history include layout item
                textBooking.typeface = bold
                textHistory.typeface = bold


            }

            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(requireActivity(), "en")
                binding?.imageSwitcherLang?.isChecked = true
                arbic = false
                val regular = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_medium)

                binding?.textSwitcher?.typeface = regular
                binding?.textArabic?.typeface = regular
                binding?.textUserAccountName?.typeface = bold
                binding?.textWalletUserId?.typeface = regular
                binding?.textUserWalletKd?.typeface = regular
                binding?.textProfileTitle?.typeface = regular
                binding?.textBookingTitle?.typeface = regular
                binding?.textPreference?.typeface = regular
                binding?.textRecharageWallet?.typeface = regular
                binding?.textSignout?.typeface = regular
                binding?.textUpcomingBooking?.typeface = bold

                // preferences include text
                text_preferences.typeface = bold
                text_locations.typeface = bold
                text_choose_preferencess.typeface = regular
                text_find_near_location.typeface = regular
                text_seat_category_preference.typeface = bold
                text_seat_type_preference.typeface = bold
                text_age_rating_preference.typeface = bold

                categoryName?.typeface = regular

                // include profile layout text
                text_profile_titles.typeface = bold
                text_make_editable.typeface = bold
                text_user_name_title.typeface = regular
                text_first_name.typeface = regular
                text_last_name.typeface = regular
                textConfPassword.typeface = regular
                text_email.typeface = regular
                text_mobile_title.typeface = regular
                text_date_birth.typeface = regular
                text_gender.typeface = regular
                text_city.typeface = regular
                enter_city.typeface = regular
                text_receive_promotion_email.typeface = regular
                txtRNotification.typeface = regular
                textRecomNotification.typeface = regular
                UpdateAccount.typeface = bold

                // Adapter item text
                textBookingHistoryTitle1?.typeface = bold
                textBookingHistoryDate1?.typeface = regular
                textBookingHistoryTime1?.typeface = regular

                textAddress1?.typeface = regular
                textviewScreenNumber1?.typeface = regular
                textviewDateInfo1?.typeface = regular
                textviewTimeInfo1?.typeface = regular
                textviewExperienceName1?.typeface = regular
                textviewSeatName1?.typeface = regular
                textKdTicketPrice1?.typeface = regular
                payDone1?.typeface = regular
                rechargeTime1?.typeface = regular
                rechargePrice1?.typeface = regular
                rechargeDate1?.typeface = regular
                paidBy1?.typeface = regular
                foodTotalPrice1?.typeface = regular
                foodPaidby1?.typeface = regular

                //  Recharge wallet include text
                text_recharge_wallet?.typeface = bold
                text_amount?.typeface = bold
                text_choose_preference_amount?.typeface = regular
                text_payment_method?.typeface = bold
                text_choose_payment_method?.typeface = regular
                text_gift_card?.typeface = regular
                text_kent?.typeface = regular
                text_credit_card?.typeface = regular
                tv_proceed_btn?.typeface = bold

                // Refund include layout item
                text_refunds.typeface = bold

                // account history include layout item
                textBooking.typeface = bold
                textHistory.typeface = bold

            }

            else -> {
                image_switcher?.isChecked = true
                arbic = false
                val regular = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_medium)

                binding?.textSwitcher?.typeface = regular
                binding?.textArabic?.typeface = regular
                binding?.textUserAccountName?.typeface = bold
                binding?.textWalletUserId?.typeface = regular
                binding?.textUserWalletKd?.typeface = regular
                binding?.textProfileTitle?.typeface = regular
                binding?.textBookingTitle?.typeface = regular
                binding?.textPreference?.typeface = regular
                binding?.textRecharageWallet?.typeface = regular
                binding?.textSignout?.typeface = regular
                binding?.textUpcomingBooking?.typeface = bold

                // preferences include text
                text_preferences.typeface = bold
                text_locations.typeface = bold
                text_choose_preferencess.typeface = regular
                text_find_near_location.typeface = regular
                text_seat_category_preference.typeface = bold
                text_seat_type_preference.typeface = bold
                text_age_rating_preference.typeface = bold

                categoryName?.typeface = regular

                // include profile layout text
                text_profile_titles.typeface = bold
                text_make_editable.typeface = bold
                text_user_name_title.typeface = regular
                text_first_name.typeface = regular
                text_last_name.typeface = regular
                textConfPassword.typeface = regular
                text_email.typeface = regular
                text_mobile_title.typeface = regular
                text_date_birth.typeface = regular
                text_gender.typeface = regular
                text_city.typeface = regular
                enter_city.typeface = regular
                text_receive_promotion_email.typeface = regular
                txtRNotification.typeface = regular
                textRecomNotification.typeface = regular
                UpdateAccount.typeface = bold

                // Adapter item text
                textBookingHistoryTitle1?.typeface = bold
                textBookingHistoryDate1?.typeface = regular
                textBookingHistoryTime1?.typeface = regular

                textAddress1?.typeface = regular
                textviewScreenNumber1?.typeface = regular
                textviewDateInfo1?.typeface = regular
                textviewTimeInfo1?.typeface = regular
                textviewExperienceName1?.typeface = regular
                textviewSeatName1?.typeface = regular
                textKdTicketPrice1?.typeface = regular
                payDone1?.typeface = regular
                rechargeTime1?.typeface = regular
                rechargePrice1?.typeface = regular
                rechargeDate1?.typeface = regular
                paidBy1?.typeface = regular
                foodTotalPrice1?.typeface = regular
                foodPaidby1?.typeface = regular


                //  Recharge wallet include text
                text_recharge_wallet?.typeface = bold
                text_amount?.typeface = bold
                text_choose_preference_amount?.typeface = regular
                text_payment_method?.typeface = bold
                text_choose_payment_method?.typeface = regular
                text_gift_card?.typeface = regular
                text_kent?.typeface = regular
                text_credit_card?.typeface = regular
                tv_proceed_btn?.typeface = bold

                // Refund include layout item
                text_refunds.typeface = bold

                // account history include layout item
                textBooking.typeface = bold
                textHistory.typeface = bold

            }

        }

        userName = preferences.getString(Constant.USER_NAME).toString()
        firstName = preferences.getString(Constant.FIRST_NAME).toString()
        lastName = preferences.getString(Constant.LAST_NAME).toString()
        email = preferences.getString(Constant.USER_EMAIL).toString()
        mobile = preferences.getString(Constant.MOBILE).toString()
        countryCode = preferences.getString(Constant.COUNTRY_CODE).toString()
        dob = preferences.getString(Constant.USER_DOB).toString()
        type = preferences.getString(Constant.TYPE_LOGIN).toString()
        binding?.textUserAccountName?.text = "$firstName $lastName"
        enter_user_names.setText(userName)
        enter_first_name.setText(firstName)
        enter_last_name.setText(lastName)
        enter_emails.setText(email)
        enter_mobile_numbers.setText(mobile)

        binding?.imageSwitcherLang?.setOnCheckedChangeListener { _, b ->
            if (b) {
                Constant.IntentKey.LANGUAGE_SELECT = "en"
                LocaleHelper.setLocale(requireActivity(), "en")
                preferences.putString(Constant.IntentKey.SELECT_LANGUAGE, "en")
                OPEN_FROM = 1
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

        myNextBooking(
            NextBookingsRequest(
                "",
                "",
                0,
                preferences.getString(Constant.USER_ID).toString(),
                true
            )
        )

        println("CheckLogin--->${preferences.getString(Constant.TYPE_LOGIN)}")
        if (preferences.getString(Constant.TYPE_LOGIN) == "GUEST") {

            binding?.textWalletUserId?.invisible()
            binding?.textUserWalletKd?.invisible()

            binding?.viewProfile?.isClickable = false
            binding?.viewProfile?.isEnabled = false
            binding?.viewProfile?.isFocusable = false

            binding?.viewRecharge?.isFocusable = false
            binding?.viewRecharge?.isEnabled = false
            binding?.viewRecharge?.isClickable = false

            binding?.viewPreference?.isEnabled = false
            binding?.viewPreference?.isFocusable = false
            binding?.viewPreference?.isClickable = false

        } else {

            binding?.textWalletUserId?.show()
            binding?.textUserWalletKd?.show()

            binding?.viewProfile?.isEnabled = true
            binding?.viewProfile?.isClickable = true
            binding?.viewProfile?.isFocusable = true

            binding?.viewRecharge?.isEnabled = true
            binding?.viewRecharge?.isFocusable = true
            binding?.viewRecharge?.isClickable = true

            binding?.viewPreference?.isEnabled = true
            binding?.viewPreference?.isFocusable = true
            binding?.viewPreference?.isClickable = true

        }

        if (mobile == "") {
            binding?.AccountUi?.hide()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            requireActivity().finish()
        }

        println("Mobile--->${mobile}")
        binding?.view2?.setOnClickListener {
            signOut()

        }

        view_knet.setOnClickListener {
            image_knet.setColorFilter(requireActivity().getColor(R.color.text_alert_color_red))
            text_kent.setTextColor(requireContext().getColor(R.color.white))

            image_credit_card.setColorFilter(requireActivity().getColor(R.color.hint_color))
            text_credit_card.setTextColor(requireContext().getColor(R.color.hint_color))
            clickEnable = 1
            rechargeType = "knet"

        }

        view30_credit_card.setOnClickListener {
            image_knet.setColorFilter(requireActivity().getColor(R.color.hint_color))
            text_kent.setTextColor(requireContext().getColor(R.color.hint_color))

            image_credit_card.setColorFilter(requireActivity().getColor(R.color.text_alert_color_red))
            text_credit_card.setTextColor(requireContext().getColor(R.color.white))

            clickEnable = 1
            rechargeType = "cc"
        }

        binding?.viewBooking?.setOnClickListener {
            myNextBooking(
                NextBookingsRequest(
                    "",
                    "",
                    0,
                    preferences.getString(Constant.USER_ID).toString(),
                    true
                )
            )
            binding?.imageUserProfile?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textProfileTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageBooking?.setColorFilter(requireActivity().getColor(R.color.white))
            binding?.textBookingTitle?.setTextColor(requireActivity().getColor(R.color.white))

            binding?.imagePreference?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textPreference?.setTextColor(requireActivity().getColor(R.color.text_color))

//            binding?.imageRecharageWallet?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRecharageWallet?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageRefundCoin?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRefund?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageHistory?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textHistoery?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.textUpcomingBooking?.show()
            binding?.recyclerviewBooking?.show()
            binding?.recycleUi?.show()
//            binding?.textUpcomingBooking?.text = bookingText
            binding?.textUpcomingBooking?.text = getString(R.string.your_upcoming_bookings)
            include_profile.hide()
            text_profile_titles.hide()
            text_make_editable.hide()
            include_preference.hide()
            include_recharge.hide()
            include_refunded.hide()
            include_history.hide()
        }

        radioGroup.setOnCheckedChangeListener { _, _ ->
            if (male.isChecked) {

                male.buttonTintList =
                    ColorStateList.valueOf(
                        getColor(
                            requireContext(),
                            R.color.text_alert_color_red
                        )
                    )
                female.buttonTintList =
                    ColorStateList.valueOf(getColor(requireContext(), R.color.text_color))
                female.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.text_color
                    )
                )
                gender = "Male"
            } else if (female.isChecked) {
                male.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.text_color
                    )
                )
                male.buttonTintList =
                    ColorStateList.valueOf(getColor(requireContext(), R.color.text_color))
                female.buttonTintList =
                    ColorStateList.valueOf(
                        getColor(
                            requireContext(),
                            R.color.text_alert_color_red
                        )
                    )
                gender = "Female"
            }
        }

        text_make_editable.setOnClickListener {
            accountChangePasswordDialog()
        }

        binding?.viewProfile?.setOnClickListener {

            binding?.imageUserProfile?.setColorFilter(requireActivity().getColor(R.color.white))
            binding?.textProfileTitle?.setTextColor(requireActivity().getColor(R.color.white))

            binding?.imageBooking?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textBookingTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imagePreference?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textPreference?.setTextColor(requireActivity().getColor(R.color.text_color))

//            binding?.imageRecharageWallet?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRecharageWallet?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageRefundCoin?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRefund?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageHistory?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textHistoery?.setTextColor(requireActivity().getColor(R.color.text_color))

            include_profile.show()
            binding?.nestedUi?.show()
            text_profile_titles.show()
            text_make_editable.show()
            binding?.recycleUi?.hide()
            include_preference.hide()
            include_recharge.hide()
            include_refunded.hide()
            include_history.hide()

        }

        binding?.viewPreference?.setOnClickListener {

            binding?.imageUserProfile?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textProfileTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageBooking?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textBookingTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imagePreference?.setColorFilter(requireActivity().getColor(R.color.white))
            binding?.textPreference?.setTextColor(requireActivity().getColor(R.color.white))

//            binding?.imageRecharageWallet?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRecharageWallet?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageRefundCoin?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRefund?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageHistory?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textHistoery?.setTextColor(requireActivity().getColor(R.color.text_color))

            include_preference.show()
            include_preference.show()
            include_profile.hide()
            binding?.nestedUi?.show()
            binding?.recycleUi?.hide()
            include_recharge.hide()
            include_refunded.hide()
            include_history.hide()
        }

        binding?.viewRecharge?.setOnClickListener {

            binding?.imageUserProfile?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textProfileTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageBooking?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textBookingTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imagePreference?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textPreference?.setTextColor(requireActivity().getColor(R.color.text_color))

//            binding?.imageRecharageWallet?.setColorFilter(requireActivity().getColor(R.color.white))
            binding?.textRecharageWallet?.setTextColor(requireActivity().getColor(R.color.white))

            binding?.imageRefundCoin?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRefund?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageHistory?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textHistoery?.setTextColor(requireActivity().getColor(R.color.text_color))

            include_recharge.show()
            include_preference.hide()
            include_profile.hide()
            binding?.nestedUi?.show()
            binding?.recycleUi?.hide()
            include_refunded.hide()
            include_history.hide()
        }

        binding?.viewRefund?.setOnClickListener {

            binding?.imageUserProfile?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textProfileTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageBooking?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textBookingTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imagePreference?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textPreference?.setTextColor(requireActivity().getColor(R.color.text_color))

//            binding?.imageRecharageWallet?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRecharageWallet?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageRefundCoin?.setColorFilter(requireActivity().getColor(R.color.white))
            binding?.textRefund?.setTextColor(requireActivity().getColor(R.color.white))

            binding?.imageHistory?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textHistoery?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.textUpcomingBooking?.show()
            binding?.recyclerviewBooking?.show()
//            binding?.textUpcomingBooking?.text = bookingText
            binding?.textUpcomingBooking?.text = getString(R.string.your_refunded_credits)
            include_recharge.hide()
            include_preference.hide()
            include_profile.hide()
            binding?.nestedUi?.show()
            binding?.recycleUi?.show()
            include_history.hide()
        }

        binding?.viewHistorys?.setOnClickListener {
            binding?.nestedUi?.show()
            binding?.appBarAccount?.show()
            myBooking(
                MyBookingRequest(
                    "",
                    "",
                    0,
                    preferences.getString(Constant.USER_ID).toString()
                )
            )
            binding?.imageUserProfile?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textProfileTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageBooking?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textBookingTitle?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imagePreference?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textPreference?.setTextColor(requireActivity().getColor(R.color.text_color))

//            binding?.imageRecharageWallet?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRecharageWallet?.setTextColor(requireActivity().getColor(R.color.text_color))

            binding?.imageRefundCoin?.setColorFilter(requireActivity().getColor(R.color.text_color))
            binding?.textRefund?.setTextColor(requireActivity().getColor(R.color.text_color))
            binding?.imageHistory?.setColorFilter(requireActivity().getColor(R.color.white))
            binding?.textHistoery?.setTextColor(requireActivity().getColor(R.color.white))
            binding?.textUpcomingBooking?.text = getString(R.string.your_bookings_history)


            include_history.hide()
            include_refunded.hide()
            include_recharge.hide()
            include_preference.hide()
            include_profile.hide()
            binding?.nestedUi?.show()
            binding?.recycleUi?.show()
        }

        Proceed_btnUi.setOnClickListener {
            if (clickEnable == 0) {
                val dialog = OptionDialog(requireContext(),
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    resources.getString(R.string.select_payment_method),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            } else {
                if (rechargeType == "cc") {
                    creditCardDialog()
                } else {
                    rechargeCard(
                        AddClubRechargeRequest(
                            rechargeAmount,
                            preferences.getString(Constant.USER_ID).toString()
                        )
                    )
                }


            }
        }

        UpdateAccount.setOnClickListener {
            val password = enter_passwords.text.toString()
            val confPassword = enterConfPasswords.text.toString()
            if (enter_passwords.text.isNullOrEmpty()) {
                updateAccount(
                    UpdateAccountRequest(
                        enter_city.text.toString(),
                        enter_date_births.text.toString(),
                        enter_emails.text.toString(),
                        enter_first_name.text.toString(),
                        gender,
                        enter_last_name.text.toString(),
                        enter_passwords.text.toString(),
                        preferences.getString(Constant.USER_ID).toString(),
                        enter_mobile_numbers.text.toString()
                    )
                )

            } else if (password.isNullOrEmpty()) {
                val dialog = OptionDialog(requireContext(),
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
            } else if (confPassword.isNullOrEmpty()) {
                val dialog = OptionDialog(requireContext(),
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
                val dialog = OptionDialog(requireContext(),
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
                val dialog = OptionDialog(requireContext(),
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
            } else {
                Constant().hideKeyboard(requireActivity())
                updateAccount(
                    UpdateAccountRequest(
                        enter_city.text.toString(),
                        enter_date_births.text.toString(),
                        enter_emails.text.toString(),
                        enter_first_name.text.toString(),
                        gender,
                        enter_last_name.text.toString(),
                        enterConfPasswords.text.toString(),
                        preferences.getString(Constant.USER_ID).toString(),
                        enter_mobile_numbers.text.toString()
                    )
                )
            }
        }


        enter_date_births.setOnClickListener {
            val mcurrentDate = Calendar.getInstance()
            var mYear = mcurrentDate[Calendar.YEAR]
            var mMonth = mcurrentDate[java.util.Calendar.MONTH]
            var mDay = mcurrentDate[java.util.Calendar.DAY_OF_MONTH]
            val mDatePicker = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val myCalendar = Calendar.getInstance()
                    myCalendar[Calendar.YEAR] = selectedYear
                    myCalendar[java.util.Calendar.MONTH] = selectedMonth
                    myCalendar[java.util.Calendar.DAY_OF_MONTH] = selectedDay
                    val myFormat = "dd-MM-yy" //Change as you need
                    val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
                    enter_date_births.text = sdf.format(myCalendar.time)
                    mDay = selectedDay
                    mMonth = selectedMonth
                    mYear = selectedYear
                }, mYear, mMonth, mDay
            )
            mDatePicker.datePicker.maxDate = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 0)
            mDatePicker.show()
        }

        //Save Prefrence
        textView3.setOnClickListener {
            updatePreference(
                PreferenceRequest(
                    arbic,
                    cinema,
                    experience.toString(),
                    ageRating.toString(),
                    seatCategory,
                    seatType,
                    preferences.getString(Constant.USER_ID).toString()
                )
            )

        }

        broadcastReceiver = MyReceiver()
        broadcastIntent()
        getAmountLoad()
        loadLocation()
        CinemaResponse()
        countryCodeLoad()
        view39_line.hide()
        view_ConfPassword.hide()
        textConfPassword.hide()
        enter_ConfPassword.hide()
    }

    private fun accountChangePasswordDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.change_password)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
        if (isAdded) {
            dialog.show()
        }
        dialog.negative_btn.text = getString(R.string.proceed)
        dialog.consSure?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.negative_btn?.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun creditCardDialog() {
        val cardinalConfigurationParameters = CardinalConfigurationParameters()
        cardinalConfigurationParameters.environment = CardinalEnvironment.STAGING
        cardinalConfigurationParameters.requestTimeout = 8000
        cardinalConfigurationParameters.challengeTimeout = 5

        val rType = JSONArray()
        rType.put(CardinalRenderType.OTP)
        rType.put(CardinalRenderType.SINGLE_SELECT)
        rType.put(CardinalRenderType.MULTI_SELECT)
        rType.put(CardinalRenderType.OOB)
        rType.put(CardinalRenderType.HTML)
        cardinalConfigurationParameters.renderType = rType

        cardinalConfigurationParameters.uiType = CardinalUiType.BOTH

        val yourUICustomizationObject = UiCustomization()
        cardinalConfigurationParameters.uiCustomization = yourUICustomizationObject

        cardinal.configure(requireActivity(), cardinalConfigurationParameters)
        val mDialogView =
            LayoutInflater.from(requireContext())
                .inflate(R.layout.checkout_creditcart_payment_alert, null)
        val mBuilder = AlertDialog.Builder(requireActivity())
            .setView(mDialogView)
        proceedAlertDialog = mBuilder.show()
        proceedAlertDialog?.show()

        proceedAlertDialog?.kd_to_pay?.text =
            " " + getString(R.string.price_kd) + " " + rechargeAmount
        proceedAlertDialog?.cardNumberTextInputEditText?.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                try {
                    if (!proceedAlertDialog?.cardNumberTextInputEditText?.text
                            .toString().isEmpty()
                    ) {
                        proceedAlertDialog?.image_american_express_card?.visibility =
                            View.VISIBLE
                        if (VISA_PREFIX.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString().substring(0, 1)
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.visa_card
                                )
                            )
                        } else if (MASTERCARD_PREFIX.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString()
                                    .substring(0, 2) + ","
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.mastercard_card
                                )
                            )
                        } else if (AMEX_PREFIX.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString()
                                    .substring(0, 2) + ","
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.amerian_card
                                )
                            )
                        } else if (DISCOVER_PREFIX.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString().substring(0, 4)
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.amerian_card
                                )
                            )
                        } else if (JCB.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString().substring(0, 1)
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.amerian_card
                                )
                            )
                        } else if (MAESTRO.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString().substring(0, 2)
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.amerian_card
                                )
                            )
                        } else {
                            proceedAlertDialog?.image_american_express_card?.visibility =
                                View.GONE
                        }
                    } else {
                        proceedAlertDialog?.image_american_express_card?.visibility =
                            View.GONE
                    }
                } catch (e: Exception) {
                }
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                try {
                    if (!proceedAlertDialog?.cardNumberTextInputEditText?.text
                            .toString().isEmpty()
                    ) {
                        proceedAlertDialog?.image_american_express_card?.visibility =
                            View.VISIBLE
                        if (VISA_PREFIX.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString().substring(0, 1)
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.visa_card
                                )
                            )
                        } else if (MASTERCARD_PREFIX.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString()
                                    .substring(0, 2) + ","
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.mastercard_card
                                )
                            )
                        } else if (AMEX_PREFIX.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString()
                                    .substring(0, 2) + ","
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.amerian_card
                                )
                            )
                        } else if (DISCOVER_PREFIX.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString().substring(0, 4)
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.disover_card
                                )
                            )
                        } else if (JCB.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString().substring(0, 4)
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.jcb_card
                                )
                            )
                        } else if (MAESTRO.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString().substring(0, 4)
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.maestro_card
                                )
                            )
                        } else if (UATP.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString().substring(0, 4)
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.uatp
                                )
                            )
                        } else {
                            proceedAlertDialog?.image_american_express_card?.visibility =
                                View.GONE
                        }
                    } else {
                        proceedAlertDialog?.image_american_express_card?.visibility =
                            View.GONE
                    }
                } catch (e: java.lang.Exception) {
                }
            }

            override fun afterTextChanged(editable: Editable) {
                try {
                    if (!proceedAlertDialog?.cardNumberTextInputEditText?.text
                            .toString().isEmpty()
                    ) {
                        proceedAlertDialog?.image_american_express_card?.visibility =
                            View.VISIBLE
                        if (VISA_PREFIX.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString().substring(0, 1)
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.visa_card
                                )
                            )
                        } else if (MASTERCARD_PREFIX.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString()
                                    .substring(0, 2) + ","
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.mastercard_card
                                )
                            )
                        } else if (AMEX_PREFIX.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString()
                                    .substring(0, 2) + ","
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.amerian_card
                                )
                            )
                        } else if (DISCOVER_PREFIX.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString().substring(0, 4)
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.disover_card
                                )
                            )
                        } else if (JCB.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString().substring(0, 4)
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.jcb_card
                                )
                            )
                        } else if (MAESTRO.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString().substring(0, 4)
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.maestro_card
                                )
                            )
                        } else if (UATP.contains(
                                proceedAlertDialog?.cardNumberTextInputEditText?.text
                                    .toString().substring(0, 4)
                            ) && !proceedAlertDialog?.cardNumberTextInputEditText?.text
                                .toString().isEmpty()
                        ) {
                            proceedAlertDialog?.image_american_express_card?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.uatp
                                )
                            )
                        } else {
                            proceedAlertDialog?.image_american_express_card?.visibility =
                                View.GONE
                        }
                    } else {
                        proceedAlertDialog?.image_american_express_card?.visibility =
                            View.GONE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        proceedAlertDialog?.text_cancel_go_back?.setOnClickListener {
            proceedAlertDialog?.dismiss()

        }
        var lastInput = ""

        proceedAlertDialog?.expireDateTextInputEditText?.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(
                p0: CharSequence?,
                start: Int,
                removed: Int,
                added: Int
            ) {
                val input = p0.toString()
                val formatter = SimpleDateFormat("MM/yy", Locale.GERMANY)
                val expiryDateDate = Calendar.getInstance()
                try {
                    expiryDateDate.time = formatter.parse(input)
                } catch (e: ParseException) {
                    if (p0?.length == 2 && !lastInput.endsWith("/")) {
                        val month = Integer.parseInt(input)
                        if (month <= 12) {
                            proceedAlertDialog?.expireDateTextInputEditText?.setText(
                                proceedAlertDialog?.expireDateTextInputEditText?.text.toString() + "/"
                            )
                            proceedAlertDialog?.expireDateTextInputEditText?.setSelection(
                                3
                            )
                        }
                    } else if (p0?.length == 2 && lastInput.endsWith("/")) {
                        val month = Integer.parseInt(input)
                        if (month <= 12) {
                            proceedAlertDialog?.expireDateTextInputEditText?.setText(
                                proceedAlertDialog?.expireDateTextInputEditText?.text.toString()
                                    .substring(0, 1)
                            )
                        }
                    }
                    lastInput =
                        proceedAlertDialog?.expireDateTextInputEditText?.text.toString()
                    //because not valid so code exits here
                    return
                }
            }
        })

        proceedAlertDialog?.btn_pay?.setOnClickListener {
            if (validateFields(proceedAlertDialog!!))

                try {
                    ccCardNo = proceedAlertDialog?.cardNumberTextInputEditText?.text.toString()
                        .replace(" ".toRegex(), "").trim()
                    ccCardCvv = proceedAlertDialog?.ccvTextInputEditText?.text.toString()
                    ccCardExpiryMonth =
                        proceedAlertDialog?.expireDateTextInputEditText?.text?.toString()
                            ?.split("/")!![0]
                    ccCardExpiryYear =
                        proceedAlertDialog?.expireDateTextInputEditText?.text?.toString()
                            ?.split("/")!![1]

                    rechargeCard(
                        AddClubRechargeRequest(
                            rechargeAmount,
                            preferences.getString(Constant.USER_ID).toString()
                        )
                    )
                } catch (e: Exception) {
                    println("exception--->${e.message}")
                }


        }
    }


    override fun datatypeFace(
        textBookingHistoryTitle: TextView,
        textBookingHistoryDate: TextView,
        textBookingHistoryTime: TextView,
        textAddress: TextView,
        textviewScreenNumber: TextView,
        textviewDateInfo: TextView,
        textviewTimeInfo: TextView,
        textviewExperienceName: TextView,
        textviewSeatName: TextView,
        textKdTicketPrice: TextView,
        payDone: TextView,
        rechargeTime: TextView,
        rechargePrice: TextView,
        rechargeDate: TextView,
        paidBy: TextView,
        foodTotalPrice: TextView,
        foodPaidby: TextView) {

        textBookingHistoryTitle1= textBookingHistoryTitle
        textBookingHistoryDate1 = textBookingHistoryDate
        textBookingHistoryTime1 = textBookingHistoryTime
        textAddress1 = textAddress
        textviewScreenNumber1 = textviewScreenNumber
        textviewDateInfo1 = textviewDateInfo
        textviewTimeInfo1 = textviewTimeInfo
        textviewExperienceName1 = textviewExperienceName
        textviewSeatName1 = textviewSeatName
        textKdTicketPrice1 = textKdTicketPrice
        payDone1 = payDone
        rechargeTime1 = rechargeTime
        rechargePrice1 = rechargePrice
        rechargeDate1 = rechargeDate
        paidBy1 = paidBy
        foodTotalPrice1 = foodTotalPrice
        foodPaidby1 = foodPaidby

    }

    private fun validateFields(proceedAlertDialog: AlertDialog): Boolean {
        return if (proceedAlertDialog.cardNumberTextInputEditText.text.toString()
                .isEmpty() && proceedAlertDialog.cardNumberTextInputEditText.text
                .toString().length != 16 && !CreditCardUtils.isValid(
                proceedAlertDialog.cardNumberTextInputEditText.text.toString().replace(" ", "")
            )
        ) {
            val dialog = OptionDialog(requireActivity(),
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.valid_card),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                },
                negativeClick = {
                })
            dialog.show()
            false
        } else if (proceedAlertDialog.expireDateTextInputEditText.text.toString()
                .isEmpty() || proceedAlertDialog.expireDateTextInputEditText.text.toString().length < 5
        ) {
            val dialog = OptionDialog(requireActivity(),
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.valid_expiry),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                },
                negativeClick = {
                })
            dialog.show()
            false
        } else if (proceedAlertDialog.expireDateTextInputEditText.text.toString()
                .isEmpty() || proceedAlertDialog.expireDateTextInputEditText.text.toString()
                .split("/")
                .toTypedArray()[0].toInt() > 12 || proceedAlertDialog.expireDateTextInputEditText.text.toString().length < 5
        ) {
            val dialog = OptionDialog(requireActivity(),
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.valid_expiry),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                },
                negativeClick = {
                })
            dialog.show()
            false
        } else if (proceedAlertDialog.ccvTextInputEditText.text.toString()
                .isEmpty() && proceedAlertDialog.ccvTextInputEditText.length() != 3
        ) {
            val dialog = OptionDialog(requireActivity(),
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.valid_cvv),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                },
                negativeClick = {
                })
            dialog.show()
            false
        } else {
            true
        }
    }

    private fun postCardData(request: PostCardRequest) {
        accountFragViewModel.postCardData(request)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
//                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        if (it.data.output.redirect == "0") {
                                            cardinal.cca_continue(
                                                it.data.output.authTransId,
                                                it.data.output.pares,
                                                requireActivity()
                                            ) { context, validateResponse, s ->
                                                println("consumerSessionId12-->" + validateResponse.actionCode + "----" + validateResponse.errorDescription)
                                                if (validateResponse.actionCode == CardinalActionCode.CANCEL) {
                                                    toast("Transaction Cancelled!")
                                                } else if (validateResponse.actionCode == CardinalActionCode.ERROR) {
                                                    toast(validateResponse.errorDescription)
                                                } else if (validateResponse.actionCode == CardinalActionCode.SUCCESS) {
                                                    if (s != null) {
                                                        requireActivity().runOnUiThread {
                                                            validateJWT(
                                                                ValidateJWTRequest(
                                                                    request.bookingid,
                                                                    request.cardNumber,
                                                                    request.cvNumber,
                                                                    request.expirationMonth,
                                                                    request.expirationYear,
                                                                    s,
                                                                    m_sessionID,
                                                                    ""
                                                                )
                                                            )
                                                        }
                                                    } else {
                                                        toast("Transaction Failed!")
                                                    }
                                                } else {
                                                    toast(validateResponse.errorDescription)
                                                }
                                            }
                                        } else {
                                            loader?.dismiss()
                                            val dialog = OptionDialog(requireActivity(),
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                it.data.output.errorDescription,
                                                positiveBtnText = R.string.ok,
                                                negativeBtnText = R.string.no,
                                                positiveClick = {
                                                },
                                                negativeClick = {
                                                })
                                            dialog.show()
                                        }
                                    } catch (e: Exception) {
                                        loader?.dismiss()

                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }

                                } else {
                                    loader?.dismiss()
                                    val dialog = OptionDialog(requireActivity(),
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
                            val dialog = OptionDialog(requireContext(),
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
//                            loader = LoaderDialog(R.string.pleasewait)
//                            loader?.show(requireActivity().supportFragmentManager, null)
                        }
                    }
                }
            }
    }

    private fun validateJWT(s: ValidateJWTRequest) {
        accountFragViewModel.validateJWT(s)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        proceedAlertDialog?.dismiss()
                                        image_knet.setColorFilter(requireActivity().getColor(R.color.hint_color))
                                        text_kent.setTextColor(requireContext().getColor(R.color.hint_color))

                                        image_credit_card.setColorFilter(
                                            requireActivity().getColor(
                                                R.color.hint_color
                                            )
                                        )
                                        text_credit_card.setTextColor(requireContext().getColor(R.color.hint_color))

                                        clickEnable = 0
                                        getProfile(
                                            ProfileRequest(
                                                latitude,
                                                longitude,
                                                preferences.getString(Constant.USER_ID).toString()
                                            )
                                        )

                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }

                                } else {
                                    loader?.dismiss()
                                    val dialog = OptionDialog(requireActivity(),
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

    private fun creditCardInit(request: HmacKnetRequest, bookinId: String) {
        accountFragViewModel.creditCardInit(request)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            // loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        cardinal = Cardinal.getInstance()
                                        val serverJwt: String = it.data.output.jwtToken

                                        val profilingConnections: TMXProfilingConnectionsInterface =
                                            TMXProfilingConnections().setConnectionTimeout(
                                                20,
                                                TimeUnit.SECONDS
                                            ).setRetryTimes(3)


                                        val config = TMXConfig() // (REQUIRED) Organisation ID
                                            .setOrgId(it.data.output.orgId)
                                            .setFPServer("h.online-metrix.net")
                                            .setContext(requireActivity())
                                            .setProfilingConnections(profilingConnections)
                                            .setProfileTimeout(20, TimeUnit.SECONDS)
                                            .setRegisterForLocationServices(true)
//
                                        TMXProfiling.getInstance().init(config)

                                        doProfile(
                                            it.data.output.deviceSessionId,
                                            it.data.output.merchantId
                                        )

                                        cardinal.init(serverJwt, object : CardinalInitService {
                                            /**
                                             * You may have your Submit button disabled on page load. Once you are set up
                                             * for CCA, you may then enable it. This will prevent users from submitting
                                             * their order before CCA is ready.
                                             */
                                            override fun onSetupCompleted(consumerSessionId: String) {
                                                refId = consumerSessionId
                                                println("consumerSessionId-->$consumerSessionId")


                                                activity?.runOnUiThread {
                                                    postCardData(
                                                        PostCardRequest(
                                                            bookinId,
                                                            ccCardNo,
                                                            ccCardCvv,
                                                            ccCardExpiryMonth,
                                                            ccCardExpiryYear,
                                                            refId
                                                        )
                                                    )
                                                }


                                            }

                                            override fun onValidated(
                                                validateResponse: ValidateResponse?,
                                                serverJwt: String?
                                            ) {
                                                activity?.runOnUiThread {
                                                    loader?.dismiss()
                                                    proceedAlertDialog?.dismiss()
                                                    val dialog = OptionDialog(requireActivity(),
                                                        R.mipmap.ic_launcher,
                                                        R.string.app_name,
                                                        getString(R.string.somethingWrong),
                                                        positiveBtnText = R.string.ok,
                                                        negativeBtnText = R.string.no,
                                                        positiveClick = {
                                                        },
                                                        negativeClick = {
                                                        })
                                                    dialog.show()
                                                }

                                                println("consumerSessionId-->" + validateResponse?.actionCode + "----" + serverJwt)
                                            }
                                        })

                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }

                                } else {
                                    loader?.dismiss()
                                    val dialog = OptionDialog(requireActivity(),
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
//                            loader = LoaderDialog(R.string.pleasewait)
//                            loader?.show(requireActivity().supportFragmentManager, null)
                        }
                    }
                }
            }
    }

    private fun doProfile(sessions1: String, merchent: String) {
        val list: List<String> = ArrayList()
        //        list.add("attribute 1");
//        list.add("attribute 2");
        val options =
            TMXProfilingOptions().setCustomAttributes(list) // Fire off the profiling request. We could use a more complex request,
        options.setSessionID(merchent + sessions1)
//        val profilingHandle = TMXProfiling.getInstance().profile(options,
//            CheckoutWithFoodActivity.CompletionNotifier()
//        )
        // Session id can be collected here
//        Log.d("TAG", "Session id = " + profilingHandle.sessionID)
        /*
         * profilingHandle can also be used to cancel this profile if needed *
         * profilingHandle.cancel();
         * */m_sessionID = sessions1
    }


    private fun CinemaResponse() {
        accountFragViewModel.foodResponse()
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                try {
                                    if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                        locationlist = it.data.output.cinemas
                                        cinemaId = it.data.output.cinemas[0].id
                                        setSpinner(it.data.output.cinemas)
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        Status.ERROR -> {
                            loader?.dismiss()
                            val dialog = OptionDialog(requireContext(),
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data?.message ?: "Something went wrong",
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

    private fun setSpinner(cinemas: ArrayList<FoodResponse.Output.Cinema>) {

        val customAdapter = CustomSpinnerAdapter(
            requireContext(),
            cinemas
        )
        spinnerPref?.adapter = customAdapter
        spinnerPref?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                cinema = locationlist[position].name
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun loadLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getLatLong()
        }
    }

    private fun getLatLong() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 3000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        LocationServices.getFusedLocationProviderClient(requireActivity())
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    if (isAdded) {
                        LocationServices.getFusedLocationProviderClient(requireActivity())
                            .removeLocationUpdates(this)
                        if (locationResult.locations.size > 0) {
                            val latestIndex = locationResult.locations.size - 1
//                        val lati = locationResult.locations[latestlocIndex].latitude
//                        val longi = locationResult.locations[latestlocIndex].longitude
                            latitude = locationResult.locations[latestIndex].latitude.toString()
                            longitude = locationResult.locations[latestIndex].longitude.toString()
                            getProfile(
                                ProfileRequest(
                                    latitude,
                                    longitude,
                                    preferences.getString(Constant.USER_ID).toString()
                                )
                            )

                            val location = Location("providerNA")
                            location.longitude = longitude.toDouble()
                            location.latitude = latitude.toDouble()
//                        fetchAddressFromLocation(location)
                        }
                    }

                }
            }, Looper.getMainLooper())
    }

    //Prefrence
    private fun updatePreference(profileRequest: PreferenceRequest) {
        accountFragViewModel.updatePreference(profileRequest)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    println("SomethingWrong--->${it.data.msg}")
                                    view39_line.hide()
                                    view_ConfPassword.hide()
                                    textConfPassword.hide()
                                    enter_ConfPassword.hide()
                                    val dialog = OptionDialog(requireContext(),
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        it.data.msg,
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                            getProfile(
                                                ProfileRequest(
                                                    latitude,
                                                    longitude,
                                                    preferences.getString(Constant.USER_ID)
                                                        .toString()
                                                )
                                            )

                                        },
                                        negativeClick = {
                                        })
                                    dialog.show()
                                } else {
                                    val dialog = OptionDialog(requireContext(),
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
                            val dialog = OptionDialog(requireContext(),
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

    @SuppressLint("InflateParams")
    val viewListForDates = ArrayList<View>()
    private fun setSeatCategoryFlexbox(layout: FlexboxLayout, seatCategory: String) {
        val list: ArrayList<ModelPreferenceCategory> = arrayListOf(
            ModelPreferenceCategory(R.drawable.family, "Family", 0),
            ModelPreferenceCategory(R.drawable.bachlor, "Bachelor", 0),
        )
        layout.removeAllViews()
        for (item in list) {
            val v: View = layoutInflater.inflate(R.layout.seat_category_item, null)
            val categoryImage: ImageView = v.findViewById(R.id.image_family) as ImageView
             categoryName = v.findViewById(R.id.category_name) as TextView

            seatAbility = if (item.count > 0) {
                1
            } else {
                0
            }
            categoryName?.text = item.cateTypeText
            Glide.with(this)
                .load(item.imgCate)
                .placeholder(R.drawable.family)
                .into(categoryImage)

            viewListForDates.add(v)
            layout.addView(v)

            if (item.cateTypeText == seatCategory) {
                categoryImage.setColorFilter(resources.getColor(R.color.text_alert_color_red))
                categoryName?.setTextColor(
                    getColor(
                        requireContext(),
                        R.color.text_alert_color_red
                    )
                )
            } else {

                categoryImage.setColorFilter(resources.getColor(R.color.hint_color))
                categoryName?.setTextColor(getColor(requireContext(), R.color.hint_color))
            }

            v.setOnClickListener {
                for (v in viewListForDates) {
                    val categoryImage1: ImageView = v.findViewById(R.id.image_family) as ImageView
                    val categoryName1: TextView = v.findViewById(R.id.category_name) as TextView
                    categoryImage1.setColorFilter(getColor(requireContext(), R.color.hint_color))
                    categoryName1.setTextColor(getColor(requireContext(), R.color.hint_color))
                }
                this.seatCategory = categoryName?.text.toString()
                categoryImage.setColorFilter(resources.getColor(R.color.text_alert_color_red))
                categoryName?.setTextColor(getColor(requireContext(), R.color.text_alert_color_red))
            }
        }
    }

    @SuppressLint("InflateParams", "CutPasteId")
    private fun setSeatTypeFlexbox(layout: FlexboxLayout, seatType: String) {
        val list: ArrayList<ModelPreferenceType> = arrayListOf(
            ModelPreferenceType("STANDARD", 0),
            ModelPreferenceType("PREMIUM", 0)
        )

        layout.removeAllViews()
        val viewListForSeatType = ArrayList<View>()
        for (type_item in list) {
            val v: View = layoutInflater.inflate(R.layout.seat_type_item, null)
            val typeName: TextView = v.findViewById(R.id.tv_seat_selectiopn) as TextView
            seatAbility = if (type_item.count > 0) {
                1
            } else {
                0
            }
            typeName.text = type_item.seatType
            viewListForSeatType.add(v)
            layout.addView(v)

            println("SeatType--->${type_item.seatType}---<${seatType}")
            if (type_item.seatType == seatType) {
                typeName.setTextColor(getColor(requireContext(), R.color.text_alert_color_red))
            } else {
                typeName.setTextColor(getColor(requireContext(), R.color.hint_color))
            }

            v.setOnClickListener {
                for (v in viewListForSeatType) {
                    val typeName1: TextView = v.findViewById(R.id.tv_seat_selectiopn) as TextView
                    typeName1.setTextColor(getColor(requireContext(), R.color.hint_color))
                }
                this.seatType = type_item.seatType
                typeName.setTextColor(getColor(requireContext(), R.color.text_alert_color_red))

            }
        }
    }

    @SuppressLint("InflateParams")
    val viewListForSeatExperience = ArrayList<View>()

    @SuppressLint("CutPasteId")
    private fun setExperienceFlexbox(
        layout: FlexboxLayout,
        experience: ArrayList<ProfileResponse.Output.Experience>
    ) {
        layout.removeAllViews()
        for (data in profileList!!) {
            val v: View = layoutInflater.inflate(R.layout.experience_item, null)
            val experienceName = v.findViewById(R.id.experience_name) as ImageView
            val experienceText = v.findViewById(R.id.experience_nametxt) as TextView
            seatAbility = if (data.count > 0) {
                1
            } else {
                0
            }
            val iconId = getMatchIcon(data.name)
            if (iconId == 0) {
                experienceText.text = getMatchIcon(data.name).toString()
                experienceText.show()
                experienceName.hide()
            } else {
                experienceName.setImageResource(getMatchIcon(data.name))
                experienceText.hide()
                experienceName.show()
            }

            layout.addView(v)
            viewListForSeatExperience.add(v)

            println("SeatType--->${data.name}---<${experience}")
            if (data.likes) {
                experienceName.setColorFilter(
                    getColor(
                        requireContext(),
                        R.color.text_alert_color_red
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )
            } else {
                experienceName.setColorFilter(
                    getColor(requireContext(), R.color.hint_color),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
            }

            v.setOnClickListener {
                for (v in viewListForSeatExperience) {
                    val experienceName1 = v.findViewById(R.id.experience_name) as ImageView
                    experienceName1.setBackgroundColor(
                        getColor(
                            requireContext(),
                            R.color.transparent
                        )
                    )
                }
                if (this.experience.contains(data.name)) {
                    this.experience.remove(data.name)
                    experienceName.setColorFilter(
                        getColor(requireContext(), R.color.hint_color),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )

                } else {
                    this.experience.add(data.name)
//                    experience.add(data.name)

                    experienceName.setColorFilter(
                        getColor(
                            requireContext(),
                            R.color.text_alert_color_red
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    println("Experience--->${data.name}")
                }
            }
        }
    }

    private fun getMatchIcon(image: String): Int {
        for (item in list) {
            if ((resources.getResourceName(item.imgSeatExperience).toString().uppercase()).contains(
                    image
                )
            ) {
                return item.imgSeatExperience
            }
        }
        return 0
    }

    @SuppressLint("InflateParams")
    val viewListForSeatAgeRating = ArrayList<String>()
    private fun setAgeRatingFlexbox(
        layout: FlexboxLayout,
        rating: List<ProfileResponse.Output.Rating>
    ) {
        val list: ArrayList<ModelPreferenceAgeRating> = arrayListOf(
            ModelPreferenceAgeRating("E", 0),
            ModelPreferenceAgeRating("PG", 0),
            ModelPreferenceAgeRating("13+", 0),
            ModelPreferenceAgeRating("15+", 0),
            ModelPreferenceAgeRating("18+", 0)
        )
        layout.removeAllViews()
        for (age_rating_item in list) {
            val v: View = layoutInflater.inflate(R.layout.age_rating_item, null)
            val ageRatingName: TextView = v.findViewById(R.id.age_rating_name) as TextView

            ageRatingName.text = age_rating_item.seatAgeRating
            seatAbility = if (age_rating_item.count > 0) {
                1
            } else {
                0
            }

            layout.addView(v)
            viewListForSeatAgeRating.add(v.toString())

            for (item in rating) {
                if (item.likes) {
                    when (age_rating_item.seatAgeRating) {
                        "E" -> {
                            ageRatingName.setTextColor(getColor(requireContext(), R.color.green))

                        }
                        "PG" -> {
                            ageRatingName.setTextColor(getColor(requireContext(), R.color.grey))

                        }
                        "13+" -> {
                            ageRatingName.setTextColor(getColor(requireContext(), R.color.yellow))

                        }
                        "15+" -> {
                            ageRatingName.setTextColor(getColor(requireContext(), R.color.yellow))

                        }
                        "18+" -> {
                            ageRatingName.setTextColor(
                                getColor(
                                    requireContext(),
                                    R.color.text_alert_color_red
                                )
                            )

                        }
                    }

                } else {
                    ageRatingName.setTextColor(getColor(requireContext(), R.color.hint_color))
                }

            }

            v.setOnClickListener {

                if (ageRating.contains(age_rating_item.seatAgeRating)) {
                    ageRating.remove(age_rating_item.seatAgeRating)
                    ageRatingName.setTextColor(getColor(requireContext(), R.color.hint_color))

                } else {
                    when (age_rating_item.seatAgeRating) {
                        "E" -> {
                            ageRatingName.setTextColor(getColor(requireContext(), R.color.green))

                        }
                        "PG" -> {
                            ageRatingName.setTextColor(getColor(requireContext(), R.color.grey))

                        }
                        "13+" -> {
                            ageRatingName.setTextColor(getColor(requireContext(), R.color.yellow))

                        }
                        "15+" -> {
                            ageRatingName.setTextColor(getColor(requireContext(), R.color.yellow))

                        }
                        "18+" -> {
                            ageRatingName.setTextColor(
                                getColor(
                                    requireContext(),
                                    R.color.text_alert_color_red
                                )
                            )

                        }
                    }

                }
                ageRating.add(age_rating_item.seatAgeRating)
            }
        }
    }

    //Recharge Spinner
    private fun rechargeAmount(output: RechargeAmountResponse.Output) {
        val customAdapter = RechargeSpinnerAdapter(
            requireActivity(),
            output.amounts
        )
        text_select_amount.adapter = customAdapter
        text_select_amount.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                rechargeAmount = getAmountList[position].amount.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }

    private fun setBookingHistoryAdapter(output: ArrayList<HistoryResponse.Output>) {
        binding?.nestedUi?.show()
        val gridLayout = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerviewBooking?.layoutManager = LinearLayoutManager(context)
        val adapter = AdapterBookingHistory(requireActivity(), output)
        binding?.recyclerviewBooking?.layoutManager = gridLayout
        binding?.recyclerviewBooking?.adapter = adapter
    }


    private fun setCancelBackSpan(view: View) {
//        try {
//            val tvCancelBack = view.findViewById<TextView>(R.id.tv_cancel_back)
//            val cancelGoBack = TextUtils.concat(
//                Constant().getSpanableText(
//                    ForegroundColorSpan(getColor(requireContext(), R.color.white)),
//                    ResourcesCompat.getFont(requireContext(), R.font.sf_pro_text_bold)!!,
//                    0,
//                    7,
//                    1.0f,
//                    SpannableString(getString(R.string.cancels))
//                ),
//
//                Constant().getSpanableText(
//                    ForegroundColorSpan(getColor(requireContext(), R.color.hint_color)),
//                    ResourcesCompat.getFont(requireContext(), R.font.sf_pro_text_regular)!!,
//                    0,
//                    11,
//                    .8f,
//                    SpannableString(getString(R.string.and_go_back))
//                )
//
//            )
//
//            tvCancelBack.text = cancelGoBack
//        } catch (e: Exception) {
//
//        }
    }

    private fun rechargeCard(addClubRechargeRequest: AddClubRechargeRequest) {
        accountFragViewModel.addClubRechargeCard(addClubRechargeRequest)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {

                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        if (rechargeType == "cc") {
                                            creditCardInit(
                                                HmacKnetRequest(
                                                    it.data.output.bookingid,
                                                    it.data.output.booktype,
                                                    it.data.output.transid,
                                                    preferences.getString(Constant.USER_ID)
                                                        .toString()
                                                ), it.data.output.bookingid.toString()
                                            )
                                        } else {
                                            loader?.dismiss()
                                            paymentMac(
                                                HmacKnetRequest(
                                                    it.data.output.bookingid,
                                                    it.data.output.booktype,
                                                    it.data.output.transid,
                                                    preferences.getString(Constant.USER_ID)
                                                        .toString()
                                                )
                                            )
                                        }


                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }

                                } else {
                                    loader?.dismiss()
                                    val dialog = OptionDialog(requireContext(),
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
                            val dialog = OptionDialog(requireContext(),
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
                            loader?.show(childFragmentManager, null)
                        }
                    }
                }
            }
    }

    // Hmac Request
    private fun paymentMac(request: HmacKnetRequest) {
        accountFragViewModel.paymentKnetHmac(request)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        val intent = Intent(
                                            requireActivity(),
                                            PaymentWebActivity::class.java
                                        )
                                        intent.putExtra("From", "recharge")
                                        intent.putExtra("PAY_URL", it.data.output.callingUrl)
                                        startActivity(intent)
                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }

                                } else {
                                    loader?.dismiss()
                                    val dialog = OptionDialog(requireActivity(),
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
//                            loader = LoaderDialog(R.string.pleasewait)
//                            loader?.show(supportFragmentManager, null)
                        }
                    }
                }
            }
    }

    private fun countryCodeLoad() {
        accountFragViewModel.countryCode(requireActivity())
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    println("LocationResponse--->${it.data.output}")
                                    countryCodeList = it.data.output
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
        mobile_code.setOnClickListener {
            bottomDialog(output)
        }
    }

    private fun updateAccount(updateAccountRequest: UpdateAccountRequest) {
        accountFragViewModel.updateAccount(updateAccountRequest)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    loader?.dismiss()
                                    retrieveUpdateResponse(it.data.output)
                                } else {
                                    val dialog = OptionDialog(requireActivity(),
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
                            loader?.show(childFragmentManager, null)
                        }
                    }
                }
            }
    }

    private fun retrieveUpdateResponse(output: UpdateAccountResponse.Output) {
        val dialog = OptionDialog(requireActivity(),
            R.mipmap.ic_launcher,
            R.string.app_name,
            getString(R.string.profileUpdate),
            positiveBtnText = R.string.ok,
            negativeBtnText = R.string.no,
            positiveClick = {
            },
            negativeClick = {
            })
        dialog.show()
        text_make_editable.show()
        view_ConfPassword.hide()
        textConfPassword.hide()
        view39_line.hide()
        enter_ConfPassword.hide()
        view19.hide()
        UpdateAccount.hide()
    }

    private fun getAmountLoad() {
        accountFragViewModel.getAmount(requireActivity())
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    getAmountList = it.data.output.amounts
                                    rechargeAmount(it.data.output)
                                } else {
                                    val dialog = OptionDialog(requireActivity(),
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

    private fun myBooking(request: MyBookingRequest) {
        accountFragViewModel.getMyBookingData(request)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        setBookingHistoryAdapter(it.data.output)

                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }

                                } else {
                                    bookingText = it.data?.msg.toString()
                                }
                            }
                        }

                        Status.ERROR -> {
                            loader?.dismiss()
                            val dialog = OptionDialog(requireContext(),
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

    private fun myNextBooking(request: NextBookingsRequest) {
        accountFragViewModel.getNextBookingData(request)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        retrieveNextBookedResponse(it.data)
                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }
                                }
                            }
                        }
                        Status.ERROR -> {
                            loader?.dismiss()
                            val dialog = OptionDialog(requireActivity(),
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data?.message ?: "Something went wrong",
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

    private fun retrieveNextBookedResponse(output: NextBookingResponse) {
        binding?.nestedUi?.show()
        val gridLayout = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerviewBooking?.layoutManager = LinearLayoutManager(context)
        val adapter = UpcomingBookingAdapter(requireContext(), output.output, this, this)
        binding?.recyclerviewBooking?.isNestedScrollingEnabled = false
        binding?.recyclerviewBooking?.layoutManager = gridLayout
        binding?.recyclerviewBooking?.adapter = adapter
        binding?.recyclerviewBooking?.show()

    }

    private fun getProfile(profileRequest: ProfileRequest) {
        accountFragViewModel.getProfile(profileRequest)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    profileList = it.data.output.experience
                                    setExperienceFlexbox(
                                        experience_list_preference!!,
                                        it.data.output.experience
                                    )
                                    val seatListPreference =
                                        view!!.findViewById<FlexboxLayout>(R.id.seat_list_preference)
                                    val typeListPreference =
                                        view!!.findViewById<FlexboxLayout>(R.id.type_list_preference)
                                    val ageRatingListPreference =
                                        view!!.findViewById<FlexboxLayout>(R.id.age_rating_list_preference)
                                    setSeatCategoryFlexbox(
                                        seatListPreference,
                                        it.data.output.seatCategory
                                    )
                                    setSeatTypeFlexbox(typeListPreference, it.data.output.seatType)
                                    setAgeRatingFlexbox(
                                        ageRatingListPreference,
                                        it.data.output.rating
                                    )
                                    retrievedProfile(it.data.output)

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

    @SuppressLint("SetTextI18n")
    private fun retrievedProfile(output: ProfileResponse.Output) {
        enter_user_names.setText(output.userName)
        enter_first_name.setText(output.firstName)
        enter_last_name.setText(output.lastName)
        enter_emails.setText(output.email)
        enter_mobile_numbers.setText(output.mobilePhone)
        enter_city.setText(output.city)
        enter_date_births.text = output.dob
        binding?.textUserAccountName?.text = output.firstName + " " + output.lastName
//        loginType = output.guest
        println("GuestChwck--->${output.guest}")

        if (output.userName.isNullOrEmpty()) {
            view_user_name.hide()
            text_user_name_title.hide()
            enter_user_name.hide()
        } else {
            view_user_name.show()
            text_user_name_title.show()
            enter_user_name.show()
        }

//        type=output.
        mobile_code.setText(output.countryCode)
        binding?.textWalletUserId?.text = getString(R.string.wallet_Id) + output.cardNumber
        binding?.textUserWalletKd?.text = getString(R.string.wallet_balance) + output.balance

        gender = output.gender

        println("CheckGender--->${output.gender}")
        if (gender == "Male") {
            male.isChecked = true
            female.isChecked = false
            male.buttonTintList =
                ColorStateList.valueOf(getColor(requireContext(), R.color.text_alert_color_red))
            female.buttonTintList =
                ColorStateList.valueOf(getColor(requireContext(), R.color.text_color))
        } else if (gender == "FeMale") {
            male.isChecked = false
            female.isEnabled = true
            male.buttonTintList =
                ColorStateList.valueOf(getColor(requireContext(), R.color.text_color))
            female.buttonTintList =
                ColorStateList.valueOf(getColor(requireContext(), R.color.text_alert_color_red))
        }
    }

    override fun onItemClick(view: CountryCodeResponse.Output) {
        mobile_code?.setText(view.isdCode)
        countryCode = view.isdCode
    }

    private fun broadcastIntent() {
        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onItemClick(view: String) {
        Constant.taglist.add(view)
    }

    private fun bottomDialog(countryList: ArrayList<CountryCodeResponse.Output>) {
        val mDialogView = layoutInflater.inflate(R.layout.countrycode_new_dialog, null)
        val mBuilder = AlertDialog.Builder(requireContext(), R.style.MyDialogTransparent)
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
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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
            mobile_code.setText(countryCode)
        }
    }

    private fun signOut() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.cancel_dialog)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.subtitle.text = getString(R.string.signout)
        dialog.imageBackground.setImageResource(R.drawable.cancel_background)
        dialog.show()

        dialog.consSure?.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            preferences.clearData()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            requireActivity().finish()
        }

        dialog.negative_btn?.setOnClickListener {
            dialog.dismiss()
        }

    }

    override fun onResume() {
        super.onResume()
        if (OPEN_FROM == 1) {
            println("FromPayment--->")
            image_knet.setColorFilter(requireActivity().getColor(R.color.hint_color))
            text_kent.setTextColor(requireContext().getColor(R.color.hint_color))

            image_credit_card.setColorFilter(requireActivity().getColor(R.color.hint_color))
            text_credit_card.setTextColor(requireContext().getColor(R.color.hint_color))
            clickEnable = 0
            getProfile(
                ProfileRequest(
                    latitude,
                    longitude,
                    preferences.getString(Constant.USER_ID).toString()
                )
            )

        }
    }

    override fun cancelReserv(foodSelctedItem: NextBookingResponse.Current) {
        cancelReservationDialog(foodSelctedItem.bookingId)
    }

    private fun cancelReservationDialog(bookingId: String) {
        val viewGroup = requireActivity().findViewById<ViewGroup>(android.R.id.content)
        val dialogView: View =
            LayoutInflater.from(requireActivity()).inflate(R.layout.cancel_dialog, viewGroup, false)
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setView(dialogView)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        alertDialog.show()

        dialogView.consSure?.setOnClickListener {
            alertDialog.dismiss()
            cancelReservation(FinalTicketRequest(bookingId, 0))
        }

        dialogView.negative_btn?.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun cancelReservation(finalTicketRequest: FinalTicketRequest) {
        accountFragViewModel.cancelReservation(finalTicketRequest)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        val dialog = OptionDialog(requireActivity(),
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            it.data.msg,
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {
                                            },
                                            negativeClick = {
                                            })
                                        dialog.show()
                                    } catch (e: Exception) {
                                        val dialog = OptionDialog(requireActivity(),
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            it.data.msg.toString(),
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {
                                            },
                                            negativeClick = {
                                            })
                                        dialog.show()
                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }

                                } else {
                                    loader?.dismiss()
                                    val dialog = OptionDialog(requireActivity(),
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

    override fun resenDmail(output: NextBookingResponse.Current) {
        resendMail(
            ResendRequest(
                output.bookingId,
                output.bookingType,
                "",
                preferences.getString(Constant.USER_ID).toString()
            )
        )
    }

    private fun resendMail(resendRequest: ResendRequest) {
        accountFragViewModel.resendMail(resendRequest)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        val dialog = OptionDialog(requireActivity(),
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            it.data.msg,
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {
                                            },
                                            negativeClick = {
                                            })
                                        dialog.show()
                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }

                                } else {
                                    loader?.dismiss()
                                    val dialog = OptionDialog(requireActivity(),
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



}