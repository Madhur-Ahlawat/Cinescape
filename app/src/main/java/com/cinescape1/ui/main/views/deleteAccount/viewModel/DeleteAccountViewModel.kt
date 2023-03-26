package com.cinescape1.ui.main.views.deleteAccount.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cinescape1.data.network.Repositories
import com.cinescape1.data.network.Result
import com.cinescape1.ui.main.views.deleteAccount.DeleteAccountRequest
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DeleteAccountViewModel @Inject constructor(private val repositories: Repositories) : ViewModel() {
    fun deleteAccount(deleteAccount: DeleteAccountRequest ) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            emit(Result.success(data = repositories.deleteAccount(deleteAccount)))
        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: "Error Occurred!", data = null))
        }
    }
}