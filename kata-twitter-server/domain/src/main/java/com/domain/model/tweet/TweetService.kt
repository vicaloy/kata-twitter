package com.domain.model.tweet

import com.domain.model.user.UserService

class TweetService(
    private val tweetRepository: TweetRepository,
    private val userService: UserService
) {

    fun createTweet(nickname: String, text: String){
        if(canUserTweet(nickname)){
            tweetRepository.add(Tweet(userService.findUserByNickname(nickname)!!, text))
        }
    }

    fun getTweetsByNickname(nickname: String): List<String>{
        return tweetRepository.getTweetsByNickname(nickname)
    }

    private fun canUserTweet(nickname: String): Boolean {
        return userService.isUserExisting(nickname)
    }

}