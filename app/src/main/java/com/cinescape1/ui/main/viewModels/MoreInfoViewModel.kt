package com.cinescape1.ui.main.viewModels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cinescape1.data.models.requestModel.ContactUsRequest
import com.cinescape1.data.network.Repositories
import com.cinescape1.data.network.Result
import com.cinescape1.utils.Status
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class MoreInfoViewModel @Inject constructor(private val repositories: Repositories) : ViewModel() {

    fun moreTabs() = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.moreTabs()
            if (data.status == Status.ERROR){
                emit(Result.error(data.message.toString(),data))
            }else{
                emit(Result.success(data = data))
            }

        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun contactUs(
        email: String,
        name: String,
        mobile: String,
        username: String,
        frontPhoto: MultipartBody.Part
    ) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val data = repositories.contctUs(
                ContactUsRequest(
                    email.toRequestBody("text".toMediaTypeOrNull()),
                    name.toRequestBody("text".toMediaTypeOrNull()),
                    mobile.toRequestBody("text".toMediaTypeOrNull()),
                    username.toRequestBody("text".toMediaTypeOrNull()),
                    frontPhoto
                ))
            if (data.status == Status.ERROR){
                emit(Result.error(data.message.toString(),data))
            }else{
                emit(Result.success(data = data))
            }

        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
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