package com.api.routes.tweet

import com.api.routes.DependencyProvider
import com.domain.action.tweet.GetTweetList
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.tweetListRouting() {
    route("/tweets") {
        get {
            try {
                tryTweetList(call)
            } catch (ex: Exception) {
                ex.printStackTrace()
                call.respondText("Internal error", status = HttpStatusCode.InternalServerError)
            }

        }
    }
}

private suspend fun tryTweetList(call: ApplicationCall) {
    val parameters = call.request.queryParameters
    val nickname = parameters["nickname"]
    val dependencyProvider = DependencyProvider()
    val list = GetTweetList(dependencyProvider.getTweetService())
    if (isNotEmptyParameter(nickname)) {
        call.respond(list.execute(nickname!!))
    } else
        call.respondText("Empty parameter", status = HttpStatusCode.BadRequest)
}

private fun isNotEmptyParameter(nickname: String?): Boolean {
    return nickname != null
}