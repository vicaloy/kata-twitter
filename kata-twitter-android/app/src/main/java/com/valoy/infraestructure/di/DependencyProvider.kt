package com.valoy.infraestructure.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.valoy.core.actions.CreateTweet
import com.valoy.core.actions.RegisterUser
import com.valoy.core.actions.GetTweetList
import com.valoy.core.domain.tweet.TweetRepository
import com.valoy.core.domain.tweet.TweetService
import com.valoy.core.domain.user.UserRepository
import com.valoy.core.domain.user.UserService
import com.valoy.infraestructure.client.TweetClient
import com.valoy.infraestructure.client.UserClient
import com.valoy.infraestructure.repository.TweetRemoteRepository
import com.valoy.infraestructure.repository.UserRemoteRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class DependencyProvider {

    private val apiEndPoint = "http://192.168.1.6:8080"

    private fun getGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    private fun getGsonConverterFactory(gson: Gson) = GsonConverterFactory.create(gson)

    private fun getOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    private fun getRestAdapter(
        apiEndPoint: String,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(apiEndPoint)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()

    private fun getUserClient(restAdapter: Retrofit): UserClient = restAdapter.create()

    private fun getTweetClient(restAdapter: Retrofit): TweetClient = restAdapter.create()

    private fun getTweetRemoteRepository(tweetClient: TweetClient): TweetRemoteRepository =
        TweetRemoteRepository(tweetClient)

    private fun getUserRemoteRepository(userClient: UserClient): UserRemoteRepository =
        UserRemoteRepository(userClient)

    private fun getUserService(userRepository: UserRepository): UserService = UserService(userRepository)

    private fun getTweetService(tweetRepository: TweetRepository): TweetService =
        TweetService(tweetRepository)

    fun getRegisterUser(): RegisterUser {
        val gson = getGson()
        val gsonConverterFactory = getGsonConverterFactory(gson)
        val okHttpClient = getOkHttpClient()
        val retrofit = getRestAdapter(apiEndPoint, okHttpClient, gsonConverterFactory)
        val userClient = getUserClient(retrofit)
        val userRepository = getUserRemoteRepository(userClient)
        val userService = getUserService(userRepository)
        return RegisterUser(userService)
    }

    fun getTweetList(): GetTweetList {
        val gson = getGson()
        val gsonConverterFactory = getGsonConverterFactory(gson)
        val okHttpClient = getOkHttpClient()
        val retrofit = getRestAdapter(apiEndPoint, okHttpClient, gsonConverterFactory)
        val tweetClient = getTweetClient(retrofit)
        val tweetRepository = getTweetRemoteRepository(tweetClient)
        val tweetService = getTweetService(tweetRepository)
        return GetTweetList(tweetService)
    }

    fun getCreateTweet(): CreateTweet{
        val gson = getGson()
        val gsonConverterFactory = getGsonConverterFactory(gson)
        val okHttpClient = getOkHttpClient()
        val retrofit = getRestAdapter(apiEndPoint, okHttpClient, gsonConverterFactory)
        val tweetClient = getTweetClient(retrofit)
        val tweetRepository = getTweetRemoteRepository(tweetClient)
        val tweetService = getTweetService(tweetRepository)
        return CreateTweet(tweetService)
    }

}