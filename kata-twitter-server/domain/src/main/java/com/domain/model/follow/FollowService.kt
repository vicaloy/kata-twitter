package com.domain.model.follow

import com.domain.model.user.User
import com.domain.model.user.UserService

class FollowService(
    private val followRepository: FollowRepository,
    private val userService: UserService
) {

    fun followUser(followedNickname: String, followerNickname: String) {
        if (canFollowUser(followedNickname, followerNickname)) {
            val follow = Follow(
                userService.findUserByNickname(followedNickname)!!,
                userService.findUserByNickname(followerNickname)!!
            )
            followRepository.add(follow)
        }
    }


    fun getFollowedUsersByNickname(nickname: String): List<User> {
        return followRepository.getFollowedByNickname(nickname)
    }

    fun getFollowerUsersByNickname(nickname: String): List<User> {
        return followRepository.getFollowersByNickname(nickname)
    }

    private fun canFollowUser(followedNickname: String, followerNickname: String): Boolean {
        return userService.isUserExisting(followedNickname) && userService.isUserExisting(followerNickname)
    }

}