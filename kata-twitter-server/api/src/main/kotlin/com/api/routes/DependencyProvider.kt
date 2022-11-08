package com.api.routes

import com.domain.model.follow.FollowService
import com.domain.model.tweet.TweetService
import com.domain.model.user.UserService
import com.storage.repository.user.RedisFollowRepository
import com.storage.repository.user.RedisTweetRepository
import com.storage.repository.user.RedisUserRepository
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

class DependencyProvider {
    val jedisPool = JedisPool(JedisPoolConfig(), "127.0.0.1", 6379)

    fun getUserService(): UserService{
        val userRepository = RedisUserRepository(jedisPool)
        return UserService(userRepository)
    }

    fun getFollowService(): FollowService{
        val userRepository = RedisUserRepository(jedisPool)
        val followRepository = RedisFollowRepository(jedisPool, userRepository)
        return FollowService(followRepository, getUserService())
    }

    fun getTweetService(): TweetService{
        val tweetRepository = RedisTweetRepository(jedisPool)
        return TweetService(tweetRepository, getUserService())
    }

}