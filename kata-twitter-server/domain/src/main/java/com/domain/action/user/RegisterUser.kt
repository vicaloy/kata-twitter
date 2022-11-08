package com.domain.action.user

import com.domain.model.user.User
import com.domain.model.user.UserService

class RegisterUser(private val userService: UserService) {

    fun execute(user: User) {
        userService.registerUser(user)
    }
}