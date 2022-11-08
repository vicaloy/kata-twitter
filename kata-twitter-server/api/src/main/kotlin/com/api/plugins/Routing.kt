package com.api.plugins

import com.api.routes.home.homeRouting
import com.api.routes.follow.userFollowRouting
import com.api.routes.follow.userFollowedListRouting
import com.api.routes.follow.userFollowerListRouting
import com.api.routes.tweet.createTweetRouting
import com.api.routes.tweet.tweetListRouting
import com.api.routes.user.userUpdateRouting
import com.api.routes.user.userRegisterRouting
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        homeRouting()
        userRegisterRouting()
        userUpdateRouting()
        userFollowRouting()
        userFollowedListRouting()
        userFollowerListRouting()
        createTweetRouting()
        tweetListRouting()
    }
}
