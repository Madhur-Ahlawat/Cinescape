package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.ContinueGuestViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContinueGuestModule{
    @Binds
    @IntoMap
    @ViewModelKey(ContinueGuestViewModel::class)
    abstract fun continueGuestViewModel(viewModel: ContinueGuestViewModel) : ViewModel

}