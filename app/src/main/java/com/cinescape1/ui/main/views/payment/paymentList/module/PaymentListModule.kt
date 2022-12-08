package com.cinescape1.ui.main.views.payment.paymentList.module

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.views.summery.viewModel.SummeryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PaymentListModule{
    @Binds
    @IntoMap
    @ViewModelKey(SummeryViewModel::class)
    abstract fun loginModule(viewModel: SummeryViewModel) : ViewModel

}