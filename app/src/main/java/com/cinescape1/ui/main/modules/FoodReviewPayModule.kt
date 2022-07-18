package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.FoodReviewPayViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FoodReviewPayModule {

    @Binds
    @IntoMap
    @ViewModelKey(FoodReviewPayViewModel::class)
    abstract fun foodReviewPayModule(viewModel: FoodReviewPayViewModel) : ViewModel

}