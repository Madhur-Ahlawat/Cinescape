package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.CinemaSessionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CinemaSessionModule {

    @Binds
    @IntoMap
    @ViewModelKey(CinemaSessionViewModel::class)
    abstract fun cinemaSessionModule(viewModel: CinemaSessionViewModel) : ViewModel

//    @ContributesAndroidInjector
//    abstract fun contributeFragment():FirstFragment
}