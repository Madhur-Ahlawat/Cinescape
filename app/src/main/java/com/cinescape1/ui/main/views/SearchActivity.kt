package com.cinescape1.ui.main.views

import android.content.BroadcastReceiver
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cinescape1.R
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivitySearchBinding
import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.viewModels.SearchViewModel
import com.cinescape1.utils.Constant
import com.cinescape1.utils.Status
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


@ActivityScoped
class SearchActivity : DaggerAppCompatActivity() {

    private var loader: LoaderDialog? = null
    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory
    private var binding: ActivitySearchBinding? = null

    @Inject
    lateinit var preferences: AppPreferences
    private val splashViewModel: SearchViewModel by viewModels { viewmodelFactory }
    private var myReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySearchBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
//        broadcastIntent()
    }


    private fun getSplashText(){
        splashViewModel.getSplashText()
            .observe(this, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE){
                                try {
//                                    openMovieForYou()
                                }catch (e:Exception){
                                    println("exception--->${e.message}")
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
                        }
                        Status.LOADING -> {

                        }
                    }
                }
            })
    }

//    private fun broadcastIntent() {
//        registerReceiver(myReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
//    }
//
//    override fun onPause() {
//        super.onPause()
//        unregisterReceiver(myReceiver)
//    }
}

