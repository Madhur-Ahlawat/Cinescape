package com.cinescape1.ui.main.views

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.CinemaSessionRequest
import com.cinescape1.data.models.requestModel.SeatLayoutRequest
import com.cinescape1.data.models.responseModel.CSessionResponse
import com.cinescape1.data.models.responseModel.CinemaSessionResponse
import com.cinescape1.data.models.responseModel.SeatLayoutResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityShowTimesBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.viewModels.ShowTimesViewModel
import com.cinescape1.ui.main.views.adapters.AdapterDayDate
import com.cinescape1.ui.main.views.adapters.CinemaDayAdapter
import com.cinescape1.ui.main.views.adapters.CinemaPageAdapter
import com.cinescape1.ui.main.views.adapters.cinemaSessionAdapters.AdapterCinemaSessionScroll
import com.cinescape1.ui.main.views.adapters.showTimesAdapters.AdapterShowTimesCinemaTitle
import com.cinescape1.ui.main.views.adapters.showTimesAdapters.AdpaterShowTimesCast
import com.cinescape1.utils.*
import com.cinescape1.utils.Constant.Companion.select_pos
import com.google.android.flexbox.FlexboxLayout
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_show_times.*
import kotlinx.android.synthetic.main.search_ui.*
import kotlinx.android.synthetic.main.seat_selection_bank_offer_alert.*
import kotlinx.android.synthetic.main.show_times_layout_include.*
import javax.inject.Inject
import kotlin.math.abs


class ShowTimesActivity : DaggerAppCompatActivity(), AdapterDayDate.RecycleViewItemClickListener,
    AdapterShowTimesCinemaTitle.CinemaAdapterListener,
    CinemaDayAdapter.RecycleViewItemClickListener, AdapterCinemaSessionScroll.LocationListener {
    private var num = 0
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferences: AppPreferences
    private val showTimeViewModel: ShowTimesViewModel by viewModels { viewModelFactory }
    private var binding: ActivityShowTimesBinding? = null
    private var up = true
    private var count = 0
    private var datePos = 0
    private var showPose = 0
    private var dateTime = ""
    private var areaCode = ""
    private var ttType = ""
    private var seatCat = ""
    private var seatType = ""
    private var movieID = ""
    private var CinemaID = ""
    private var SessionID = ""
    private var type = ""
    var datePosition = ""
    var dt = ""
    private var loader: LoaderDialog? = null
    private var showDataCSessionResponse: CSessionResponse.Output? = null
    private var showData: CinemaSessionResponse.Output? = null
    private var daySessionResponse: ArrayList<CinemaSessionResponse.DaySession> = ArrayList()
    var totalPriceResponse: Double = 0.0
    var selectSeatClick: Int = 0
    var seatAbility: Int = 0
    private var categoryClick: Boolean = false
    private var mAlertDialog: AlertDialog? = null
    var adapterShowTimesCinemaTitle: AdapterShowTimesCinemaTitle? = null
    private var languageCheck: String = "en"
    private var broadcastReceiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowTimesBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
                languageCheck = "ar"
                println("getLocalLanguage--->${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}")
            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(this, "en")
                languageCheck = "en"
            }
            else -> {
                languageCheck = "en"
                LocaleHelper.setLocale(this, "en")
            }
        }
        setContentView(view)

        //ScreenClick
        view?.setOnClickListener {
            binding?.imageView25?.show()
            searchUi.hide()
            imageView36.show()
        }

        //AppBar Hide
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
        type = intent.getStringExtra("type").toString()
        movieID = intent.getStringExtra(Constant.IntentKey.MOVIE_ID)!!
        when (type) {
            "comingSoon" -> {
                val layoutParams =
                    (binding?.recylerviewShowTimeDate?.layoutParams as? ViewGroup.MarginLayoutParams)
                layoutParams?.setMargins(0, 0, 0, 16)
                binding?.recylerviewShowTimeDate?.layoutParams = layoutParams

//                val layoutParams2 = (binding?.showTimesUi?.layoutParams as? ViewGroup.MarginLayoutParams)
//                layoutParams?.setMargins(0, -50, 0, 0)
//                binding?.showTimesUi?.layoutParams = layoutParams2


                binding?.moviePage?.hide()
                binding?.comingSoon?.show()
                binding?.viewpager?.hide()
                binding?.centerView?.hide()
                binding?.imageView48?.hide()
//                binding?.textView110?.hide()
                include.show()
                getShowTimes()

            }
            "movie" -> {
                val layoutParams =
                    (binding?.recylerviewShowTimeDate?.layoutParams as? ViewGroup.MarginLayoutParams)
                layoutParams?.setMargins(0, 0, 0, 16)
                binding?.recylerviewShowTimeDate?.layoutParams = layoutParams

                binding?.viewpager?.hide()
                binding?.imageView48?.hide()
//                binding?.textView110?.hide()
                binding?.centerView?.hide()

                getCinemaData(CinemaSessionRequest(dateTime, movieID))
            }
            else -> {
                val layoutParams =
                    (binding?.recylerviewShowTimeDate?.layoutParams as? ViewGroup.MarginLayoutParams)
                layoutParams?.setMargins(0, 24, 0, 8)
                binding?.recylerviewShowTimeDate?.layoutParams = layoutParams

                binding?.moviePage?.show()
                binding?.comingSoon?.hide()
                binding?.viewpager?.show()
                binding?.imageView48?.show()
//                binding?.textView110?.show()
                binding?.centerView?.show()
                include.hide()
                getShowTimes()
            }
        }
        movedNext()
        broadcastReceiver = MyReceiver()
        broadcastIntent()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val hasFocus = false
        if (hasFocus) {
            binding?.imageView25?.hide()
            val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.addRule(RelativeLayout.ALIGN_PARENT_END)
            binding?.search?.layoutParams = params

        } else {
            binding?.imageView25?.show()
            val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.addRule(RelativeLayout.ALIGN_PARENT_END)
            binding?.search?.layoutParams = params
        }
    }

    private fun broadcastIntent() {
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun movedNext() {

        binding?.search?.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding?.imageView25?.hide()
                val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                params.addRule(RelativeLayout.ALIGN_PARENT_END)
                binding?.search?.layoutParams = params

            } else {
                binding?.imageView25?.show()
                val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                params.addRule(RelativeLayout.ALIGN_PARENT_END)
                binding?.search?.layoutParams = params
            }
        }


        binding?.view67?.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Insert Subject here")
            val appUrl = "https://play.google.com/store/apps/details?id=my.example.javatpoint"
            shareIntent.putExtra(Intent.EXTRA_TEXT, appUrl)
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        binding?.view68?.setOnClickListener {
            Toast.makeText(
                applicationContext,
                "This Feeture Will Avilable Soon",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding?.moviePage?.setOnClickListener {
            if (!up) {
                up = true
                binding?.imageUp?.setImageResource(R.drawable.arrow_down)
                include.hide()
            } else {
                up = false
                binding?.imageUp?.setImageResource(R.drawable.arrow_up)
                include.show()
            }
            return@setOnClickListener
        }

        binding?.imageView25?.setOnClickListener {
            onBackPressed()
        }

        //Search Show
        imageView36.setOnClickListener {
            binding?.imageView25?.hide()
            searchUi.show()
            imageView36.hide()
        }
        //Search Hide
        imageView35.setOnClickListener {
            binding?.imageView25?.show()
            searchUi.hide()
            imageView36.show()
        }

        movieSearch.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (movieSearch.compoundDrawables[2] != null) {
                    if (event.x >= movieSearch.right - movieSearch.left - movieSearch.compoundDrawables[2].bounds.width()
                    ) {
                        binding?.imageView25?.show()
                        searchUi.hide()
                        imageView36.show()
                    }
                }
            }
            false
        }

        movieSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding?.imageView25?.show()
                searchUi.hide()
                imageView36.show()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun getShowTimes() {
        getShowTimes(CinemaSessionRequest(dateTime, movieID))
    }

    private fun getShowTimes(json: CinemaSessionRequest) {
        showTimeViewModel.getMsessionsNew(this, json)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {
                                    try {
                                        binding?.LayoutTime?.show()
                                        datePosition = it.data.output.days[0].wdf
                                        dt = it.data.output.days[0].dt
                                        dateTime = it.data.output.days[0].dt
                                        if (json.dated == "")
                                            daySessionResponse = it.data.output.daySessions
                                        setShowTimesDayDateAdapter(it.data.output.days)
                                        updateUiShowTimes(it.data.output)
                                        showData = it.data.output
                                        setTitleAdapter()
                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }
                                } else {
                                    loader?.dismiss()
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        it.data?.msg.toString(),
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                            finish()
                                        },
                                        negativeClick = {
                                            finish()
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

    private fun getCinemaData(json: CinemaSessionRequest) {
        showTimeViewModel.getCinemaData(this, json)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {
                                    try {
                                        showDataCSessionResponse = it.data.output
                                        retrieveMovieData(it.data.output)
                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }
                                } else {
                                    loader?.dismiss()
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        it.data?.msg.toString(),
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                            finish()
                                        },
                                        negativeClick = {
                                            finish()
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

    @SuppressLint("SetTextI18n")
    private fun retrieveMovieData(output: CSessionResponse.Output) {
        println("MovieData--->${output}")
        binding?.moviePage?.hide()
        binding?.LayoutTime?.show()

        //Day Data
        if (count == 0) {
            val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            binding?.recylerviewShowTimeDate?.layoutManager = LinearLayoutManager(this)
            val adapter = CinemaDayAdapter(this, output.days, this)
            binding?.recylerviewShowTimeDate?.layoutManager = gridLayout
            binding?.recylerviewShowTimeDate?.adapter = adapter
            count = 1
        }

        //From Cinema Session
        println("output.daySessions--->${output.daySessions}")
        binding?.recyclerviewCinemaTitle?.show()
        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerviewCinemaTitle?.layoutManager = LinearLayoutManager(this)
        val adapter = AdapterCinemaSessionScroll(this, output.daySessions, this)
        binding?.recyclerviewCinemaTitle?.layoutManager = gridLayout
        binding?.recyclerviewCinemaTitle?.adapter = adapter
        count = 1

        println("""updateUiCinemaSession ----> $output""")
        binding?.textFilmHouseName?.text = output.cinema.name
        binding?.textFilmHouseName?.isSelected =true
        binding?.textMovieType?.text = output.cinema.address1 + "\n" + output.cinema.address2
        binding?.textView56?.hide()
        binding?.imageView26?.hide()
        binding?.imageView39?.show()
        //Map From Cinema
        binding?.imageView39?.setOnClickListener {
            val strUri =
                "http://maps.google.com/maps?q=loc:" + output.cinema.latitude + "," + output.cinema.longitude + " (" + "Label which you want" + ")"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
            intent.setClassName(
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity"
            )
            startActivity(intent)

        }

        Glide
            .with(this)
            .load(output.cinema.appThumbImageUrl)
            .placeholder(R.drawable.app_icon)
            .into(binding?.imageShow!!)
    }

    //GetSeatLayout
    private fun getSeatLayout(request: SeatLayoutRequest, name: String, pos: Int) {
        showTimeViewModel.getSeatLayout(this, request)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {
                                    try {
                                        showSeatTypePopup(it.data.output, name, pos)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }

                                } else {
                                    loader?.dismiss()
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
                            println("loading--->Seat")
                            loader = LoaderDialog(R.string.pleasewait)
                            loader?.show(supportFragmentManager, null)
                        }
                    }
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUiShowTimes(output: CinemaSessionResponse.Output) {
        println("""updateUiCinemaSession ----> $output""")
        binding?.textFilmHouseName?.text = output.movie.title
        binding?.textFilmHouseName?.isSelected =true
        binding?.textView56?.text = output.movie.rating

        when (output.movie.rating) {
            "PG" -> {
                binding?.textView56?.setBackgroundResource(R.color.grey)

            }
            "G" -> {
                binding?.textView56?.setBackgroundResource(R.color.green)

            }
            "18+" -> {
                binding?.textView56?.setBackgroundResource(R.color.red)

            }
            "13+" -> {
                binding?.textView56?.setBackgroundResource(R.color.yellow)

            }
            "15+" -> {
                binding?.textView56?.setBackgroundResource(R.color.yellow)

            }
            "E" -> {
                binding?.textView56?.setBackgroundResource(R.color.wowOrange)

            }
            "T" -> {
                binding?.textView56?.setBackgroundResource(R.color.tabIndicater)

            }
            else -> {
                binding?.textView56?.setBackgroundResource(R.color.blue)
            }
        }
        if (output.movie.rating == "") {
            binding?.ratingUi?.hide()
        } else {
            binding?.ratingUi?.show()
        }

        binding?.textMovieType?.text =
            output.movie.language + " | " + output.movie.genre + " | " + output.movie.runTime +" "+ getString(R.string.min)

        if (output.movie.trailerUrl.isNullOrEmpty()) {
            binding?.imageView26?.hide()
        } else {
            binding?.imageView26?.show()
            binding?.imageView26?.setOnClickListener {
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra("trailerUrl", output.movie.trailerUrl)
                startActivity(intent)
            }
        }


        Glide
            .with(this)
            .load(output.movie.mobimgbig)
            .placeholder(R.drawable.movie_details)
            .into(binding?.imageShow!!)

        setShowTimesCastAdapter(output.movie)
    }

    @SuppressLint("SetTextI18n")
    private fun setShowTimesCastAdapter(movie: CinemaSessionResponse.Movie) {
        text_genres.text = movie.genre
        textView10.text = movie.language
        text_sysnopsis_detail.text = movie.synopsis
        text_directoe_name.text = movie.director.firstName + " " + movie.director.lastName
        recyclerview_show_times_cast.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val adapter = AdpaterShowTimesCast(this, movie.cast)
        recyclerview_show_times_cast.adapter = adapter
    }

    private fun setShowTimesDayDateAdapter(days: ArrayList<CinemaSessionResponse.Days>) {
        if (count == 0) {
            val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            binding?.recylerviewShowTimeDate?.layoutManager = LinearLayoutManager(this)
            val adapter = AdapterDayDate(this, days, this)
            binding?.recylerviewShowTimeDate?.layoutManager = gridLayout
            binding?.recylerviewShowTimeDate?.adapter = adapter
            count = 1
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    //Cinema All Cinema and Show
    private fun setTitleAdapter() {

        binding?.viewpager?.adapter =
            CinemaPageAdapter(this, daySessionResponse,binding?.viewpager)
        binding?.viewpager?.offscreenPageLimit = 3
        binding?.viewpager?.clipChildren = false
        binding?.viewpager?.clipToPadding = false
        binding?.viewpager?.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val transfer = CompositePageTransformer()

        val nextItemVisiblePx =resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx


        transfer.addTransformer(MarginPageTransformer(40))
        transfer.addTransformer(object : com.github.islamkhsh.viewpager2.ViewPager2.PageTransformer,
            ViewPager2.PageTransformer {
            override fun transformPage(page: View, position: Float) {
               // println("rowIndex---->1-$position")
                page.translationX = -pageTranslationX * position

                select_pos = position.toInt()
                val r = 1- abs(position)
                page.scaleY = (0.85f+ r*0.14f)
            }

        })
        binding?.viewpager?.setPageTransformer(transfer)
        binding?.textView110?.show()
        binding?.textView110?.text = daySessionResponse[0].cinema.address1

        binding?.viewpager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                //select_pos = position
                binding?.textView110?.show()
                binding?.textView110?.text = daySessionResponse[position].cinema.address1
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding?.textView110?.show()
                val gridLayout =
                    GridLayoutManager(this@ShowTimesActivity, 1, GridLayoutManager.VERTICAL, false)
                binding?.recyclerviewCinemaTitle?.layoutManager =
                    LinearLayoutManager(this@ShowTimesActivity)
                adapterShowTimesCinemaTitle = AdapterShowTimesCinemaTitle(
                    this@ShowTimesActivity,
                    daySessionResponse[position].experienceSessions,
                    this@ShowTimesActivity
                )
                binding?.recyclerviewCinemaTitle?.layoutManager = gridLayout
                binding?.recyclerviewCinemaTitle?.adapter = adapterShowTimesCinemaTitle
            }

        })

    }

    override fun onDateClick(city: CinemaSessionResponse.Days, view: View, pos: Int) {
        datePos = pos
        binding?.recylerviewShowTimeDate?.let { focusOnView(view, it) }
        dateTime = city.dt.toString()
        println("ShowClicked--->${dateTime}")
        datePosition = city.wdf
        dt = city.dt
        getShowTimes(CinemaSessionRequest(dateTime, movieID))
    }

    private fun focusOnView(view: View, scrollView: RecyclerView) {
        Handler(Looper.getMainLooper()).post {
            val vLeft = view.left
            val vRight = view.right
            val sWidth = scrollView.width
            scrollView.scrollBy((vLeft + vRight - sWidth) / 2, 0)
        }
    }

    override fun onShowClicked(
        show: CinemaSessionResponse.Show,
        name: String,
        position: Int,
        cinemaPos: Int
    ) {
        showPose = cinemaPos
        CinemaID = show.cinemaId
        SessionID = show.sessionId

        if (!preferences.getBoolean(Constant.IS_LOGIN)) {
            type
            val intent = Intent(this, LoginActivity::class.java).putExtra("AREA_CODE", areaCode)
                .putExtra("type", type)
                .putExtra("from", "Details")
                .putExtra("movieId", movieID)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        } else {
            getSeatLayout(
                SeatLayoutRequest(show.cinemaId, dateTime, movieID, show.sessionId),
                name,
                position
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Constant.SEAT_SESSION_CLICK == 1) {
            Constant.SEAT_SESSION_CLICK = 0
            showPose = data?.getIntExtra("CINEMA_POS", 0)!!
            getSeatLayout(
                SeatLayoutRequest(
                    data.getStringExtra("CINEMAID").toString(), dateTime, movieID,
                    data.getStringExtra("SESSIONID").toString()
                ), data.getStringExtra("NAME").toString(),
                data.getIntExtra("SHOW_POS", 0)
            )
        }
    }

    @SuppressLint("CutPasteId", "SetTextI18n")
    private fun showSeatTypePopup(output: SeatLayoutResponse.Output, name: String, pos: Int) {
        val mDialogView = layoutInflater.inflate(R.layout.seat_selection_main_alert_dailog, null)
        val mBuilder = AlertDialog.Builder(this, R.style.MyDialogTransparent)
            .setView(mDialogView)
        mBuilder.setCancelable(false)

        mAlertDialog = mBuilder.show()
        mAlertDialog?.show()
        mAlertDialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        val totalPrice = mDialogView.findViewById<TextView>(R.id.text_total_t_kd)
        val termsCond = mDialogView.findViewById<TextView>(R.id.textView103)
        val ratingDesc = mDialogView.findViewById<TextView>(R.id.text_category_decription)
        val ratingUi = mDialogView.findViewById<CardView>(R.id.rating_ui)
        val rating = mDialogView.findViewById<TextView>(R.id.text_age_category)
        val terms = mDialogView.findViewById<TextView>(R.id.text_agree)
        val tvGiftCard = mDialogView.findViewById<TextView>(R.id.tv_gift_card)
        val tvGiftVoucher = mDialogView.findViewById<TextView>(R.id.tv_gift_voucher)
        val textBankOffer = mDialogView.findViewById<TextView>(R.id.text_bank_offer)
        val ClickUi = mDialogView.findViewById<ConstraintLayout>(R.id.vw_ticket_qtyUi)
        val cancelDialog = mDialogView.findViewById<ConstraintLayout>(R.id.cancelDialog)

        val viewGift = mDialogView.findViewById<View>(R.id.view_gift)
        val bankOffers = mDialogView.findViewById<AutoCompleteTextView>(R.id.bank_offers1)

        val viewGiftVoucher = mDialogView.findViewById<View>(R.id.view_gift_voucher)
        val viewGiftCard1 = mDialogView.findViewById<View>(R.id.view_gift_card1)

        termsCond.paintFlags = tvGiftCard.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        tvGiftCard.paintFlags = tvGiftCard.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        tvGiftVoucher.paintFlags = tvGiftVoucher.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        textBankOffer.paintFlags = textBankOffer.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        ratingDesc.text = output.movie.ratingDescription
        if (output.movie.rating.isNullOrEmpty()) {
            rating.hide()
        } else {
            rating.show()
            rating.text = output.movie.rating
        }
        when (output.movie.rating) {
            "PG" -> {
                ratingUi.setCardBackgroundColor(this.resources.getColor(R.color.grey))

            }
            "G" -> {
                ratingUi.setCardBackgroundColor(this.resources.getColor(R.color.green))

            }
            "18+" -> {
                ratingUi.setCardBackgroundColor(this.resources.getColor(R.color.red))

            }
            "13+" -> {
                ratingUi.setCardBackgroundColor(this.resources.getColor(R.color.yellow))

            }
            "E" -> {
                ratingUi.setCardBackgroundColor(this.resources.getColor(R.color.wowOrange))

            }
            "T" -> {
                ratingUi.setCardBackgroundColor(this.resources.getColor(R.color.tabIndicater))

            }
            else -> {
                ratingUi.setCardBackgroundColor(this.resources.getColor(R.color.blue))

            }
        }

        cancelDialog.setOnClickListener {
            mAlertDialog?.dismiss()
        }

        textBankOffer.setOnClickListener {
            textBankOffer.setTextColor(ContextCompat.getColor(this, R.color.text_alert_color_red))
            tvGiftCard.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvGiftVoucher.setTextColor(ContextCompat.getColor(this, R.color.white))
            viewGift.visibility = View.VISIBLE
            viewGiftCard1.visibility = View.GONE
            viewGiftVoucher.visibility = View.GONE

            bankOffers.setOnClickListener {
                val bDialogView = LayoutInflater.from(this)
                    .inflate(R.layout.seat_selection_bank_offer_alert, null)
                val bBuilder = AlertDialog.Builder(this, R.style.MyDialogTransparent)
                    .setView(bDialogView)
                val bAlertDialog = bBuilder.show()
                bAlertDialog.show()
                bAlertDialog.window?.setBackgroundDrawableResource(R.color.transparent)

                val cancelGoBack1 = TextUtils.concat(
                    Constant().getSpanableText(
                        ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)),
                        ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)!!,
                        0,
                        7,
                        1.3f,
                        SpannableString(this.getString(R.string.cancels))
                    ),

                    Constant().getSpanableText(
                        ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_color)),
                        ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)!!,
                        0,
                        11,
                        1.1f,
                        SpannableString(this.getString(R.string.and_go_back))
                    )
                )
                bAlertDialog.textView1_cancel_back.text = cancelGoBack1

                bAlertDialog.textView1_cancel_back.setOnClickListener {
                    bAlertDialog.dismiss()
                }
            }
        }

        tvGiftVoucher.setOnClickListener {
            textBankOffer.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvGiftCard.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvGiftVoucher.setTextColor(ContextCompat.getColor(this, R.color.text_alert_color_red))
            viewGift.visibility = View.GONE
            viewGiftCard1.visibility = View.GONE
            viewGiftVoucher.visibility = View.VISIBLE
        }

        tvGiftCard.setOnClickListener {
            textBankOffer.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvGiftVoucher.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvGiftCard.setTextColor(ContextCompat.getColor(this, R.color.text_alert_color_red))
            viewGift.visibility = View.GONE
            viewGiftVoucher.visibility = View.GONE
            viewGiftCard1.visibility = View.VISIBLE
        }

        val btnDecrease: TextView = mDialogView.findViewById(R.id.text_decrease)
        val txtNumber: TextView = mDialogView.findViewById(R.id.text_number)
        val btnIncrease: TextView = mDialogView.findViewById(R.id.text_increase)
//        val textCancelGoback = mDialogView.findViewById<TextView>(R.id.text_cancel_goback)
        val textProceeds = mDialogView.findViewById<TextView>(R.id.text_proceeds)
        try {
            val cancelGoBack = TextUtils.concat(
                Constant().getSpanableText(
                    ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)),
                    ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)!!,
                    0,
                    7,
                    1.3f,
                    SpannableString(this.getString(R.string.cancels))
                ),
                Constant().getSpanableText(
                    ForegroundColorSpan(ContextCompat.getColor(this, R.color.hint_color)),
                    ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)!!,
                    0,
                    11,
                    1.1f,
                    SpannableString(this.getString(R.string.and_go_back))
                )
            )


        } catch (e: Exception) {
            println("SeatCatogry--->${e.message}")
        }


        val selectSeatCategory = mDialogView.findViewById<FlexboxLayout>(R.id.select_seat_category)
        val textView5 = mDialogView.findViewById<TextView>(R.id.textView5)
        selectSeatCategory.removeAllViews()
        totalPrice.text = getString(R.string.price_kd) + " 0.000"

        if (output.seatTypes[0].seatTypes.size==0){
            textView5.text = getString(R.string.select_seat_type)
        }else{
            textView5.text = getString(R.string.select_seat_category)
        }

        val viewListForDates = ArrayList<View>()
        for (item in output.seatTypes) {
            val v: View = LayoutInflater.from(this)
                .inflate(R.layout.seat_selection_category_item, selectSeatCategory, false)
            val imageSeatSelection: ImageView = v.findViewById(R.id.image_seat_selection)
            val tvSeatSelectiopn: TextView = v.findViewById(R.id.tv_seat_selectiopn)
            val tv_seat_avialable: TextView = v.findViewById(R.id.tv_seat_avialable)
            val tv_kd_price: TextView = v.findViewById(R.id.tv_kd_price)
            when (item.seatType) {
                "Family" -> {
                    Glide.with(this)
                        .load(item.icon)
                        .placeholder(R.drawable.family)
                        .into(imageSeatSelection)
                }
                "Bachelor" -> {
                    Glide.with(this)
                        .load(item.icon)
                        .placeholder(R.drawable.bachlor)
                        .into(imageSeatSelection)
                }
                "Standard" -> {
                    Glide.with(this)
                        .load(item.icon)
                        .placeholder(R.drawable.stander)
                        .into(imageSeatSelection)
                }
                "First Row" -> {
                    Glide.with(this)
                        .load(item.icon)
                        .placeholder(R.drawable.first_row)
                        .into(imageSeatSelection)
                }
                "Premium" -> {
                    Glide.with(this)
                        .load(item.icon)
                        .placeholder(R.drawable.premium)
                        .into(imageSeatSelection)
                }
                else -> {
                    Glide.with(this)
                        .load(item.icon)
                        .placeholder(R.drawable.ic_seat_icon_1_select)
                        .into(imageSeatSelection)
                }
            }
            viewListForDates.add(v)
            if (item.seatTypes.isNullOrEmpty()) {
                tv_seat_avialable.show()
                tv_kd_price.show()
                tv_seat_avialable.text = item.count
//                tv_seat_avialable.text = item.count.toString() + " Available"
                tv_kd_price.text = item.price.toString()

                seatAbility = if (item.count > "") {
                    1
                } else {
                    0
                }
            } else {
                tv_seat_avialable.hide()
                tv_kd_price.hide()
            }
            println("LanguageCheck--->${languageCheck}")

            if (languageCheck == "en") {
                println("LanguageCheck--->${languageCheck}")
                tvSeatSelectiopn.text = item.seatType

            } else {
                tvSeatSelectiopn.text = item.seatTypeStr

            }
//            tvSeatSelectiopn.text = item.seatType
            selectSeatCategory.addView(v)
            v.setOnClickListener {
                areaCode = item.areacode
                ttType = item.ttypeCode
                seatCat = item.seatType
//                println("seatTypeCheck--->${ttType+"areaCode-->${areaCode}"}")
                totalPriceResponse = item.priceInt
                totalPrice.text = getString(R.string.price_kd) + " 0.000"
                num = 0
                txtNumber.text = "0"
                var imageSeatSelection1: ImageView?
                var tvSeatSelection1: TextView?
                var tvSeatAvailable11: TextView?
                var tvKdPrice11: TextView?
                println("seatTypeArrayOne--->${item.seatTypes.size}")
                if (item.seatTypes.isNotEmpty()) {
                    categoryClick = false
                    btnDecrease.isEnabled = false
                    btnIncrease.isEnabled = false
                    btnDecrease.isClickable = false
                    btnIncrease.isClickable = false

                    ClickUi.hide()
                } else {
                    categoryClick = true

                    ClickUi.show()
                    btnDecrease.isEnabled = true
                    btnIncrease.isEnabled = true
                    btnDecrease.isClickable = true
                    btnIncrease.isClickable = true
                }
                for (v in viewListForDates) {
                    imageSeatSelection1 =
                        v.findViewById(R.id.image_seat_selection) as ImageView
                    tvSeatSelection1 =
                        v.findViewById(R.id.tv_seat_selectiopn) as TextView
                    tvSeatAvailable11 =
                        v.findViewById(R.id.tv_seat_avialable) as TextView
                    tvKdPrice11 = v.findViewById(R.id.tv_kd_price) as TextView
                    imageSeatSelection1.setColorFilter(getColor(R.color.hint_color))
                    tvSeatSelection1.setTextColor(getColor(R.color.hint_color))
                    tvSeatAvailable11.setTextColor(getColor(R.color.hint_color))
                    tvKdPrice11.setTextColor(getColor(R.color.hint_color))
//
                }
                imageSeatSelection.setColorFilter(getColor(R.color.text_alert_color_red))
                tvSeatSelectiopn.setTextColor(getColor(R.color.text_alert_color_red))
                tv_seat_avialable.setTextColor(getColor(R.color.text_alert_color_red))
                tv_kd_price.setTextColor(getColor(R.color.text_alert_color_red))

                selectSeatClick + 1
                val selectSeatType = mDialogView.findViewById<FlexboxLayout>(R.id.select_seat_type)
                val tv_select_seat_type =
                    mDialogView.findViewById<TextView>(R.id.tv_select_seat_type)
                val view2s_line = mDialogView.findViewById<View>(R.id.view2s_line)
                selectSeatType.removeAllViews()
                if (item.seatTypes.isNotEmpty()) {
                    val viewListForDates = ArrayList<View>()
                    selectSeatType.show()
                    tv_select_seat_type.show()
                    view2s_line.show()
                    for (data in item.seatTypes) {
                        val v: View = LayoutInflater.from(this)
                            .inflate(R.layout.seat_selection_type_item, selectSeatType, false)
                        viewListForDates.add(v)
                        val imgSeatSelectionType: ImageView =
                            v.findViewById(R.id.img_seat_selection_type)
                        val imgMetroInfo: ImageView = v.findViewById(R.id.img_metro_info)
                        val textseatType: TextView = v.findViewById(R.id.textseat_type)
                        val tvSeatAvialable: TextView = v.findViewById(R.id.tv_seat_avialable)
                        val tvKdPrice: TextView = v.findViewById(R.id.tv_kd_price)


                        when (data.seatType) {
                            "Family" -> {
                                Glide.with(this)
                                    .load(item.icon)
                                    .placeholder(R.drawable.family)
                                    .into(imgSeatSelectionType)
                            }
                            "Bachelor" -> {
                                Glide.with(this)
                                    .load(item.icon)
                                    .placeholder(R.drawable.bachlor)
                                    .into(imgSeatSelectionType)
                            }
                            "Standard" -> {
                                Glide.with(this)
                                    .load(item.icon)
                                    .placeholder(R.drawable.stander)
                                    .into(imgSeatSelectionType)
                            }
                            "First Row" -> {
                                Glide.with(this)
                                    .load(item.icon)
                                    .placeholder(R.drawable.first_row)
                                    .into(imgSeatSelectionType)
                            }
                            "Premium" -> {
                                Glide.with(this)
                                    .load(item.icon)
                                    .placeholder(R.drawable.premium)
                                    .into(imgSeatSelectionType)
                            }
                            else -> {
                                Glide.with(this)
                                    .load(item.icon)
                                    .placeholder(R.drawable.ic_seat_icon_1_select)
                                    .into(imgSeatSelectionType)
                            }
                        }

                        if (languageCheck == "en") {
                            textseatType.text = data.seatType

                        } else {
                            textseatType.text = data.seatTypeStr

                        }
                        imgMetroInfo.setImageResource(R.drawable.ic_icon_metro_info)
//                        textseatType.text = data.seatType
                        tvSeatAvialable.text = data.count
//                        tvSeatAvialable.text = data.count.toString() + getString(R.string.available)
                        tvKdPrice.text = data.price.toString()
                        selectSeatType.addView(v)

                        v.setOnClickListener {
                            totalPrice.text = getString(R.string.price_kd) + " 0.000"
                            num = 0
                            txtNumber.text = "0"
                            var imageSeatSelection1: ImageView?
                            var tvSeatSelectiopn1: TextView?
                            var tvSeatAvialable1: TextView?
                            var tvKdPrice1: TextView?

                            println("seatTypeArrayOne1--->${item.seatTypes.size}")
                            if (item.seatTypes.isNotEmpty()) {

                                categoryClick = true
                                ClickUi.show()
                                btnDecrease.isEnabled = true
                                btnIncrease.isEnabled = true
                                btnDecrease.isClickable = true
                                btnIncrease.isClickable = true
                            } else {
                                categoryClick = false
                                ClickUi.hide()
                                btnDecrease.isEnabled = false
                                btnIncrease.isEnabled = false
                                btnDecrease.isClickable = false
                                btnIncrease.isClickable = false
                            }

                            for (v in viewListForDates) {
                                imageSeatSelection1 =
                                    v.findViewById(R.id.img_seat_selection_type) as ImageView
                                tvSeatSelectiopn1 =
                                    v.findViewById(R.id.textseat_type) as TextView
                                tvSeatAvialable1 =
                                    v.findViewById(R.id.tv_seat_avialable) as TextView
                                tvKdPrice1 = v.findViewById(R.id.tv_kd_price) as TextView
                                imageSeatSelection1!!.setColorFilter(getColor(R.color.hint_color))
                                tvSeatSelectiopn1.setTextColor(getColor(R.color.hint_color))
                                tvSeatAvialable1.setTextColor(getColor(R.color.hint_color))
                                tvKdPrice1.setTextColor(getColor(R.color.hint_color))
                            }
                            imgSeatSelectionType.setColorFilter(getColor(R.color.text_alert_color_red))
                            textseatType.setTextColor(getColor(R.color.text_alert_color_red))
                            tvSeatAvialable.setTextColor(getColor(R.color.text_alert_color_red))
                            tvKdPrice.setTextColor(getColor(R.color.text_alert_color_red))
                            areaCode = data.areacode
                            ttType = data.ttypeCode
                            seatType = data.seatType
                            totalPriceResponse = data.priceInt
                            println("seatTypeCheck--->${ttType + "areaCode-->${areaCode}"}")

                        }
                    }
                } else {
                    selectSeatType.invisible()
                    tv_select_seat_type.hide()
                    view2s_line.hide()
                }
            }

            btnDecrease.setOnClickListener {
                if (totalPriceResponse < 0) {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        getString(R.string.selectAnotherSeat),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                } else {
                    if (num < 0 || num == 0) {
//                        Toast.makeText(this, "sorry", Toast.LENGTH_LONG).show()
                    } else {
                        num -= 1
                        txtNumber.text = num.toString()
                        totalPrice.text =
                            getString(R.string.price_kd) + " " + Constant.DECIFORMAT.format((totalPriceResponse * num) / 100)

//                        totalPrice.text = "KD ${(totalPriceResponse * num) / 1000.0}"

                        println("divide--->$totalPriceResponse")
                    }
                }
            }

            btnIncrease.setOnClickListener {
                if (totalPriceResponse < 0) {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        getString(R.string.selectOneSeat),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                } else {
                    if (num < 0 || num == output.seatCount) {

                    toast("${getString(R.string.seatLimit)} ${" "+output.seatCount} ${" "+getString(R.string.seat)}")

                    } else {
                        num += 1
                        txtNumber.text = num.toString()
                        totalPrice.text =
                            getString(R.string.price_kd) + " " + Constant.DECIFORMAT.format((totalPriceResponse * num) / 100)

                    }
                }
            }

            textProceeds.setOnClickListener {
                if (!categoryClick) {
                    if (output.seatTypes[0].seatTypes.size==0){
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            getString(R.string.select_seat_type),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                            },
                            negativeClick = {
                            })
                        dialog.show()
                    }else{
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            getString(R.string.select_seat_category),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                            },
                            negativeClick = {
                            })
                        dialog.show()
                    }

                } else if (num == 0) {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        getString(R.string.selectSeat),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
//                    selectSeatAlert()
                } else {
                    this.startActivityForResult(

                        Intent(
                            mAlertDialog?.context,
                            SeatScreenMainActivity::class.java
                        ).putExtra("AREA_CODE", areaCode)
                            .putExtra("SEAT_DATA", output)
                            .putExtra("TT_TYPE", ttType)
                            .putExtra("SHOW_DATA", showData)
                            .putExtra("SHOW_DATA_CSESSION", showDataCSessionResponse)
                            .putExtra("CINEMA", name)
                            .putExtra("SEAT_CAT", seatCat)
                            .putExtra("SEAT_TYPE", seatType)
                            .putExtra("DATE_POS", datePos)
                            .putExtra("SEAT_POS", num)
                            .putExtra("DateTime", dateTime)
                            .putExtra("MovieId", movieID)
                            .putExtra("CinemaID", CinemaID)
                            .putExtra("DatePosition", datePosition)
                            .putExtra("dt", dt)
                            .putExtra("SessionID", SessionID)
                            .putExtra("SHOW_POS", pos)
                            .putExtra("CINEMA_POS", showPose), 50
                    )

                    categoryClick = false
                    num = 0
                    mAlertDialog?.dismiss()
                    println("dateCheck--->${dateTime},  Movieid--->${movieID},CinemaId--->${CinemaID} ,SessionID--->${SessionID}")
                }
            }
        }

        try {
            val spanString = SpannableString(getString(R.string.termsCondition))

            val termsAndCondition: ClickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                }
            }

            spanString.setSpan(termsAndCondition, 37, 55, 0)
            spanString.setSpan(ForegroundColorSpan(getColor(R.color.silver)), 37, 55, 0)
            spanString.setSpan(UnderlineSpan(), 37, 55, 0)
            terms.movementMethod = LinkMovementMethod.getInstance()
            terms.setText(spanString, TextView.BufferType.SPANNABLE)
            terms?.isSelected = true
        } catch (e: Exception) {
            println("ErrorData--->${e.message}")
        }

        loader?.dismiss()
    }

    override fun onResume() {
        super.onResume()
        if (Constant.ON_BACK_FOOD == 1) {
            mAlertDialog?.dismiss()
        }
    }

    override fun onMovieDateClick(
        dayDateItem: CSessionResponse.Output.Day,
        itemView: View,
        position: Int
    ) {
        datePos = position
        dateTime = dayDateItem.dt
        getCinemaData(CinemaSessionRequest(dateTime, movieID))
    }

    override fun onShowClicked(
        show: CSessionResponse.Output.DaySession.Show,
        name: String,
        position: Int,
        cinemaPos: Int,
        movieCinemaId: String
    ) {
        showPose = cinemaPos
        CinemaID = show.cinemaId
        SessionID = show.sessionId
        movieID = movieCinemaId

        if (!preferences.getBoolean(Constant.IS_LOGIN)) {
            type
            val intent = Intent(this, LoginActivity::class.java).putExtra("AREA_CODE", areaCode)
                .putExtra("type", type)
                .putExtra("from", "Details")
                .putExtra("movieId", movieID)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        } else {
            getSeatLayout(
                SeatLayoutRequest(show.cinemaId, dateTime, movieID, show.sessionId), name, position
            )
        }

    }

}