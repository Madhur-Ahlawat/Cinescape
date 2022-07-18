package com.cinescape1.ui.main.views.fragments

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
import com.cinescape1.ui.main.viewModels.HomeViewModel
import com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter.AdapterCommingSoon
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ComingSoonFragment(private val nowshowing: ArrayList<MoviesResponse.Nowshowing>) : DaggerFragment() {
    private var recyclerComingSoon: RecyclerView? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferences: AppPreferences
    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coming_soon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerComingSoon = view.findViewById<View>(R.id.recyclerview_coming_soon) as RecyclerView
        val gridLayout = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        recyclerComingSoon?.setHasFixedSize(true)
        val adapter = AdapterCommingSoon(nowshowing, requireActivity())
        recyclerComingSoon?.layoutManager = gridLayout
        recyclerComingSoon?.adapter = adapter

    }

}