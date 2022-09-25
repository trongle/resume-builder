package com.tronglt.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

val json = Json {
    ignoreUnknownKeys = true
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(json)
    }
}
