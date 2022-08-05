package com.cinescape1.ui.main.views.fragments

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.FragmentHomeBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.viewModels.HomeViewModel
import com.cinescape1.ui.main.views.adapters.home.HomeParentAdapter
import com.cinescape1.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : DaggerFragment(),HomeParentAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferences: AppPreferences
    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }
    private var binding: FragmentHomeBinding? = null
    private var loader: LoaderDialog? = null
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(requireActivity(), "ar")
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
        //AppBar Hide
        requireActivity().window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
        getHomeData()
        broadcastReceiver = MyReceiver()
        broadcastIntent()
    }

    private fun getHomeData() {
        homeViewModel.getHomeData()
            .observe(requireActivity()) {
                it?.let { resource ->
                    println("ErrorData---${resource}")
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {
                                    try {
                                        updateUI(it.data.output)
                                    } catch (e: Exception) {
                                        println("exception--->${e.message}")
                                    }
                                } else {

                                    val dialog = OptionDialog(requireContext(),
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
                            val dialog = OptionDialog(requireContext(),
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
                            if (isAdded) {
                            loader = LoaderDialog(R.string.pleasewait)
                            loader?.show(requireActivity().supportFragmentManager, null)
                        }
                        }
                    }
                }
            }
    }

    private fun updateUI(output: HomeDataResponse.Output) {
        try {
            val homeData: ArrayList<HomeDataResponse.HomeOne> = ArrayList()
            for (data in output.homeOnes) {
                when (data.key) {
                    "slider" -> {
                        if (data.movieData.isNotEmpty()) {
                            homeData.add(data)
                        }
                    }
                    "advance" -> {
                        if (data.movieData.isNotEmpty()) {
                            homeData.add(data)
                        }
                    }
                    "recommend" -> {
                        if (data.movieData.isNotEmpty()) {
                            homeData.add(data)
                        }
                    }
                    "nowShowing" -> {
                        if (data.movieData.isNotEmpty()) {
                            homeData.add(data)
                        }
                    }
                    "topPick" -> {
                        if (data.movieData.isNotEmpty()) {
                            homeData.add(data)
                        }
                    }
                    "thisWeek" -> {
                        if (data.movieData.isNotEmpty()) {
                            homeData.add(data)
                        }
                    }
                    "cinemas" -> {
                        if (data.cinemas.isNotEmpty()) {
                            homeData.add(data)
                        }
                    }
                    "lastChance" -> {
                        if (data.movieData.isNotEmpty()) {
                            homeData.add(data)
                        }
                    }
                    "comingSoon" -> {
                        if (data.movieData.isNotEmpty()) {
                            homeData.add(data)
                        }
                    }
                    "offers" -> {
                        if (data.offers.isNotEmpty()) {
                            homeData.add(data)
                        }
                    }
                }
            }
            val gridLayout =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
            mainList?.layoutManager = LinearLayoutManager(context)
            val adapter = HomeParentAdapter(requireActivity(), homeData,this)
            mainList?.layoutManager = gridLayout
            mainList?.adapter = adapter
            binding?.homeShimmer?.hide()
            binding?.homeUi?.show()
        } catch (e: Exception) {
            print("ExceptionMsg--->${e.message}")
        }
    }

    private fun broadcastIntent() {
        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }



    override fun onSeeAllClick(type:Int) {
        val navView = requireActivity().findViewById(R.id.navigationView) as BottomNavigationView
        val item = navView.getChildAt(0) as BottomNavigationMenuView
        val itemView = item.getChildAt(1)
        itemView.performClick()
//        requireActivity().supportFragmentManager.beginTransaction().apply {
//            replace(R.id.container, MoviesFragment(type))
//            commit()
//        }
    }
}