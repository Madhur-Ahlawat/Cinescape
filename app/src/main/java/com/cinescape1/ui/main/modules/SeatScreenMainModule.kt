package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.SeatScreenMainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SeatScreenMainModule {

    @Binds
    @IntoMap
    @ViewModelKey(SeatScreenMainViewModel::class)
    abstract fun seatScreenMaiModule(viewModel: SeatScreenMainViewModel) : ViewModel

}