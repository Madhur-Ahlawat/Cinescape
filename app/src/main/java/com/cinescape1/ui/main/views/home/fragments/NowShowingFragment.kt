package com.cinescape1.ui.main.views.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.ui.main.views.home.viewModel.HomeViewModel
import com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter.AdapterNowShowing
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class NowShowingFragment(private val nowshSnowshoeing: ArrayList<MoviesResponse.Nowshowing>) : DaggerFragment() {
    private var recyclerNowShowing: RecyclerView? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferences: AppPreferences
    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_now_shoing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerNowShowing = view.findViewById<View>(R.id.recyclerview_now_showing) as RecyclerView
        val gridLayout = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        recyclerNowShowing?.setHasFixedSize(true)
        val adapter = AdapterNowShowing(nowshSnowshoeing,requireActivity())
        recyclerNowShowing?.layoutManager = gridLayout
        recyclerNowShowing?.adapter = adapter


//        val factory = layoutInflater
//
//        val textEntryView: View = factory.inflate(R.layout.fragment_movies, null)
//
//       val search =
//            textEntryView.findViewById<View>(R.id.searchMovie) as SearchView
//        val inflatedView: View = layoutInflater.inflate(R.layout.fragment_movies, null)
//        val search = inflatedView.findViewById<View>(R.id.search) as SearchView
//        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                println("searchMovie--->${query}")
////                if(recyclerNowShowing?.contains(query)!!){
////                    adapter.filter(query);
////                }else{
////                    Toast.makeText(requireActivity(), "No Match found", Toast.LENGTH_LONG).show();
////                }
//                return false;
//            }
//            override fun onQueryTextChange(newText: String): Boolean {
//
//                return false
//            }
//        })
    }
}

