package com.tronglt

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import com.tronglt.plugins.json
import java.util.*

@Serializable
data class LoginRequest(val userName: String, val password: String) {
    init {
        val errors = mutableListOf<String>()
        if (userName.isBlank()) {
            errors.add("field userName is required.")
        }
        if (password.isBlank()) {
            errors.add("field password is required.")
        }

        if (errors.isNotEmpty()) {
            throw IllegalArgumentException(errors.joinToString("\n"))
        }
    }
}

fun Application.authRoutes() {
    routing {
        post("login") {
            val request = call.receive<LoginRequest>()
            val config = this@authRoutes.environment.config
            fun verifyPassword(verifying: CharArray, hashedPassword: String) =
                BCrypt.verifyer().verify(verifying, hashedPassword).verified

            val user = transaction {
                UserTable.select { UserTable.userName.eq(request.userName) }.firstOrNull()?.let {
                    if (!verifyPassword(request.password.toCharArray(), it[UserTable.password])) {
                        throw DomainException("User not found.")
                    }

                    User(
                        it[UserTable.id].value,
                        it[UserTable.userName],
                        it[UserTable.fullName],
                        it[UserTable.email],
                        it[UserTable.isAdmin]
                    )
                }
            } ?: throw DomainException("User not found.")

            val token = JWT.create()
                .withAudience(config.property("jwt.audience").getString())
                .withClaim("id", 1)
                .withIssuer(config.property("jwt.issuer").getString())
                .withExpiresAt(Date(System.currentTimeMillis() + (60000 * 60 * 2)))
                .sign(Algorithm.HMAC256(config.property("jwt.secret").getString()))

            call.respond(mapOf("token" to token, "user" to json.encodeToString(user)))
        }
    }
}