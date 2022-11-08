package com.valoy.core.actions

import com.valoy.core.domain.user.User
import com.valoy.core.domain.user.UserService
import com.valoy.common.InstantExecutorExtension
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class RegisterUserTest {

    private val name = "Victoria"
    private val nickname = "vicky"
    private val user = User(name, nickname)
    private lateinit var registerUser: RegisterUser

    @RelaxedMockK
    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        registerUser = RegisterUser(userService)
    }

    @Test
    fun `create user`() = runTest {
        coEvery { userService.registerUser(user) } returns Unit
        registerUser.execute(user)
        coVerify(exactly = 1) { userService.registerUser(user) }
    }

    @Test
    fun `cannot register two repeated users`() = runTest {
        coEvery { userService.registerUser(user) } throws RuntimeException()
        assertThrows<RuntimeException> { registerUser.execute(user) }
    }
}
