package com.cinescape1.ui.main.viewModels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cinescape1.data.models.requestModel.CinemaSessionRequest
import com.cinescape1.data.network.Repositories
import com.cinescape1.data.network.Result
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CinemaSessionViewModel @Inject constructor(private val repositories: Repositories) : ViewModel() {


    fun getMsessionsNew(context: Activity, request: CinemaSessionRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            emit(Result.success(data = repositories.getMsessionsNew(request)))
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }


}