package com.valoy.presentation.ui.register

import com.valoy.common.InstantExecutorExtension
import com.valoy.core.actions.RegisterUser
import com.valoy.core.domain.user.User
import com.valoy.core.domain.user.UserRepository
import com.valoy.presentation.extensions.getValueForTest
import com.valoy.presentation.ui.register.models.RegisterUserResult
import com.valoy.presentation.ui.register.models.UserDTO
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class RegisterViewModelTest {
    private val user = User("vicky", "victoria")
    private val userRegister = UserDTO("vicky", "victoria")
    private lateinit var registerViewModel: RegisterViewModel

    @RelaxedMockK
    private lateinit var registerUser: RegisterUser

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        registerViewModel = RegisterViewModel(registerUser)
    }

    @Test
    fun `get result when register user correctly`() = runTest {
        `given user registered correctly`()
        `when user click register`()
        `then get result`(RegisterUserResult.RegisteredSuccessful)
    }

    @Test
    fun `get result when register user with empty name`() = runTest {
        registerViewModel.onRegisterUserClick(UserDTO("", "Victoria"))
        `then get result`(RegisterUserResult.NameEmptyError)
    }

    @Test
    fun `get result when register user with empty nickname`() = runTest {
        registerViewModel.onRegisterUserClick(UserDTO("vicky", ""))
        `then get result`(RegisterUserResult.NicknameEmptyError)
    }

    @Test
    fun `go to tweets when register user correctly`() = runTest {
        `given user registered correctly`()
        `when user click register`()
        `then go to tweets`()
    }

    @Test
    fun `register user fails`() = runTest {
        `given user registered incorrectly`()
        `when user click register`()
        `then get result`(RegisterUserResult.ServerError)
    }

    private fun `given user registered incorrectly`() {
        coEvery { registerUser.execute(user) } throws UserRepository.AddUserException()
    }

    private fun `given user registered correctly`() {
        coEvery { registerUser.execute(user) } returns Unit
    }

    private fun `when user click register`() {
        registerViewModel.onRegisterUserClick(userRegister)
    }

    private fun `then get result`(expected: RegisterUserResult) {
        val result = registerViewModel.onResultRegister().getValueForTest().getContentIfNotHandled()
        assertEquals(expected, result)
    }

    private fun `then go to tweets`() {
        val result = registerViewModel.onGoToTweets().getValueForTest().getContentIfNotHandled()
        assertEquals(true, result)
    }


}