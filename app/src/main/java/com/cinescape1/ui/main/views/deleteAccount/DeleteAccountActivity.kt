package com.cinescape1.ui.main.views.deleteAccount

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.DeleteAccountIncludeBinding
import com.cinescape1.databinding.DeleteAccountLayoutBinding
import com.cinescape1.ui.main.views.deleteAccount.viewModel.DeleteAccountViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

@Suppress("DEPRECATION")
class DeleteAccountActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private val deleteAccountViewModel: DeleteAccountViewModel by viewModels { viewModelFactory }
    private var binding: DeleteAccountLayoutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DeleteAccountLayoutBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        binding?.imgBackBtn?.setOnClickListener {
            onBackPressed()
        }

    }


}