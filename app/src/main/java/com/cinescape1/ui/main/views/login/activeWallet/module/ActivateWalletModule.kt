package com.cinescape1.ui.main.views.login.activeWallet.module

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.views.login.activeWallet.viewModel.ActivateWalletViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ActivateWalletModule{
    @Binds
    @IntoMap
    @ViewModelKey(ActivateWalletViewModel::class)
    abstract fun activateWalletViewModel(viewModel: ActivateWalletViewModel) : ViewModel

}