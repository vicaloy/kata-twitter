package com.domain.action.tweet

import com.domain.model.tweet.TweetService

class GetTweetList(private val tweetService: TweetService) {

    fun execute(nickname: String): List<String> {
        return tweetService.getTweetsByNickname(nickname)
    }
}