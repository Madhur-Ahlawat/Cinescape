package com.cinescape1.ui.main.views.login.otpVerification.module

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.views.login.otpVerification.viewModel.OtpVerificationViewModel
import com.cinescape1.ui.main.views.login.viewModel.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OtpVerificationModule{
    @Binds
    @IntoMap
    @ViewModelKey(OtpVerificationViewModel::class)
    abstract fun loginModule(viewModel: OtpVerificationViewModel) : ViewModel

//    @ContributesAndroidInjector
//    abstract fun contributeFragment():FirstFragment
}