package com.valoy.presentation.ui.tweets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.valoy.core.actions.CreateTweet
import com.valoy.core.actions.GetTweetList

@Suppress("UNCHECKED_CAST")
class TweetsViewModelFactory(
    private val nickname: String,
    private val getTweetList: GetTweetList,
    private val createTweet: CreateTweet
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TweetsViewModel(nickname, getTweetList, createTweet) as T
}
