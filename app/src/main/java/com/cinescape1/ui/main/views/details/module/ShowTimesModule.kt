package com.cinescape1.ui.main.views.details.module

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.views.details.viewModel.ShowTimesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShowTimesModule {

    @Binds
    @IntoMap
    @ViewModelKey(ShowTimesViewModel::class)
    abstract fun showTimesModule(viewModel: ShowTimesViewModel) : ViewModel

//    @ContributesAndroidInjector
//    abstract fun contributeFragment():FirstFragment

}