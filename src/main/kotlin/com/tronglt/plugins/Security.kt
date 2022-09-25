package com.tronglt.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*

fun Application.configureSecurity() {
    authentication {
        jwt("jwt-auth") {
            val config = this@configureSecurity.environment.config
            val jwtAudience = config.property("jwt.audience").getString()
            realm = config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.property("jwt.secret").getString()))
                    .withAudience(jwtAudience)
                    .withIssuer(config.property("jwt.issuer").getString())
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
