package com.api.models

import com.domain.model.user.User

@kotlinx.serialization.Serializable
data class RegisterRequest(val name: String, val nickname: String){
    companion object{
        fun mapToUser(registerRequest: RegisterRequest): User {
            return User(registerRequest.name, registerRequest.nickname)
        }

        fun mapToApiUser(users: List<User>): List<RegisterRequest>{
            return users.map { RegisterRequest(it.name, it.nickname) }
        }
    }

}