package com.cinescape1.ui.main.views.seatLayout

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
import com.cinescape1.ui.main.views.seatLayout.viewModel.SeatScreenMainViewModel
import com.cinescape1.ui.main.views.details.ShowTimesActivity
import com.cinescape1.ui.main.views.adapters.CinemaSeatPagerAdapter
import com.cinescape1.ui.main.views.adapters.cinemaSessionAdapters.AdapterCinemaSessionScroll
import com.cinescape1.ui.main.views.adapters.seatlayout.SeatShowTimesCinemaAdapter
import com.cinescape1.ui.main.views.food.FoodActivity
import com.cinescape1.ui.main.views.login.LoginActivity
import com.cinescape1.ui.main.views.summery.SummeryActivity
import com.cinescape1.utils.*
import com.cinescape1.utils.Constant.Companion.SEAT_SESSION_CLICK
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_seat_screen_main.*
import kotlinx.android.synthetic.main.cancel_dialog.*
import javax.inject.Inject


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
    private var DatePosition = ""
    private var date_pos = 0
    private var show_pos = 0
    private var cinema_pos = 0
    private var seatQuanitity = 0
    private var seatQt = 0
    private var loader: LoaderDialog? = null
    private var passingValArrayList: ArrayList<ReserveSeatRequest.ReseveSeatVO> = ArrayList()
    private var dateTime = ""
    private var MovieId = ""
    private var CinemaID = ""
    private var SessionID = ""
    private var dt = ""
    private var broadcastReceiver: BroadcastReceiver? = null
    private var mAlertDialog: AlertDialog? = null
    private var num = 0
    private var cinemaSessionResponse: SeatLayoutResponse.Output? = null
    private var seatAbility: Int = 0
    private var languageCheck: String = "en"
    private var totalPriceResponse: Double = 0.0
    private var selectSeatClick: Int = 0
    private var showDataCSessionResponse: CSessionResponse.Output? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatScreenMainBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        text_seat_types.paintFlags = text_seat_types.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        setCinemasTitleAdapter(view!!)

        areaCode = intent.getStringExtra("AREA_CODE").toString()
        cinemaName = intent.getStringExtra("CINEMA").toString()
        seatCat = intent.getStringExtra("SEAT_CAT").toString()
        ttType = intent.getStringExtra("TT_TYPE").toString()
        seatType = intent.getStringExtra("SEAT_TYPE").toString()
        date_pos = intent.getIntExtra("DATE_POS", 0)
        show_pos = intent.getIntExtra("SHOW_POS", 0)
        cinema_pos = intent.getIntExtra("CINEMA_POS", 0)
        seatQuanitity = intent.getIntExtra("SEAT_POS", 0)

        dateTime = intent.getStringExtra("DateTime").toString()
        MovieId = intent.getStringExtra("MovieId").toString()
        CinemaID = intent.getStringExtra("CinemaID").toString()
        SessionID = intent.getStringExtra("SessionID").toString()
        DatePosition = intent.getStringExtra("DatePosition").toString()
        dt = intent.getStringExtra("dt").toString()

        if (!preferences.getBoolean(Constant.IS_LOGIN)) {
            val intent = Intent(this, LoginActivity::class.java).putExtra("AREA_CODE", areaCode)
                .putExtra("TT_TYPE", ttType)
                .putExtra("CINEMA", cinemaName)
                .putExtra("SEAT_CAT", seatCat)
                .putExtra("SEAT_TYPE", seatType)
                .putExtra("DATE_POS", date_pos)
                .putExtra("SEAT_POS", num)
                .putExtra("DateTime", dateTime)
                .putExtra("MovieId", MovieId)
                .putExtra("CinemaID", CinemaID)
                .putExtra("DatePosition", DatePosition)
                .putExtra("dt", dt)
                .putExtra("FROM", "seat")
                .putExtra("SessionID", SessionID)
                .putExtra("SHOW_POS", show_pos)
                .putExtra("CINEMA_POS", cinema_pos)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()

        }

        binding?.viewCancel?.setOnClickListener {
            cancelDialog()
        }

        binding?.zoomLinearLayout?.setOnTouchListener { v, event ->
            binding?.zoomLinearLayout?.init(this@SeatScreenMainActivity)
            false
        }


        binding?.tvSeatFilmTitle?.show()
        getSeatLayout(SeatLayoutRequest(CinemaID, dateTime, MovieId, SessionID))
        broadcastReceiver = MyReceiver()
        broadcastIntent()

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
            if (output.daySessions.isNullOrEmpty()) {
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
                        .putExtra("CINEMAID", CinemaID)
                        .putExtra("SESSIONID", SessionID)
                        .putExtra("SHOW_POS", show_pos)
                        .putExtra("CINEMA_POS", cinema_pos)
                    setResult(50, intent)
                    finish()
                }

            }

            binding?.tvSeatFilmTitle?.text = output.movie.title
            binding?.tvSeatFilmTitle1?.text = output.movie.title

            binding?.tvSeatFilmType?.text = output.movie.rating
            binding?.tvSeatFilmType1?.text = output.movie.rating
            when (output.movie.rating) {
                "PG" -> {
                    binding?.tvSeatFilmType?.setBackgroundResource(R.color.grey)
                    binding?.tvSeatFilmType1?.setBackgroundResource(R.color.grey)

                }
                "G" -> {
                    binding?.tvSeatFilmType?.setBackgroundResource(R.color.green)
                    binding?.tvSeatFilmType1?.setBackgroundResource(R.color.green)

                }
                "18+" -> {
                    binding?.tvSeatFilmType?.setBackgroundResource(R.color.red)
                    binding?.tvSeatFilmType1?.setBackgroundResource(R.color.red)

                }
                "13+" -> {
                    binding?.tvSeatFilmType?.setBackgroundResource(R.color.yellow)
                    binding?.tvSeatFilmType1?.setBackgroundResource(R.color.yellow)

                }
                "E" -> {
                    binding?.tvSeatFilmType?.setBackgroundResource(R.color.wowOrange)
                    binding?.tvSeatFilmType1?.setBackgroundResource(R.color.wowOrange)

                }
                "T" -> {
                    binding?.tvSeatFilmType?.setBackgroundResource(R.color.tabIndicater)
                    binding?.tvSeatFilmType1?.setBackgroundResource(R.color.tabIndicater)

                }
                else -> {
                    binding?.tvSeatFilmType?.setBackgroundResource(R.color.blue)
                    binding?.tvSeatFilmType1?.setBackgroundResource(R.color.blue)

                }
            }

            CinemaID = output.daySessions[cinema_pos].shows[show_pos].cinemaId
            MovieId = output.movie.id
            SessionID = output.daySessions[cinema_pos].shows[show_pos].sessionId
            binding?.tvCinemaName?.text = output.cinema.name
            binding?.tvSeatTimingDate?.text =
                "${output.daySessions[cinema_pos].shows[show_pos].showTime} | $DatePosition | ${dt}"
            binding?.textType?.text =  "${output.daySessions[cinema_pos].shows[show_pos].experience} | ${output.daySessions[cinema_pos].shows[show_pos].format} | ${seatCat}"

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
            val btnFont = resources.getDimension(R.dimen.size12).toInt()
            for (row in output.rows) {
                println("CheckRows--->${row}")
                val trow = TableRow(this)
                val lp = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT)
                trow.layoutParams = lp
                trow.gravity = Gravity.CENTER_VERTICAL
                val mtext1 = TextView(this)
                mtext1.setTextColor(ContextCompat.getColor(this, R.color.text_color))
                mtext1.gravity = Gravity.FILL_VERTICAL
                mtext1.setPadding(5, 5, 5, 5)
                mtext1.text = row.name
                mtext1.textSize = 18f

                val lp1 = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                // LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen._30sdp), LinearLayout.LayoutParams.WRAP_CONTENT);
                lp1.setMargins(10, 15, 10, 10)
                mtext1.layoutParams = lp1
                trow.addView(mtext1)
                var seatbtn: TextView

                for (seatitem in row.seats) {
                    seatbtn = TextView(this)
                    seatbtn.tag = seatitem
                    val lp1 = TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                    // LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen._30sdp), LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp1.setMargins(10, 10, 10, 10)
                    seatbtn.textSize = 12f
                    seatbtn.layoutParams = lp1
                    seatbtn.gravity = Gravity.CENTER_HORIZONTAL
                    seatbtn.setTextColor(Color.BLACK)
                    seatbtn.text = seatitem.seatnumber
                    seatbtn.setBackgroundResource(R.drawable.ic_available)
                    if (areaCode == seatitem.areacode) {
                        seatbtn.show()
                    } else {
                        seatbtn.hide()
                    }
                    if (seatitem.display) {
                        seatbtn.show()
                        when (seatitem.status) {
                            "SOLD" -> {
                                seatbtn.setBackgroundResource(R.drawable.ic_not_available)
                                seatbtn.isEnabled = false
                                seatbtn.isClickable = false
                            }
                            "EMPTY" -> {
                                if (areaCode == seatitem.areacode) {
                                    seatbtn.setBackgroundResource(R.drawable.ic_available)
                                    seatbtn.isEnabled = true
                                    seatbtn.isClickable = true
                                } else {
                                    seatbtn.setBackgroundResource(R.drawable.ic_not_available)
                                    seatbtn.isEnabled = false
                                    seatbtn.isClickable = false
                                }
                            }
                        }
                    } else {
                        seatbtn.invisible()
                    }
                    trow.addView(seatbtn)
                    seatbtn.setOnClickListener {
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
                            if (seatQuanitity == seatSize) {
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    getString(R.string.selectSeatQt) + " " + seatQuanitity,
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
//                val animZoomOut = AnimationUtils.loadAnimation(
//                    this,
//                    R.anim.zoom_out
//                )
//                binding?.seatTable?.startAnimation(animZoomOut)

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

    private fun setCinemasTitleAdapter(view: View) {
        view_proceed.setOnClickListener {
            println("SeatQuantity--->${seatQt},--->${seatQuanitity}")
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
            } else if (seatQt != seatQuanitity) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.PleaseSelect) + " ${seatQuanitity} " + getString(R.string.seat),
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
            ReserveSeatRequest(CinemaID, passingValArrayList, SessionID, MovieId, ttType)
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
                                                ).putExtra("CINEMA_ID", CinemaID)
                                                    .putExtra("SESSION_ID", SessionID)
                                                    .putExtra("BOOKING", it.data.output.booktype)
                                                    .putExtra("PRICE", it.data.output.totalPrice)
                                                    .putExtra("TRANS_ID", it.data.output.transid)
                                                    .putExtra("type", "0")

                                            )
                                            finish()
                                        } else {
                                            startActivity(
                                                Intent(
                                                    this@SeatScreenMainActivity,
                                                    SummeryActivity::class.java
                                                ).putExtra("CINEMA_ID", CinemaID)
                                                    .putExtra("SESSION_ID", SessionID)
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
        cinema_pos = cinemaPos
        CinemaID = show.cinemaId
        SessionID = show.sessionId
        show_pos = position

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