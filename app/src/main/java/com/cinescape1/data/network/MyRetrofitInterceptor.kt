package com.cinescape1.data.network

import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.utils.Constant
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

 class MyRetrofitInterceptor : Interceptor {
    @Inject
    lateinit var preferences: AppPreferences
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val token = preferences.getString(Constant.status)// get token logic
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", token.toString())
            .build()
        return chain.proceed(newRequest)
    }


}