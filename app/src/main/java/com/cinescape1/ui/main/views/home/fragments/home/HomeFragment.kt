package com.cinescape1.ui.main.views.home.fragments.home

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.app.Dialog
import androidx.core.content.res.ResourcesCompat
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
import com.cinescape1.ui.main.views.adapters.HomeChildAdapter
import com.cinescape1.ui.main.views.home.viewModel.HomeViewModel
import com.cinescape1.ui.main.views.home.fragments.home.adapter.HomeParentAdapter
import com.cinescape1.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@Suppress("DEPRECATION")
class HomeFragment : DaggerFragment(), HomeParentAdapter.RecycleViewItemClickListener,
    HomeParentAdapter.TypeFaceInter, HomeChildAdapter.ImageChangeIcon {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }
    private var binding: FragmentHomeBinding? = null
    private var loader: Dialog? = null
    private var broadcastReceiver: BroadcastReceiver? = null
    var homeTitle1: TextView? = null
    var txtSeeAll1: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, null, false)
        val view = binding?.root

        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(requireActivity(), "ar")
                Constant.LANGUAGE = "${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}"
                val regular = ResourcesCompat.getFont(requireActivity(), R.font.montserrat_regular)
                val bold = ResourcesCompat.getFont(requireActivity(), R.font.montserrat_bold)
                val medium = ResourcesCompat.getFont(requireActivity(), R.font.montserrat_medium)

                binding?.textSwitcher?.typeface = regular
                binding?.textArabic?.typeface = medium

                homeTitle1?.typeface = bold
                txtSeeAll1?.typeface = regular
            }

            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(requireActivity(), "en")
                Constant.LANGUAGE = "${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}"
                val regular = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_medium)

                binding?.textSwitcher?.typeface = regular
                binding?.textArabic?.typeface = medium

                homeTitle1?.typeface = bold
                txtSeeAll1?.typeface = regular
            }

            else -> {
                LocaleHelper.setLocale(requireActivity(), "en")
                Constant.LANGUAGE = "${preferences.getString(Constant.IntentKey.SELECT_LANGUAGE)}"
                val regular = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(requireActivity(), R.font.sf_pro_text_medium)

                binding?.textSwitcher?.typeface = regular
                binding?.textArabic?.typeface = medium
                homeTitle1?.typeface = bold
                txtSeeAll1?.typeface = regular

            }
        }
        return view!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requireActivity().window.statusBarColor = this.resources.getColor(R.color.black50)

        //AppBar Hide
        requireActivity().window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//            statusBarColor = Color.TRANSPARENT
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
                            loader = LoaderDialog.getInstance(requireContext(),layoutInflater)
                            loader?.show()
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
                            println("offer123--->${data.offers.size}")
                            homeData.add(data)
                        }
                    }
                }
            }

            mainList.isLayoutFrozen= false
            val gridLayout = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
            mainList?.layoutManager = LinearLayoutManager(context)
            val adapter = HomeParentAdapter(requireActivity(), homeData,this, this)
            mainList?.layoutManager = gridLayout
            mainList?.adapter = adapter
            mainList?.overScrollMode = View.OVER_SCROLL_NEVER
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
    }

    override fun typeFace(homeTitle: TextView, txtSeeAll: TextView) {
        homeTitle1 = homeTitle
        txtSeeAll1 = txtSeeAll
    }

    override fun arabicClick(imgArabic: ImageView) {
           if (preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar")  {
                LocaleHelper.setLocale(requireActivity(), "ar")
                imgArabic.setImageResource(R.drawable.arebic_red_icon)
               println("ARAaaaaa-----yes")
            }

           if (preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en") {
               LocaleHelper.setLocale(requireActivity(), "en")
               imgArabic.setImageResource(R.drawable.now_showing_diagonal)
               println("ARAaaaaa-----no")
           }

    }
}