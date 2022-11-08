package com.domain.action.follow

import com.domain.model.follow.FollowService
import com.domain.model.user.User

class UserFollowerList(private val followService: FollowService) {

    fun execute(nickname: String): List<User> {
        return followService.getFollowerUsersByNickname(nickname)
    }
}