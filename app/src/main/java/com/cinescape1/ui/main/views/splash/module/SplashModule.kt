package com.cinescape1.ui.main.views.splash.module

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.views.player.viewModel.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SplashModule{
    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun splashModule(viewModel: SplashViewModel) : ViewModel

//    @ContributesAndroidInjector
//    abstract fun contributeFragment():FirstFragment
}