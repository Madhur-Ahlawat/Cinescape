package com.cinescape1.ui.main.views.details.nowShowing.module

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.views.details.nowShowing.viewModel.ShowTimesViewModel
import com.cinescape1.ui.main.views.details.cinemaLocation.viewModel.CinemaLocationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CinemaLocationModule {

    @Binds
    @IntoMap
    @ViewModelKey(CinemaLocationViewModel::class)
    abstract fun showTimesModule(viewModel: CinemaLocationViewModel) : ViewModel

//    @ContributesAndroidInjector
//    abstract fun contributeFragment():FirstFragment

}