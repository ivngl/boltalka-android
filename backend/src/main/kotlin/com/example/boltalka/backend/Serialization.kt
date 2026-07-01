package com.example.boltalka.backend

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        })
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(cause.localizedMessage ?: "Internal error", ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }
}

fun Application.configureCORS() {
    install(CORS) {
        anyHost()
    }
}

fun Application.configureRouting() {
    routing {
        chatRoutes()
        messageRoutes()
        contactRoutes()
    }
}
