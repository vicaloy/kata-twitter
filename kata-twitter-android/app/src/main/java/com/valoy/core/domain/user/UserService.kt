package com.valoy.core.domain.user

class UserService(private val userRepository: UserRepository) {
    suspend fun registerUser(user: User) {
        userRepository.add(user)
    }
}