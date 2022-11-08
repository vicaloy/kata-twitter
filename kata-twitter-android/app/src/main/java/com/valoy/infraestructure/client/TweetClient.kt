package com.valoy.infraestructure.client

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface TweetClient {
    @GET("/tweets")
    suspend fun get(@Query("nickname") nickname: String): Response<List<String>>

    @Multipart
    @POST("/tweet")
    suspend fun add(@Part("nickname") nickname: RequestBody, @Part("text") text: RequestBody): ResponseBody


}