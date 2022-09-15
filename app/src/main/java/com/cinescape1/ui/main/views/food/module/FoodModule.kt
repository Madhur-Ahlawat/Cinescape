package com.cinescape1.ui.main.views.food.module

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.views.food.viewModel.FoodViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FoodModule {

    @Binds
    @IntoMap
    @ViewModelKey(FoodViewModel::class)
    abstract fun foodModule(viewModel: FoodViewModel) : ViewModel

//    @ContributesAndroidInjector
//    abstract fun contributeFragment():FirstFragment

}