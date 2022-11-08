package com.valoy.presentation.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valoy.core.actions.RegisterUser
import com.valoy.core.domain.user.User
import com.valoy.presentation.ui.common.Event
import com.valoy.presentation.ui.common.runSuspendCatching
import com.valoy.presentation.ui.register.models.RegisterUserResult
import com.valoy.presentation.ui.register.models.UserDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUser: RegisterUser) : ViewModel() {

    private val registerUserResult: MutableLiveData<Event<RegisterUserResult>> by lazy {
        MutableLiveData<Event<RegisterUserResult>>()
    }

    private val goToTweets: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    fun onRegisterUserClick(user: UserDTO) {

        viewModelScope.launch(Dispatchers.IO) {

            runSuspendCatching { registerUser.execute(UserDTO.toUser(user)) }
                .onSuccess {
                    registerUserResult.postValue(Event(RegisterUserResult.RegisteredSuccessful))
                    goToTweets.postValue(Event(true))

                }
                .onFailure { throwable -> registerUserResult.postValue(Event(registerUserResult(throwable))) }
        }
    }

    private fun registerUserResult(throwable: Throwable): RegisterUserResult {
        return when (throwable) {
            is User.EmptyNameException -> RegisterUserResult.NameEmptyError
            is User.EmptyNicknameException -> RegisterUserResult.NicknameEmptyError
            else -> RegisterUserResult.ServerError
        }
    }

    fun onResultRegister(): LiveData<Event<RegisterUserResult>> = registerUserResult
    fun onGoToTweets(): LiveData<Event<Boolean>> = goToTweets
}