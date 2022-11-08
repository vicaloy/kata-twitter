package com.domain.action.follow

import com.domain.model.follow.FollowService
import com.domain.model.user.User

class UserFollowedList(private val followService: FollowService) {

    fun execute(nickname: String): List<User> {
        return followService.getFollowedUsersByNickname(nickname)
    }
}