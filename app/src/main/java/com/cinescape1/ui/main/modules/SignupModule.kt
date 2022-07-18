package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.LoginViewModel
import com.cinescape1.ui.main.viewModels.SignupViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SignupModule {
    @Binds
    @IntoMap
    @ViewModelKey(SignupViewModel::class)
    abstract fun signupModule(viewModel: SignupViewModel) : ViewModel

//    @ContributesAndroidInjector
//    abstract fun contributeFragment():FirstFragment

}