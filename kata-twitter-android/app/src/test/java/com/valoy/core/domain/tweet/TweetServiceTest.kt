package com.valoy.core.domain.tweet

import com.valoy.common.InstantExecutorExtension
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class TweetServiceTest {

    private val tweet = Tweet("text")
    private val tweets = listOf(tweet)
    private val emptyTweets = listOf<Tweet>()
    private val nickname = "mari"
    private lateinit var tweetService: TweetService

    @RelaxedMockK
    private lateinit var tweetRepository: TweetRepository

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        tweetService = TweetService(tweetRepository)
    }

    @Test
    fun `create tweet`() = runTest {
        `given nickname and text create tweet`()
        `when create tweet`()
        `then verify tweet is created`()
    }

    @Test
    fun `create tweet fails`() = runTest {
        `given nickname and text throw error`()
        assertThrows<TweetRepository.AddTweetException>{
            tweetService.createTweet(nickname, tweet)
        }
    }

    @Test
    fun `return list of tweets`() = runTest {
        `given nickname return list`(tweets)
        val result = `when return tweets`()
        `then return list of tweets`(tweets, result)
    }

    @Test
    fun `return list of tweets empty`() = runTest {
        `given nickname return list`(emptyTweets)
        val result = `when return tweets`()
        `then return list of tweets`(emptyTweets, result)
    }

    @Test
    fun `throw error when list tweets fails`() = runTest {
        coEvery { tweetRepository.get(nickname) } throws TweetRepository.GetTweetsException()
        assertThrows<TweetRepository.GetTweetsException> { tweetService.getTweets(nickname) }
    }

    private suspend fun `given nickname return list`(list: List<Tweet>) {
        coEvery { tweetRepository.get(nickname) } returns list
    }

    private suspend fun `when return tweets`(): List<Tweet>? {
        return tweetService.getTweets(nickname)
    }

    private fun `then return list of tweets`(expected: List<Tweet>, actual: List<Tweet>?) {
        assertEquals(expected, actual)
    }

    private suspend fun `given nickname and text throw error`() {
        coEvery { tweetRepository.add(nickname, tweet) } throws TweetRepository.AddTweetException()
    }

    private suspend fun `given nickname and text create tweet`() {
        coEvery { tweetRepository.add(nickname, tweet) } returns Unit
    }

    private suspend fun `when create tweet`() {
        tweetService.createTweet(nickname, tweet)
    }

    private suspend fun `then verify tweet is created`() {
        coVerify(exactly = 1) { tweetRepository.add(nickname, tweet) }
    }

}