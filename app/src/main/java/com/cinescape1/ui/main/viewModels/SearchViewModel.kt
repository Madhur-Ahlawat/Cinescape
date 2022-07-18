package com.cinescape1.ui.main.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cinescape1.data.models.requestModel.FinalTicketRequest
import com.cinescape1.data.network.Repositories
import com.cinescape1.data.network.Result
import com.cinescape1.utils.Status
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val repositories: Repositories) : ViewModel() {

//    fun getSplashText() = liveData(Dispatchers.IO) {
//        emit(Result.loading(data = null))
//        try {
//            emit(Result.success(data = repositories.getSplashText()))
//        } catch (exception: Exception) {
//            emit(Result.error(exception.message ?: "Error Occurred!",data = null))
//        }
//    }

    fun getSplashText() = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.getSplashText()
            if (data.status == Status.ERROR){
                emit(Result.error(data.message.toString(),data))
            }else{
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!",data = null))
        }
    }
}