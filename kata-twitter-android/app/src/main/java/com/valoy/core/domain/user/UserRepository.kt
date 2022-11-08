package com.valoy.core.domain.user

interface UserRepository {
    suspend fun add(user: User)

    class AddUserException: RuntimeException()
}