package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.MoreInfoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MoreInfoModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoreInfoViewModel::class)
    abstract fun moreInfopModule(viewModel: MoreInfoViewModel) : ViewModel

//    @ContributesAndroidInjector
//    abstract fun contributeFragment():FirstFragment


}