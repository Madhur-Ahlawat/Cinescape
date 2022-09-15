package com.cinescape1.ui.main.views.home.fragments

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.FoodResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.FragmentFoodBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.home.viewModel.HomeViewModel
import com.cinescape1.ui.main.views.food.FoodActivity
import com.cinescape1.ui.main.views.home.HomeActivity
import com.cinescape1.ui.main.views.adapters.foodAdapters.CustomSpinnerAdapter
import com.cinescape1.utils.*
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class FoodFragment : DaggerFragment(), AdapterView.OnItemSelectedListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferences: AppPreferences
    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }
    private var binding: FragmentFoodBinding? = null
    private var loader: LoaderDialog? = null
    private var locationlist = ArrayList<FoodResponse.Output.Cinema>()
    private var cinemaId = ""
    private var yourLocation: String = ""
    private var broadcastReceiver: BroadcastReceiver? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodBinding.inflate(layoutInflater, null, false)
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
        if (yourLocation == "") {
            binding?.textFindNearLocation?.text = getString(R.string.find_near_me)
        } else {
            binding?.textFindNearLocation?.text = getString(R.string.find_near_me)
        }


        binding?.textCancelGoback?.setOnClickListener {
            println("updateFood--->$")
            val intent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(intent)
        }

        binding?.textProceeds?.setOnClickListener {
            startActivity(
                Intent(requireActivity(), FoodActivity::class.java)
                    .putExtra("CINEMA_ID", cinemaId)
                    .putExtra("BOOKING", "FOOD")
                    .putExtra("type", "0")
            )
        }
        broadcastReceiver = MyReceiver()
        broadcastIntent()
        return view!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodResponse()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        cinemaId = locationlist[position].id
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun foodResponse() {
        homeViewModel.foodResponse()
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                try {
                                    if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                        binding?.foodLayout?.show()
                                        locationlist = it.data.output.cinemas
                                        binding?.spinner?.onItemSelectedListener = this
                                        val customAdapter = CustomSpinnerAdapter(
                                            requireActivity(),
                                            it.data.output.cinemas
                                        )
                                        binding?.spinner?.adapter = customAdapter

                                        println("foodCinemas--->${it.data.output.cinemas}")
                                    }
                                } catch (e: Exception) {

                                }
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
                            loader = LoaderDialog(R.string.pleasewait)
                            loader?.show(requireActivity().supportFragmentManager, null)
                        }
                    }
                }
            }
    }

    private fun broadcastIntent() {
        requireActivity().registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

}