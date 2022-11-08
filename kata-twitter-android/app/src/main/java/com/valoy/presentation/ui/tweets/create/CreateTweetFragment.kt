package com.valoy.presentation.ui.tweets.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.valoy.R
import com.valoy.databinding.CreateTweetFragmentBinding
import com.valoy.infraestructure.di.DependencyProvider
import com.valoy.presentation.ui.tweets.TweetsViewModel
import com.valoy.presentation.ui.tweets.TweetsViewModelFactory
import com.valoy.presentation.ui.tweets.create.models.CreateTweetResult
import com.valoy.presentation.ui.tweets.list.ListTweetFragmentArgs

class CreateTweetFragment : Fragment() {

    private val dependencyProvider = DependencyProvider()

    private val args: ListTweetFragmentArgs by navArgs()

    private var _binding: CreateTweetFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TweetsViewModel by activityViewModels {
        TweetsViewModelFactory(
            args.nickname,
            dependencyProvider.getTweetList(),
            dependencyProvider.getCreateTweet()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateTweetFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateTweetClick()
        observeResult()
        observeGoTweetList()
    }

    private fun onCreateTweetClick() {
        binding.createButton.setOnClickListener {
            viewModel.onCreateTweetClick(binding.textTextField.editText?.text.toString())
        }

    }

    private fun observeGoTweetList() {
        viewModel.onGoTweetList().observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }

    private fun observeResult() {
        viewModel.onCreateTweetResult().observe(viewLifecycleOwner) { result ->
            showSnackbar(getTextResult(result))
        }
    }

    private fun getTextResult(result: CreateTweetResult): String {
        return when (result) {
            CreateTweetResult.CreatedSuccessful -> resources.getString(R.string.tweet_created)
            CreateTweetResult.ServerError -> resources.getString(R.string.server_error)
            CreateTweetResult.EmptyTextTweet -> resources.getString(R.string.empty_tweet)
        }
    }

    private fun showSnackbar(text: String) {
        val snackbar = Snackbar.make(binding.container, text, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}