package com.cinescape1.ui.main.views.login.otpVerification.viewModel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cinescape1.data.models.requestModel.GuestRequest
import com.cinescape1.data.models.requestModel.LoginRequest
import com.cinescape1.data.models.requestModel.OtpVerifyRequest
import com.cinescape1.data.models.requestModel.RegisterRequest
import com.cinescape1.data.network.Repositories
import com.cinescape1.data.network.Result
import com.cinescape1.utils.Status
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class OtpVerificationViewModel @Inject constructor(private val repositories: Repositories) : ViewModel() {

    fun mVerify(
        otpVerifyRequest: OtpVerifyRequest
    ) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.otpVirfy(otpVerifyRequest)
            if (data.status == Status.ERROR){
                emit(Result.error(data.message.toString(),data))
            }else{
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }


}