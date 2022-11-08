package com.valoy.presentation.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.valoy.R
import com.valoy.databinding.RegisterFragmentBinding
import com.valoy.infraestructure.di.DependencyProvider
import com.valoy.presentation.ui.register.models.RegisterUserResult
import com.valoy.presentation.ui.register.models.UserDTO


class RegisterFragment : Fragment() {

    private val dependencyProvider = DependencyProvider()
    private var _binding: RegisterFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(dependencyProvider.getRegisterUser())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegisterFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMessage()
        observeGoToTweets()
        onRegisterClick()
    }

    private fun onRegisterClick() {
        binding.registerButton.setOnClickListener {
            /*val userRegister = UserDTO(
                binding.nameTextField.editText?.text.toString(),
                binding.nicknameTextField.editText?.text.toString()
            )
            viewModel.onRegisterUserClick(userRegister)*/
            goToTweets("Pepe")
        }
    }

    private fun observeMessage() {
        viewModel.onResultRegister().observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                showSnackbar(getTextResult(result))
            }
        }
    }

    private fun getTextResult(registerUserResult: RegisterUserResult): String {
        return when (registerUserResult) {
            RegisterUserResult.RegisteredSuccessful -> resources.getString(R.string.user_registered)
            RegisterUserResult.ServerError -> resources.getString(R.string.server_error)
            RegisterUserResult.NicknameEmptyError -> resources.getString(R.string.empty_nickname)
            RegisterUserResult.NameEmptyError -> resources.getString(R.string.empty_name)
        }
    }

    private fun observeGoToTweets() {
        viewModel.onGoToTweets().observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                goToTweets(binding.nicknameTextField.editText?.text.toString())
            }
        }
    }

    private fun showSnackbar(text: String) {
        val snackbar = Snackbar.make(binding.container, text, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    private fun goToTweets(nickname: String) {
        findNavController().navigate(RegisterFragmentDirections.listTweets(nickname))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        Log.d("RegisterFragment", "onResume")
    }

}