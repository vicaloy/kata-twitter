package com.api.routes.follow

import com.api.routes.DependencyProvider
import com.domain.action.follow.FollowUser
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userFollowRouting() {
    route("/follow") {
        post {
            try {
                tryFollow(call)
            } catch (ex: Exception) {
                call.respondText("Internal error", status = HttpStatusCode.InternalServerError)
            }

        }
    }
}

private suspend fun tryFollow(call: ApplicationCall) {
    val parameters = call.receiveParameters()
    val followedNickname = parameters["followedNickname"]
    val followerNickname = parameters["followerNickname"]
    val dependencyProvider = DependencyProvider()
    val followUser = FollowUser(dependencyProvider.getFollowService())
    if (isNotEmptyParameters(followedNickname, followerNickname)) {
        followUser.execute(followedNickname!!, followerNickname!!)
        call.respondText("User followed correctly", status = HttpStatusCode.OK)
    } else
        call.respondText("Empty parameter", status = HttpStatusCode.BadRequest)
}

private fun isNotEmptyParameters(followedNickname: String?, followerNickname: String?): Boolean {
    return followedNickname != null && followerNickname != null
}