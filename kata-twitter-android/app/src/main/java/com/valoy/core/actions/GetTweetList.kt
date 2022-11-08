package com.valoy.core.actions

import com.valoy.core.domain.tweet.Tweet
import com.valoy.core.domain.tweet.TweetService

class GetTweetList(private val tweetService: TweetService) {
    suspend fun execute(nickname: String): List<Tweet>? {
        return tweetService.getTweets(nickname)
    }
}