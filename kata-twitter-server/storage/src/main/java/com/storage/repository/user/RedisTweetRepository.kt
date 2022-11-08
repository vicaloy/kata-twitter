package com.storage.repository.user

import com.domain.model.tweet.Tweet
import com.domain.model.tweet.TweetRepository
import com.storage.repository.models.RedisTweet
import org.apache.commons.lang3.SerializationUtils
import org.apache.commons.lang3.SerializationUtils.serialize
import redis.clients.jedis.*


class RedisTweetRepository(private val jedisPool: JedisPool) : TweetRepository {

    private val TWEETS_COLLECTION = "tweets_collection"
    //private val jedisPool = JedisPool(JedisPoolConfig(), "127.0.0.1", 6379)

    override fun add(tweet: Tweet) {
        try {
            tryAdd(tweet)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getTweetsByNickname(nickname: String): List<String> {
        return try {
            tryGetTweets(nickname)
        } catch (ex: Exception) {
            ex.printStackTrace()
            emptyList()
        }
    }

    private fun tryAdd(tweet: Tweet) {
        jedisPool.resource.use { jedis ->
            val redisTweet = RedisTweet(tweet.user.nickname, tweet.text)
            val redisTweetSerialized = serialize(redisTweet)
            jedis.sadd(TWEETS_COLLECTION.toByteArray(), redisTweetSerialized)
        }
    }

    private fun tryGetTweets(nickname: String): List<String> {
        val redisTweets = mutableListOf<RedisTweet>()
        jedisPool.resource.use { jedis ->
            val tweetsMembers = jedis.smembers(TWEETS_COLLECTION.toByteArray())
            for (tweet in tweetsMembers) {
                val redisTweet: RedisTweet = SerializationUtils.deserialize(tweet)
                redisTweets.add(redisTweet)
            }
        }
        val userTweets = redisTweets.filter { it.nickname == nickname }
        return getMapTweets(userTweets)
    }

    private fun getMapTweets(redisTweets: List<RedisTweet>): List<String> {
        val tweets = mutableListOf<String>()
        redisTweets.forEach {
            tweets.add(it.text)
        }
        return tweets
    }
}