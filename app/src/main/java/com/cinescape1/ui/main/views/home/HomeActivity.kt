package com.cinescape1.ui.main.views.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.NextBookingsRequest
import com.cinescape1.data.models.responseModel.FoodResponse
import com.cinescape1.data.models.responseModel.NextBookingResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityHomeBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.adapters.sliderAdapter.AdapterMultiMovieAlertBooking
import com.cinescape1.ui.main.views.finalTicket.FinalTicketActivity
import com.cinescape1.ui.main.views.food.FoodActivity
import com.cinescape1.ui.main.views.home.adapter.CustomSpinnerAdapter
import com.cinescape1.ui.main.views.home.fragments.account.AccountPageFragment
import com.cinescape1.ui.main.views.home.fragments.home.HomeFragment
import com.cinescape1.ui.main.views.home.fragments.more.MorePageFragment
import com.cinescape1.ui.main.views.home.fragments.movie.MoviesFragment
import com.cinescape1.ui.main.views.home.fragments.movie.MoviesFragment.Companion.positionState
import com.cinescape1.ui.main.views.home.viewModel.HomeViewModel
import com.cinescape1.ui.main.views.login.LoginActivity
import com.cinescape1.utils.*
import com.cinescape1.utils.Constant.IntentKey.Companion.BACKFinlTicket
import com.cinescape1.utils.Constant.IntentKey.Companion.BOOKINGClick
import com.cinescape1.utils.Constant.IntentKey.Companion.NextBookingsResponse
import com.cinescape1.utils.Constant.IntentKey.Companion.OPEN_FROM
import com.google.android.gms.location.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.alert_booking.view.*
import kotlinx.android.synthetic.main.alert_booking2.view.*
import kotlinx.android.synthetic.main.fragment_food.*
import java.security.MessageDigest
import javax.inject.Inject

@Suppress("DEPRECATION")
class HomeActivity : DaggerAppCompatActivity(),
    AdapterMultiMovieAlertBooking.RecycleViewItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }
    private var binding: ActivityHomeBinding? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var timeback: Long = 0
    private var mAlertDialog: AlertDialog? = null
    private var cinemaId = ""
    private var loader: LoaderDialog? = null
    private var spinner: AppCompatSpinner? = null
    private var locationlist = ArrayList<FoodResponse.Output.Cinema>()
    private var broadcastReceiver: BroadcastReceiver? = null
    private var buttonClick = 0
    private var flagHome = false
    var homeFrag = 0
    var movieFrag = 0
    var accountFrag = 0
    var moreFrag = 0
    private var foodNotHomeTab = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(this, "en")
            }
            else -> {
                LocaleHelper.setLocale(this, "en")
            }
        }

        homeFrag = 0
        movieFrag = 0
        accountFrag = 0
        moreFrag = 0
        foodNotHomeTab = true

        setContentView(view)

        navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    foodNotHomeTab = true
                    if (homeFrag == 0) {

                        BACKFinlTicket += 1
                        buttonClick = 0
                        manageBooking()
                        flagHome = true
                        setCurrentFragment(HomeFragment())

                        homeFrag = 1
                        movieFrag = 0
                        accountFrag = 0
                        moreFrag = 0

                    }

                }
                R.id.movieFragment -> {
                    foodNotHomeTab = false

                    if (movieFrag == 0) {
                        positionState = 0
                        buttonClick = 1
                        BACKFinlTicket += 1
                        buttonClick = 1
                        flagHome = false
                        binding?.imageView42?.hide()
                        setCurrentFragment(MoviesFragment(0))

                        homeFrag = 0
                        movieFrag = 1
                        accountFrag = 0
                        moreFrag = 0
                    }

                }
                R.id.foodFragment -> {
                    buttonClick = 1
                    BACKFinlTicket += 1
                    binding?.imageView42?.hide()
                    if (!preferences.getBoolean(Constant.IS_LOGIN)) {
                        startActivity(
                            Intent(this, LoginActivity::class.java).putExtra("CINEMA_ID", cinemaId)
                                .putExtra("BOOKING", "FOOD")
                        )
                    } else {
                        foodDialog()
                    }
                }
                R.id.accountFragment -> {
                    foodNotHomeTab = false

                    if (accountFrag == 0) {
                        if (preferences.getBoolean(Constant.IS_LOGIN)) {
                            buttonClick = 1
                            BACKFinlTicket += 1
                            buttonClick = 1
                            flagHome = false
                            binding?.imageView42?.hide()
                            setCurrentFragment(AccountPageFragment())
                        } else {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        homeFrag = 0
                        movieFrag = 0
                        accountFrag = 1
                        moreFrag = 0
                    }


                }
                R.id.moreFragment -> {
                    foodNotHomeTab = false

                    if (moreFrag == 0) {
                        buttonClick = 1
                        BACKFinlTicket += 1
                        buttonClick = 1
                        flagHome = false
                        binding?.imageView42?.hide()
                        setCurrentFragment(MorePageFragment())

                        homeFrag = 0
                        movieFrag = 0
                        accountFrag = 0
                        moreFrag = 1
                    }


                }
            }
            true
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        broadcastReceiver = MyReceiver()
        broadcastIntent()
        foodResponse()
        setNextBooking()

        if (OPEN_FROM == 1) {
            setCurrentFragment(AccountPageFragment())
            binding?.navigationView?.selectedItemId = R.id.accountFragment
        } else {
            setCurrentFragment(HomeFragment())
            flagHome = true
            if (preferences.getBoolean(Constant.IS_LOGIN) && buttonClick == 0) {
                manageBooking()
            } else {
                binding?.imageView42?.hide()
            }

        }



    }

    private fun manageBooking() {

        if (NextBookingsResponse != null && buttonClick == 0) {
            binding?.imageView42?.show()
        } else {
            binding?.imageView42?.hide()
        }

        binding?.imageView42?.setOnClickListener {
            BACKFinlTicket = 0
            if (NextBookingsResponse != null && buttonClick == 0) {
                buttonClick = 1
                bookingsPopup(NextBookingsResponse!!)

            }
        }

    }

    private fun foodResponse() {
        homeViewModel.foodResponse().observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loader?.dismiss()
                        resource.data?.let { it ->
                            try {
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    locationlist = it.data.output.cinemas
                                    cinemaId = it.data.output.cinemas[0].id
                                    if (intent.hasExtra("BOOKING")) {
                                        foodDialog()
                                    }
                                }
                            } catch (e: Exception) {
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    it.data?.msg.toString(),
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {

                                    },
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
                            it.data?.message ?: "Something went wrong",
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {

                            },
                            negativeClick = {})
                        dialog.show()
                    }
                    Status.LOADING -> {
                    }
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun foodDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_food)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(R.color.black50))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()

        spinner = dialog.spinner
        dialog.text_cancel_goback.setOnClickListener {
            if (flagHome == true) {
                buttonClick = 0
                manageBooking()
                flagHome = true
                dialog.dismiss()
                if (!foodNotHomeTab) {
                    binding?.navigationView?.selectedItemId = R.id.homeFragment
                    setCurrentFragment(HomeFragment())
                }
            } else {
                flagHome = false
                dialog.dismiss()
            }

        }

        dialog.text_proceeds?.setOnClickListener {
            if (!preferences.getBoolean(Constant.IS_LOGIN)) {
                dialog.dismiss()
                startActivity(
                    Intent(this, LoginActivity::class.java).putExtra("CINEMA_ID", cinemaId)
                        .putExtra("BOOKING", "FOOD").putExtra("type", "0")
                )
            } else {
                dialog.dismiss()
                startActivity(
                    Intent(this, FoodActivity::class.java).putExtra("CINEMA_ID", cinemaId)
                        .putExtra("BOOKING", "FOOD").putExtra("typeSkip", "SkipButtonHide")
                )
            }
        }

        val customAdapter = CustomSpinnerAdapter(
            this, locationlist
        )
        dialog.spinner?.adapter = customAdapter
        dialog.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                cinemaId = locationlist[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun broadcastIntent() {
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }

    private fun setNextBooking() {
        myNextBooking(
            NextBookingsRequest(
                "", "", 0, preferences.getString(Constant.USER_ID).toString(), false
            )
        )
    }


    private fun myNextBooking(request: NextBookingsRequest) {
        homeViewModel.getNextBookingData(request).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { it ->
                            if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                if (BOOKINGClick == 0) {
                                    retrieveNextBookedResponse(it.data)
                                    NextBookingsResponse = it.data
                                    BOOKINGClick += 1
                                    flagHome = true
                                    println("ListOutputResponse2--------->${NextBookingsResponse}")
                                } else {
                                    NextBookingsResponse = it.data
                                    flagHome = true
                                    println("ListOutputResponse222--------->${NextBookingsResponse}")
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                    }
                    Status.LOADING -> {
                    }
                }
            }
        }
    }

    private fun retrieveNextBookedResponse(output: NextBookingResponse) {
        if (output != null && buttonClick == 0) {
            binding?.imageView42?.show()
        } else {
            binding?.imageView42?.hide()
        }

        binding?.imageView42?.setOnClickListener {
            BACKFinlTicket = 0
            if (NextBookingsResponse != null && buttonClick == 0) {
                buttonClick = 1
                bookingsPopup(output)
            }
        }

    }

    private fun bookingsPopup(output: NextBookingResponse) {
        when (output.output.size) {
            1 -> {
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_booking, null)
                mAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val mBuilder = AlertDialog.Builder(this, R.style.YourThemeName).setView(mDialogView)
                mAlertDialog = mBuilder.create()

                mAlertDialog?.show()
                mDialogView.text_bombshell.isSelected = true
                mDialogView.text_location_name.text = output.output[0].cinemaname
                mDialogView.text_screen_number.text = output.output[0].screenId.toString()
                mDialogView.text_date_visible.text = output.output[0].showDate
                mDialogView.text_time_visible.text = output.output[0].showTime
                mDialogView.text_bombshell.text = output.output[0].moviename
                mDialogView.text13.text = output.output[0].mcensor

                Glide.with(this).load(output.output[0].posterhori)
                    .transform(CutOffLogo())
                    .placeholder(R.drawable.placeholder_movie_alert_poster)
                    .into(mDialogView.image_booking_alert)

                //cancel button click of custom layout
                mDialogView.image_cross_icon.setOnClickListener {
                    //dismiss dialog
                    mAlertDialog?.dismiss()
                    buttonClick = 0
                }

                mDialogView.go_to_booking_btn1.setOnClickListener {
                    setCurrentFragment(AccountPageFragment())
                    binding?.navigationView?.selectedItemId = R.id.accountFragment
                    mAlertDialog?.dismiss()
                    buttonClick = 0
                }

                mDialogView.image_booking_alert.setOnClickListener {
                    BACKFinlTicket += 1
                    buttonClick = 0
                    val intent = Intent(this, FinalTicketActivity::class.java)
                    intent.putExtra(Constant.IntentKey.BOOKING_ID, output.output[0].bookingId)
                    intent.putExtra(
                        Constant.IntentKey.TRANSACTION_ID, output.output[0].transId.toString()
                    )
                    intent.putExtra(Constant.IntentKey.BOOK_TYPE, output.output[0].bookingType)
                    intent.putExtra("FROM", "MTicket")
                    intent.putExtra("FROM_ACCOUNT", "Home")
                    startActivity(intent)
                    mAlertDialog?.dismiss()
                }

                when (output.output[0].experience) {
                    "4DX" -> {
                        Glide.with(this).load(R.drawable.four_dx)
                            .into(mDialogView.text_experience_name)
                    }
                    "Standard" -> {
                        Glide.with(this).load(R.drawable.standard)
                            .into(mDialogView.text_experience_name)
                    }
                    "VIP" -> {
                        Glide.with(this).load(R.drawable.vip).into(mDialogView.text_experience_name)
                    }
                    "IMAX" -> {
                        Glide.with(this).load(R.drawable.imax)
                            .into(mDialogView.text_experience_name)
                    }
                    "3D" -> {
                        Glide.with(this).load(R.drawable.threed_black)
                            .into(mDialogView.text_experience_name)
                    }
                    "DOLBY" -> {
                        Glide.with(this).load(R.drawable.dolby_black)
                            .into(mDialogView.text_experience_name)
                    }
                    "ELEVEN" -> {
                        Glide.with(this).load(R.drawable.eleven_black)
                            .into(mDialogView.text_experience_name)
                    }
                    "SCREENX" -> {
                        Glide.with(this).load(R.drawable.screenx_black)
                            .into(mDialogView.text_experience_name)
                    }
                    "PREMIUM" -> {
                        Glide.with(this).load(R.drawable.premium_black)
                            .into(mDialogView.text_experience_name)
                    }
                }

                val ratingColor = output.output[0].ratingColor
                try {
                    mDialogView.text13.setBackgroundColor(Color.parseColor(ratingColor))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            else -> {
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_booking2, null)
                mAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val mBuilder = AlertDialog.Builder(this, R.style.YourThemeName).setView(mDialogView)
                mAlertDialog = mBuilder.create()


                mAlertDialog?.show()

                mDialogView.img_cross_icon.setOnClickListener {
                    buttonClick = 0
                    mAlertDialog?.dismiss()
                }

                mDialogView.text_have_upcoming_booking.text = getString(R.string.upcoming_booking)
                val recyclerViewAlertBooking =
                    mDialogView.findViewById<View>(R.id.recyclerViewAlertBooking) as RecyclerView
                val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                val adapter = AdapterMultiMovieAlertBooking(this, output.output, this)
                recyclerViewAlertBooking.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

                //buh
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(recyclerViewAlertBooking)
                recyclerViewAlertBooking.layoutManager = gridLayout
                recyclerViewAlertBooking.adapter = adapter
                adapter.renewItems(output.output)
                mDialogView.textView_dots.attachToRecyclerView(recyclerViewAlertBooking)
            }
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - timeback > 1000) {
            timeback = System.currentTimeMillis()
            toast("Press Again to Exit")
            return
        }
        super.onBackPressed()
    }

    override fun onDateClick(showtimeListItem: NextBookingResponse.Current) {
        buttonClick = 0
        binding?.navigationView?.selectedItemId = R.id.accountFragment
        setCurrentFragment(AccountPageFragment())
        mAlertDialog?.dismiss()
    }

    override fun onItemClick(showtimeListItem: NextBookingResponse.Current) {

        buttonClick = 0
        BACKFinlTicket += 1
        mAlertDialog?.dismiss()
        val intent = Intent(this, FinalTicketActivity::class.java)
        intent.putExtra(Constant.IntentKey.BOOKING_ID, showtimeListItem.bookingId)
        intent.putExtra(Constant.IntentKey.TRANSACTION_ID, showtimeListItem.transId.toString())
        intent.putExtra(Constant.IntentKey.BOOK_TYPE, showtimeListItem.bookingType)
        intent.putExtra("FROM", "MTicket")
        intent.putExtra("FROM_ACCOUNT", "Home")
        startActivity(intent)

    }

    class CutOffLogo : BitmapTransformation() {
        override fun transform(
            pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int
        ): Bitmap = Bitmap.createBitmap(
            toTransform, 0, 0, toTransform.width, toTransform.height - 200   // number of pixels
        )

        override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
    }


}