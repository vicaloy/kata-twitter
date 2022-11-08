package com.api.routes.tweet

import com.api.routes.DependencyProvider
import com.domain.action.tweet.CreateTweet
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createTweetRouting() {
    route("/tweet") {
        post {
            try {
                tryCreate(call)
            } catch (ex: Exception) {
                call.respondText("Internal error", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}

private suspend fun tryCreate(call: ApplicationCall) {
    val parameters = call.receiveParameters()
    val nickname = parameters["nickname"]
    val text = parameters["text"]
    val dependencyProvider = DependencyProvider()
    val createTweet = CreateTweet(dependencyProvider.getTweetService())
    if (isNotEmptyParameters(nickname, text)) {
        createTweet.execute(nickname!!, text!!)
        call.respondText("Tweet created correctly", status = HttpStatusCode.OK)
    } else {
        call.respondText("Empty parameter", status = HttpStatusCode.BadRequest)
    }
}

private fun isNotEmptyParameters(nickname: String?, text: String?): Boolean {
    return nickname != null && text != null
}