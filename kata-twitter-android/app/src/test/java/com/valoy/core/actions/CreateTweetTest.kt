package com.valoy.core.actions

import com.valoy.common.InstantExecutorExtension
import com.valoy.core.domain.tweet.Tweet
import com.valoy.core.domain.tweet.TweetRepository
import com.valoy.core.domain.tweet.TweetService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class CreateTweetTest{
    private val tweet = Tweet("text")
    private val nickname = "mari"
    private lateinit var createTweet: CreateTweet

    @RelaxedMockK
    private lateinit var tweetService: TweetService

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        createTweet = CreateTweet(tweetService)
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
            createTweet.execute(nickname, tweet)
        }
    }

    private suspend fun `given nickname and text throw error`() {
        coEvery { tweetService.createTweet(nickname, tweet) } throws TweetRepository.AddTweetException()
    }

    private suspend fun `given nickname and text create tweet`() {
        coEvery { tweetService.createTweet(nickname, tweet) } returns Unit
    }

    private suspend fun `when create tweet`() {
        createTweet.execute(nickname, tweet)
    }

    private suspend fun `then verify tweet is created`() {
        coVerify(exactly = 1) { tweetService.createTweet(nickname, tweet) }
    }

}