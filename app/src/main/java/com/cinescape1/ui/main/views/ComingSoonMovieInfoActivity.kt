package com.cinescape1.ui.main.views

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.ImgComingSoon
import com.cinescape1.data.models.responseModel.MovieDetailResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityComingSoonInfoBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.viewModels.ComingSoonInfoViewModel
import com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter.AdapterComingSoonSimilarMovies
import com.cinescape1.utils.Constant
import com.cinescape1.utils.MyReceiver
import com.cinescape1.utils.Status
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_coming_soon_info.*
import javax.inject.Inject

class ComingSoonMovieInfoActivity : DaggerAppCompatActivity() {

    private var movieSimilarList: List<ImgComingSoon>? = arrayListOf(
        ImgComingSoon(R.drawable.img_coming_soon),
        ImgComingSoon(R.drawable.img_demo),
        ImgComingSoon(R.drawable.img_coming_soon),
        ImgComingSoon(R.drawable.wonderwoman),
        ImgComingSoon(R.drawable.img_coming_soon)
    )

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private val comingSoonInfoViewModel: ComingSoonInfoViewModel by viewModels { viewModelFactory }

    private var binding: ActivityComingSoonInfoBinding? = null
    var loader: LoaderDialog? = null
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComingSoonInfoBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        setCastAdapter(view!!)
        setSimilarMoviesAdapter(view)

       image_back_btns.setOnClickListener {
           onBackPressed()
       }
        getMovie("HO00001412")

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
    private fun getMovie(mid : String){
        comingSoonInfoViewModel.comingSoonMovieInfo(this, mid)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {
                                    try {
                                        updateUiComingSoon(it.data.output)
                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }

                                } else {
                                    loader?.dismiss()
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

    private fun updateUiComingSoon(output: MovieDetailResponse.Output) {
        println("MovieDetailResponse -----> ${output}")
        text_movie_title_name.text = output.title
        Glide.with(this).load(output.trailerUrl).placeholder(R.drawable.movie_default).into(imageView4)
        text_directoe_name.text = output.director.firstName + " " + output.director.lastName
        text_genres.text = output.genre
        textView10.text = output.mlanguage
        text_sysnopsis_detail.text = output.synopsis

    }


    private fun setCastAdapter(view: View) {
        val recyclerCast = view.findViewById<View>(R.id.recyclerview_cast) as RecyclerView

        val gridLayout = GridLayoutManager(this@ComingSoonMovieInfoActivity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerCast.layoutManager = LinearLayoutManager(this)
//        val adapter = AdapterComingSoonMovieInfoCast(movieCastList!!)
//        recyclerCast.layoutManager = gridLayout
//        recyclerCast.adapter = adapter
//      //  adapter.loadNewData(movieCastList!!)

    }

    private fun setSimilarMoviesAdapter(view: View){
        val recyclerSimilarMovies = view.findViewById<View>(R.id.recycler_similar_movies) as RecyclerView
        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerSimilarMovies.layoutManager = LinearLayoutManager(this)
        val adapter = AdapterComingSoonSimilarMovies(movieSimilarList!!)
        recyclerSimilarMovies.layoutManager = gridLayout
        recyclerSimilarMovies.adapter = adapter
        adapter.loadNewData(movieSimilarList!!)

    }

}