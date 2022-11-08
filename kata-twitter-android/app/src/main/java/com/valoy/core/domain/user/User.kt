package com.valoy.core.domain.user

import java.lang.RuntimeException

data class User(val name: String, val nickname: String) {

    init {
        validateName()
        validateNickname()
    }

    private fun validateName(){
        if (name.isEmpty())
            throw EmptyNameException()
    }

    private fun validateNickname(){
        if(nickname.isEmpty())
            throw EmptyNicknameException()
    }

    class EmptyNameException: RuntimeException()
    class EmptyNicknameException: RuntimeException()
}