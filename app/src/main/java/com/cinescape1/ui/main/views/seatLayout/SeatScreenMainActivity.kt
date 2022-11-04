package com.cinescape1.ui.main.views.seatLayout

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.ReserveSeatRequest
import com.cinescape1.data.models.requestModel.SeatLayoutRequest
import com.cinescape1.data.models.responseModel.CSessionResponse
import com.cinescape1.data.models.responseModel.SeatLayoutResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivitySeatScreenMainBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.adapters.CinemaSeatPagerAdapter
import com.cinescape1.ui.main.views.adapters.cinemaSessionAdapters.AdapterCinemaSessionScroll
import com.cinescape1.ui.main.views.adapters.seatlayout.SeatShowTimesCinemaAdapter
import com.cinescape1.ui.main.views.details.ShowTimesActivity
import com.cinescape1.ui.main.views.food.FoodActivity
import com.cinescape1.ui.main.views.login.LoginActivity
import com.cinescape1.ui.main.views.seatLayout.viewModel.SeatScreenMainViewModel
import com.cinescape1.ui.main.views.summery.SummeryActivity
import com.cinescape1.utils.*
import com.cinescape1.utils.Constant.Companion.SEAT_SESSION_CLICK
import com.otaliastudios.zoom.ZoomEngine
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_seat_screen_main.*
import kotlinx.android.synthetic.main.cancel_dialog.*
import javax.inject.Inject


@Suppress("DEPRECATION")
class SeatScreenMainActivity : DaggerAppCompatActivity(),
    SeatShowTimesCinemaAdapter.SeatCinemaAdapterListener,
    AdapterCinemaSessionScroll.LocationListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private val seatScreenMainViewModel: SeatScreenMainViewModel by viewModels { viewModelFactory }
    private var binding: ActivitySeatScreenMainBinding? = null
    private var areaCode = ""
    private var cinemaName = ""
    private var seatCat = ""
    private var seatType = ""
    private var ttType = ""
    private var datePosition = ""
    private var datePos = 0
    private var showPos = 0
    private var cinemaPos1 = 0
    private var seatQuantity = 0
    private var seatQt = 0
    private var loader: LoaderDialog? = null
    private var passingValArrayList: ArrayList<ReserveSeatRequest.ReseveSeatVO> = ArrayList()
    private var dateTime = ""
    private var movieId = ""
    private var cinemaID = ""
    private var sessionID = ""
    private var dt = ""
    private var broadcastReceiver: BroadcastReceiver? = null
    private var num = 0
    private var movieTitle = ""
    private var movieImage = ""
    private var movieRating = ""
    private var movieRatingColor = ""
    private var movieCinemaName = ""
    private var movieTimeDate = ""
    private var movieType = ""
    private var cinemaSessionResponse: SeatLayoutResponse.Output? = null
    private lateinit var zllib: com.otaliastudios.zoom.ZoomLayout
    private var counter = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatScreenMainBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        text_seat_types.paintFlags = text_seat_types.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        setCinemasTitleAdapter()

        binding?.zllib?.engine?.addListener(blistering)
        areaCode = intent.getStringExtra("AREA_CODE").toString()
        cinemaName = intent.getStringExtra("CINEMA").toString()
        seatCat = intent.getStringExtra("SEAT_CAT").toString()
        ttType = intent.getStringExtra("TT_TYPE").toString()
        seatType = intent.getStringExtra("SEAT_TYPE").toString()
        datePos = intent.getIntExtra("DATE_POS", 0)
        showPos = intent.getIntExtra("SHOW_POS", 0)
        cinemaPos1 = intent.getIntExtra("CINEMA_POS", 0)
        seatQuantity = intent.getIntExtra("SEAT_POS", 0)

        dateTime = intent.getStringExtra("DateTime").toString()
        movieId = intent.getStringExtra("MovieId").toString()
        cinemaID = intent.getStringExtra("CinemaID").toString()
        sessionID = intent.getStringExtra("SessionID").toString()
        datePosition = intent.getStringExtra("DatePosition").toString()
        dt = intent.getStringExtra("dt").toString()

        if (!preferences.getBoolean(Constant.IS_LOGIN)) {
            val intent = Intent(this, LoginActivity::class.java).putExtra("AREA_CODE", areaCode)
                .putExtra("TT_TYPE", ttType)
                .putExtra("CINEMA", cinemaName)
                .putExtra("SEAT_CAT", seatCat)
                .putExtra("SEAT_TYPE", seatType)
                .putExtra("DATE_POS", datePos)
                .putExtra("SEAT_POS", num)
                .putExtra("DateTime", dateTime)
                .putExtra("MovieId", movieId)
                .putExtra("CinemaID", cinemaID)
                .putExtra("DatePosition", datePosition)
                .putExtra("dt", dt)
                .putExtra("FROM", "seat")
                .putExtra("SessionID", sessionID)
                .putExtra("SHOW_POS", showPos)
                .putExtra("CINEMA_POS", cinemaPos1)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()

        }

        binding?.viewCancel?.setOnClickListener {
            cancelDialog()
        }


        binding?.tvSeatFilmTitle?.show()
        getSeatLayout(SeatLayoutRequest(cinemaID, dateTime, movieId, sessionID))
        broadcastReceiver = MyReceiver()
        broadcastIntent()

    }

    private var blistering: ZoomEngine.Listener = object : ZoomEngine.Listener {
        override fun onIdle(engine: ZoomEngine) {

        }

        override fun onUpdate(engine: ZoomEngine, matrix: Matrix) {
            counter++
            if (counter > 8000) {
                counter = 0
                return
            }
            if (counter % 3 ==5) {
                return
            }


            val calculatedHeight = binding?.seatTable?.height!! / 4
            val calculatedWidth =  binding?.seatTable?.width!! / 4
            val rectOfTbl = HelperUtils.locateViewWithZoom( binding?.seatTable!!, engine.realZoom)
            val rectVisibleArea = HelperUtils.locateView(binding?.zllib!!)

            // this comments are for understanding purpose only
            Log.d("CustomListener", "loc left  ${rectOfTbl.left} loc right ${rectOfTbl.right}")
            Log.d("CustomListener", "loc top  ${rectOfTbl.top} loc bottom ${rectOfTbl.bottom}")
            Log.d(
                "CustomListener",
                " loc zl left ${rectVisibleArea.left}  loc zl right ${rectVisibleArea.right}"
            )
            Log.d(
                "CustomListener",
                " loc zl top ${rectVisibleArea.top}  loc zl bottom ${rectVisibleArea.bottom}"
            )


            var leftDif = 0
            var topDif = 0
            var rightDif = 0
            var bottomDif = 0

            leftDif = if (rectOfTbl.left < rectVisibleArea.left) {
                rectVisibleArea.left - rectOfTbl.left
            } else {
                0
            }

            if (rectOfTbl.top < rectVisibleArea.top) {
                topDif = rectVisibleArea.top - rectOfTbl.top
            } else {
                topDif = 0
            }

            rightDif = if (rectOfTbl.right > rectVisibleArea.right) {
                rectOfTbl.right - rectVisibleArea.right
            } else {
                0
            }

            if (rectOfTbl.bottom > rectVisibleArea.bottom) {
                bottomDif = rectOfTbl.bottom - rectVisibleArea.bottom
            } else {
                bottomDif = 0
            }

            //scale back to original values
            if (engine.realZoom > 1.0) {
                if (leftDif != 0) leftDif = Math.round(leftDif / engine.realZoom)
                if (topDif != 0) topDif = Math.round(topDif / engine.realZoom)
                if (rightDif != 0) rightDif = Math.round(rightDif / engine.realZoom)
                if (bottomDif != 0) bottomDif = Math.round(bottomDif / engine.realZoom)
            }

            // genarate bitmap image of layout with rectangle
//            val bitmap = HelperUtils.getBitmapFromView(
//                binding?.seatTable!!,
//                leftDif, topDif, rightDif, bottomDif
//            )

//            ivPreview.layoutParams.height = calculatedHeight
//            ivPreview.layoutParams.width = calculatedWidth
//            ivGradientBg.layoutParams.height = calculatedHeight
//            ivGradientBg.layoutParams.width = calculatedWidth
//            ivPreview.setImageBitmap(bitmap)
//            ivGradientBg.visibility = View.VISIBLE
//            ivPreview.visibility = View.VISIBLE
            hidePreviewLayoutsAfterSometime()
        }
    }

    /**
     * This method will hide preview layout after specified time
     */
    private fun hidePreviewLayoutsAfterSometime() {
        Handler().postDelayed({
//            ivGradientBg.visibility = View.GONE
//            ivPreview.visibility = View.GONE
        }, 900)
    }
    //GetSeatLayout
    private fun getSeatLayout(request: SeatLayoutRequest) {
        seatScreenMainViewModel.getSeatLayout(request)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {
                                    try {
                                        cinemaSessionResponse = it.data.output
                                        createRows(it.data.output)
                                        setMovieData(it.data.output)

                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession123 ---> ${e.message + e.stackTrace}")
                                        e.printStackTrace()
                                    }

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
    private fun setMovieData(output: SeatLayoutResponse.Output) {
        binding?.SeatnestedScroll?.show()
        binding?.constraintLayout4?.show()

        binding?.viewpager?.adapter =
            CinemaSeatPagerAdapter(this, output.daySessions, binding?.viewpager)

        try {
            if (output.daySessions.isEmpty()) {
                binding?.textOtherShowtimes?.hide()
            } else {
                binding?.textOtherShowtimes?.show()
            }

            binding?.textOtherShowtimes?.setOnClickListener {
                if (binding?.recyclerviewCinemasName?.visibility == View.VISIBLE) {
                    binding?.vwsTilte1?.hide()
                    binding?.tvSeatFilmTitle1?.hide()
                    binding?.tvSeatFilmType1?.hide()
                    binding?.tvSeatFilmUi?.hide()
                    binding?.viewpager?.hide()

                    binding?.recyclerviewCinemasName?.hide()
                } else {
                    binding?.vwsTilte1?.show()
                    binding?.tvSeatFilmTitle1?.show()
                    binding?.tvSeatFilmType1?.show()
                    binding?.tvSeatFilmUi?.show()
                    binding?.viewpager?.hide()
                    movedBottom()
                    binding?.recyclerviewCinemasName?.show()
                }
            }

            binding?.textSeatTypes?.setOnClickListener {
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.cancel_dialog)
                dialog.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                dialog.window!!.setGravity(Gravity.BOTTOM)
                dialog.subtitle.text = getString(R.string.seatTypeReset)
                dialog.negative_btn.text = getString(R.string.yes)
                dialog.txtSureNew.text = getString(R.string.no)
                dialog.txtGoBack.text = getString(R.string.gobackComma)
                dialog.show()

                dialog.consSure?.setOnClickListener {
                    dialog.dismiss()
//                    finish()
                }

                dialog.negative_btn?.setOnClickListener {
                    SEAT_SESSION_CLICK = 1
                    val intent = Intent(this, ShowTimesActivity::class.java)
                    intent
                        .putExtra("NAME", cinemaName)
                        .putExtra("CINEMAID", cinemaID)
                        .putExtra("SESSIONID", sessionID)
                        .putExtra("SHOW_POS", showPos)
                        .putExtra("CINEMA_POS", cinemaPos1)
                    setResult(50, intent)
                    finish()
                }

            }

            movieTitle = output.movie.title
            movieRating = output.movie.rating
            movieRatingColor= output.movie.ratingColor

            binding?.tvSeatFilmTitle?.text = output.movie.title
            binding?.tvSeatFilmTitle1?.text = output.movie.title

            binding?.tvSeatFilmType?.text = output.movie.rating
            binding?.tvSeatFilmType1?.text = output.movie.rating
            val ratingColor=output.movie.ratingColor

            binding?.tvSeatFilmType?.setBackgroundColor(Color.parseColor(ratingColor))
            binding?.tvSeatFilmType1?.setBackgroundColor(Color.parseColor(ratingColor))

            cinemaID = output.daySessions[cinemaPos1].shows[showPos].cinemaId
            movieId = output.movie.id
            sessionID = output.daySessions[cinemaPos1].shows[showPos].sessionId
            binding?.tvCinemaName?.text = output.cinema.name
            movieCinemaName = output.cinema.name
            movieTimeDate =
                "${output.daySessions[cinemaPos1].shows[showPos].showTime} | $datePosition | $dt"
            movieImage = output.movie.mobimgbig
            binding?.tvSeatTimingDate?.text =
                "${output.daySessions[cinemaPos1].shows[showPos].showTime} | $datePosition | $dt"
            movieType =
                "${output.daySessions[cinemaPos1].shows[showPos].experience} | ${output.daySessions[cinemaPos1].shows[showPos].format} | $seatCat"
            binding?.textType?.text =
                "${output.daySessions[cinemaPos1].shows[showPos].experience} | ${output.daySessions[cinemaPos1].shows[showPos].format} | $seatCat"

            setTitleAdapter(output.daySessions)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun movedBottom() {
        binding?.SeatnestedScroll?.post { binding?.SeatnestedScroll?.fullScroll(View.FOCUS_DOWN) }
    }

    private fun setTitleAdapter(daySessions: ArrayList<SeatLayoutResponse.DaySession>) {
        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerviewCinemasName?.layoutManager = LinearLayoutManager(this)
        val adapter = SeatShowTimesCinemaAdapter(this, daySessions, this)
        binding?.recyclerviewCinemasName?.layoutManager = gridLayout
        binding?.recyclerviewCinemasName?.adapter = adapter
    }

    private fun createRows(output: SeatLayoutResponse.Output) {
        binding?.SeatnestedScroll?.show()
        binding?.constraintLayout4?.show()
        try {
            for (row in output.rows) {
                val trow = TableRow(this)
                val lp = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT)
                trow.layoutParams = lp
                trow.gravity = Gravity.CENTER_VERTICAL
                val text1 = TextView(this)
                text1.setTextColor(ContextCompat.getColor(this, R.color.text_color))
                text1.gravity = Gravity.FILL_VERTICAL
                text1.setPadding(5, 5, 5, 5)
                text1.text = row.name
                text1.textSize = 18f

                val lp1 = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                // LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen._30sdp), LinearLayout.LayoutParams.WRAP_CONTENT);
                lp1.setMargins(10, 15, 10, 10)
                text1.layoutParams = lp1
                trow.addView(text1)
                var seatBtn: TextView

                for (seatItem in row.seats) {
                    seatBtn = TextView(this)
                    seatBtn.tag = seatItem
                    val layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                    // LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen._30sdp), LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 10, 10, 10)
                    seatBtn.textSize = 12f
                    seatBtn.layoutParams = layoutParams
                    seatBtn.gravity = Gravity.CENTER_HORIZONTAL
                    seatBtn.setTextColor(Color.BLACK)
                    seatBtn.text = seatItem.seatnumber
                    seatBtn.setBackgroundResource(R.drawable.ic_available)
                    if (areaCode == seatItem.areacode) {
                        seatBtn.show()
                    } else {
                        seatBtn.hide()
                    }
                    if (seatItem.display) {
                        seatBtn.show()
                        when (seatItem.status) {
                            "SOLD" -> {
                                seatBtn.setBackgroundResource(R.drawable.ic_not_available)
                                seatBtn.isEnabled = false
                                seatBtn.isClickable = false
                            }
                            "EMPTY" -> {
                                if (areaCode == seatItem.areacode) {
                                    seatBtn.setBackgroundResource(R.drawable.ic_available)
                                    seatBtn.isEnabled = true
                                    seatBtn.isClickable = true
                                } else {
                                    seatBtn.setBackgroundResource(R.drawable.ic_not_available)
                                    seatBtn.isEnabled = false
                                    seatBtn.isClickable = false
                                }
                            }
                        }
                    } else {
                        seatBtn.invisible()
                    }
                    trow.addView(seatBtn)
                    seatBtn.setOnClickListener {
                        val seatData = it.tag as SeatLayoutResponse.SeatVO
                        val image = it as TextView
                        if (seatData.selected) {
                            image.setBackgroundResource(R.drawable.ic_available)
                            image.setTextColor(getColor(R.color.black))
                            seatData.selected = false
                            passingValArrayList.remove(ReserveSeatRequest.ReseveSeatVO(seatData.passingValue))

                            seatQt -= 1
                        } else {
                            val seatSize = passingValArrayList.size
                            if (seatQuantity == seatSize) {
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    getString(R.string.selectSeatQt) + " " + seatQuantity,
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {
                                    },
                                    negativeClick = {
                                    })
                                dialog.show()
                            } else {
                                image.setBackgroundResource(R.drawable.ic_selected)
                                seatData.selected = true
                                passingValArrayList.add(ReserveSeatRequest.ReseveSeatVO(seatData.passingValue))
                                seatQt += 1
                                image.setTextColor(getColor(R.color.white))
                            }
                        }
                    }
                }

                binding?.seatTable?.addView(trow)

            }
        } catch (e: Exception) {
            println("CheckException--->${e.message}")
            e.printStackTrace()
        }
    }

    private fun cancelDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.cancel_dialog)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.show()

        dialog.consSure?.setOnClickListener {
            finish()
        }

        dialog.negative_btn?.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun setCinemasTitleAdapter() {
        view_proceed.setOnClickListener {
            println("SeatQuantity--->${seatQt},--->${seatQuantity}")
            if (seatQt < 1) {
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
            } else if (seatQt != seatQuantity) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.PleaseSelect) + " $seatQuantity " + getString(R.string.seat),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            } else {
                reserveSeat()
            }
        }
    }

    //GetSeatLayout
    private fun reserveSeat() {
        seatScreenMainViewModel.reserveSeat(
            this,
            ReserveSeatRequest(cinemaID, passingValArrayList, sessionID, movieId, ttType)
        )
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {
                                    try {
                                        if (it.data.output.fc) {
                                            startActivity(
                                                Intent(
                                                    this@SeatScreenMainActivity,
                                                    FoodActivity::class.java
                                                ).putExtra("CINEMA_ID", cinemaID)
                                                    .putExtra("SESSION_ID", sessionID)
                                                    .putExtra("BOOKING", it.data.output.booktype)
                                                    .putExtra("PRICE", it.data.output.totalPrice)
                                                    .putExtra("TRANS_ID", it.data.output.transid)
                                                    .putExtra("type", "0")
                                                    .putExtra("movieTitle", movieTitle)
                                                    .putExtra("movieRating", movieRating)
                                                    .putExtra("movieRatingColor", movieRatingColor)
                                                    .putExtra("movieCinemaName", movieCinemaName)
                                                    .putExtra("movieTimeDate", movieTimeDate)
                                                    .putExtra("movieImage", movieImage)
                                                    .putExtra(
                                                        "movieType", movieType

                                                    )

                                            )
                                            finish()
                                        } else {
                                            startActivity(
                                                Intent(
                                                    this@SeatScreenMainActivity,
                                                    SummeryActivity::class.java
                                                ).putExtra("CINEMA_ID", cinemaID)
                                                    .putExtra("SESSION_ID", sessionID)
                                                    .putExtra("TRANS_ID", it.data.output.transid)
                                            )
                                            finish()
                                        }
                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }

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

    override fun onShowClicked(
        show: CSessionResponse.Output.DaySession.Show,
        name: String,
        position: Int,
        cinemaPos: Int,
        movieCinemaId: String
    ) {

        println("Cinema--->${position}")
    }

    override fun onSeatShowClicked(
        show: SeatLayoutResponse.DaySession.Show,
        name: String,
        position: Int,
        cinemaPos: Int
    ) {
        cinemaPos1 = cinemaPos
        cinemaID = show.cinemaId
        sessionID = show.sessionId
        showPos = position

        SEAT_SESSION_CLICK = 1
        val intent = Intent(this, ShowTimesActivity::class.java)
        intent.putExtra("NAME", name).putExtra("CINEMAID", show.cinemaId)
            .putExtra("SESSIONID", show.sessionId).putExtra("SHOW_POS", position)
            .putExtra("CINEMA_POS", cinemaPos)
        setResult(50, intent)
        finish()
    }

    @SuppressLint("CutPasteId")

    private fun broadcastIntent() {
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

}