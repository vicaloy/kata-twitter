package com.valoy.presentation.ui.tweets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valoy.core.actions.CreateTweet
import com.valoy.core.actions.GetTweetList
import com.valoy.core.domain.tweet.Tweet
import com.valoy.presentation.ui.common.runSuspendCatching
import com.valoy.presentation.ui.tweets.create.models.CreateTweetResult
import com.valoy.presentation.ui.tweets.list.models.ListTweetResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TweetsViewModel(
    private val nickname: String,
    private val getTweetList: GetTweetList,
    private val createTweet: CreateTweet
) : ViewModel() {

    init {
        listTweets()
    }


    private val listTweetsResult: MutableLiveData<ListTweetResult> by lazy {
        MutableLiveData<ListTweetResult>()
    }

    private val crateTweetsResult: MutableLiveData<CreateTweetResult> by lazy {
        MutableLiveData<CreateTweetResult>()
    }

    private val tweets: MutableLiveData<List<Tweet>> by lazy {
        MutableLiveData<List<Tweet>>()
    }

    private val goTweetList: MutableLiveData<Unit> by lazy {
        MutableLiveData<Unit>()
    }

    private fun listTweets() {
        viewModelScope.launch(Dispatchers.IO) {

            runSuspendCatching { getTweetList.execute(nickname) }
                .onSuccess { list -> tweets.postValue(list) }
                .onFailure { listTweetsResult.postValue(ListTweetResult.ServerError) }
        }
    }

    fun onCreateTweetClick(text: String) {

        viewModelScope.launch(Dispatchers.IO) {

            runSuspendCatching { createTweet.execute(nickname, Tweet(text)) }
                .onSuccess {
                    crateTweetsResult.postValue(CreateTweetResult.CreatedSuccessful)
                    goTweetList.postValue(Unit)
                    addTweetToList(text)

                }
                .onFailure { throwable -> crateTweetsResult.postValue(createTweetFails(throwable)) }
        }
    }

    private fun addTweetToList(text: String) {
        val list = tweets.value?.toMutableList()
        list?.add(Tweet(text))
        tweets.postValue(list)
    }

    private fun createTweetFails(throwable: Throwable): CreateTweetResult {
        return if (throwable is Tweet.EmptyTextException)
            CreateTweetResult.EmptyTextTweet
        else
            CreateTweetResult.ServerError
    }

    fun onListTweetResult(): LiveData<ListTweetResult> = listTweetsResult
    fun onTweets(): LiveData<List<Tweet>> = tweets
    fun onCreateTweetResult(): LiveData<CreateTweetResult> = crateTweetsResult
    fun onGoTweetList(): LiveData<Unit> = goTweetList
}
