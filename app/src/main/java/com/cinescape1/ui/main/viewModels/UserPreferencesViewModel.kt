package com.cinescape1.ui.main.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cinescape1.data.models.requestModel.PreferenceRequest
import com.cinescape1.data.models.requestModel.ProfileRequest
import com.cinescape1.data.network.Repositories
import com.cinescape1.data.network.Result
import com.cinescape1.utils.Status
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class UserPreferencesViewModel @Inject constructor(private val repositories: Repositories) :
    ViewModel() {
    fun updatePreference(request: PreferenceRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.preference(request)
            if (data.status == Status.ERROR){
                emit(Result.error(data.message.toString(),data))
            }else{
                emit(Result.success(data = data))
            }

        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun getProfile(request: ProfileRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.getProfile(request)
            if (data.status == Status.ERROR){
                emit(Result.error(data.message.toString(),data))
            }else{
                emit(Result.success(data = data))
            }

        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }
    fun foodResponse() = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.foodResponse()
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