package com.domain.action.tweet

import com.domain.model.tweet.TweetService

class CreateTweet(private val tweetService: TweetService) {
    fun execute(nickname: String, text: String) {
        tweetService.createTweet(nickname, text)
    }
}