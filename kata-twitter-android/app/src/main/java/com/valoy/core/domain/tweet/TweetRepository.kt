package com.valoy.core.domain.tweet

import kotlin.RuntimeException

interface TweetRepository {
    suspend fun get(nickname: String): List<Tweet>?
    suspend fun add(nickname: String, tweet: Tweet)

    class GetTweetsException: RuntimeException()
    class AddTweetException : RuntimeException()
}