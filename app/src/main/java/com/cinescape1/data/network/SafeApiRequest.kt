package com.cinescape1.data.network

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): Result<T> {
        Result.loading(
            null
        )
        return try {
            val response = call.invoke()
             if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                response.getErrorObject()
            }
        } catch (e: IOException) {
            e.printStackTrace()
             Result.error(
                NO_INTERNET_MESSAGE,null
            )
        } catch (e: Exception) {
            e.printStackTrace()
             Result.error(
                SOMETHING_WENT_WRONG_MESSAGE,null
            )
        }

    }
}

@Throws(Exception::class)
fun <T : Any> Response<T>.getErrorObject(): Result<T> {
    val error = errorBody()?.string()
    val message = StringBuilder()
    error?.let {
        try {

            message.append((JSONObject(it).getJSONObject("error")).get("message"))
            println("message-->: ${message.toString()}")

        } catch (e: JSONException) {
            try {
                println("message-->: ${message.toString()}")

                message.append((JSONObject(it).getString("error")))
            } catch (e: JSONException) {
                e.printStackTrace()
                message.append(SOMETHING_WENT_WRONG_MESSAGE)
            }
        }
    }
    return Result.error(message.toString(), null)
}

private const val NO_INTERNET = 1
private const val SOMETHING_WENT_WRONG = 2
private const val NO_INTERNET_MESSAGE = "No internet connection"
private const val SOMETHING_WENT_WRONG_MESSAGE = "Something went wrong"