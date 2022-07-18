package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.OfferDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OfferDetailsModule{
    @Binds
    @IntoMap
    @ViewModelKey(OfferDetailsViewModel::class)
    abstract fun splashModule(viewModel: OfferDetailsViewModel) : ViewModel

//    @ContributesAndroidInjector
//    abstract fun contributeFragment():FirstFragment
}