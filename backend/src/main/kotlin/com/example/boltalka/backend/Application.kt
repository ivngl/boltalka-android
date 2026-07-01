package com.example.boltalka.backend

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = getPort(), module = Application::module).start(wait = true)
}

private fun getPort(): Int = System.getenv("PORT")?.toIntOrNull() ?: 8080

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureCORS()
    configureRouting()
}
