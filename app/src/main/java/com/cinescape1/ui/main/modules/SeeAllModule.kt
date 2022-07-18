package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.CinemaSessionViewModel
import com.cinescape1.ui.main.viewModels.SeeAllViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SeeAllModule {

    @Binds
    @IntoMap
    @ViewModelKey(SeeAllViewModel::class)
    abstract fun seeAllModule(viewModel: SeeAllViewModel) : ViewModel
}