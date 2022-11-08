package com.valoy.core.actions

import com.valoy.common.InstantExecutorExtension
import com.valoy.core.domain.tweet.Tweet
import com.valoy.core.domain.tweet.TweetRepository
import com.valoy.core.domain.tweet.TweetService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class GetTweetListTest {
    private val tweets = listOf(Tweet("text"))
    private val emptyTweets = listOf<Tweet>()
    private val nickname = "mari"
    private lateinit var getTweetList: GetTweetList

    @RelaxedMockK
    private lateinit var tweetService: TweetService

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        getTweetList = GetTweetList(tweetService)
    }

    @Test
    fun `list of tweets not empty`() = runTest {
        `given nickname return list`(tweets)
        val result = `when return tweets`()
        `then return list of tweets`(tweets, result)
    }

    @Test
    fun `list of tweets empty`() = runTest {
        `given nickname return list`(emptyTweets)
        val result = `when return tweets`()
        `then return list of tweets`(emptyTweets, result)
    }

    @Test
    fun `throw error when list tweets fails`() = runTest {
        coEvery { tweetService.getTweets(nickname) } throws TweetRepository.GetTweetsException()
        assertThrows<RuntimeException> { getTweetList.execute(nickname) }
    }

    private suspend fun `given nickname return list`(list: List<Tweet>) {
        coEvery { tweetService.getTweets(nickname) } returns list
    }

    private suspend fun `when return tweets`(): List<Tweet>? {
        return getTweetList.execute(nickname)
    }

    private fun `then return list of tweets`(expected: List<Tweet>, actual: List<Tweet>?) {
        assertEquals(expected, actual)
    }
}