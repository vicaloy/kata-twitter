package com.valoy.infraestructure.request

import com.valoy.core.domain.user.User

data class RegisterUserRequest(val name: String, val nickname: String){
    companion object {
        fun fromUser(user: User) = RegisterUserRequest(user.name, user.nickname)
    }
}