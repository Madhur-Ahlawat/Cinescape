package com.cinescape1.ui.main.viewModels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cinescape1.data.models.requestModel.ActivatwWalletRequest
import com.cinescape1.data.network.Repositories
import com.cinescape1.data.network.Result
import com.cinescape1.utils.Status
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ActivateWalletViewModel @Inject constructor(private val repositories: Repositories) : ViewModel() {

//    fun getSplashText() = liveData(Dispatchers.IO) {
//        emit(Result.loading(data = null))
//        try {
//            emit(Result.success(data = repositories.getSplashText()))
//        } catch (exception: Exception) {
//            emit(Result.error(exception.message ?: "Error Occurred!",data = null))
//        }
//    }

    fun activateCard(activatwWalletRequest: ActivatwWalletRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.activateCard(activatwWalletRequest)
            if (data.status == Status.ERROR){
                emit(Result.error(data.message.toString(),data))
            }else{
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!",data = null))
        }
    }

    fun countryCode(
        context: Activity
    ) = liveData(Dispatchers.IO) {
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
}