package com.cinescape1.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.UnderlineSpan
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.app.ActivityCompat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.ArrayList


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATED_IDENTITY_EQUALS")
 class Constant {
    fun compareVersion(
            context: Context,
            apiVersion: String
    ): Boolean {
        return try {
            val appV: Float = version.toFloat()
            val apiV = apiVersion.toFloat()
            println("app vers$version")
            println("apii ver" + apiV.toString() + "==clas name" + context.javaClass.name)
            appV < apiV
        } catch (e: java.lang.Exception) {
            false
        }
    }

    private fun addClickablePartTextViewResizable(
            strSpanned: Spanned, tv: TextView,
            maxLine: Int, spanableText: String, viewMore: Boolean
    ): SpannableStringBuilder? {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)
        if (str.contains(spanableText)) {
            ssb.setSpan(object : MySpannable(false) {
                override fun onClick(widget: View) {
                    super.onClick(widget)
                    if (viewMore) {
                        tv.layoutParams = tv.layoutParams
                        tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                        tv.invalidate()
                        makeTextViewResizable(tv, -1, "", false)
                    } else {
                        tv.layoutParams = tv.layoutParams
                        tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                        tv.invalidate()
                        makeTextViewResizable(tv, 5, ".. Read More", true)
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)
        }
        return ssb
    }

    fun makeTextViewResizable(
            tv: TextView,
            maxLine: Int,
            expandText: String,
            viewMore: Boolean
    ) {
        if (tv.tag == null) {
            tv.tag = tv.text
        }
        val vto = tv.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val obs = tv.viewTreeObserver
                obs.removeGlobalOnLayoutListener(this)
                if (maxLine == 0) {
                    val lineEndIndex = tv.layout.getLineEnd(0)
                    val text =
                            tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                                    .toString() + "... " + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    tv.setText(
                            addClickablePartTextViewResizable(
                                    Html.fromHtml(tv.text.toString()), tv, maxLine, expandText,
                                    viewMore
                            ), TextView.BufferType.SPANNABLE
                    )
                } else if (maxLine > 0 && tv.lineCount >= maxLine) {
                    val lineEndIndex = tv.layout.getLineEnd(maxLine - 1)
                    val text =
                            tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                                    .toString() + "... " + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    tv.setText(
                            addClickablePartTextViewResizable(
                                    Html.fromHtml(tv.text.toString()), tv, maxLine, expandText,
                                    viewMore
                            ), TextView.BufferType.SPANNABLE
                    )
                } else {
                    val lineEndIndex =
                            tv.layout.getLineEnd(tv.layout.lineCount - 1)
                    val text =
                            tv.text.subSequence(0, lineEndIndex).toString() + "" + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    tv.setText(
                            addClickablePartTextViewResizable(
                                    Html.fromHtml(tv.text.toString()), tv, lineEndIndex, expandText,
                                    viewMore
                            ), TextView.BufferType.SPANNABLE
                    )
                }
            }
        })
    }



    fun getSpanableText(color: ForegroundColorSpan, font:Typeface, start:Int, end:Int, size:Float, string:SpannableString): SpannableString {
        string.setSpan(color, start,end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        string.setSpan(RelativeSizeSpan(size), start,end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        string.setSpan(font, start,end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return string
    }

    fun getSpanableText1(color: ForegroundColorSpan, font:Typeface, underlineText:UnderlineSpan,  start:Int, end:Int, size:Float, string:SpannableString): SpannableString {
        string.setSpan(color, start,end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        string.setSpan(RelativeSizeSpan(size), start,end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        string.setSpan(font, start,end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        string.setSpan(underlineText, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return string
    }


    fun updateApp(context: Context) {
        val appPackageName = context.packageName
        try {
            context.startActivity(
                    Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")
                    )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                    Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
            )
        }
    }


    interface EnableLocationClickEvents {
        companion object {
            const val ENABLE_LOCATION_BUTTON = 1
            const val NO_THANKS = 2
        }
    }

    interface TermConditionAndPrivacy {
        companion object {
            const val PRIVACY = 0
            const val TERM_OF_USE = 1
            const val TC = 2
        }
    }

    interface PaymentType {
        companion object {
            const val THEATER = "theatre"
            const val SHOWSELECTION = "campaign"
            const val CAMPAGION = "showselection"
            const val ORDERSNACKS = "ordersnacks"
            const val GIFTCARD = "giftcard"
            const val CHOOSEMOVIE = "chooseMovie"
            const val MYBOOKINGS = "myBookings"
            const val INTHEATRE = "inTheatre"
            const val LOYALITY = "LOYALTY"

        }
    }

    interface LoginType {
        companion object {
            const val FACEBOOK = "fb"
            const val GOOGLE = "gplus"
            const val REGISTER_EMAIL = "registerEmail"
            const val PVR = "pvr"
            const val GUEST = "guest"
            const val TRUECALLER = "TC"
        }
    }
    interface BroadCast {
        companion object {
            const val ACTIVE_BROADCAST = "com.wowcinemas.TimerBroadCast"
        }
    }
    companion object{
        const val platform = "ANDROID"
        const val version = "1.5"
        const val status = "success"
        const val SUCCESS_CODE = 10001
         var SEE_ALL_TYPE = 0
        const val TITLE = "title"
        const val IS_FROM = "isFrom"
        const val ENABLE_GPS_LOCATION_REQUEST = 2
        const val CHANGE_LOCATION_REQUEST = 3
        const val REQUEST_SELECT_FILE = 2222
        const val REQUEST_CAMERA = 1111
         var SEAT_SESSION_CLICK = 0
        const val RESULT_OK = -1
        var LANGUAGE = "en"
        var ON_BACK_FOOD = 0
        var ET = 0
        var AT = 0
        var timerCounter = 0

        var FNB = "fnb"

        var taglist: ArrayList<String> = ArrayList()
        const val LETTER_SPACING = 0.2
        const val IS_CALLED_FROM_ALL_THEATRES = "isCalledFromTheatres"
        const val IS_CALLED_FROM_FNB = "isCalledFromFnB"
        // Preferences
        var DECIFORMAT = DecimalFormat("0.000")

        const val FIRST_NAME = "first_name"
        const val COUNTRY_CODE = "country_code"
        const val USER_CITY = "user_city"
        const val LAST_NAME = "last_name"
        const val USER_DOB = "last_dob"
        const val USER_NAME = "user_name"
        const val CHECK_BOX = "status"
        const val FACEBOOK_USER_ID = "facebook_user_id"
        const val USER_GENDER = "user_gender"
        const val USER_AGE = "user_age"
        const val AUTH_TOKEN = "auth_token"
        const val EXPIRY_TIME = "expiry_time"
        const val SESSION_ID = "session_id"
        const val USER_ID = "user_id"
        const val SHOW_PRESS = "press"
        const val USER_IMAGE = "user_image"
        const val USER_NUMBER = "user_number"
        const val API_CALL = "api_call"
        const val API_CALL2 = "api_call2"
        const val BOOKING_FIRST_INDEX = "booking_first_index"
        const val LOYALITY_POINT = "loyality_point"
        const val IS_LOGIN = "is_login"
        var select_pos = 0
        const val TYPE_LOGIN = "type_login"
        const val ID_TOKEN = "id_token"
        const val IS_FOOD = "is_food"
        const val IS_WHATSAPP = "is_whatsapp"
        const val CORPORATE_USER = "corporate_user"
        const val DOB = "dob"
        const val GENDER = "gender"
        const val LOYALITY_STATUS = "loyality_status"
        const val USER_EMAIL = "user_email"
        const val SAVED_ADDRESS = "saved_address"
        const val SAVED_LATITUDE = "lat"
        const val SAVED_LONGITUDE = "lng"
        const val ADDRESS_STATUS = "address_status"
        var DISPLAY = 1
        const val PREFLANG = "preflang"
        const val USER_CARD = "user_card"
        const val IS_REF = "is_ref"
        const val IS_HM = "is_hm"
        const val IS_HF = "is_hf"
        const val IS_WF = "is_wf"
        const val IS_HL = "is_HL"
        const val IS_LY = "is_LY"
        const val LSM = "lsm"
        const val LS = "ls"
        const val RM = "rm"
        const val SVM = "svm"
        const val CVD = "cvd"

        const val USEOFFER = "useroffer"
        const val APPLYUSEOFFER = "applyuseroffer"

        const val SELECTED_CITY_ID = "selectedCityId"
        const val SELECTED_CITY_NAME = "selectedCityName"
        const val LOCATION_CHANGED = "locationChange"
        const val IMAGE_COUNT = "count"
        const val GCM_TOKEN = "gcm_token"
        const val PAYMENT_OPTION = "payment"
        const val LOGIN_TYPE = "loginType"
        const val LAT = "lat"
        const val LNG = "lng"
        const val SHOULD_PASSWORD_FIELD_VISIBLE = "should_password_visible"


        const val doClear = "doclear"
        const val MOBILE = "mobile"
        const val mid = "movieId"

        const val termsCondition = "https://cinescapeweb.wemonde.co/app/tc"
        const val privacyPolicy = "https://cinescapeweb.wemonde.co/app/pp"
        const val SEAT_BOOKED = 2
        const val SEAT_SELECTED = 3
        const val SEAT_NOT_ABAILABLE = 0
        const val QR_CODE_IMAGE_PATH = "./MyQRCode.png"

    }

    var DECIFORMAT = DecimalFormat("0.000")
    interface PCBackStackActivity {

        companion object {
            const val LOYALITY_SPECIFICATION_ACTIVITY = "loyalityspecificationactivity"
            const val LOYALITY_NONMEMBER_ACTIVITY = "loyalitynonmemberactivity"
            const val MAGAZINE_ACTIVITY = "magazineactivity"
            const val SOUND_LIKE_PLANE_ACTIVITY = "soundlikeplan"
            const val THEATER_ACTIVITY = "theatreActivity"
            const val THEATER_DETAILS = "theaterdetailsActivity"
            const val PCSCAN_ACTIVITY = "PCCouponScan"
            const val GRAB_ACTIVITY = "GrabABiteActivity"
            const val VKAAO_PAGE = "PCTheatreDemandActivity"
            const val PCPRE_ACTIVITY = "PCPreBookActivity"
            const val MYTICKET_ACTIVITY = "myTicketActivity"
            const val PREFERENCES_ACTIVITY = "preferencesActivity"
            const val GIFT_CARD_ACTIVITY = "giftCardActivity"
            const val OFFER_ACTIVITY = "offerActivity"
            const val LANDING_ACTIVITY = "landingActivity"
            const val QUICKBITE_ACTIVITY = "quickBiteActivity"
            const val SPLASH_ACTIVITY = "splashActivity"
            const val OPEN_ACTIVITY_NAME = "activity_name"
            const val SOCIAL_INFORMATION = "social_information"
            const val ORDER_SNACKS = "ordersancks"
            const val ORDER_SUMMARY = "ordersummary"
            const val IN_SEAT_DINING = "inSeatDining"
        }
    }


    // public static String BI_PT="bi_pt";
    interface IntentKey {
        companion object {
            const val MOVIE_ID = "movieId"
            const val MOVIE_DATE = "movieDate"
            const val VKAO_ID = "vkaoId"
            const val IS_TRAILER = "is_trailer"
            const val MOVIE_DATA = "moviedata"
            const val NOTIFICATION_ID = "notificationID"
            const val CINEMALIST = "cinemalist"
            const val MASTER_MOVIE_ID = "masterMovieId"
            const val RELEASE_DATE = "release_date"
            const val LANGUAGE = "language"
            const val CITY_ID = "1"
            const val DEFAULT_PAGE_NUMBER = "1"
            const val CHANGE_LOCATION = "changelocation"
            const val SHOW_ID = "showID"
            const val CINEMA_ID = "cinemaID"
            const val SCREEN_ID = "screenID"
            const val CINEMA_NAME = "cinemaName"
            const val CINEMA_ADDRESS = "cinemaAddress"
            const val IS_MOVIE_DETAIL = "isMovieDetail"
            const val TRANSACTION_ID = "transactionId"
            const val FROM = "from"
            const val TICKET_BOOKING_DETAILS = "ticket_booking_details"
            const val FOOD_ITEM = "food_item"
            const val BOOK_TYPE = "booktype"
            const val GIFT_PURCHASE_SUMMARY = "gift_purchase_summary"
            const val SOUNDS_LIKE_A_PLAN_DATA = "sounds_like_a_plan"
            const val transid = "transid"
            const val cinemacode = "cinemacode"
            const val FromCinema_TO_Food_Sound_like_a_Plan = "FromCinema_TO_Food_Sound_like_a_Plan"
            const val TITLE_HEADER = "title_header"
            const val MOVIE_NAME = "movieName"
            const val IS_COMING_SOON = "isComingSoon"
            const val GENER = "gener"
            const val PAYMENT_TYPE = "payment_type"
            const val IS_SESSION_EXPIRE = "is_session_expire"
            const val REMAING_TIME = "remaing_time"
            const val LATITUDE = "latitude"
            const val LONGITUDE = "longitude"
            const val YOUTUBE_URL = "youtube_url"
            const val MOVIE_LIST = "youtube_url"
            const val QRCODE_URL = "qrcode_url"
            const val DATE = "date"
            const val SER_KEY = "ser"
            const val GIFT_CARD_DETAILS = "gift_card_details"
            const val OFFER_ID = "offerId"
            const val PAYMENT_TYPE_SELECTED = "payment_mode"
            const val BOOKING_ID = "bookingId"
            const val Offer_ID = "offerId"
            const val SHOW_LIST = "show_list"
            const val SU = "show_url"
            const val SM = "show_messsage"
            const val KOTAK_SATURDAY_DETAILS = "kotak_saturday_details"
            const val CHOOSE_MOVIE_DATA = "chooseMovieData"
            const val SELECT_LANGUAGE = "select_language"
            var OPEN_FROM :Int=0
            var USER_ID :String=""
            var LANGUAGE_SELECT = "en"
            var TimerTime :Long=360
            var TimerExtand :Long=90
            var TOTAL_PRICE :Long=0
            var TimerExtandCheck :Boolean=false
        }
    }


    lateinit var pgdialog: ProgressDialog
     lateinit var mContext: Context


    fun isValidMail(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidMobile(phone: String?): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }


    fun hideKeyboard(activity: Activity) {
        val imm =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun dismissDialog() {
        try {
            if (pgdialog.isShowing) pgdialog.dismiss()
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    fun isNetworkStatusAvialable(
            context: Context,
            screenName: String?
    ): Boolean {
        val conMgr =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.activeNetworkInfo
        if (netInfo == null || !netInfo.isConnected || !netInfo.isAvailable) {

            return false
        }
        return true
    }

    fun isConnected(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }


    fun applyLetterSpacing(
            pcTextView: TextView,
            text: String,
            letterspacing: Double
    ) {

        pcTextView.setText(text)
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi / 160f)
    }

    fun convertDpToPixel(dp: Float, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return px.toInt()
    }


    fun removeTrailingZeroFormater(d: Float): String? {

            val df = DecimalFormat("#0.00")
            df.format(d.toDouble())

        return d.toString()
        }

    fun removeTrailingZeroFormater1(d: Float): String? {
        return if (d == (d.toLong()).toFloat())
            String.format("%d", d.toLong())
        else {
            val df = DecimalFormat("#0.00")
            df.format(d.toDouble())
        }
    }

    fun createQrCode(text: String):Bitmap{
        val decodedString: ByteArray = Base64.decode(text, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//        val bmp: Bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888)
//        val buffer: ByteBuffer = ByteBuffer.wrap(decodedString)
//        bmp.copyPixelsFromBuffer(buffer)
        return decodedByte
    }

    fun hasPermissions(context: Activity, permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (i in permissions.indices) {
                if (ActivityCompat.checkSelfPermission(
                                context,
                                permissions[i]
                        ) !== PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }


    fun changeFormat(date: String?): String? {
        return if (date != null && date != "") {
            try {
                val sdf = SimpleDateFormat("dd-MM-yyyy")
                val d = sdf.parse(date)
                val displayFormat = SimpleDateFormat("dd MMM, yyyy")
                displayFormat.format(d)

                // all done
            } catch (e: java.lang.Exception) {
                Log.e("gDatStriFrDefStringDate", e.toString())
                ""
            }
        } else {
            ""
        }
    }

    fun getDeviceId(context: Activity): String {

        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun hideKeyboard(){
        val imm: InputMethodManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

}

