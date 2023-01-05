package com.cinescape1.ui.main.views.details.commingSoon.module

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.views.details.nowShowing.viewModel.ShowTimesViewModel
import com.cinescape1.ui.main.views.details.cinemaLocation.viewModel.CinemaLocationViewModel
import com.cinescape1.ui.main.views.details.commingSoon.viewModel.ComingSoonViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ComingSoonModule {

    @Binds
    @IntoMap
    @ViewModelKey(ComingSoonViewModel::class)
    abstract fun showTimesModule(viewModel: ComingSoonViewModel) : ViewModel

}