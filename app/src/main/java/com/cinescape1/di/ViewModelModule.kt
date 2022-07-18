package com.cinescape1.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module


@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: OurViewModelFactory): ViewModelProvider.Factory
}