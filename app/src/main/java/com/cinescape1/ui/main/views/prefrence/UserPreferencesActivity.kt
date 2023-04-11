package com.cinescape1.ui.main.views.prefrence

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.*
import com.cinescape1.data.models.requestModel.PreferenceRequest
import com.cinescape1.data.models.requestModel.ProfileRequest
import com.cinescape1.data.models.responseModel.FoodResponse
import com.cinescape1.data.models.responseModel.ProfileResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityUserPreferencesBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.prefrence.viewModel.UserPreferencesViewModel
import com.cinescape1.ui.main.views.home.adapter.CustomSpinnerAdapter
import com.cinescape1.ui.main.views.home.HomeActivity
import com.cinescape1.utils.*
import com.google.android.flexbox.FlexboxLayout
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.account_preference_layout.*
import javax.inject.Inject

class UserPreferencesActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferences: AppPreferences
    private val userPreferencesViewModel: UserPreferencesViewModel by viewModels { viewModelFactory }
    private var binding: ActivityUserPreferencesBinding? = null
    private var MyReceiver: BroadcastReceiver? = null
    private var ageRatingList: java.util.ArrayList<ProfileResponse.Output.Rating>? = null
    private var loader: LoaderDialog? = null
    private var profileList: ArrayList<ProfileResponse.Output.Experience>? = null
    private val list: ArrayList<ModelPreferenceExperience> = arrayListOf(
        ModelPreferenceExperience(R.drawable.img_vip,0),
        ModelPreferenceExperience(R.drawable.img_imax,0),
        ModelPreferenceExperience(R.drawable.img_4dx,0),
        ModelPreferenceExperience(R.drawable.img_dolby,0),
        ModelPreferenceExperience(R.drawable.img_3d,0),
        ModelPreferenceExperience(R.drawable.img_eleven,0),
        ModelPreferenceExperience(R.drawable.img_2d,0),
        ModelPreferenceExperience(R.drawable.img_screenx,0)
    )

    private var seatCateogry: String=""
    private var seatType: String=""
    private val experience:ArrayList<String> = ArrayList()
    private var ageRating: ArrayList<String> = ArrayList()
    var seatAbility: Int = 0
    private var cinemaId = ""
    private var broadcastReceiver: BroadcastReceiver? = null
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private var latitude: String = ""
    private var longitude: String = ""
    private var cinema: String = ""
    private var arbic: Boolean = false
    private var locationlist = ArrayList<FoodResponse.Output.Cinema>()

    private var languageCheck: String = "en"

    private val myPreference = "MyPrefs"
    private var sharedPreferences: SharedPreferences? = null
    private val onBoardingClick = "Name"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPreferencesBinding.inflate(layoutInflater, null, false)
        val view = binding?.root

        Constant.experienceList.clear()
        Constant.ageRating1.clear()



        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                arbic=true
                LocaleHelper.setLocale(this, "ar")
                languageCheck = "ar"
                println("getLocalLanguage--->${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}")
                val regular = ResourcesCompat.getFont(this, R.font.gess_light)
                val bold = ResourcesCompat.getFont(this, R.font.gess_bold)
                val medium = ResourcesCompat.getFont(this, R.font.gess_medium)

                binding?.btnGetPersonal?.typeface = bold
                binding?.textChoosePreference?.typeface = regular
                binding?.textLocation?.typeface = bold
                binding?.textFindNearLocation?.typeface = regular
                binding?.textSeatCategory?.typeface = bold
                binding?.textSeatType?.typeface = bold
                binding?.textExperience?.typeface = bold
                binding?.textAgeRating?.typeface = bold
                binding?.skipUi?.typeface = bold
                binding?.doneBtn?.typeface = bold
                binding?.andProceed?.typeface = regular

            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                arbic=false
                LocaleHelper.setLocale(this, "en")
                languageCheck = "en"
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(this, R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)

                binding?.btnGetPersonal?.typeface = bold
                binding?.textChoosePreference?.typeface = regular
                binding?.textLocation?.typeface = bold
                binding?.textFindNearLocation?.typeface = regular
                binding?.textSeatCategory?.typeface = bold
                binding?.textSeatType?.typeface = bold
                binding?.textExperience?.typeface = bold
                binding?.textAgeRating?.typeface = bold
                binding?.skipUi?.typeface = bold
                binding?.doneBtn?.typeface = bold
                binding?.andProceed?.typeface = regular

            }
            else -> {
                LocaleHelper.setLocale(this, "en")
                languageCheck = "en"

                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(this, R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)

                binding?.btnGetPersonal?.typeface = bold
                binding?.textChoosePreference?.typeface = regular
                binding?.textLocation?.typeface = bold
                binding?.textFindNearLocation?.typeface = regular
                binding?.textSeatCategory?.typeface = bold
                binding?.textSeatType?.typeface = bold
                binding?.textExperience?.typeface = bold
                binding?.textAgeRating?.typeface = bold
                binding?.skipUi?.typeface = bold
                binding?.doneBtn?.typeface = bold
                binding?.andProceed?.typeface = regular

            }
        }
        setContentView(view)
        //AppBar Hide
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )



        broadcastIntent()
        movedNext()
        getProfile(ProfileRequest(latitude, longitude,preferences.getString(Constant.USER_ID).toString()))
        broadcastReceiver = MyReceiver()
        broadcastIntent()
        loadLocation()
        CinemaResponse()

    }

    private fun CinemaResponse() {
        userPreferencesViewModel.foodResponse()
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                try {
                                    if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                        locationlist = it.data.output.cinemas
                                        cinemaId =  it.data.output.cinemas[0].id
                                        setSpinnerData(it.data.output.cinemas)
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

    private fun setSpinnerData(cinemas: ArrayList<FoodResponse.Output.Cinema>) {
        try {
            val customAdapter = CustomSpinnerAdapter(this, cinemas)
            binding?.spinnerPref?.adapter = customAdapter
            binding?.spinnerPref?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) {
                    cinema = locationlist[position].name

                    println("LocationData------------->${cinema}")
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    private fun loadLocation() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@UserPreferencesActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
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
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.getFusedLocationProviderClient(this@UserPreferencesActivity)
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(applicationContext)
                        .removeLocationUpdates(this)
                    if (locationResult.locations.size > 0) {
                        val latestIndex = locationResult.locations.size - 1
//                        val lati = locationResult.locations[latestlocIndex].latitude
//                        val longi = locationResult.locations[latestlocIndex].longitude
                        latitude = locationResult.locations[latestIndex].latitude.toString()
                        longitude = locationResult.locations[latestIndex].longitude.toString()

                        val location = Location("providerNA")
                        location.longitude = longitude.toDouble()
                        location.latitude = latitude.toDouble()
//                        fetchAddressFromLocation(location)
                    } else {

                    }
                }
            }, Looper.getMainLooper())
    }

    private fun broadcastIntent() {
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }


    private fun movedNext() {
        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE)

        binding?.doneBtn?.setOnClickListener {

            val editor = sharedPreferences?.edit()
            editor?.putBoolean(onBoardingClick, true)
            editor?.commit()

            Constant.seatCategoryList.clear()
            Constant.seatTypeList.clear()

//            Constant.experience.addAll(Constant.experienceList)
//            Constant.ageRating.addAll(Constant.ageRating1)

            Constant.seatCategoryList.add(preferences.getString(Constant.SEAT_CATEGORY)!!)
            Constant.seatTypeList.add(preferences.getString(Constant.SEAT_TYPE)!!)

            println("UpdateExperience--->$experience")
            updatePreference(PreferenceRequest(arbic,cinema,
                Constant.experience.distinct().toString(),
                Constant.ageRating.distinct().toString(),
                Constant.seatCategoryList.distinct().toString(),
                Constant.seatTypeList.distinct().toString(),
                preferences.getString(Constant.USER_ID).toString()))
        }

        binding?.textSkipProceed?.setOnClickListener {
            val editor = sharedPreferences?.edit()
            editor?.putBoolean(onBoardingClick, true)
            editor?.commit()

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        binding?.textFindNearLocation?.setOnClickListener {
            val url =
                "http://maps.google.com/maps?daddr=" + latitude + "," + longitude
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    @SuppressLint("InflateParams")
    private fun  createSeatCategory(layout: FlexboxLayout, seatCategory: String) {
//        val list: java.util.ArrayList<ModelPreferenceCategory> = arrayListOf(
//            ModelPreferenceCategory(R.drawable.family_icons, "Family", 0),
//            ModelPreferenceCategory(R.drawable.family_normal_icon, "Bachelor", 0)
//        )
        val list: java.util.ArrayList<ModelPreferenceCategory> = arrayListOf(
            ModelPreferenceCategory(R.drawable.family_icons,getString(R.string.family),0),
            ModelPreferenceCategory(R.drawable.family_normal_icon,getString(R.string.bachlor),0)
        )

        val listFA: java.util.ArrayList<ModelSeatCategoryFA> =
            arrayListOf(ModelSeatCategoryFA(R.drawable.family_active))
        val listFN: java.util.ArrayList<ModelSeatCategoryFA> =
            arrayListOf(ModelSeatCategoryFA(R.drawable.family_icons))
        val listBA: java.util.ArrayList<ModelSeatCategoryFA> =
            arrayListOf(ModelSeatCategoryFA(R.drawable.family_n_active))
        val listBN: java.util.ArrayList<ModelSeatCategoryFA> =
            arrayListOf(ModelSeatCategoryFA(R.drawable.family_normal_icon))

        layout.removeAllViews()
        val viewListForDates = java.util.ArrayList<View>()

        for (item in list) {
            val v: View = layoutInflater.inflate(R.layout.seat_category_item, null)
            val categoryImage: ImageView = v.findViewById(R.id.image_family) as ImageView
            val categoryName = v.findViewById(R.id.category_name) as TextView

            seatAbility = if (item.count > 0) {
                1
            } else {
                0
            }
            categoryName.text = item.cateTypeText
            Glide.with(this).load(item.imgCate).placeholder(R.drawable.family).into(categoryImage)

            viewListForDates.add(v)
            layout.addView(v)

            val seat = seatCategory.replace("[", "").replace("]", "")
            println("SeatCategory212--->${item.cateTypeText}--->${seat}")

            if (item.cateTypeText.uppercase() == seat.uppercase()) {

                preferences.putString(Constant.SEAT_CATEGORY, item.cateTypeText)
                if (seat == resources.getString(R.string.family)) {
//                    Constant.seatCategoryList.add(item.cateTypeText)

                    for (items in listFA) {

                        Glide.with(this).load(items.imgCate).placeholder(R.drawable.family_active)
                            .into(categoryImage)
//                        categoryImage.setImageResource(R.drawable.family_active)
                    }
                    categoryName.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.text_alert_color_red
                        )
                    )
                }

                if (seat == resources.getString(R.string.bachlor)) {
//                    Constant.seatCategoryList.add(item.cateTypeText)

                    for (items in listBA) {
                        println("SeatListClick22222 ------------->22")
                        Glide.with(this).load(items.imgCate).placeholder(R.drawable.family_n_active)
                            .into(categoryImage)

                    }
                    categoryName.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.text_alert_color_red
                        )
                    )
                }


            } else {

                if (seat == resources.getString(R.string.family)) {

                    Glide.with(this).load(listBN[0].imgCate).placeholder(R.drawable.family_icons)
                        .into(categoryImage)

                    categoryName.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.hint_color
                        )
                    )
                }

                if (seat == resources.getString(R.string.bachlor)) {

                    Glide.with(this).load(listFN[0].imgCate)
                        .placeholder(R.drawable.family_normal_icon).into(categoryImage)
                    categoryName.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.hint_color
                        )
                    )
                }

            }

            v.setOnClickListener {

                for (v in viewListForDates) {
                    val categoryImage1: ImageView = v.findViewById(R.id.image_family) as ImageView
                    val categoryName1: TextView = v.findViewById(R.id.category_name) as TextView
//                  categoryImage1.setColorFilter(getColor(requireContext(), R.color.hint_color))

                    if (item.cateTypeText == resources.getString(R.string.family)) {
                        for (items in listFN) {
                            Glide.with(this).load(listBN[0].imgCate)
                                .placeholder(R.drawable.family_normal_icon).into(categoryImage1)
//                            if (seatTypeCheck == 0){
//                                Glide.with(this).load(items.imgCate).placeholder(R.drawable.family_icons).into(categoryImage1)
//                            }else{
//                                Glide.with(this).load(listBN[0].imgCate).placeholder(R.drawable.family_normal_icon).into(categoryImage1)
//                            }
                            println("SeatListClick22222 ------------->listFN1")
                        }
                    }

                    if (item.cateTypeText == resources.getString(R.string.bachlor)) {
                        for (items in listBN) {
                            println("SeatListClick22222 ------------->listBN1")

                            Glide.with(this).load(listFN[0].imgCate)
                                .placeholder(R.drawable.family_icons).into(categoryImage1)
//                            if (seatTypeCheck == 0){
//                                Glide.with(this).load(listBN[0].imgCate).placeholder(R.drawable.family_normal_icon).into(categoryImage1)
//                            }else{
//                                Glide.with(this).load(listFN[0].imgCate).placeholder(R.drawable.family_icons).into(categoryImage1)
//                            }
                        }
                    }

                    categoryName1.setTextColor(
                        ContextCompat.getColorStateList(
                            this,
                            R.color.hint_color
                        )
                    )
                }

                if (Constant.seatCategoryList.contains(item.cateTypeText)) {
                    if (item.cateTypeText == resources.getString(R.string.family)) {
                        Constant.seatCategoryList.removeAll{it == item.cateTypeText}

                        for (items in listFN) {
                            println("SeatListClick22222 ------------->listFN3")
                            Glide.with(this).load(listFN[0].imgCate)
                                .placeholder(R.drawable.family_icons).into(categoryImage)
                        }
                    }

                    if (item.cateTypeText == resources.getString(R.string.bachlor)) {
                        Constant.seatCategoryList.removeAll{it == item.cateTypeText}

                        for (items in listBN) {
                            println("SeatListClick22222 ------------->listFN3")
                            Glide.with(this).load(listBN[0].imgCate)
                                .placeholder(R.drawable.family_normal_icon).into(categoryImage)
                        }
                    }
                    categoryName.setTextColor(
                        ContextCompat.getColorStateList(
                            this,
                            R.color.hint_color
                        )
                    )

                } else {

                    Constant.seatCategoryList.clear()
                    Constant.seatTypeList.clear()
                    preferences.putString(Constant.SEAT_CATEGORY, item.cateTypeText)
                    Constant.seatCategoryList.add(item.cateTypeText)

                    if (item.cateTypeText == resources.getString(R.string.family)) {
                        for (items in listFA) {
                            println("SeatListClick22222 ------------->listFA2")
//                            seatTypeCheck = 1
                            Glide.with(this).load(items.imgCate).dontAnimate()
                                .placeholder(R.drawable.family_active).into(categoryImage)
                        }
                    }

                    if (item.cateTypeText == resources.getString(R.string.bachlor)) {
                        for (items in listBA) {
                            println("SeatListClick22222 ------------->listBA2")
                            Glide.with(this).load(items.imgCate).dontAnimate()
                                .placeholder(R.drawable.family_n_active).into(categoryImage)

                        }
                    }
                    categoryName.setTextColor(
                        ContextCompat.getColorStateList(
                            this,
                            R.color.text_alert_color_red
                        )
                    )

                }

            }

        }
    }

    @SuppressLint("InflateParams")
    private fun createSeatType(layout: FlexboxLayout, seatType : String) {

        val list: java.util.ArrayList<ModelPreferenceType> = arrayListOf(
            ModelPreferenceType(getString(R.string.standards),0),
            ModelPreferenceType(getString(R.string.premiums),0))

        layout.removeAllViews()
        val viewListForSeatType = java.util.ArrayList<View>()
        for (type_item in list) {
            val v: View = layoutInflater.inflate(R.layout.seat_type_list_item, null)
            val typeName: TextView = v.findViewById(R.id.tv_seat_selectiopn) as TextView
            val imgSeatSelectiopn: ImageView = v.findViewById(R.id.imgSeatSelectiopn) as ImageView


            if (type_item.seatType == getString(R.string.standards)){
                imgSeatSelectiopn.setImageResource(R.drawable.standard_white)

            }else{

                imgSeatSelectiopn.setImageResource(R.drawable.premium_white)
            }

            seatAbility = if (type_item.count > 0) {
                1
            } else {
                0
            }
            typeName.text = type_item.seatType


            viewListForSeatType.add(v)
            layout.addView(v)

            val seat = seatType.replace("[", "").replace("]", "")
            println("SeatType221--->${type_item.seatType}---<${seat}")

            if (type_item.seatType.uppercase() == seat.uppercase()) {

                preferences.putString(Constant.SEAT_TYPE, type_item.seatType)
                typeName.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.text_alert_color_red
                    )
                )

                imgSeatSelectiopn.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_alert_color_red),
                    android.graphics.PorterDuff.Mode.MULTIPLY)

            } else {

                typeName.setTextColor(ContextCompat.getColor(this, R.color.hint_color))

                imgSeatSelectiopn.setColorFilter(
                    ContextCompat.getColor(this, R.color.hint_color),
                    android.graphics.PorterDuff.Mode.MULTIPLY)
            }

            v.setOnClickListener {
                for (v in viewListForSeatType) {
                    println("SeatListClick21No ------------->${type_item.seatType}")
                    val typeName1: TextView = v.findViewById(R.id.tv_seat_selectiopn) as TextView
                    val imgSeatSelectiopn1: ImageView = v.findViewById(R.id.imgSeatSelectiopn) as ImageView
                    typeName1.setTextColor(
                        ContextCompat.getColorStateList(
                            this,
                            R.color.hint_color
                        )
                    )

                    imgSeatSelectiopn1.setColorFilter(
                        ContextCompat.getColor(this, R.color.hint_color),
                        android.graphics.PorterDuff.Mode.MULTIPLY)

                }

                println("SeatListClick21Yes ------------->${type_item.seatType}")

                if (Constant.seatTypeList.contains(type_item.seatType)) {
                    Constant.seatTypeList.removeAll{it == type_item.seatType}

                    typeName.setTextColor(ContextCompat.getColorStateList(this, R.color.hint_color))

                    imgSeatSelectiopn.setColorFilter(
                        ContextCompat.getColor(this, R.color.hint_color),
                        android.graphics.PorterDuff.Mode.MULTIPLY)

                } else {

                    Constant.seatTypeList.clear()
                    Constant.seatCategoryList.clear()

                    Constant.seatTypeList.add(type_item.seatType)

                    preferences.putString(Constant.SEAT_TYPE, type_item.seatType)

                    typeName.setTextColor(
                        ContextCompat.getColorStateList(
                            this, R.color.text_alert_color_red))

                    imgSeatSelectiopn.setColorFilter(
                        ContextCompat.getColor(this, R.color.text_alert_color_red),
                        android.graphics.PorterDuff.Mode.MULTIPLY)

                    println("SeatListClick21Yes ------------->${type_item.seatType}")
                }

            }
        }

    }

    @SuppressLint("InflateParams")
    private fun createExperience(layout: FlexboxLayout,
                                 experience:ArrayList<ProfileResponse.Output.Experience>) {

        val list4dx: java.util.ArrayList<ModelExperiences> =
            arrayListOf(ModelExperiences(R.drawable.fourdx_white))
        val listStandard: java.util.ArrayList<ModelExperiences> =
            arrayListOf(ModelExperiences(R.drawable.standard_white))
        val listVip: java.util.ArrayList<ModelExperiences> =
            arrayListOf(ModelExperiences(R.drawable.vip_white))
        val listImax: java.util.ArrayList<ModelExperiences> =
            arrayListOf(ModelExperiences(R.drawable.imax_white))
        val list3D: java.util.ArrayList<ModelExperiences> =
            arrayListOf(ModelExperiences(R.drawable.threed_white))
        val listDolby: java.util.ArrayList<ModelExperiences> =
            arrayListOf(ModelExperiences(R.drawable.dolby_white))
        val listEleven: java.util.ArrayList<ModelExperiences> =
            arrayListOf(ModelExperiences(R.drawable.eleven_white))
        val listScreen: java.util.ArrayList<ModelExperiences> =
            arrayListOf(ModelExperiences(R.drawable.screenx_white))
        val listPremium: java.util.ArrayList<ModelExperiences> =
            arrayListOf(ModelExperiences(R.drawable.premium_white))

        layout.removeAllViews()
        val viewListForSeatExperience = java.util.ArrayList<View>()

        for (data in profileList!!) {
            val v: View = layoutInflater.inflate(R.layout.experience_account_item, null)
            val experienceName = v.findViewById(R.id.experience_name) as ImageView
            val experienceText = v.findViewById(R.id.experience_nametxt) as TextView

            val lowerCase = data.name.toLowerCase()
            val url = "https://s3.eu-west-1.amazonaws.com/cinescape.uat/experience/${lowerCase}.png"
            println("data.name--------->${data.name}------>${lowerCase}")


            when (data.name) {

                "4DX" -> {
                    Glide.with(this).load(list4dx[0].imgCate).placeholder(R.drawable.four_dx)
                        .into(experienceName)

                }
                "STANDARD" -> {
//                    Glide.with(requireContext()).load(R.drawable.standard).into(experienceName)
                    for (items in listStandard) {
                        Glide.with(this).load(items.imgCate).placeholder(R.drawable.standard)
                            .into(experienceName)
                    }

                }

                "VIP" -> {
//                    Glide.with(requireContext()).load(R.drawable.vip).into(experienceName)
                    for (items in listVip) {
                        Glide.with(this).load(items.imgCate).placeholder(R.drawable.vip)
                            .into(experienceName)
                    }


                }
                "IMAX" -> {
//                    Glide.with(requireContext()).load(R.drawable.imax).into(experienceName)

                    for (items in listImax) {
                        Glide.with(this).load(items.imgCate).placeholder(R.drawable.imax)
                            .into(experienceName)
                    }
                }
                "3D" -> {
                    for (items in list3D) {
                        Glide.with(this).load(items.imgCate).placeholder(R.drawable.threed_black)
                            .into(experienceName)
                    }

                }
                "DOLBY" -> {
                    for (items in listDolby) {
                        Glide.with(this).load(items.imgCate).placeholder(R.drawable.dolby_black)
                            .into(experienceName)
                    }

                }
                "ELEVEN" -> {

                    for (items in listEleven) {
                        Glide.with(this).load(items.imgCate).placeholder(R.drawable.eleven_black)
                            .into(experienceName)
                    }


                }
                "SCREENX" -> {
                    for (items in listScreen) {
                        Glide.with(this).load(items.imgCate).placeholder(R.drawable.screenx_black)
                            .into(experienceName)
                    }

                }
                "PREMIUM" -> {
                    for (items in listPremium) {
                        Glide.with(this).load(items.imgCate).placeholder(R.drawable.premium_black)
                            .into(experienceName)
                    }

                }

            }


            seatAbility = if (data.count > 0) {
                1
            } else {
                0
            }


            layout.addView(v)
            viewListForSeatExperience.add(v)

            println("SeatType21--->${data.name}---<${experience}")
            if (data.likes) {
                experienceName.setColorFilter(
                    ContextCompat.getColor(this, R.color.text_alert_color_red),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
                println("ExperienceLikes----->${data.name}---->${data.likes}")
                Constant.experience.add(data.name)

            } else {
                experienceName.setColorFilter(
                    ContextCompat.getColor(this, R.color.hint_color),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
            }

            v.setOnClickListener {
                for (v in viewListForSeatExperience) {
                    val experienceName1 = v.findViewById(R.id.experience_name) as ImageView
                    experienceName1.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.transparent
                        )
                    )
                }
                if (Constant.experience.contains(data.name)) {
                    Constant.experience.removeAll{it == data.name}
//                    Constant.experience.remove(data.name)
                    experienceName.setColorFilter(
                        ContextCompat.getColor(this, R.color.hint_color),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                } else {

                    Constant.experience.add(data.name)
                    experienceName.setColorFilter(
                        ContextCompat.getColor(
                            this, R.color.text_alert_color_red
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    println("Experience--->${data.name}")
                }
            }
        }

    }

    private fun getMatchIcon(image:String):Int{
        for (item in list){
            if ((resources.getResourceName(item.imgSeatExperience).toString().uppercase()).contains(image)) {
                return item.imgSeatExperience
            }
        }
        return 0
    }

    @SuppressLint("InflateParams")
    val viewListForSeatAgeRating = ArrayList<String>()
    private fun createAgeRating(layout: FlexboxLayout) {
        val list: ArrayList<ModelPreferenceAgeRating> = arrayListOf(
            ModelPreferenceAgeRating("E",0),
            ModelPreferenceAgeRating("PG",0),
            ModelPreferenceAgeRating("13+",0),
            ModelPreferenceAgeRating("15+",0),
            ModelPreferenceAgeRating("18+",0)
        )
        layout.removeAllViews()

        for (age_rating_item in ageRatingList!!) {
            val v: View = layoutInflater.inflate(R.layout.age_rating_item, null)
            val ageRatingName: TextView = v.findViewById(R.id.age_rating_name) as TextView

            ageRatingName.text = age_rating_item.name
            seatAbility = if (age_rating_item.count > 0) {
                1
            } else {
                0
            }

            layout.addView(v)
            viewListForSeatAgeRating.add(v.toString())

            if (age_rating_item.likes) {
                ageRatingName.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.text_alert_color_red
                    )
                )
                Constant.ageRating.add(age_rating_item.name)

            } else {
                ageRatingName.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.hint_color
                    )
                )
            }

            v.setOnClickListener {

                if (Constant.ageRating.contains(age_rating_item.name)) {
                    Constant.ageRating.removeAll{it == age_rating_item.name}

//                    Constant.ageRating.remove(age_rating_item.name)

                    ageRatingName.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.hint_color
                        )
                    )

                } else {

                    ageRatingName.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.text_alert_color_red
                        )
                    )
                    Constant.ageRating.add(age_rating_item.name)

                }
            }
        }

    }

    private fun updatePreference(profileRequest: PreferenceRequest) {
        userPreferencesViewModel.updatePreference(profileRequest)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    println("SomethingWrong--->${it.data.msg}")
                                    val intent = Intent(
                                        this@UserPreferencesActivity,
                                        HomeActivity::class.java
                                    )
                                    startActivity(intent)

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

    private fun getProfile(profileRequest: ProfileRequest) {
        userPreferencesViewModel.getProfile(profileRequest)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    println("SomethingWrong--->${it.data.msg}")
                                    profileList = it.data.output.experience
                                    ageRatingList = it.data.output.rating
                                    createSeatCategory(binding?.seatList!!, it.data.output.seatCategory)
                                    createSeatType(binding?.typeList!!, it.data.output.seatType)
                                    createExperience(binding?.experienceList!!, it.data.output.experience)
                                    createAgeRating(binding?.ageRatingList!!)
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
}