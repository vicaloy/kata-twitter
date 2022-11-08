package com.api.routes.user

import com.domain.action.user.RegisterUser
import com.api.models.RegisterRequest
import com.api.routes.DependencyProvider
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.domain.model.user.UserService

fun Route.userRegisterRouting() {
    route("/user") {
        post {
            try {
                tryFollow(call)
            } catch (ex: UserService.NicknameInUseException) {
                call.respondText("User not registered, nickname in use", status = HttpStatusCode.NotAcceptable)
            } catch (ex: Exception) {
                call.respondText(
                    "Internal error", status = HttpStatusCode.InternalServerError)
            }

        }
    }
}

private suspend fun tryFollow(call: ApplicationCall) {
    val apiUser = call.receive<RegisterRequest>()
    val dependencyProvider = DependencyProvider()
    val registerUser = RegisterUser(dependencyProvider.getUserService())
    registerUser.execute(RegisterRequest.mapToUser(apiUser))
    call.respondText("User registered correctly", status = HttpStatusCode.OK)
}