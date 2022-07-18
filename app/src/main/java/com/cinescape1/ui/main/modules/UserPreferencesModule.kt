package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.LoginViewModel
import com.cinescape1.ui.main.viewModels.UserPreferencesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class UserPreferencesModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserPreferencesViewModel::class)
    abstract fun userPreferencesModule(viewModel: UserPreferencesViewModel): ViewModel

//    @ContributesAndroidInjector
//    abstract fun contributeFragment():FirstFragment
}
