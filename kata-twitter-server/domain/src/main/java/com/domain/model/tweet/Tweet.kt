package com.domain.model.tweet

import com.domain.model.user.User

data class Tweet(val user: User, val text: String)