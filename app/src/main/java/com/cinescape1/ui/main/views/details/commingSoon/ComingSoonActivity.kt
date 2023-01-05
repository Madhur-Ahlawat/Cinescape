package com.cinescape1.ui.main.views.details.commingSoon

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.GetMovieResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityComingSoonBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.details.nowShowing.ShowTimesActivity
import com.cinescape1.ui.main.views.details.adapter.SimilarMovieAdapter
import com.cinescape1.ui.main.views.details.commingSoon.viewModel.ComingSoonViewModel
import com.cinescape1.ui.main.views.player.PlayerActivity
import com.cinescape1.utils.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.show_times_layout_include.*
import javax.inject.Inject

@Suppress("DEPRECATION")
class ComingSoonActivity : DaggerAppCompatActivity(),SimilarMovieAdapter.RecycleViewItemClickListener {
    private var loader: LoaderDialog? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferences: AppPreferences
    private val showTimeViewModel: ComingSoonViewModel by viewModels { viewModelFactory }
    private var binding: ActivityComingSoonBinding? = null

    //language
    private var languageCheck: String = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComingSoonBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
                languageCheck = "ar"
                val regular = ResourcesCompat.getFont(this, R.font.gess_light)
                val bold = ResourcesCompat.getFont(this, R.font.gess_bold)
                binding?.textFilmHouseName?.typeface = bold // heavy

                binding?.textShare?.typeface = regular
                binding?.textNotify?.typeface = regular
                binding?.textMovieType?.typeface = regular

                // include layout
                text_cast?.typeface = bold
                text_director?.typeface = bold
                text_directoe_name?.typeface = regular
                text_genre?.typeface = bold
                text_genres?.typeface = regular
                textView8?.typeface = bold
                textView10?.typeface = regular
                text_synopsis?.typeface = bold
                text_sysnopsis_detail?.typeface = regular


            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(this, "en")
                languageCheck = "en"
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(this, R.font.sf_pro_text_heavy)

                binding?.textFilmHouseName?.typeface = heavy // heavy
                binding?.textShare?.typeface = regular
                binding?.textNotify?.typeface = regular
                binding?.textMovieType?.typeface = regular

                // include layout
                text_cast?.typeface = bold
                text_director?.typeface = bold
                text_directoe_name?.typeface = regular
                text_genre?.typeface = bold
                text_genres?.typeface = regular
                textView8?.typeface = bold
                textView10?.typeface = regular
                text_synopsis?.typeface = bold
                text_sysnopsis_detail?.typeface = regular

            }
            else -> {
                languageCheck = "en"
                LocaleHelper.setLocale(this, "en")
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val heavy = ResourcesCompat.getFont(this, R.font.sf_pro_text_heavy)

                binding?.textFilmHouseName?.typeface = heavy // heavy

                binding?.textShare?.typeface = regular
                binding?.textNotify?.typeface = regular
                binding?.textMovieType?.typeface = regular

                // include layout
                text_cast?.typeface = bold
                text_director?.typeface = bold
                text_directoe_name?.typeface = regular
                text_genre?.typeface = bold
                text_genres?.typeface = regular
                textView8?.typeface = bold
                textView10?.typeface = regular
                text_synopsis?.typeface = bold
                text_sysnopsis_detail?.typeface = regular


            }
        }

        //AppBar Hide
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        setContentView(view)
        movieDetails(intent.getStringExtra(Constant.IntentKey.MOVIE_ID).toString())
    }
    private fun movieDetails(movieId: String) {
        showTimeViewModel.movieDetails(movieId)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {
                                    try {
                                        movieDetailsData(it.data.output)
                                    } catch (e: Exception) {
                                        val dialog = OptionDialog(this,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            it.data.msg,
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {
                                                finish()
                                            },
                                            negativeClick = {
                                                finish()
                                            })
                                        dialog.show()                                    }
                                } else {
                                    loader?.dismiss()
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        it.data?.msg.toString(),
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

    private fun movieDetailsData(output: GetMovieResponse.Output) {
        binding?.LayoutTime?.show()
        binding?.textFilmHouseName?.text = output.movie.title
        binding?.textFilmHouseName?.isSelected = true
        binding?.view67?.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Insert Subject here")
            val appUrl = output.movie.shareUrl
            shareIntent.putExtra(Intent.EXTRA_TEXT, appUrl)
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        if (output.movie.language != null)
        binding?.textMovieType?.text =
            "" + output.movie.genre + " | " + output.movie.runTime + " " + getString(
                R.string.min
            )

        if (output.movie.trailerUrl.isEmpty()) {
            binding?.imageView26?.hide()
        } else {
            binding?.imageView26?.show()
            binding?.imageView26?.setOnClickListener {
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra("trailerUrl", output.movie.trailerUrl)
                startActivity(intent)
            }
        }

        Glide
            .with(this)
            .load(output.movie.mobimgbig)
            .placeholder(R.drawable.movie_details)
            .into(binding?.imageShow!!)

        text_genres.text = output.movie.genre
        textView10.text = output.movie.language
        textView123.text = output.movie.subTitle
        text_sysnopsis_detail.text = output.movie.synopsis
        text_directoe_name.text =
            output.movie.director.firstName + " " + output.movie.director.lastName

            if (output.similar.isEmpty()) {
                textView6.hide()
                similarShowing.hide()
            } else {
                textView6.show()
                similarShowing.show()
            }

        //Similar Movie
        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        similarShowing.layoutManager = LinearLayoutManager(this)
        val adapter = SimilarMovieAdapter(this, output.similar, this)
        similarShowing.layoutManager = gridLayout
        similarShowing.adapter = adapter


//        text_genres.text = movie.genre
//        textView10.text = movie.language
//        textView123.text = movie.subTitle
//        text_sysnopsis_detail.text = movie.synopsis
//        text_directoe_name.text = movie.director.firstName + " " + movie.director.lastName
//        println("cast---->${movie.cast}")
//
//        if (movie.cast.isNotEmpty()) {
//            text_cast.show()
//            binding?.include?.recyclerviewShowTimesCast?.show()
//        } else {
//            text_cast.hide()
//            binding?.include?.recyclerviewShowTimesCast?.hide()
//        }
//            binding?.include?.recyclerviewShowTimesCast?.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//            val adapterCast = AdpaterShowTimesCast(this, movie.cast, this)
//            binding?.include?.recyclerviewShowTimesCast?.adapter = adapterCast

    }

    override fun onSimilarItemClick(view: GetMovieResponse.Output.Similar) {
        val intent = Intent(this, ShowTimesActivity::class.java)
        intent.putExtra(Constant.IntentKey.MOVIE_ID, view.id)
        startActivity(intent)
    }

}