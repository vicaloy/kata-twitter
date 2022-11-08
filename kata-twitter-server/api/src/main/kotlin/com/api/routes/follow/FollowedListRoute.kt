package com.api.routes.follow

import com.api.models.RegisterRequest
import com.api.routes.DependencyProvider
import com.domain.action.follow.UserFollowedList
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userFollowedListRouting() {
    route("/followed") {
        get {
            try {
                tryFollowedList(call)
            } catch (ex: Exception) {
                ex.printStackTrace()
                call.respondText("Internal error", status = HttpStatusCode.InternalServerError)
            }

        }
    }
}

private suspend fun tryFollowedList(call: ApplicationCall) {
    val parameters = call.request.queryParameters
    val nickname = parameters["nickname"]
    val dependencyProvider = DependencyProvider()
    val userFollowedList = UserFollowedList(dependencyProvider.getFollowService())
    if (isNotEmptyParameter(nickname)) {
        val list = userFollowedList.execute(nickname!!)
        call.respond(RegisterRequest.mapToApiUser(list))
    } else
        call.respondText("Empty parameter", status = HttpStatusCode.BadRequest)
}

private fun isNotEmptyParameter(nickname: String?): Boolean {
    return nickname != null
}