package com.cinescape1.ui.main.views.login.resetPassword.module

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.views.login.resetPassword.viewModel.ResetPasswordViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ResetPasswordModule{
    @Binds
    @IntoMap
    @ViewModelKey(ResetPasswordViewModel::class)
    abstract fun resetPasswordViewModel(viewModel: ResetPasswordViewModel) : ViewModel

}