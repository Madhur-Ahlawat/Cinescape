package com.cinescape1.ui.main.views.home.fragments.home.seeAll

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.data.models.responseModel.OfferResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivitySeeAllBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.adapters.OfferSeeAllAdapter
import com.cinescape1.ui.main.views.adapters.SeeAllAdapter
import com.cinescape1.ui.main.views.home.fragments.home.seeAll.viewModel.SeeAllViewModel
import com.cinescape1.utils.*
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

@Suppress("UNCHECKED_CAST", "DEPRECATION")
class SeeAllActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private val seeAllViewModel: SeeAllViewModel by viewModels { viewModelFactory }
    private var binding: ActivitySeeAllBinding? = null
    private var parentLayoutManager: RecyclerView.LayoutManager? = null
    private var type: String? = null
    private var loader: LoaderDialog? = null
    private var broadcastReceiver: BroadcastReceiver? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeAllBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunction()
    }

    private fun manageFunction() {

        intent.getStringExtra("title").toString()
        binding?.back?.setOnClickListener {
            onBackPressed()
        }

        type = intent.getStringExtra("type")

        if (type == "offer") {
            offers()
        } else {
            // Get Array Data
            binding?.seeAllUi?.show()
            val arrayList = intent.getSerializableExtra("arrayList")
            println("stockList--->${arrayList}")

            intent.getSerializableExtra("arrayList")
            toast("hello")
            parentLayoutManager = LinearLayoutManager(this)
            val seeAllAdapter =
                SeeAllAdapter(this, arrayList as ArrayList<HomeDataResponse.MovieData>)
            binding?.recyclerSeeAll?.layoutManager = GridLayoutManager(this, 3)
            binding?.recyclerSeeAll?.adapter = seeAllAdapter
        }
        broadcastReceiver = MyReceiver()
        broadcastIntent()
    }

    private fun broadcastIntent() {
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun offers() {
        seeAllViewModel.offers().observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    retrieveData(it.data.output)
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
                                    finish()
                                },
                                negativeClick = {
                                    finish()
                                })
                            dialog.show()
                        }
                        Status.LOADING -> {
                            loader = LoaderDialog(R.string.pleasewait)
                            loader?.show(this.supportFragmentManager, null)
                        }
                    }
                }
            }
    }

    private fun retrieveData(data: ArrayList<OfferResponse.Output>) {
        binding?.seeAllUi?.show()
        intent.getSerializableExtra("arrayList")
        parentLayoutManager = LinearLayoutManager(this)
        val seeAllAdapter = OfferSeeAllAdapter(this, data)
        binding?.recyclerSeeAll?.layoutManager = GridLayoutManager(this, 1)
        binding?.recyclerSeeAll?.adapter = seeAllAdapter
    }
}