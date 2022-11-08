package com.valoy.presentation.ui.tweets.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.valoy.R
import com.valoy.databinding.ListTweetFragmentBinding
import com.valoy.infraestructure.di.DependencyProvider
import com.valoy.presentation.ui.tweets.TweetsViewModel
import com.valoy.presentation.ui.tweets.TweetsViewModelFactory
import com.valoy.presentation.ui.tweets.list.models.ListTweetResult


class ListTweetFragment : Fragment() {

    private val dependencyProvider = DependencyProvider()

    private val args: ListTweetFragmentArgs by navArgs()

    private var _binding: ListTweetFragmentBinding? = null
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
        _binding = ListTweetFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeTweets()
        observeResult()
        onGoToCreateTweetClick()
        setTweetsRecyclerDivider()
    }

    private fun onGoToCreateTweetClick() {
        binding.floatingCreateTweet.setOnClickListener {
            findNavController().navigate(ListTweetFragmentDirections.createTweet(args.nickname))
        }
    }

    private fun observeResult() {
        viewModel.onListTweetResult().observe(viewLifecycleOwner) { result ->
            showSnackbar(getTextResult(result))
        }
    }

    private fun getTextResult(listRegisterResult: ListTweetResult): String {
        return when (listRegisterResult) {
            ListTweetResult.ServerError -> resources.getString(R.string.server_error)
        }
    }

    private fun setTweetsRecyclerDivider(){
        val dividerItemDecoration = DividerItemDecoration(
            binding.tweetsRecycler.context,
            LinearLayoutManager.VERTICAL
        )
        binding.tweetsRecycler.addItemDecoration(dividerItemDecoration)
    }
    private fun observeTweets() {
        viewModel.onTweets().observe(viewLifecycleOwner) { tweets ->
            val tweetsAdapter = TweetsAdapter(tweets)
            binding.tweetsRecycler.adapter = tweetsAdapter
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