package com.cinescape1.ui.main.viewModels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cinescape1.data.models.requestModel.ReserveSeatRequest
import com.cinescape1.data.models.requestModel.SeatLayoutRequest
import com.cinescape1.data.network.Repositories
import com.cinescape1.data.network.Result
import com.cinescape1.utils.Status
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SeatScreenMainViewModel @Inject constructor(private val repositories: Repositories) : ViewModel() {

    fun getSeatLayout(request: SeatLayoutRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.getSeatLayout(request)
            if (data.status == Status.ERROR){
                emit(Result.error(data.message.toString(),data))
            }else{
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!",data = null))
        }
    }

    fun reserveSeat(context: Activity, request: ReserveSeatRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            emit(Result.success(data = repositories.reserveSeat(request)))
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }
}