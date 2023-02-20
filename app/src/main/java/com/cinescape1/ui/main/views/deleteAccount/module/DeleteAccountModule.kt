package com.cinescape1.ui.main.views.deleteAccount.module

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.views.deleteAccount.viewModel.DeleteAccountViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DeleteAccountModule {

    @Binds
    @IntoMap
    @ViewModelKey(DeleteAccountViewModel::class)
    abstract fun deleteAccountModule(viewModel: DeleteAccountViewModel): ViewModel
}