package com.api.routes.user

import com.api.routes.DependencyProvider
import com.domain.action.user.UpdateUser
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userUpdateRouting() {
    route("/user") {
        put {
            try {
                tryUpdate(call)
            } catch (ex: Exception) {
                call.respondText("Internal error", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}

private suspend fun tryUpdate(call: ApplicationCall) {
    val parameters = call.receiveParameters()
    val nickname = parameters["nickname"]
    val newName = parameters["newName"]
    val dependencyProvider = DependencyProvider()
    val updateUser = UpdateUser(dependencyProvider.getUserService())
    if (isNotEmptyParameters(nickname, newName)) {
        updateUser.execute(nickname!!, newName!!)
        call.respondText("User updated correctly", status = HttpStatusCode.OK)
    } else {
        call.respondText("Empty parameter", status = HttpStatusCode.BadRequest)
    }
}

private fun isNotEmptyParameters(nickname: String?, newName: String?): Boolean {
    return nickname != null && newName != null
}