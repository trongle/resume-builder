package com.tronglt

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.transactions.transaction
import com.tronglt.plugins.json
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.sql.*

@Serializable
data class User(
    val id: Int,
    val userName: String,
    val fullName: String?,
    val email: String?,
    val isAdmin: Boolean,
    @Transient val password: String? = null
)

@Serializable
data class CreateUserRequest(
    val userName: String,
    val password: String,
    val fullName: String? = null,
    val email: String? = null
) {
    init {
        val errors = mutableListOf<String>()
        if (userName.isBlank()) {
            errors.add("Field userName is required.")
        }
        if (password.isBlank()) {
            errors.add("Field password is required.")
        }

        if (errors.isNotEmpty()) {
            throw IllegalArgumentException(errors.joinToString("\n"))
        }
    }
}

fun Application.userRoutes() {
    routing {
        route("users") {
            authenticate("jwt-auth") {
                get {
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal.payload.getClaim("id").asInt()

                    // Check whether use is admin. Only admin user can get user list information.
                    transaction {
                        UserTable
                            .select { UserTable.id.eq(userId) and UserTable.isAdmin.eq(true) }
                            .firstOrNull()
                    } ?: throw Unauthorization("Permission denied.")

                    val request = call.request.queryParameters
                    val perPage = 10
                    val page = request["page"]?.toLong() ?: 1L
                    val offset = (page - 1) * perPage

                    val conditions =
                        if (request.contains("searchTerm")) UserTable.userName.like("%${request["searchTerm"]}%")
                        else null
                    val query = (conditions?.let { UserTable.select(it) } ?: UserTable.selectAll()).andWhere {
                        UserTable.id.neq(userId)
                    }

                    val lastPage = transaction {
                        val total = query.count()
                        if (total % perPage == 0L) total / perPage else total / perPage + 1
                    }

                    val users = transaction {
                        query.limit(perPage, offset)
                            .let {
                                // Check ordering conditions
                                if (!request["sortColumn"].isNullOrBlank() && !request["sortBy"].isNullOrBlank()) {
                                    val sortBy = SortOrder.valueOf(request["sortBy"]!!.uppercase())
                                    when (request["sortColumn"]!!) {
                                        "userName" -> it.orderBy(UserTable.userName to sortBy)
                                        "fullName" -> it.orderBy(UserTable.fullName to sortBy)
                                        "email" -> it.orderBy(UserTable.email to sortBy)
                                    }
                                }
                                it
                            }
                            .map {
                                User(
                                    it[UserTable.id].value,
                                    it[UserTable.userName],
                                    it[UserTable.fullName],
                                    it[UserTable.email],
                                    it[UserTable.isAdmin]
                                )
                            }
                    }

                    call.respond(
                        mapOf(
                            "users" to json.encodeToString(users),
                            "lastPage" to json.encodeToString(lastPage)
                        )
                    )
                }
            }
            post {
                val request = call.receive<CreateUserRequest>()

                // Store user's data into the database.
                transaction {
                    if (UserTable.select(UserTable.userName.eq(request.userName)).count() > 0) {
                        throw DomainException("User name already exists. Please try other one.")
                    }

                    UserTable.insert {
                        it[userName] = request.userName
                        it[fullName] = request.fullName
                        it[password] = hashPassword(request.password.toCharArray())
                        it[email] = request.email
                        it[createdAt] = now()
                    }
                }

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}