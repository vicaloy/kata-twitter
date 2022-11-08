package com.api

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.api.plugins.*

 fun main() {
    embeddedServer(Netty, port = 8080, host = "192.168.1.6") {
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}
