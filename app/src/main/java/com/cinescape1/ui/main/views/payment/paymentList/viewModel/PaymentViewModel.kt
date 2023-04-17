package com.cinescape1.ui.main.views.payment.paymentList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cinescape1.data.models.requestModel.*
import com.cinescape1.data.network.Repositories
import com.cinescape1.data.network.Result
import com.cinescape1.ui.main.views.payment.PaymentMethodSealedClass
import com.cinescape1.ui.main.views.payment.paymentList.BankOfferRequest
import com.cinescape1.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PaymentViewModel @Inject constructor(private val repositories: Repositories) : ViewModel() {
    fun tckSummary(request: TicketSummaryRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.tckSummary(request)
            if (data.status == Status.ERROR) {
                emit(Result.error(data.message.toString(), data))
            } else {
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    private var _selectedpaymentMethod: MutableStateFlow<PaymentMethodSealedClass> =
        MutableStateFlow(PaymentMethodSealedClass.NONE)
    val selectedpaymentMethod: StateFlow<PaymentMethodSealedClass> = _selectedpaymentMethod

    fun setpaymentMethodSelectionStateFlow(paymentMethodEnum: PaymentMethodSealedClass) {
        _selectedpaymentMethod.value = paymentMethodEnum
    }

    var selectedPaymentMethod: PaymentMethodSealedClass =
        PaymentMethodSealedClass.NONE

    fun setPaymentMethodSelection(paymentMethodEnum: PaymentMethodSealedClass) {
        selectedPaymentMethod = paymentMethodEnum
    }

    fun paymentList(request: TicketSummaryRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.paymentList(request)
            if (data.status == Status.ERROR) {
                emit(Result.error(data.message.toString(), data))
            } else {
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun cancelTrans(request: CancelTransRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.cancelTrans(request)
            if (data.status == Status.ERROR) {
                emit(Result.error(data.message.toString(), data))
            } else {
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun paymentKnetHmac(request: HmacKnetRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.paymentKnetHmac(request)
            if (data.status == Status.ERROR) {
                emit(Result.error(data.message.toString(), data))
            } else {
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun paymentWallet(request: HmacKnetRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.paymentWallet(request)
            if (data.status == Status.ERROR) {
                emit(Result.error(data.message.toString(), data))
            } else {
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun bankApply(request: BankOfferRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.bnankApply(request)
            if (data.status == Status.ERROR) {
                emit(Result.error(data.message.toString(), data))
            } else {
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun bankRemove(request: BankOfferRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.bnankRemove(request)
            if (data.status == Status.ERROR) {
                emit(Result.error(data.message.toString(), data))
            } else {
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun giftCardApply(request: GiftCardRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.giftCardRequest(request)
            if (data.status == Status.ERROR) {
                emit(Result.error(data.message.toString(), data))
            } else {
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun giftCardRemove(request: GiftCardRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.giftCardRemove(request)
            if (data.status == Status.ERROR) {
                emit(Result.error(data.message.toString(), data))
            } else {
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun voucherApply(request: GiftCardRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.voucherApply(request)
            if (data.status == Status.ERROR) {
                emit(Result.error(data.message.toString(), data))
            } else {
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun creditCardInit(request: HmacKnetRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.creditCardInit(request)
            if (data.status == Status.ERROR) {
                emit(Result.error(data.message.toString(), data))
            } else {
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun postCardData(request: PostCardRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.postCardData(request)
            if (data.status == Status.ERROR) {
                emit(Result.error(data.message.toString(), data))
            } else {
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun validateJWT(request: ValidateJWTRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.validateJWT(request)
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