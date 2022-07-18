package com.cinescape1.ui.main.views

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.CinemaSessionRequest
import com.cinescape1.data.models.responseModel.CinemaSessionResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityCinemaSessionBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.viewModels.CinemaSessionViewModel
import com.cinescape1.utils.Constant
import com.cinescape1.utils.MyReceiver
import com.cinescape1.utils.Status
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_cinema_session.*
import javax.inject.Inject


class CinemaSessionActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private val cinemaSessionViewModel: CinemaSessionViewModel by viewModels { viewModelFactory }
    private var binding: ActivityCinemaSessionBinding? = null
    private var dateTime = ""
    var movieID = ""
    var loader: LoaderDialog? = null
    private var broadcastReceiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCinemaSessionBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        movieID = intent.getStringExtra(Constant.IntentKey.MOVIE_ID)!!

//        getCinemasSession()
        setShowTimesScroll()
        broadcastIntent()
        broadcastReceiver = MyReceiver()
        broadcastIntent()
    }

    private fun broadcastIntent() {
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }

    private fun getCinemasSession() {
        getCinemaSession(
            CinemaSessionRequest(
                dateTime,
                movieID
            )
        )
    }

//    override fun onResume() {
//        super.onResume()
//        if (Constant.ON_BACK_FOOD==1){
//            mAlertDialog?.dismiss()
//        }
//    }

    private fun getCinemaSession(json: CinemaSessionRequest) {
        cinemaSessionViewModel.getMsessionsNew(this, json)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {
                                    try {
                                        if (json.dated == "")
                                            setDayDateAdapter(it.data.output.days)

                                        updateUiCinemaSession(it.data.output)
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

    private fun updateUiCinemaSession(output: CinemaSessionResponse.Output) {
        println("updateUiCinemaSession ----> ${output}")
        text_film_house_name.text = output.movie.title
        Glide.with(this).load(output.movie.trailerUrl).placeholder(R.drawable.movie_default)
            .into(image_show)
    }


//    private fun dateTimeAdapter(){
//                    val gridLayout = GridLayoutManager(this@ShowTimesActivity, 1, GridLayoutManager.HORIZONTAL, false)
//            recyclerDayDate.layoutManager = LinearLayoutManager(this)
//            val adapter = AdapterDayDate(this, daydateList!!, this)
//            recyclerDayDate.layoutManager = gridLayout
//            recyclerDayDate.adapter = adapter
//            adapter.loadNewData(daydateList!!)
//    }

    private fun setShowTimesScroll() {
//        val recyclerShowtimeScroll = findViewById<View>(R.id.recycler_showtime_scroll) as RecyclerView
//        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
//        recyclerShowtimeScroll.layoutManager = LinearLayoutManager(this)
//        val adapter = AdapterCinemaSessionScroll(this, showtimeScrolList!!)
//        recyclerShowtimeScroll.layoutManager = gridLayout
//        recyclerShowtimeScroll.adapter = adapter
//        adapter.loadNewData(showtimeScrolList!!)
    }


    private fun setDayDateAdapter(days: ArrayList<CinemaSessionResponse.Days>) {
//        val recyclerviewDayDate = findViewById<View>(R.id.recyclerview_day_date) as RecyclerView
//        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
//        recyclerviewDayDate.layoutManager = LinearLayoutManager(this)
//        val adapter = AdapterDayDate(this, days, this)
//        recyclerviewDayDate.layoutManager = gridLayout
//        recyclerviewDayDate.adapter = adapter
    }


//    override fun onDateClick(city: CinemaSessionResponse.Days, view: View) {
//        val recyclerviewDayDate = findViewById<View>(R.id.recyclerview_day_date) as RecyclerView
//        println("DATE CLICK ${city.dt}")
//        focusOnView(view, recyclerviewDayDate)
//        dateTime = city.dt
//    }


}