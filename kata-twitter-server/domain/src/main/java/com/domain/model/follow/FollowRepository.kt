package com.domain.model.follow

import com.domain.model.user.User

interface FollowRepository {
    fun add(follow: Follow)
    fun getFollowersByNickname(nickname: String): List<User>
    fun getFollowedByNickname(nickname: String): List<User>
}