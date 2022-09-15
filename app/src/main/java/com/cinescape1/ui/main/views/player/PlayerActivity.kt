package com.cinescape1.ui.main.views.player

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.cinescape1.R
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityPlayerBinding
import com.cinescape1.ui.main.views.player.viewModel.PlayerViewModel
import com.cinescape1.utils.MyReceiver
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import dagger.android.support.DaggerAppCompatActivity
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

class PlayerActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferences: AppPreferences
    private val playerViewModel: PlayerViewModel by viewModels { viewModelFactory }
    private var binding: ActivityPlayerBinding? = null
    private var broadcastReceiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        binding?.imageView38?.setOnClickListener {
            finish()
        }

        val trailerUrl = intent.getStringExtra("trailerUrl")
        getYoutubeVideoId(trailerUrl)
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

    private fun getYoutubeVideoId(youtubeUrl: String?): String? {
        var vId: String? = null
        val pattern = Pattern.compile(
            "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
            Pattern.CASE_INSENSITIVE
        )
        val matcher: Matcher = pattern.matcher(youtubeUrl)
        if (matcher.matches()) {
            vId = matcher.group(1)
            println("youtubeUrlCheck-->${vId}")
            playVideo(vId)
        }
        return vId
    }

    private fun playVideo(vid: Any) {
        val youtubeFragment =
            fragmentManager.findFragmentById(R.id.youtubeFragment) as YouTubePlayerFragment
        youtubeFragment.initialize("AIzaSyAD0AZTCMULaXpQfulSKNWuLlLl-nbdSf0",
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer, b: Boolean
                ) {
                    // do any work here to cue video, play video, etc.
                    youTubePlayer.cueVideo(vid.toString())
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                }
            })
    }
}