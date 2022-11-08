package com.storage.repository.models


data class RedisFollow(val followed: String, val follower: String) : java.io.Serializable