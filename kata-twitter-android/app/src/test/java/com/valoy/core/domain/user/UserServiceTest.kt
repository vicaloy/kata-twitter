package com.valoy.core.domain.user

import com.valoy.common.InstantExecutorExtension
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class UserServiceTest {

    private val name = "Victoria"
    private val nickname = "vicky"
    private val user = User(name, nickname)
    private lateinit var userService: UserService

    @RelaxedMockK
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        userService = UserService(userRepository)
    }

    @Test
    fun `create user`() = runTest {
        `given user added correctly`()
        `when user is registered`()
        `then user is added`()
    }

    @Test
    fun `throw error when add user fails`() = runTest {
        coEvery { userRepository.add(user) } throws UserRepository.AddUserException()
        assertThrows<UserRepository.AddUserException> {  `when user is registered`() }
    }

    private fun `given user added correctly`(){
        coEvery { userRepository.add(user) } returns Unit
    }

    private suspend fun `when user is registered`(){
        userService.registerUser(user)
    }

    private suspend fun `then user is added`(){
        coVerify(exactly = 1) { userRepository.add(user) }
    }
}
