package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.SearchViewModel
import com.cinescape1.ui.main.viewModels.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SearchModule{
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun searchModule(viewModel: SearchViewModel) : ViewModel

//    @ContributesAndroidInjector
//    abstract fun contributeFragment():FirstFragment
}