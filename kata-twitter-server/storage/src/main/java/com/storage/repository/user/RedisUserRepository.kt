package com.storage.repository.user

import com.domain.model.user.User
import com.domain.model.user.UserRepository
import com.storage.repository.models.RedisUser
import org.apache.commons.lang3.SerializationUtils.deserialize
import org.apache.commons.lang3.SerializationUtils.serialize
import redis.clients.jedis.*

class RedisUserRepository(private val jedisPool: JedisPool) : UserRepository {

    private val USERS_COLLECTION = "users_collection"
    //private val jedisPool = JedisPool(JedisPoolConfig(), "127.0.0.1", 6379)

    override fun add(user: User) {
        try {
            tryAdd(user)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun findByNickname(nickname: String): User? {
        return try {
            tryFindUserByNickname(nickname)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }

    }

    override fun updateName(nickname: String, name: String) {
        try {
            val user = User(nickname, name)
            tryAdd(user)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun tryAdd(user: User) {
        jedisPool.resource.use { jedis ->
            val nickname = user.nickname.toByteArray()
            val redisUser: ByteArray = serialize(RedisUser.mapToRedisUser(user))
            jedis.hset(USERS_COLLECTION.toByteArray(), nickname, redisUser)
        }
    }

    private fun tryFindUserByNickname(nickname: String): User? {
        var user: User? = null
        jedisPool.resource.use { jedis ->
            val userFounds = jedis.hmget(USERS_COLLECTION.toByteArray(), nickname.toByteArray())
            val userFound = userFounds.firstOrNull()
            if (userFound != null) {
                val rediUser: RedisUser = deserialize(userFound)
                user = RedisUser.mapToUser(rediUser)
            }
        }
        return user
    }

}