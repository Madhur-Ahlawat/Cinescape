package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.CheckoutWithFoodViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CheckoutWithFoodModule {

    @Binds
    @IntoMap
    @ViewModelKey(CheckoutWithFoodViewModel::class)
    abstract fun checkoutWithFoodModule(viewModel: CheckoutWithFoodViewModel) : ViewModel

}