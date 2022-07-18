package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.ShowTimesViewModel
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