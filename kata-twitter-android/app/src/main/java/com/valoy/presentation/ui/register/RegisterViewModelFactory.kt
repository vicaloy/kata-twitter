package com.valoy.presentation.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.valoy.core.actions.RegisterUser
import com.valoy.presentation.ui.tweets.TweetsViewModel

class RegisterViewModelFactory(private val registerUser: RegisterUser): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = RegisterViewModel(registerUser) as T
}
