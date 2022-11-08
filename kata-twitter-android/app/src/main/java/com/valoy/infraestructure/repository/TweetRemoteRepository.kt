package com.valoy.infraestructure.repository

import com.valoy.core.domain.tweet.Tweet
import com.valoy.core.domain.tweet.TweetRepository
import com.valoy.infraestructure.client.TweetClient
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.HttpException
import java.net.SocketTimeoutException

class TweetRemoteRepository(private val tweetClient: TweetClient) : TweetRepository {

    override suspend fun get(nickname: String): List<Tweet>? {
        return try {
            tweetClient.get(nickname).body()?.map { text -> Tweet(text) }
        } catch (ex: SocketTimeoutException) {
            throw TweetRepository.GetTweetsException()
        }
    }

    override suspend fun add(nickname: String, tweet: Tweet) {
        try {
            val nicknameRequest: RequestBody = RequestBody.create(MediaType.parse("text/plain"), nickname)
            val textRequest: RequestBody = RequestBody.create(MediaType.parse("text/plain"), tweet.text)
            tweetClient.add(nicknameRequest, textRequest)
        } catch (ex: SocketTimeoutException) {
            throw TweetRepository.AddTweetException()
        } catch (ex: HttpException) {
            throw TweetRepository.AddTweetException()
        }
    }

}