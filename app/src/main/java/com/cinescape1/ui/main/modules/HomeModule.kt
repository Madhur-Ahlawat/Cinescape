package com.cinescape1.ui.main.modules

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.viewModels.HomeViewModel
import com.cinescape1.ui.main.viewModels.MoreInfoViewModel
import com.cinescape1.ui.main.viewModels.fragmentsViewModels.AccountFragViewModel
import com.cinescape1.ui.main.viewModels.fragmentsViewModels.HomeFragViewModel
import com.cinescape1.ui.main.viewModels.fragmentsViewModels.MoviesFragViewModel
import com.cinescape1.ui.main.views.fragments.*
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class HomeModule{

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun homeModule(viewModel: HomeViewModel) : ViewModel

    @ContributesAndroidInjector
    abstract fun homeFragment():HomeFragment

    @ContributesAndroidInjector
    abstract fun foodFragment():FoodFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeFragViewModel::class)
    abstract fun homeFragModule(viewModel: HomeFragViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun moviesFragment():MoviesFragment

    @ContributesAndroidInjector
    abstract fun moreFragment():MorePageFragment

    @ContributesAndroidInjector
    abstract fun nowSowingFragment(): NowShowingFragment

   @ContributesAndroidInjector
    abstract fun comingsoonFragment(): ComingSoonFragment


    @Binds
    @IntoMap
    @ViewModelKey(MoviesFragViewModel::class)
    abstract fun moviesFragModule(viewModel: MoviesFragViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MoreInfoViewModel::class)
    abstract fun mFragModule(viewModel: MoreInfoViewModel): ViewModel
    

    @ContributesAndroidInjector
    abstract fun accountPageFragment(): AccountPageFragment

    @Binds
    @IntoMap
    @ViewModelKey(AccountFragViewModel::class)
    abstract fun accountFragViewModel(viewModel: AccountFragViewModel): ViewModel

}
