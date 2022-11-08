package com.domain.action.user

import com.domain.model.user.UserService

class UpdateUser(private val userService: UserService) {

    fun execute(nickname: String, name: String) {
        userService.updateUser(nickname, name)
    }
}