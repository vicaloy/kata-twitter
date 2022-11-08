package com.valoy.core.domain.tweet

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class TweetTest{
    @Test
    fun `cannot create tweet with empty text`() {
        assertThrows<Tweet.EmptyTextException> {
            Tweet("")
        }
    }

}