package com.cinescape1.ui.main.views.payment.paymentFaield.module

import androidx.lifecycle.ViewModel
import com.cinescape1.di.ViewModelKey
import com.cinescape1.ui.main.views.payment.paymentFaield.viewModel.PaymentFailedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PaymentFailedModule{
    @Binds
    @IntoMap
    @ViewModelKey(PaymentFailedViewModel::class)
    abstract fun loginModule(viewModel: PaymentFailedViewModel) : ViewModel

}