package com.valoy.core.actions

import com.valoy.core.domain.tweet.Tweet
import com.valoy.core.domain.tweet.TweetService

class CreateTweet(private val tweetService: TweetService) {
    suspend fun execute(nickname: String, tweet: Tweet) {
        tweetService.createTweet(nickname, tweet)
    }
}