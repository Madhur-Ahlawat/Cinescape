package com.cinescape1.ui.main.views.prefrence

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
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
import com.cinescape1.data.models.ModelPreferenceAgeRating
import com.cinescape1.data.models.ModelPreferenceCategory
import com.cinescape1.data.models.ModelPreferenceExperience
import com.cinescape1.data.models.ModelPreferenceType
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
import javax.inject.Inject

class UserPreferencesActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferences: AppPreferences
    private val userPreferencesViewModel: UserPreferencesViewModel by viewModels { viewModelFactory }
    private var binding: ActivityUserPreferencesBinding? = null
    private var MyReceiver: BroadcastReceiver? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPreferencesBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
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
                                        setSpinner(it.data.output.cinemas)
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

    private fun setSpinner(cinemas: ArrayList<FoodResponse.Output.Cinema>) {

        val customAdapter = CustomSpinnerAdapter(
            this,
            cinemas
        )
        binding?.SpinerPref?.adapter = customAdapter
        binding?.SpinerPref?.onItemSelectedListener=  object :
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
        binding?.doneBtn?.setOnClickListener {
            println("UpdateExperience--->$experience")
            updatePreference(PreferenceRequest(arbic,cinema,experience.toString(), ageRating.toString(), seatCateogry, seatType, preferences.getString(Constant.USER_ID).toString()))
        }
        binding?.textSkipProceed?.setOnClickListener {
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
    val viewListForDates = ArrayList<View>()
    private fun  createSeatCategory(layout: FlexboxLayout) {
        val list: ArrayList<ModelPreferenceCategory> = arrayListOf(
            ModelPreferenceCategory(R.drawable.family,"Family",0),
            ModelPreferenceCategory(R.drawable.bachlor,"Bachelor",0),
        )

        layout.removeAllViews()
        for (item in list) {
            val v: View = layoutInflater.inflate(R.layout.seat_category_item, null)
            val categoryImage: ImageView = v.findViewById(R.id.image_family) as ImageView
            val categoryName: TextView = v.findViewById(R.id.category_name) as TextView

            if (languageCheck == "ar"){
                val regular = ResourcesCompat.getFont(this, R.font.gess_light)
                categoryName.typeface = regular

            }else{
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                categoryName.typeface = regular
            }

            seatAbility = if (item.count > 0) {
                1
            } else {
                0
            }
            categoryName.text = item.cateTypeText
            Glide.with(this)
                .load(item.imgCate)
                .placeholder(R.drawable.family)
                .into(categoryImage)

            viewListForDates.add(v)
            layout.addView(v)
            v.setOnClickListener {
                for (v in viewListForDates){
                    val categoryImage1: ImageView = v.findViewById(R.id.image_family) as ImageView
                    val categoryName1: TextView = v.findViewById(R.id.category_name) as TextView
                    categoryImage1.setColorFilter(getColor(R.color.hint_color))
                    categoryName1.setTextColor(getColor(R.color.hint_color))
                }
                println("categoryName--->${categoryName.text}")
                seatCateogry=categoryName.text.toString()
                categoryImage.setColorFilter(getColor(R.color.text_alert_color_red))
                categoryName.setTextColor(getColor(R.color.text_alert_color_red))
            }
        }
    }

    @SuppressLint("InflateParams")
    val viewListForSeatType = ArrayList<View>()
    @SuppressLint("CutPasteId")
    private fun createSeatType(layout: FlexboxLayout) {

        val list: ArrayList<ModelPreferenceType> = arrayListOf(
            ModelPreferenceType("Standard",0),
            ModelPreferenceType("Premium",0)
        )

        layout.removeAllViews()
        for (type_item in list) {
            val v: View = layoutInflater.inflate(R.layout.seat_type_item, null)
            val typeName: TextView = v.findViewById(R.id.tv_seat_selectiopn) as TextView

            if (languageCheck == "ar"){
                val regular = ResourcesCompat.getFont(this, R.font.gess_light)
                typeName.typeface = regular

            }else{
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                typeName.typeface = regular
            }

            seatAbility = if (type_item.count > 0) {
                1
            } else {
                0
            }

            typeName.text = type_item.seatType

            viewListForSeatType.add(v)
            layout.addView(v)

            v.setOnClickListener {
                for (v in viewListForSeatType){
                    val typeName1: TextView = v.findViewById(R.id.tv_seat_selectiopn) as TextView
                    typeName1.setTextColor(getColor(R.color.hint_color))
                }
                println("seatType--->${type_item.seatType}")
                seatType=type_item.seatType
                typeName.setTextColor(getColor(R.color.text_alert_color_red))

            }
        }

    }

    @SuppressLint("InflateParams")
    val viewListForSeatExperience = ArrayList<View>()
    private fun createExperience(layout: FlexboxLayout) {
        layout.removeAllViews()
        for (data in profileList!!) {
            val v: View = layoutInflater.inflate(R.layout.experience_item, null)
            val experienceName = v.findViewById(R.id.experience_name) as ImageView
            val experienceText = v.findViewById(R.id.experience_nametxt) as TextView
            if (languageCheck == "ar"){
                val regular = ResourcesCompat.getFont(this, R.font.gess_light)
                experienceText.typeface = regular

            }else{
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                experienceText.typeface = regular
            }

            seatAbility = if (data.count > 0) {
                1
            } else {
                0
            }

            val iconId=getMatchIcon(data.name)
            if (iconId==0){
                experienceText.text=getMatchIcon(data.name).toString()
                experienceText.show()
                experienceName.hide()
            }else{
                experienceName.setImageResource(getMatchIcon(data.name))
                experienceText.hide()
                experienceName.show()
            }
            layout.addView(v)
            viewListForSeatExperience.add(v)
            v.setOnClickListener {
                for (v in viewListForSeatExperience){
                    val experienceName1 = v.findViewById(R.id.experience_name) as ImageView
                    experienceName1.setBackgroundColor(getColor(R.color.transparent))
                }
                if (experience.contains(data.name)){
                    experience.remove(data.name)
                    experienceName.setColorFilter(ContextCompat.getColor(this, R.color.hint_color), android.graphics.PorterDuff.Mode.MULTIPLY)

                }else{
                experience.add(data.name)
                experienceName.setColorFilter(ContextCompat.getColor(this, R.color.text_alert_color_red), android.graphics.PorterDuff.Mode.MULTIPLY)
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
    val viewListForSeatAgeRating = ArrayList<View>()
    private fun createAgeRating(layout: FlexboxLayout) {
        val list: ArrayList<ModelPreferenceAgeRating> = arrayListOf(
            ModelPreferenceAgeRating("E",0),
            ModelPreferenceAgeRating("PG",0),
            ModelPreferenceAgeRating("13+",0),
            ModelPreferenceAgeRating("15+",0),
            ModelPreferenceAgeRating("18+",0)
        )

        layout.removeAllViews()
        for (type_item in list) {
            val v: View = layoutInflater.inflate(R.layout.age_rating_item, null)
            val ageRatingName: TextView = v.findViewById(R.id.age_rating_name) as TextView
            if (languageCheck == "ar"){
                val regular = ResourcesCompat.getFont(this, R.font.gess_light)
                ageRatingName.typeface = regular

            }else{
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                ageRatingName.typeface = regular
            }
            ageRatingName.text = type_item.seatAgeRating
            seatAbility = if (type_item.count > 0) {
                1
            } else {
                0
            }

            layout.addView(v)
            viewListForSeatAgeRating.add(v)
            v.setOnClickListener {

                if (ageRating.contains(type_item.seatAgeRating)){
                    ageRating.remove(type_item.seatAgeRating)
                    ageRatingName.setTextColor(getColor(R.color.hint_color))

                }else{
                    when (type_item.seatAgeRating) {
                        "E" -> {
                            ageRatingName.setTextColor(getColor(R.color.green))

                        }
                        "PG" -> {
                            ageRatingName.setTextColor(getColor(R.color.grey))

                        }
                        "13+" -> {
                            ageRatingName.setTextColor(getColor(R.color.yellow))

                        }
                        "15+" -> {
                            ageRatingName.setTextColor(getColor(R.color.yellow))

                        }
                        "18+" -> {
                            ageRatingName.setTextColor(getColor(R.color.text_alert_color_red))

                        }
                    }
                    ageRating.add(type_item.seatAgeRating)
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
                                    createSeatCategory(binding?.seatList!!)
                                    createSeatType(binding?.typeList!!)
                                    createExperience(binding?.experienceList!!)
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