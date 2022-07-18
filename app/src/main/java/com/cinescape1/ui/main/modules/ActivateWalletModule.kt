package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.ActivateWalletViewModel
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