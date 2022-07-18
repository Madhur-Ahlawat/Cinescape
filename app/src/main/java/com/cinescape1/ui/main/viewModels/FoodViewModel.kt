package com.cinescape1.ui.main.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cinescape1.data.models.requestModel.CancelTransRequest
import com.cinescape1.data.models.requestModel.GetFoodRequest
import com.cinescape1.data.models.requestModel.SaveFoodRequest
import com.cinescape1.data.network.Repositories
import com.cinescape1.data.network.Result
import com.cinescape1.utils.Status
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class FoodViewModel  @Inject constructor(private val repositories: Repositories) : ViewModel() {

    fun getFood(request: GetFoodRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.getFood(request)
            if (data.status == Status.ERROR){
                emit(Result.error(data.message.toString(),data))
            }else{
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!",data = null))
        }
    }

    fun cancelTrans(request: CancelTransRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.cancelTrans(request)
            if (data.status == Status.ERROR){
                emit(Result.error(data.message.toString(),data))
            }else{
                emit(Result.success(data = data))
            }
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!",data = null))
        }
    }


    fun saveFood(request: SaveFoodRequest) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.saveFood(request)
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