package com.storage.repository.user

import com.domain.model.follow.Follow
import com.domain.model.follow.FollowRepository
import com.domain.model.user.User
import com.domain.model.user.UserRepository
import com.storage.repository.models.RedisFollow
import org.apache.commons.lang3.SerializationUtils.deserialize
import org.apache.commons.lang3.SerializationUtils.serialize
import redis.clients.jedis.*

class RedisFollowRepository(
    private val jedisPool: JedisPool,
    private val userRepository: UserRepository) : FollowRepository {

    private val FOLLOWING_COLLECTION = "follows_collection"
    //private val jedisPool = JedisPool(JedisPoolConfig(), "127.0.0.1", 6379)


    override fun add(follow: Follow) {
        try {
            tryFollow(follow)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getFollowersByNickname(nickname: String): List<User> {
        val following = getFollows()
        val followers = following.filter { it.followed.nickname == nickname }
        val users = followers.map { it.follower }
        return users
    }

    override fun getFollowedByNickname(nickname: String): List<User> {
        val following = getFollows()
        val followed = following.filter { it.follower.nickname == nickname }
        val users = followed.map { it.followed }
        return users
    }

    private fun getFollows(): List<Follow> {
        return try {
            tryGetFollows()
        } catch (ex: Exception) {
            ex.printStackTrace()
            emptyList()
        }
    }

    private fun tryGetFollows(): List<Follow> {
        val redisFollows = mutableListOf<RedisFollow>()
        jedisPool.resource.use { jedis ->
            val followsMembers = jedis.smembers(FOLLOWING_COLLECTION.toByteArray())
            for (follow in followsMembers) {
                val redisFollow: RedisFollow = deserialize(follow)
                redisFollows.add(redisFollow)
            }
        }

        return getMapFollows(redisFollows)
    }

    private fun getMapFollows(redisFollows: List<RedisFollow>): List<Follow> {
        val follows = mutableListOf<Follow>()
        redisFollows.forEach {
            val followed = userRepository.findByNickname(it.followed)
            val follower = userRepository.findByNickname(it.follower)
            if (followed != null && follower != null)
                follows.add(Follow(followed, follower))
        }
        return follows
    }

    private fun tryFollow(follow: Follow) {
        jedisPool.resource.use { jedis ->
            val redisFollow =
                RedisFollow(follow.followed.nickname, follow.follower.nickname)
            val redisFollowSerialized = serialize(redisFollow)
            jedis.sadd(FOLLOWING_COLLECTION.toByteArray(), redisFollowSerialized)
        }
    }


}