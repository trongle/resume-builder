package com.tronglt.plugins

import com.tronglt.DomainException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPage() {
    fun Application.configureStatusPages() {
        install(StatusPages) {
            exception<BadRequestException> { call, cause ->
                when (cause.cause) {
                    is IllegalArgumentException -> {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            mapOf("errors" to cause.cause?.message?.split("\n"))
                        )
                    }

                    else -> call.respond(HttpStatusCode.BadRequest, cause.message ?: "")
                }
            }

            exception<DomainException> { call, cause ->
                call.respond(HttpStatusCode.BadRequest, mapOf("errors" to cause.message?.split("\n")))
            }
        }
    }
}