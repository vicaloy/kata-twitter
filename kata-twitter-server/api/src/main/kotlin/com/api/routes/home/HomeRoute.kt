package com.api.routes.home

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.homeRouting() {
    route("/") {
        get {
            call.respondText("Kata Twitter Server", status = HttpStatusCode.OK)
        }
    }
}
