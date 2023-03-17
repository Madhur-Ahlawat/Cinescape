package com.cinescape1.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import com.cinescape1.data.models.responseModel.NextBookingResponse
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATED_IDENTITY_EQUALS")
class Constant {
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


    fun getSpanableText(
        color: ForegroundColorSpan,
        font: Typeface,
        start: Int,
        end: Int,
        size: Float,
        string: SpannableString
    ): SpannableString {
        string.setSpan(color, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        string.setSpan(RelativeSizeSpan(size), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        string.setSpan(font, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return string
    }


    companion object {
         val experience: java.util.ArrayList<String> = java.util.ArrayList()
         val seatTypeList: java.util.ArrayList<String> = java.util.ArrayList()
         val seatCategoryList: java.util.ArrayList<String> = java.util.ArrayList()
         var ageRating: java.util.ArrayList<String> = java.util.ArrayList()

        const val platform = "ANDROID"
        const val version = "1.5"
        const val status = "success"
        const val SUCCESS_CODE = 10001

        const val ENABLE_GPS_LOCATION_REQUEST = 2
        const val CHANGE_LOCATION_REQUEST = 3
        const val REQUEST_SELECT_FILE = 2222
        const val REQUEST_CAMERA = 11111
        const val RESULT_OK = -1
        const val IMAGE_COUNT = "count"
        var SEE_ALL_TYPE = 0
        var SEE_ALL_TYPES = 0
        var SEAT_SESSION_CLICK = 0
        var ON_BACK_FOOD = 0
        var FNB = "fnb"
        var taglist: ArrayList<String> = ArrayList()
        var select_pos = 0
        var SEAT_TYPE = ""
        var SEAT_CATEGORY = ""

        // Preferences
        var DECIFORMAT = DecimalFormat("0.000")
        const val FIRST_NAME = "first_name"
        const val COUNTRY_CODE = "country_code"
        const val USER_CITY = "user_city"
        const val LAST_NAME = "last_name"
        const val USER_DOB = "last_dob"
        const val USER_NAME = "user_name"
        const val USER_GENDER = "user_gender"
        const val USER_ID = "user_id"

        const val IS_LOGIN = "is_login"

        const val TYPE_LOGIN = "type_login"
        const val ID_TOKEN = "id_token"
        const val USER_EMAIL = "user_email"
        var DISPLAY = 1
        var applyCheck = 2
        const val MOBILE = "mobile"
        const val mid = "movieId"
        const val transId = ""
        const val bookingId = ""

        const val termsCondition = "https://cinescapeweb.wemonde.co/app/tc"
        const val termsConditions = "https://www.cinescape.com.kw/terms-and-conditions"
        const val privacyPolicy = "https://cinescapeweb.wemonde.co/app/pp"



        var bankOfferClick = false
        const val promoClick = false

        const val KNETID = "401"
        const val CREDITCARDID = "402"
         var LANGUAGE = "language"

        var walletCheck = 0

    }



    // public static String BI_PT="bi_pt";
    interface IntentKey {
        companion object {
            const val MOVIE_ID = "movieId"
            const val LANGUAGE = "language"
            const val TRANSACTION_ID = "transactionId"
            const val BOOK_TYPE = "booktype"
            const val transid = "transid"
            const val OFFER_ID = "offerId"
            const val BOOKING_ID = "bookingId"
            const val Offer_ID = "offerId"
            const val SELECT_LANGUAGE = "select_language"
            var OPEN_FROM: Int = 0
            var DialogShow: Boolean = true
            var BOOKINGClick: Int=0
            var BACKFinlTicket: Int=0
            var NextBookingsResponse: NextBookingResponse?=null
            var USER_ID: String = ""
            var LANGUAGE_SELECT = "en"
            var TimerTime: Long = 360
            var TimerExtand: Long = 90
            var TimerExtandCheck: Boolean = false
        }
    }

    lateinit var mContext: Context

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

    fun createQrCode(text: String): Bitmap {
        val decodedString: ByteArray = Base64.decode(text, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//        val bmp: Bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888)
//        val buffer: ByteBuffer = ByteBuffer.wrap(decodedString)
//        bmp.copyPixelsFromBuffer(buffer)
        return decodedByte
    }


    fun changeFormat(date: String?): String? {
        return if (date != null && date != "") {
            try {
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                val d = sdf.parse(date)
                val displayFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
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


    fun addMargins(v: View, left: Int, top: Int, right: Int, bottom: Int) {
        var params = v.layoutParams as LinearLayout.LayoutParams
        if (params == null) params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val oldLeft = params.leftMargin
        val oldTop = params.topMargin
        val oldRight = params.rightMargin
        val oldBottom = params.bottomMargin
        params.setMargins(oldLeft + left, oldTop + top, oldRight + right, oldBottom + bottom)
        v.layoutParams = params
    }

    fun appBarHide(activity: Activity){
        //AppBar Hide
        activity.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
    }
}