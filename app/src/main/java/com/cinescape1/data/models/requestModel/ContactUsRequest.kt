package com.cinescape1.data.models.requestModel

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class ContactUsRequest(
    val email: RequestBody,
    val mobile: RequestBody,
    val msg: RequestBody,
    val name: RequestBody,
    val file: MultipartBody.Part
)