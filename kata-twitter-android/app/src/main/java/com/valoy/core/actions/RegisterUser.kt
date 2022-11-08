package com.valoy.core.actions

import com.valoy.core.domain.user.User
import com.valoy.core.domain.user.UserService

class RegisterUser(private val userService: UserService) {
    suspend fun execute(user: User) {
        userService.registerUser(user)
    }
}