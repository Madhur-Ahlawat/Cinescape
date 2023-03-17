package com.cinescape1.ui.main.views.login.viewModel

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


class LoginViewModel @Inject constructor(private val repositories: Repositories) : ViewModel() {

    /////////////////////////// Firebase Facebook Authentication Starts ////////////////////////////

    val loginManager: LoginManager = LoginManager.getInstance()
    private val mCallbackManager = CallbackManager.Factory.create()

    fun userLogin(context: Activity, mobile: String, password: String) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.userLogin(LoginRequest(mobile,password))
            if (data.status == Status.ERROR){
                emit(Result.error(data.message.toString(),data))
            }else{
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun continueGuest(guestRequest: GuestRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.continueGuest(guestRequest)
            if (data.status == Status.ERROR){
                emit(Result.error(data.message.toString(),data))
            }else{
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!",data = null))
        }
    }




    fun resisters(
        context: Activity,
        dob: String,
        isd: String,
        email: String,
        firstName: String,
        gender: String,
        lastName: String,
        mobilePhone: String,
        password: String,
        promoEmail: Boolean,
        promoMobile: Boolean,
        reserveNotification: Boolean,
    ) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.resisters(
                RegisterRequest(isd,dob,email,firstName,gender,lastName, mobilePhone,password,promoEmail
                ,promoMobile,reserveNotification)
            )

            if (data.status == Status.ERROR){
                emit(Result.error(data.message.toString(),data))
            }else{
                emit(Result.success(data = data))
            }

        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }
    fun countryCode(
        context: Activity) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.countryCode()
            if (data.status == Status.ERROR){
                emit(Result.error(data.message.toString(),data))
            }else{
                emit(Result.success(data = data))
            }

        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

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