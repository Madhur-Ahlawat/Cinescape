package com.cinescape1.ui.main.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cinescape1.data.network.Repositories
import com.cinescape1.data.network.Result
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val repositories: Repositories) : ViewModel() {

    fun getSplashText() = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            emit(Result.success(data = repositories.getSplashText()))
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!",data = null))
        }
    }

}