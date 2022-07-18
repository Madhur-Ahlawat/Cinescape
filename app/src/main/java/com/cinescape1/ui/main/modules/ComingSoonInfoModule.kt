package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.ComingSoonInfoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ComingSoonInfoModule {

    @Binds
    @IntoMap
    @ViewModelKey(ComingSoonInfoViewModel::class)
    abstract fun ComingSoonInfoModule(viewModel: ComingSoonInfoViewModel) : ViewModel

//    @ContributesAndroidInjector
//    abstract fun contributeFragment():FirstFragment

}