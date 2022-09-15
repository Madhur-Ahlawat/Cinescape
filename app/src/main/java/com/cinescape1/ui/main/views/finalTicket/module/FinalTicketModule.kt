package com.cinescape1.ui.main.views.finalTicket.module

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.views.finalTicket.viewModel.FinalTicketViewModel
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