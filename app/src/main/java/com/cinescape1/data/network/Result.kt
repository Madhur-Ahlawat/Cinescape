package com.cinescape1.data.network

import com.cinescape1.utils.Status

//sealed class Result<out R> {
//    data class Success<out T>(val data: T) : Result<T>()
//    data class Error(val code: Int = 0, val message: String? = null) : Result<Nothing>()
//}

data class Result<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {



        fun <T> error(msg: String, data: T?): Result<T> {
            return Result(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Result<T> {
            return Result(Status.LOADING, data, null)
        }

        fun <T> success(data: T?): Result<T> {
            return Result(Status.SUCCESS, data, null)
        }

    }

}
