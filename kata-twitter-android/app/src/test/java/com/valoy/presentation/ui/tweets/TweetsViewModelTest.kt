package com.valoy.presentation.ui.tweets

import com.valoy.common.InstantExecutorExtension
import com.valoy.core.actions.CreateTweet
import com.valoy.core.actions.GetTweetList
import com.valoy.core.domain.tweet.Tweet
import com.valoy.core.domain.tweet.TweetRepository
import com.valoy.presentation.extensions.getValueForTest
import com.valoy.presentation.ui.tweets.create.models.CreateTweetResult
import com.valoy.presentation.ui.tweets.list.models.ListTweetResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class TweetsViewModelTest{
    private val tweet = Tweet("text")
    private val tweets = listOf(tweet)
    private val emptyTweets = listOf<Tweet>()
    private val nickname = "mari"
    private val text = "text"
    private lateinit var tweetViewModel: TweetsViewModel

    @RelaxedMockK
    private lateinit var getTweetList: GetTweetList

    @RelaxedMockK
    private lateinit var createTweet: CreateTweet

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        tweetViewModel = TweetsViewModel(nickname, getTweetList, createTweet)
    }


    //todo cambiar nomenclatura de funciones a camelcase revisar namings
    @Test
    fun `invoke create tweet`() = runTest {
        `given nickname and tweet`()
        `when click create tweet`(text)
        `then invoke create tweet`()
    }

    @Test
    fun `go to tweet list when tweet is created`() = runTest {
        `given nickname and tweet`()
        `when click create tweet`(text)
        `then go to tweet list`()
    }

    @Test
    fun `notify created successful tweet when tweet is created`() = runTest {
        `given successful create tweet response`()
        `when click create tweet`(text)
        `then notify`(CreateTweetResult.CreatedSuccessful)
    }

    @Test
    fun `get result create tweet fails`() = runTest {
        `given nickname and text create tweet throw error`()
        `when click create tweet`(text)
        `then notify`(CreateTweetResult.ServerError)
    }

    @Test
    fun `get result create tweet empty fails`() = runTest {
        val givenEmptyText = ""
        `when click create tweet`(givenEmptyText)
        `then notify`(CreateTweetResult.EmptyTextTweet)
    }

    @Test
    fun `return list of tweets`() = runTest {
        `given nickname return tweets`(tweets)
        `when create tweets view model`()
        `then return list of tweets`(tweets)
    }

    @Test
    fun `return list of tweets empty`() = runTest {
        `given nickname return tweets`(emptyTweets)
        `when create tweets view model`()
        `then return list of tweets`(emptyTweets)
    }

    @Test
    fun `get result when returning list of tweets fails`() = runTest {
        `given nickname return tweets throw error`()
        `when create tweets view model`()
        `then get server error result`()
    }

    private suspend fun `given nickname and text create tweet throw error`() {
        coEvery { createTweet.execute(nickname, tweet) } throws TweetRepository.AddTweetException()
    }


    private fun `then notify`(expected: CreateTweetResult) {
        val result = tweetViewModel.onCreateTweetResult().getValueForTest()
        assertEquals(expected, result)
    }

    private fun `then go to tweet list`() {
        val result = tweetViewModel.onGoTweetList().getValueForTest()
        assertNotNull(result)
    }

    private fun `then get server error result`() {
        val result = tweetViewModel.onListTweetResult().getValueForTest()
        assertEquals(ListTweetResult.ServerError, result)
    }

    private suspend fun `given nickname return tweets throw error`() {
        coEvery { getTweetList.execute(nickname) } throws  TweetRepository.GetTweetsException()
    }

    private suspend fun `given nickname return tweets`(list: List<Tweet>) {
        coEvery { getTweetList.execute(nickname) } returns list
    }

    private suspend fun `given nickname and tweet`() {
        coEvery { createTweet.execute(nickname, tweet) } returns Unit
    }

    private suspend fun `given successful create tweet response`() {
        coEvery { createTweet.execute(nickname, tweet) } returns Unit
    }

    private fun `when create tweets view model`() {
        tweetViewModel = TweetsViewModel(nickname, getTweetList, createTweet)
    }

    private fun `when click create tweet`(text: String) {
        tweetViewModel.onCreateTweetClick(text)
    }

    private fun `then invoke create tweet`() {
        coVerify(exactly = 1) { createTweet.execute(nickname, tweet) }
    }

    private fun `then return list of tweets`(expected: List<Tweet>) {
        assertEquals(expected, tweetViewModel.onTweets().getValueForTest())
    }
}