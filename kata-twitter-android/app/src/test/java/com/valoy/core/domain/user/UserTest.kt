package com.valoy.core.domain.user

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class UserTest{

    @Test
    fun `cannot create user with empty name`() {
        assertThrows<User.EmptyNameException> {
            User("", "vicky")
        }
    }

    @Test
    fun `cannot create user with empty nickname`() {
        assertThrows<User.EmptyNicknameException> {
            User("victoria", "")
        }
    }
}