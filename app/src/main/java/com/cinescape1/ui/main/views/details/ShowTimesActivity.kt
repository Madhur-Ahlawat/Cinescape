package com.cinescape1.ui.main.views.details

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Paint
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
import com.cinescape1.data.models.responseModel.GetMovieResponse
import com.cinescape1.data.models.responseModel.SeatLayoutResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityShowTimesBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.adapters.AdapterDayDate
import com.cinescape1.ui.main.views.adapters.CinemaPageAdapter
import com.cinescape1.ui.main.views.adapters.cinemaSessionAdapters.AdapterCinemaSessionScroll
import com.cinescape1.ui.main.views.adapters.showTimesAdapters.AdapterShowTimesCinemaTitle
import com.cinescape1.ui.main.views.adapters.showTimesAdapters.AdpaterShowTimesCast
import com.cinescape1.ui.main.views.details.viewModel.ShowTimesViewModel
import com.cinescape1.ui.main.views.login.LoginActivity
import com.cinescape1.ui.main.views.player.PlayerActivity
import com.cinescape1.ui.main.views.seatLayout.SeatScreenMainActivity
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


@Suppress("DEPRECATION", "NAME_SHADOWING")
class ShowTimesActivity : DaggerAppCompatActivity(), AdapterDayDate.RecycleViewItemClickListener,
    AdapterShowTimesCinemaTitle.CinemaAdapterListener, AdapterCinemaSessionScroll.LocationListener {
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
    private var cinemaID = ""
    private var sessionID = ""
    private var type = ""
    private var datePosition = ""
    private var dt = ""
    private var loader: LoaderDialog? = null
    private var showDataCSessionResponse: CSessionResponse.Output? = null
    private var showData: CinemaSessionResponse.Output? = null
    private var daySessionResponse: ArrayList<CinemaSessionResponse.DaySession> = ArrayList()
    private var totalPriceResponse: Double = 0.0
    private var selectSeatClick: Int = 0
    private var seatAbility: Int = 0
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

                binding?.moviePage?.hide()
                binding?.comingSoon?.show()
                binding?.viewpager?.hide()
                binding?.centerView?.hide()
                binding?.imageView48?.hide()
                include.show()

                movieDetails(movieID)

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

    @SuppressLint("ClickableViewAccessibility")
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


        binding?.view68?.setOnClickListener {
            Toast.makeText(
                applicationContext,
                "This Feature Will Available Soon",
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
            if (event.action == ACTION_UP) {
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

        movieSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
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

    private fun movieDetails(movieId: String) {
        showTimeViewModel.movieDetails(movieId)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {
                                    try {
                                        binding?.LayoutTime?.show()
                                        movieDetailsData(it.data.output.movie)
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

    private fun movieDetailsData(output: GetMovieResponse.Output.Movie) {
        println("""updateUiCinemaSession ----> $output""")
        binding?.textFilmHouseName?.text = output.title
        binding?.textFilmHouseName?.isSelected = true
        binding?.textView56?.text = output.rating


        binding?.view67?.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Insert Subject here")
            val appUrl = output.shareUrl
            shareIntent.putExtra(Intent.EXTRA_TEXT, appUrl)
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        when (output.rating) {
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
        if (output.rating == "") {
            binding?.ratingUi?.hide()
        } else {
            binding?.ratingUi?.show()
        }

        binding?.textMovieType?.text =
            output.language + " | " + output.genre + " | " + output.runTime + " " + getString(R.string.min)

        if (output.trailerUrl.isEmpty()) {
            binding?.imageView26?.hide()
        } else {
            binding?.imageView26?.show()
            binding?.imageView26?.setOnClickListener {
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra("trailerUrl", output.trailerUrl)
                startActivity(intent)
            }
        }
        Glide
            .with(this)
            .load(output.mobimgbig)
            .placeholder(R.drawable.movie_details)
            .into(binding?.imageShow!!)

        textView6.hide()
        text_genres.text = output.genre
        textView10.text = output.language
        textView123.text = output.subTitle
        text_sysnopsis_detail.text = output.synopsis
        text_directoe_name.text = output.director.firstName + " " + output.director.lastName

        if (output.cast.isEmpty()) {
            text_cast.hide()
            recyclerview_show_times_cast.hide()
        } else {
            text_cast.show()
            recyclerview_show_times_cast.show()
        }

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
        binding?.textFilmHouseName?.isSelected = true
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
            output.movie.language + " | " + output.movie.genre + " | " + output.movie.runTime + " " + getString(
                R.string.min
            )

        if (output.movie.trailerUrl.isEmpty()) {
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
        textView123.text = movie.subTitle
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
            CinemaPageAdapter(this, daySessionResponse, binding?.viewpager)
        binding?.viewpager?.offscreenPageLimit = 3
        binding?.viewpager?.clipChildren = false
        binding?.viewpager?.clipToPadding = false
        binding?.viewpager?.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val transfer = CompositePageTransformer()

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx


        transfer.addTransformer(MarginPageTransformer(40))
        transfer.addTransformer(object : com.github.islamkhsh.viewpager2.ViewPager2.PageTransformer,
            ViewPager2.PageTransformer {
            override fun transformPage(page: View, position: Float) {
                // println("rowIndex---->1-$position")
                page.translationX = -pageTranslationX * position

                select_pos = position.toInt()
                val r = 1 - abs(position)
                page.scaleY = (0.85f + r * 0.14f)
            }

        })
        binding?.viewpager?.setPageTransformer(transfer)
        binding?.textView110?.show()
        binding?.textView110?.text = daySessionResponse[0].cinema.address1

        binding?.viewpager?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

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
        dateTime = city.dt
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
        cinemaID = show.cinemaId
        sessionID = show.sessionId

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
//        val terms = mDialogView.findViewById<TextView>(R.id.text_agree)
        val tvGiftCard = mDialogView.findViewById<TextView>(R.id.tv_gift_card)
        val tvGiftVoucher = mDialogView.findViewById<TextView>(R.id.tv_gift_voucher)
        val textBankOffer = mDialogView.findViewById<TextView>(R.id.text_bank_offer)
        val clickUi = mDialogView.findViewById<ConstraintLayout>(R.id.vw_ticket_qtyUi)
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
        if (output.movie.rating.isEmpty()) {
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

        }

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
        val textProceeds = mDialogView.findViewById<TextView>(R.id.text_proceeds)

        val selectSeatCategory = mDialogView.findViewById<FlexboxLayout>(R.id.select_seat_category)
        val textView5 = mDialogView.findViewById<TextView>(R.id.textView5)
        selectSeatCategory.removeAllViews()
        totalPrice.text = getString(R.string.price_kd) + " 0.000"

        if (output.seatTypes[0].seatTypes.size == 0) {
            textView5.text = getString(R.string.select_seat_type)
        } else {
            textView5.text = getString(R.string.select_seat_category)
        }

        val viewListForDates = ArrayList<View>()
        for (item in output.seatTypes) {
            val v: View = LayoutInflater.from(this)
                .inflate(R.layout.seat_selection_category_item, selectSeatCategory, false)
            val imageSeatSelection: ImageView = v.findViewById(R.id.image_seat_selection)
            val tvSeatSelection: TextView = v.findViewById(R.id.tv_seat_selectiopn)
            val tvSeatAvailable2: TextView = v.findViewById(R.id.tv_seat_avialable)
            val tvKdPrice2: TextView = v.findViewById(R.id.tv_kd_price)
            Glide.with(this)
                .load(item.icon)
                .into(imageSeatSelection)

            viewListForDates.add(v)
            if (item.seatTypes.isEmpty()) {
                tvSeatAvailable2.show()
                tvKdPrice2.show()
                tvSeatAvailable2.text = item.count
                tvKdPrice2.text = item.price.toString()

                seatAbility = if (item.count > "") {
                    1
                } else {
                    0
                }
            } else {
                tvSeatAvailable2.hide()
                tvKdPrice2.hide()
            }

            if (languageCheck == "en") {
                println("LanguageCheck--->${languageCheck}")
                tvSeatSelection.text = item.seatType

            } else {
                tvSeatSelection.text = item.seatTypeStr

            }
            selectSeatCategory.addView(v)
            v.setOnClickListener {
                areaCode = item.areacode
                ttType = item.ttypeCode
                seatCat = item.seatType
                totalPriceResponse = item.priceInt
//                totalPrice.text = getString(R.string.price_kd) + " 0.000"
                num = 1
                txtNumber.text = "1"
                Toast.makeText(this, "1--->$totalPriceResponse", Toast.LENGTH_SHORT).show()

                totalPrice.text =
                    getString(R.string.price_kd) + " " + Constant.DECIFORMAT.format((totalPriceResponse * num) / 100)

                var imageSeatSelection1: ImageView?
                var tvSeatSelection1: TextView?
                var tvSeatAvailable11: TextView?
                var tvKdPrice11: TextView?
                if (item.seatTypes.isNotEmpty()) {
                    categoryClick = false
                    btnDecrease.isEnabled = false
                    btnIncrease.isEnabled = false
                    btnDecrease.isClickable = false
                    btnIncrease.isClickable = false
                    clickUi.hide()
                } else {
                    categoryClick = true
                    clickUi.show()
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
                }
                Glide.with(this)
                    .load(item.iconActive)
                    .into(imageSeatSelection)
                imageSeatSelection.setColorFilter(getColor(R.color.text_alert_color_red))
                tvSeatSelection.setTextColor(getColor(R.color.text_alert_color_red))
                tvSeatAvailable2.setTextColor(getColor(R.color.text_alert_color_red))
                tvKdPrice2.setTextColor(getColor(R.color.text_alert_color_red))

                selectSeatClick + 1
                val selectSeatType = mDialogView.findViewById<FlexboxLayout>(R.id.select_seat_type)
                val tvSelectSeatType =
                    mDialogView.findViewById<TextView>(R.id.tv_select_seat_type)
                val view2sLine = mDialogView.findViewById<View>(R.id.view2s_line)
                selectSeatType.removeAllViews()
                if (item.seatTypes.isNotEmpty()) {

                    val viewListForDates = ArrayList<View>()
                    selectSeatType.show()
                    tvSelectSeatType.show()
                    view2sLine.show()
                    for (data in item.seatTypes) {
                        try {
                            selectSeatType.show()
                            val v: View = LayoutInflater.from(this)
                                .inflate(R.layout.seat_selection_type_item, selectSeatType, false)
                            viewListForDates.add(v)
                            val imgSeatSelectionType: ImageView =
                                v.findViewById(R.id.img_seat_selection_type)
                            val imgMetroInfo: ImageView = v.findViewById(R.id.img_metro_info)
                            val textSeatType: TextView = v.findViewById(R.id.textseat_type)
                            val tvSeatAvailable: TextView = v.findViewById(R.id.tv_seat_avialable)
                            val tvKdPrice: TextView = v.findViewById(R.id.tv_kd_price)
                            println("dayaCheck--->${selectSeatType.visibility}")
                            if (languageCheck == "en") {
                                textSeatType.text = data.seatType
                            } else {
                                textSeatType.text = data.seatTypeStr
                            }
                            selectSeatType.show()

                            Glide.with(this)
                                .load(data.icon)
                                .into(imgSeatSelectionType)

                            imgMetroInfo.setImageResource(R.drawable.ic_icon_metro_info)
                            tvKdPrice.text = data.price.toString()
                            tvSeatAvailable.text = data.count
                            selectSeatType.addView(v)

                            v.setOnClickListener {
//                                totalPrice.text = getString(R.string.price_kd) + " 0.000"
                                num = 1
                                txtNumber.text = "1"
                                Toast.makeText(this, "2--->$totalPriceResponse", Toast.LENGTH_SHORT)
                                    .show()
                                totalPrice.text =
                                    getString(R.string.price_kd) + " " + Constant.DECIFORMAT.format(
                                        (totalPriceResponse * num) / 100
                                    )

                                var imageSeatSelection1: ImageView?
                                var tvSeatSelection1: TextView?
                                var tvSeatAvailable1: TextView?
                                var tvKdPrice1: TextView?

                                println("seatTypeArrayOne1--->${item.seatTypes.size}")
                                if (item.seatTypes.isNotEmpty()) {

                                    categoryClick = true
                                    clickUi.show()
                                    btnDecrease.isEnabled = true
                                    btnIncrease.isEnabled = true
                                    btnDecrease.isClickable = true
                                    btnIncrease.isClickable = true
                                } else {
                                    categoryClick = false
                                    clickUi.hide()
                                    btnDecrease.isEnabled = false
                                    btnIncrease.isEnabled = false
                                    btnDecrease.isClickable = false
                                    btnIncrease.isClickable = false
                                }

                                for (v in viewListForDates) {
                                    imageSeatSelection1 =
                                        v.findViewById(R.id.img_seat_selection_type) as ImageView
                                    tvSeatSelection1 =
                                        v.findViewById(R.id.textseat_type) as TextView
                                    tvSeatAvailable1 =
                                        v.findViewById(R.id.tv_seat_avialable) as TextView
                                    tvKdPrice1 = v.findViewById(R.id.tv_kd_price) as TextView
                                    imageSeatSelection1!!.setColorFilter(getColor(R.color.hint_color))
                                    tvSeatSelection1!!.setTextColor(getColor(R.color.hint_color))
                                    tvSeatAvailable1.setTextColor(getColor(R.color.hint_color))
                                    tvKdPrice1.setTextColor(getColor(R.color.hint_color))
                                }

                                Glide.with(this)
                                    .load(data.iconActive)
                                    .into(imgSeatSelectionType)
                                println("imageActive--->${data.iconActive}")
                                imgSeatSelectionType.setColorFilter(getColor(R.color.text_alert_color_red))
                                textSeatType.setTextColor(getColor(R.color.text_alert_color_red))
                                tvSeatAvailable.setTextColor(getColor(R.color.text_alert_color_red))
                                tvKdPrice.setTextColor(getColor(R.color.text_alert_color_red))
                                areaCode = data.areacode
                                ttType = data.ttypeCode
                                seatType = data.seatType
                                totalPriceResponse = data.priceInt
                                println("seatTypeCheck--->${ttType + "areaCode-->${areaCode}"}")

                            }

                        } catch (e: Exception) {
                            println("manageException--->${e.message}")
                        }


                    }
                } else {
                    println("manageException1--->$")

                    selectSeatType.invisible()
                    tvSelectSeatType.hide()
                    view2sLine.hide()
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
                    if (num < 1 || num == 1) {
                        Toast.makeText(this, "sorry", Toast.LENGTH_LONG).show()
                    } else {
                        num -= 1
                        txtNumber.text = num.toString()
                        totalPrice.text =
                            getString(R.string.price_kd) + " " + Constant.DECIFORMAT.format((totalPriceResponse * num) / 100)
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
                        if (num == 1) {
                            btnDecrease.hide()
                        } else {
                            btnDecrease.show()

                        }
                        toast(
                            "${getString(R.string.seatLimit)} ${" " + output.seatCount} ${
                                " " + getString(
                                    R.string.seat
                                )
                            }"
                        )

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
                    if (output.seatTypes[0].seatTypes.size == 0) {
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
                    } else {
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
                            .putExtra("CinemaID", cinemaID)
                            .putExtra("DatePosition", datePosition)
                            .putExtra("dt", dt)
                            .putExtra("SessionID", sessionID)
                            .putExtra("SHOW_POS", pos)
                            .putExtra("CINEMA_POS", showPose), 50
                    )

                    categoryClick = false
                    num = 0
                    mAlertDialog?.dismiss()
                    println("dateCheck--->${dateTime},  MovieId--->${movieID},CinemaId--->${cinemaID} ,SessionID--->${sessionID}")
                }
            }
        }

        loader?.dismiss()
    }

    override fun onResume() {
        super.onResume()
        if (Constant.ON_BACK_FOOD == 1) {
            mAlertDialog?.dismiss()
        }
    }

    override fun onShowClicked(
        show: CSessionResponse.Output.DaySession.Show,
        name: String,
        position: Int,
        cinemaPos: Int,
        movieCinemaId: String
    ) {
        showPose = cinemaPos
        cinemaID = show.cinemaId
        sessionID = show.sessionId
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