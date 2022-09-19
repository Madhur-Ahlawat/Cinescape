package com.cinescape1.ui.main.views.payment.paymentFaield.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cinescape1.data.models.requestModel.FinalTicketRequest
import com.cinescape1.data.network.Repositories
import com.cinescape1.data.network.Result
import com.cinescape1.utils.Status
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class PaymentFailedViewModel @Inject constructor(private val repositories: Repositories) :
    ViewModel() {

    fun paymentFailed(
        otpVerifyRequest: FinalTicketRequest
    ) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.tckFailed(otpVerifyRequest)
            if (data.status == Status.ERROR) {
                emit(Result.error(data.message.toString(), data))
            } else {
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }


}