package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.FinalTicketViewModel
import com.cinescape1.ui.main.viewModels.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FinalTicketModule{
    @Binds
    @IntoMap
    @ViewModelKey(FinalTicketViewModel::class)
    abstract fun finalTicketModule(viewModel: FinalTicketViewModel) : ViewModel

//    @ContributesAndroidInjector
//    abstract fun contributeFragment():FirstFragment
}