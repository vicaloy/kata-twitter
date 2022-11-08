package com.domain.model.follow

import com.domain.model.user.User

data class Follow(val followed: User, val follower: User)