package com.domain.model.user

interface UserRepository {
    fun add(user: User)

    fun updateName(nickname: String, name: String)
    fun findByNickname(nickname: String): User?
}
