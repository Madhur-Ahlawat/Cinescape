package com.cinescape1.ui.main.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.ModelPreferenceExperience
import com.cinescape1.data.models.requestModel.NextBookingsRequest
import com.cinescape1.data.models.responseModel.FoodResponse
import com.cinescape1.data.models.responseModel.NextBookingResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityHomeBinding
import com.cinescape1.ui.main.FinalTicketActivity
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.viewModels.HomeViewModel
import com.cinescape1.ui.main.views.adapters.foodAdapters.CustomSpinnerAdapter
import com.cinescape1.ui.main.views.adapters.sliderAdapter.AdapterMultiMovieAlertBooking
import com.cinescape1.ui.main.views.fragments.AccountPageFragment
import com.cinescape1.ui.main.views.fragments.HomeFragment
import com.cinescape1.ui.main.views.fragments.MorePageFragment
import com.cinescape1.ui.main.views.fragments.MoviesFragment
import com.cinescape1.utils.*
import com.cinescape1.utils.Constant.IntentKey.Companion.OPEN_FROM
import com.google.android.gms.location.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.alert_booking.view.*
import kotlinx.android.synthetic.main.alert_booking2.view.*
import kotlinx.android.synthetic.main.fragment_food.*
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }
    private var binding: ActivityHomeBinding? = null
    var bookingId = ""
    var transId = ""
    private val PERMISSION_ID = 42
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var Timeback: Long = 0
    var mAlertDialog: AlertDialog? = null
    private var cinemaId = ""
    private var openFromFood = ""
    private var loader: LoaderDialog? = null
    private var locationlist = ArrayList<FoodResponse.Output.Cinema>()

    private var broadcastReceiver: BroadcastReceiver? = null
    val list: ArrayList<ModelPreferenceExperience> = arrayListOf(
        ModelPreferenceExperience(R.drawable.img_vip, 0),
        ModelPreferenceExperience(R.drawable.img_imax, 0),
        ModelPreferenceExperience(R.drawable.img_4dx, 0),
        ModelPreferenceExperience(R.drawable.img_dolby, 0),
        ModelPreferenceExperience(R.drawable.img_3d, 0),
        ModelPreferenceExperience(R.drawable.img_eleven, 0),
        ModelPreferenceExperience(R.drawable.img_2d, 0),
        ModelPreferenceExperience(R.drawable.img_screenx, 0)
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
                println("getLocalLanguageMore--->${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}")
            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(this, "en")
            }
            else -> {
                LocaleHelper.setLocale(this, "en")
            }
        }

        println("CheckLanguage--->${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}")
        setContentView(view)

        if (OPEN_FROM == 1) {
            setCurrentFragment(AccountPageFragment())
            binding?.navigationView?.selectedItemId = R.id.accountFragment
        } else {
            setCurrentFragment(HomeFragment())
            setNextBooking()
        }
        if (intent.hasExtra("BOOKING")) {
            FoodDialog()
        }

        println("OpenFrom--->${OPEN_FROM}")
        navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    setCurrentFragment(HomeFragment())
                }
                R.id.movieFragment -> {
                    setCurrentFragment(MoviesFragment())
                }
                R.id.foodFragment -> {
                    FoodDialog()
                }
                R.id.accountFragment -> {
                    setCurrentFragment(AccountPageFragment())
                }
                R.id.moreFragment -> {
                    setCurrentFragment(MorePageFragment())
                }
            }
            true
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        broadcastReceiver = MyReceiver()
        broadcastIntent()
        foodResponse()
    }

    private fun foodResponse() {
        homeViewModel.foodResponse()
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                try {
                                    if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                        locationlist = it.data.output.cinemas
                                        cinemaId = it.data.output.cinemas[0].id

                                        println("CinemaId--->${cinemaId}")
                                    }
                                } catch (e: Exception) {

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

    private fun FoodDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_food)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.show()

        dialog.text_cancel_goback.setOnClickListener {
            dialog.dismiss()
        }
        dialog.text_proceeds?.setOnClickListener {


            if (!preferences.getBoolean(Constant.IS_LOGIN)) {
                dialog.dismiss()
                startActivity(
                    Intent(this, LoginActivity::class.java)
                        .putExtra("CINEMA_ID", cinemaId)
                        .putExtra("BOOKING", "FOOD")
                        .putExtra("type", "0")
                )
            } else {
                dialog.dismiss()
                startActivity(
                    Intent(this, FoodActivity::class.java)
                        .putExtra("CINEMA_ID", cinemaId)
                        .putExtra("BOOKING", "FOOD")
                        .putExtra("type", "0")
                )
            }

        }

        val customAdapter = CustomSpinnerAdapter(
            this,
            locationlist
        )
        dialog.cinescape_ai_kout?.adapter = customAdapter
        dialog.cinescape_ai_kout?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
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
                "",
                "",
                0,
                preferences.getString(Constant.USER_ID).toString(),
                false
            )
        )
    }

    private fun myNextBooking(request: NextBookingsRequest) {
        homeViewModel.getNextBookingData(request)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {
                                        if (it.data.output.isEmpty()) {
                                            binding?.imageView42?.hide()
                                        } else {
                                            binding?.imageView42?.show()
                                        }
                                        retrieveNextBookedResponse(it.data)
                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession ---> ${e.message}")
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
        println("OutputSize--->${output.output.size}")

        binding?.imageView42?.setOnClickListener {
            mAlertDialog?.show()
        }

        mAlertDialog?.show()

        when (output.output.size) {
            0 -> {
                println("Nothing------->${output.output.size}")
            }
            1 -> {
                val runnable = Runnable {
                    val mDialogView =
                        LayoutInflater.from(this).inflate(R.layout.alert_booking, null)
                    val mBuilder = AlertDialog.Builder(this, R.style.NewDialog).setView(mDialogView)
                    mAlertDialog = mBuilder.show()
                    mAlertDialog?.show()
                    mAlertDialog?.window?.setBackgroundDrawableResource(R.color.black70)

                    mDialogView.text_location_name.text = output.output[0].cinemaname
                    mDialogView.text_screen_number.text = output.output[0].screenId.toString()
                    mDialogView.text_experience_name.text = output.output[0].experience
                    mDialogView.text_date_visible.text = output.output[0].showDate
                    mDialogView.text_time_visible.text = output.output[0].showTime
                    mDialogView.text_bombshell.text = output.output[0].moviename
                    mDialogView.text13.text = output.output[0].mcensor
                    when (output.output[0].mcensor) {
                        "PG" -> {
                            mDialogView.text13.setBackgroundResource(R.color.grey)

                        }
                        "G" -> {
                            mDialogView.text13.setBackgroundResource(R.color.green)

                        }
                        "18+" -> {
                            mDialogView.text13.setBackgroundResource(R.color.red)

                        }
                        "13+" -> {
                            mDialogView.text13.setBackgroundResource(R.color.yellow)

                        }
                        "15+" -> {
                            mDialogView.text13.setBackgroundResource(R.color.yellow)

                        }
                        "E" -> {
                            mDialogView.text13.setBackgroundResource(R.color.wowOrange)

                        }
                        "T" -> {
                            mDialogView.text13.setBackgroundResource(R.color.tabIndicater)

                        }
                        else -> {
                            mDialogView.text13.setBackgroundResource(R.color.blue)

                        }
                    }

                    Glide.with(this)
                        .load(output.output[0].posterhori)
                        .placeholder(R.drawable.movie_default)
                        .into(mDialogView.image_booking_alert)


                    //cancel button click of custom layout
                    mDialogView.image_cross_icon.setOnClickListener {
                        //dismiss dialog
                        mAlertDialog?.dismiss()
                    }

                    mDialogView.go_to_booking_btn1.setOnClickListener {
                        setCurrentFragment(AccountPageFragment())
                        binding?.navigationView?.selectedItemId = R.id.accountFragment
                        mAlertDialog?.dismiss()
                    }

                    mDialogView.image_booking_alert.setOnClickListener {
                        val intent = Intent(this, FinalTicketActivity::class.java)
                        intent.putExtra(Constant.IntentKey.BOOKING_ID, output.output[0].bookingId)
                        intent.putExtra(
                            Constant.IntentKey.TRANSACTION_ID,
                            output.output[0].transId.toString()
                        )
                        intent.putExtra(Constant.IntentKey.BOOK_TYPE, output.output[0].bookingType)
                        startActivity(intent)

                    }
                }
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed(runnable, 2000)
            }
            else -> {
                val runnable = Runnable {
                    val mDialogView =
                        LayoutInflater.from(this).inflate(R.layout.alert_booking2, null)
                    val mBuilder = AlertDialog.Builder(this, R.style.NewDialog).setView(mDialogView)
                    mAlertDialog = mBuilder.show()
                    mAlertDialog?.show()

                    mBuilder.setCancelable(false)
                    mAlertDialog?.setCanceledOnTouchOutside(false)
                    mAlertDialog?.window?.setBackgroundDrawableResource(R.color.black70)
                    mDialogView.img_cross_icon.setOnClickListener {
                        //dismiss dialog
                        mAlertDialog?.dismiss()
                    }

                    mDialogView.text_have_upcoming_booking.text=getString(R.string.upcoming_booking)
                    mDialogView.go_to_booking_btn.setOnClickListener {
                        binding?.navigationView?.selectedItemId = R.id.accountFragment
                        setCurrentFragment(AccountPageFragment())
                        mAlertDialog?.dismiss()
                    }

                    val recyclerViewAlertBooking =
                        mDialogView.findViewById<View>(R.id.recyclerViewAlertBooking) as RecyclerView
                    val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                    recyclerViewAlertBooking.layoutManager = LinearLayoutManager(this)
                    val adapter = AdapterMultiMovieAlertBooking(this, output.output)
                    recyclerViewAlertBooking.layoutManager = gridLayout
                    recyclerViewAlertBooking.adapter = adapter
                    adapter.renewItems(output.output)
                    mDialogView.textView_dots.attachToRecyclerView(recyclerViewAlertBooking)
                }

                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed(runnable, 2000)

            }
        }

    }



    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        val latitude: String = location.latitude.toString()
                        val longitude: String = location.longitude.toString()

//                        getAddress(latitude,longitude)
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()!!
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation

        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - Timeback > 1000) {
            Timeback = System.currentTimeMillis()
            Toast.makeText(this, "Press Again to Exit", Toast.LENGTH_SHORT).show()
            return
        }
        super.onBackPressed()
    }

}