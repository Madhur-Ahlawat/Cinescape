package com.cinescape1.ui.main.views.home.fragments.movie

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import androidx.viewpager.widget.ViewPager
import com.cinescape1.R
import com.cinescape1.data.models.MovieTypeModel
import com.cinescape1.data.models.requestModel.MovieRequest
import com.cinescape1.data.models.responseModel.FilterModel
import com.cinescape1.data.models.responseModel.FilterTypeModel
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.FragmentMoviesBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.home.viewModel.HomeViewModel
import com.cinescape1.ui.main.views.adapters.MovieTypeAdapter
import com.cinescape1.ui.main.views.adapters.filterAdapter.AdapterFilterTitle
import com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter.AdapterComingSoon
import com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter.AdapterFilterCategory
import com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter.AdapterNowShowing
import com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter.AdvanceBookingAdapter
import com.cinescape1.utils.*
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.search_ui.*
import javax.inject.Inject


class MoviesFragment(val type: Int) : DaggerFragment(),
    AdapterFilterCategory.RecycleViewItemClickListener,
    MovieTypeAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }
    private var binding: FragmentMoviesBinding? = null
    private val dataList: ArrayList<FilterModel> = ArrayList()
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var filter: ImageView
    private var loader: LoaderDialog? = null
    private var isChecked = false
    private var moviesResponse: MoviesResponse? = null

    private var comingSoonFilter = ArrayList<MoviesResponse.Comingsoon>()
    private var broadcastReceiver: BroadcastReceiver? = null

    //Filter Params
    var filterDataList: ArrayList<FilterTypeModel> = ArrayList()
    var exp_data = "ALL"
    var cinema_data = "ALL"
    var timing_data = "ALL"
    var rating_data = "ALL"
    var genre_data = "ALL"
    var language_data = "ALL"

    private val list = ArrayList<MovieTypeModel>()

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(requireActivity(), "ar")
                println("getLocalLanguage--->${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}")
            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(requireActivity(), "en")
            }
            else -> {
                LocaleHelper.setLocale(requireActivity(), "en")
            }
        }
        return view!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.viewpager)
        tabLayout = view.findViewById(R.id.tab_layout)
        filter = view.findViewById(R.id.filter)
        tabLayout.addTab(tabLayout.newTab().setText("NOW SHOWING"))
        tabLayout.addTab(tabLayout.newTab().setText("COMING SOON"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        list.add(MovieTypeModel(getString(R.string.nowSowing)))
        list.add(MovieTypeModel(getString(R.string.commingSoon)))
        list.add(MovieTypeModel(getString(R.string.advanceBooking)))


        val gridLayout = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerType?.layoutManager = LinearLayoutManager(context)
        val adapter = MovieTypeAdapter(list, requireActivity(), this)
        binding?.recyclerType?.layoutManager = gridLayout
        binding?.recyclerType?.adapter = adapter
        println("MovieException----${Constant.SEE_ALL_TYPE}")

        if (Constant.SEE_ALL_TYPE == 0) {
            binding?.recyclerType?.scrollToPosition(0)
        } else {
            binding?.recyclerType?.scrollToPosition(1)
        }

        //AppBar Hide
// Show status bar
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        requireActivity().window.statusBarColor = Color.BLACK

        binding?.searchMovie?.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding?.tabLayout?.hide()
                binding?.filter?.hide()
                val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                params.addRule(RelativeLayout.ALIGN_PARENT_END)
                binding?.searchMovie?.layoutParams = params
            } else {
                binding?.tabLayout?.show()
                binding?.filter?.show()
                println("called this----")
                val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                params.addRule(RelativeLayout.ALIGN_PARENT_END)
                binding?.searchMovie?.layoutParams = params
            }
        }
        movesData(
            MovieRequest(
                cinema_data,
                exp_data,
                genre_data,
                language_data,
                rating_data,
                timing_data
            )
        )
        movedNext()
        broadcastReceiver = MyReceiver()
        broadcastIntent()
    }

    private fun broadcastIntent() {
        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun movedNext() {
        //filter
        imageView33.setOnClickListener {
            isChecked
            imageView33.setImageResource(R.drawable.ic_icon_awesome_filter)
            filterNowShowingDialog(moviesResponse?.output!!)
        }

        movieSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMovieUi.hide()
                searchUi.hide()
                Constant().hideKeyboard(requireActivity())
                imageView36.hide()
                return@OnEditorActionListener true
            }
            false
        })

        binding?.fragmentMovie?.setOnClickListener {
            searchMovieUi.hide()
            searchUi.hide()
            Constant().hideKeyboard(requireActivity())
            imageView36.hide()

        }

        movieSearch.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (movieSearch.compoundDrawables[2] != null) {
                    if (event.x >= movieSearch.right - movieSearch.left - movieSearch.compoundDrawables[2].bounds.width()
                    ) {
                        movieSearch.text.clear()
                        searchMovieUi.hide()
                        searchUi.hide()
                        imageView36.hide()
                        Constant().hideKeyboard(requireActivity())
                    }
                }
            }
            false
        }
    }

    private fun movesData(movieRequest: MovieRequest) {
        homeViewModel.getMoviesData(movieRequest)
            .observe(requireActivity()) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            if (Constant.SUCCESS_CODE == it.data?.data?.code && Constant.status == it.data.data.result) {
                                try {
                                    binding?.recyclerType?.show()
                                    binding?.filterUi?.show()
                                    moviesResponse = it.data.data
                                    comingSoonFilter = it.data.data.output.comingsoon
                                    if (Constant.SEE_ALL_TYPE == 0) {
                                        nowSowing(it.data.data.output.nowshowing)
                                    } else {
                                        comingSoon(it.data.data.output.comingsoon)
                                    }
                                } catch (e: Exception) {
                                    println("MovieException----${e.message}")
                                }
                            } else {
                                val dialog = OptionDialog(requireActivity(),
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    it.data?.data?.msg!!,
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {

                                    },
                                    negativeClick = {
                                    })
                                dialog.show()
                            }
                        }
                        Status.ERROR -> {
                            loader?.dismiss()
                            val dialog = OptionDialog(requireActivity(),
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
                            if (isAdded) {
                                loader = LoaderDialog(R.string.pleasewait)
                                loader?.show(requireActivity().supportFragmentManager, null)
                            }
                        }
                    }
                }
            }
    }

    private fun advanceBooking(advanceBooking: List<MoviesResponse.AdvanceBooking>) {
        if (advanceBooking.isNullOrEmpty()) {
            binding?.noData?.show()
        } else {
            binding?.noData?.hide()
            val gridLayout =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            val adapter = AdvanceBookingAdapter(advanceBooking, requireActivity())
            binding?.fragmentMovie?.layoutManager = gridLayout
            binding?.fragmentMovie?.adapter = adapter
        }
    }

    private fun comingSoon(comingsoon: ArrayList<MoviesResponse.Comingsoon>) {
        val gridLayout = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        val adapter = AdapterComingSoon(comingsoon, requireActivity())
        binding?.fragmentMovie?.layoutManager = gridLayout
        binding?.fragmentMovie?.adapter = adapter

    }

    private fun nowSowing(nowShowing: ArrayList<MoviesResponse.Nowshowing>) {
        val gridLayout =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding?.fragmentMovie?.setHasFixedSize(true)
        var comingSoonList: ArrayList<MoviesResponse.Nowshowing>
        if (nowShowing.size > 0) {
            binding?.noData?.hide()
            binding?.movieLayout?.show()
            comingSoonList = nowShowing
            val adapter = AdapterNowShowing(nowShowing, requireActivity())
            binding?.fragmentMovie?.layoutManager = gridLayout
            binding?.fragmentMovie?.adapter = adapter

            movieSearch.addTextChangedListener(object : TextWatcher {
                @SuppressLint("NotifyDataSetChanged")
                override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                    comingSoonList = ArrayList()
                    comingSoonList = if (cs == "") {
                        nowShowing
                    } else {
                        ArrayList()
                    }
                    for (i in 0 until nowShowing.size) {
                        try {
                            if (nowShowing[i].title.lowercase()
                                    .contains(cs.toString().lowercase())
                            ) {
                                comingSoonList.add(nowShowing[i])
                            }
                        } catch (e: Exception) {
                            println("SearchMsg--->${e.message}")
                        }
                    }
                    println("comingSoonSize--->${comingSoonList.size}")
                    adapter.updateList(comingSoonList)
                }

                override fun beforeTextChanged(
                    arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int
                ) {
                    // TODO Auto-generated method stub
                }

                override fun afterTextChanged(arg0: Editable) {
                    // TODO Auto-generated method stub
                }
            })
        } else {
            val adapter = AdapterNowShowing(nowShowing, requireActivity())
            binding?.fragmentMovie?.layoutManager = gridLayout
            binding?.fragmentMovie?.adapter = adapter
            binding?.noData?.show()
            toast("No movie available!")
        }
    }

    private fun filterNowShowingDialog(output: MoviesResponse.Output) {
        dataList.clear()
        val mDialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.filter_alert_page_dailog, null)
        val mBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogFilter)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()

//Outside Clickable  False
        mAlertDialog.setCancelable(true)
        mAlertDialog.setCanceledOnTouchOutside(true)
        mAlertDialog.show()
        val selectedList = arrayListOf<String>()
        val selectedList1 = arrayListOf<String>()
        val selectedList2 = arrayListOf<String>()
        val selectedList3 = arrayListOf<String>()
        val selectedList4 = arrayListOf<String>()
        val selectedList5 = arrayListOf<String>()
        val selectedCinemaList = arrayListOf<MoviesResponse.Cinema>()
        val selectedMovieList = arrayListOf<MoviesResponse.MovieTimings>()

        if (output.cinemas?.size!! > 0) {
            dataList.add(
                FilterModel(
                    getString(R.string.location),
                    2,
                    output.experiences,
                    selectedList4,
                    output.cinemas,
                    null,
                    null,
                    selectedCinemaList
                )
            )
        }
        if (output.experiences.size > 0) {
            dataList.add(
                FilterModel(
                    getString(R.string.experience),
                    1,
                    output.experiences,
                    selectedList,
                    null,
                    null,
                    null,
                    null
                )
            )
        }
        if (output.movieTimings.size > 0) {
            dataList.add(
                FilterModel(
                    getString(R.string.time),
                    3,
                    output.experiences,
                    selectedList5,
                    null,
                    output.movieTimings,
                    selectedMovieList,
                    null
                )
            )
        }
        if (output.ratings.size > 0) {
            dataList.add(
                FilterModel(
                    getString(R.string.rating),
                    4,
                    output.ratings,
                    selectedList1,
                    null,
                    null,
                    null,
                    null
                )
            )
        }
        if (output.genreList.size > 0) {
            dataList.add(
                FilterModel(
                    getString(R.string.genre),
                    5,
                    output.genreList,
                    selectedList2,
                    null,
                    null,
                    null,
                    null
                )
            )
        }

        if (output.alllanguages.size > 0) {
            dataList.add(
                FilterModel(
                    getString(R.string.languange),
                    6,
                    output.nslanguages,
                    selectedList3,
                    null,
                    null,
                    null,
                    null
                )
            )
        }

        val recyclerviewExperience =
            mDialogView.findViewById<View>(R.id.recyclerview_filter_title) as RecyclerView
        val gridLayout = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        recyclerviewExperience.layoutManager = LinearLayoutManager(context)
        val adapter = AdapterFilterTitle(requireActivity(), dataList, filterDataList)
        recyclerviewExperience.layoutManager = gridLayout
        recyclerviewExperience.adapter = adapter

        val textViewDone = mDialogView.findViewById<View>(R.id.textView_done) as CardView
        val textReset = mDialogView.findViewById<View>(R.id.text_reset) as ConstraintLayout

        textReset.setOnClickListener {
            dataList.clear()
            binding?.recyclerviewCategory?.hide()
            mAlertDialog.dismiss()
        }

        textViewDone.setOnClickListener {
            isChecked = false
            if (dataList.size > 0) {
                imageView33.setImageResource(R.drawable.ic_icon_awesome_filter)
                binding?.recyclerviewCategory?.hide()
                updateFilterData(dataList, "")
                mAlertDialog.dismiss()
            }
        }
    }

    private fun updateFilterData(dataList: ArrayList<FilterModel>, crossItem: String) {
        filterDataList = getFinalFilterList(dataList, crossItem)
        movesData(
            MovieRequest(
                cinema_data,
                exp_data,
                genre_data,
                language_data,
                rating_data,
                timing_data
            )
        )
        if (filterDataList.size > 0) {
            binding?.recyclerviewCategory?.show()
            updateFilterView(filterDataList)
        }
    }

    private fun updateFilterView(filterDataList: ArrayList<FilterTypeModel>) {
        val gridLayout = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        val adapter = AdapterFilterCategory(requireActivity(), getList(filterDataList), this)
        binding?.recyclerviewCategory?.layoutManager = gridLayout
        binding?.recyclerviewCategory?.adapter = adapter
    }

    private fun getList(filterDataList: ArrayList<FilterTypeModel>): ArrayList<String> {
        val list = ArrayList<String>()
        for (data in filterDataList) {
            list.addAll(data.selectedList)
        }

        return list
    }

    private fun getFinalFilterList(
        dataList: ArrayList<FilterModel>,
        crossItem: String
    ): ArrayList<FilterTypeModel> {
        val list = ArrayList<FilterTypeModel>()
        val list1 = ArrayList<String>()
        val list11 = ArrayList<String>()
        val list2 = ArrayList<String>()
        val list22 = ArrayList<String>()
        for (data in dataList) {
            when (data.type) {
                1 -> {
                    if (data.selectedList.contains(crossItem.split("-")[0]))
                        data.selectedList.remove(crossItem.split("-")[0])
                    list.add(FilterTypeModel(1, data.selectedList))
                    exp_data = if (data.selectedList.size == 0)
                        "ALL"
                    else TextUtils.join(",", data.selectedList)
                }
                2 -> {
                    if (data.selectedList.contains(crossItem))
                        data.selectedList.remove(crossItem)
                    for (item in data.selectedList) {
                        println("hhhhhhh---$item")
                        list1.add(item.split("-")[1])
                        list11.add(item)
                    }
                    list.add(FilterTypeModel(2, list11))
                    cinema_data = if (list1.size == 0)
                        "ALL"
                    else TextUtils.join(",", list1)
                }
                3 -> {
                    if (data.selectedList.contains(crossItem))
                        data.selectedList.remove(crossItem)
                    for (item in data.selectedList) {
                        list2.add(item.split("-")[1])
                        list22.add(item)
                    }
                    list.add(FilterTypeModel(3, list22))
                    timing_data = if (list2.size == 0)
                        "ALL"
                    else TextUtils.join(",", list2)
                }
                4 -> {
                    if (data.selectedList.contains(crossItem.split("-")[0]))
                        data.selectedList.remove(crossItem.split("-")[0])
                    list.add(FilterTypeModel(4, data.selectedList))
                    rating_data = if (data.selectedList.size == 0)
                        "ALL"
                    else TextUtils.join(",", data.selectedList)
                }
                5 -> {
                    if (data.selectedList.contains(crossItem.split("-")[0]))
                        data.selectedList.remove(crossItem.split("-")[0])
                    list.add(FilterTypeModel(5, data.selectedList))
                    genre_data = if (data.selectedList.size == 0)
                        "ALL"
                    else TextUtils.join(",", data.selectedList)
                }
                6 -> {
                    if (data.selectedList.contains(crossItem.split("-")[0]))
                        data.selectedList.remove(crossItem.split("-")[0])
                    list.add(FilterTypeModel(6, data.selectedList))
                    language_data = if (data.selectedList.size == 0)
                        "ALL"
                    else TextUtils.join(",", data.selectedList)
                }
            }
        }

        return list
    }

    override fun onCrossClick(item: String) {
        updateFilterData(dataList, item)
    }

    private fun filterCommingSoonDialog(output: MoviesResponse.Output) {
        dataList.clear()
        val mDialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.filter_alert_page_dailog, null)
        val mBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogFilter)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()

//Outside Clickable  False
        mAlertDialog.setCancelable(true)
        mAlertDialog.setCanceledOnTouchOutside(true)
        mAlertDialog.show()
        val selectedList = arrayListOf<String>()
        val selectedList1 = arrayListOf<String>()
        val selectedList2 = arrayListOf<String>()
        val selectedList3 = arrayListOf<String>()
        val selectedList4 = arrayListOf<String>()
        val selectedList5 = arrayListOf<String>()
        val selectedCinemaList = arrayListOf<MoviesResponse.Cinema>()
        val selectedMovieList = arrayListOf<MoviesResponse.MovieTimings>()

//        if (output.cinemas?.size!! > 0) {
//            dataList.add(
//                FilterModel(
//                    getString(R.string.location),
//                    2,
//                    output.experiences,
//                    selectedList4,
//                    output.cinemas,
//                    null,
//                    null,
//                    selectedCinemaList
//                )
//            )
//        }
//        if (output.experiences.size > 0) {
//            dataList.add(
//                FilterModel(
//                    getString(R.string.experience),
//                    1,
//                    output.experiences,
//                    selectedList,
//                    null,
//                    null,
//                    null,
//                    null
//                )
//            )
//        }
//        if (output.movieTimings.size > 0) {
//            dataList.add(
//                FilterModel(
//                    getString(R.string.time),
//                    3,
//                    output.experiences,
//                    selectedList5,
//                    null,
//                    output.movieTimings,
//                    selectedMovieList,
//                    null
//                )
//            )
//        }
//        if (output.ratings.size > 0) {
//            dataList.add(
//                FilterModel(
//                    getString(R.string.rating),
//                    4,
//                    output.ratings,
//                    selectedList1,
//                    null,
//                    null,
//                    null,
//                    null
//                )
//            )
//        }
        if (output.genreList.size > 0) {
            dataList.add(
                FilterModel(
                    getString(R.string.genre),
                    5,
                    output.genreList,
                    selectedList2,
                    null,
                    null,
                    null,
                    null
                )
            )
        }

        if (output.alllanguages.size > 0) {
            dataList.add(
                FilterModel(
                    getString(R.string.languange),
                    6,
                    output.cslanguages,
                    selectedList3,
                    null,
                    null,
                    null,
                    null
                )
            )
        }

        val recyclerviewExperience =
            mDialogView.findViewById<View>(R.id.recyclerview_filter_title) as RecyclerView
        val gridLayout = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        recyclerviewExperience.layoutManager = LinearLayoutManager(context)
        val adapter = AdapterFilterTitle(requireActivity(), dataList, filterDataList)
        recyclerviewExperience.layoutManager = gridLayout
        recyclerviewExperience.adapter = adapter

        val textViewDone = mDialogView.findViewById<View>(R.id.textView_done) as CardView
        val textReset = mDialogView.findViewById<View>(R.id.text_reset) as ConstraintLayout

        textReset.setOnClickListener {
            dataList.clear()
            binding?.recyclerviewCategory?.hide()
            mAlertDialog.dismiss()
        }

        textViewDone.setOnClickListener {
            isChecked = false
            if (dataList.size > 0) {
                imageView33.setImageResource(R.drawable.ic_icon_awesome_filter)
                binding?.recyclerviewCategory?.hide()
                updateFilterData(dataList, "")
                mAlertDialog.dismiss()
            }
        }
    }

    override fun onMovieTypeClick(position: Int) {
        if (isAdded) {
            val smoothScroller: SmoothScroller =
                object : LinearSmoothScroller(this.requireActivity()) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
            smoothScroller.targetPosition = position
            binding?.recyclerType?.layoutManager?.startSmoothScroll(smoothScroller)

        }

        when (position) {
            0 -> {
                try {
                    binding?.filterUi?.show()
                    //filter
                    imageView33.setOnClickListener {
                        isChecked
                        imageView33.setImageResource(R.drawable.ic_icon_awesome_filter)
                        filterNowShowingDialog(moviesResponse?.output!!)
                    }

                    nowSowing(moviesResponse?.output?.nowshowing!!)
                } catch (e: Exception) {
                    println("exception--->${e.message}")
                }
            }
            1 -> {
                try {
                    binding?.filterUi?.show()
                    //filter
                    imageView33.setOnClickListener {
                        isChecked
                        imageView33.setImageResource(R.drawable.ic_icon_awesome_filter)
                        filterCommingSoonDialog(moviesResponse?.output!!)
                    }
                    comingSoon(moviesResponse?.output?.comingsoon!!)
                } catch (e: Exception) {
                    println("Exception--->${e.message}")
                }
            }
            2 -> {
                try {
                    binding?.filterUi?.hide()
                    advanceBooking(moviesResponse?.output?.advanceBooking!!)
                } catch (e: Exception) {
                    println("Exception--->${e.message}")
                }
            }
        }
    }

}