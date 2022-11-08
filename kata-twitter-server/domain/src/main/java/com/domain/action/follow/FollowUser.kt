package com.domain.action.follow

import com.domain.model.follow.FollowService

class FollowUser(private val followService: FollowService) {

    fun execute(followedNickname: String, followerNickname: String) {
        followService.followUser(followedNickname, followerNickname)
    }
}