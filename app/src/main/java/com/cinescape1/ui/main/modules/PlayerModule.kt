package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.FoodViewModel
import com.cinescape1.ui.main.viewModels.PlayerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PlayerModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    abstract fun playerModule(viewModel: PlayerViewModel) : ViewModel
}