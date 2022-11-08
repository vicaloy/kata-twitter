package com.valoy.core.domain.tweet

class TweetService(private val tweetRepository: TweetRepository) {
    suspend fun getTweets(nickname: String): List<Tweet>? {
        return tweetRepository.get(nickname)
    }

    suspend fun createTweet(nickname: String, tweet: Tweet) {
        tweetRepository.add(nickname, tweet)
    }
}