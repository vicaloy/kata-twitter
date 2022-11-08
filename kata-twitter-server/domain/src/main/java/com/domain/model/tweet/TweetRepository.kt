package com.domain.model.tweet

interface TweetRepository {
    fun add(tweet: Tweet)
    fun getTweetsByNickname(nickname: String): List<String>
}