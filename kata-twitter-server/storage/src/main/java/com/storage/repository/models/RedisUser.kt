package com.storage.repository.models

import com.domain.model.user.User

data class RedisUser(val name: String, val nickname: String) : java.io.Serializable {
    companion object {
        fun mapToUser(redisUser: RedisUser): User {
            return User(redisUser.name, redisUser.nickname)
        }

        fun mapToRedisUser(user: User): RedisUser {
            return RedisUser(user.name, user.nickname)
        }
    }
}

