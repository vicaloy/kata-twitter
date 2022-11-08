package com.valoy.presentation.ui.register.models

import com.valoy.core.domain.user.User

data class UserDTO(private val name: String, private val nickname: String){
    companion object{
        fun toUser(user: UserDTO): User = User(user.name, user.nickname)
    }
}