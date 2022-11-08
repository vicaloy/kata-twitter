package com.valoy.infraestructure.client

import com.valoy.infraestructure.request.RegisterUserRequest
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface UserClient {
    @POST("/user")
    suspend fun add(@Body request: RegisterUserRequest): ResponseBody
}