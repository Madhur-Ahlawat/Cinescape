package com.cinescape1.ui.main.views

import android.content.BroadcastReceiver
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityMoreInfoBinding
import com.cinescape1.ui.main.viewModels.MoreInfoViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MoreInfoActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferences: AppPreferences
    private val moreInfoModel: MoreInfoViewModel by viewModels { viewModelFactory }
    private var binding: ActivityMoreInfoBinding? = null
    private var MyReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreInfoBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

    }


    override fun onPause() {
        super.onPause()
        unregisterReceiver(MyReceiver)
    }
}