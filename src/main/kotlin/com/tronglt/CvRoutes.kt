package com.tronglt

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class Cv(
    val id: Int,
    val name: String,
    val email: String,
    val mobile: String,
    val github: String? = null,
    val linkedln: String? = null,
    val description: String? = null,
    val jobTitle: String? = null,
    val company: String? = null,
    val period: String? = null,
    val jobDescription: String? = null,
    val university: String? = null,
    val faculty: String? = null,
    val gpa: String? = null,
    val skills: String? = null,
)

@Serializable
data class CreateCvRequest(
    val name: String,
    val email: String,
    val mobile: String,
    val github: String? = null,
    val linkedln: String? = null,
    val description: String? = null,
    val jobTitle: String? = null,
    val company: String? = null,
    val period: String? = null,
    val jobDescription: String? = null,
    val university: String? = null,
    val faculty: String? = null,
    val gpa: String? = null,
    val skills: String? = null,
) {
    init {
        val errors = mutableListOf<String>()
        if (name.isBlank()) {
            errors.add("Field name is required.")
        }
        if (email.isBlank()) {
            errors.add("Field email is required.")
        }
        if (mobile.isBlank()) {
            errors.add("Field mobile is required.")
        }

        if (errors.isNotEmpty()) {
            throw IllegalArgumentException(errors.joinToString("\n"))
        }
    }
}

fun Application.cvRoutes() {
    routing {
        authenticate("jwt-auth") {
            route("cvs") {
                post {
                    val principal = call.principal<JWTPrincipal>()!!
                    val id = principal.payload.getClaim("id").asInt()
                    val request = call.receive<CreateCvRequest>()

                    // Store user's data into the database.
                    transaction {
                        CvTable.insert {
                            it[userId] = id
                            it[name] = request.name
                            it[email] = request.email
                            it[mobile] = request.mobile
                            it[github] = request.github
                            it[linkedln] = request.linkedln
                            it[description] = request.description
                            it[jobTitle] = request.jobTitle
                            it[company] = request.company
                            it[period] = request.period
                            it[jobDescription] = request.jobDescription
                            it[university] = request.university
                            it[faculty] = request.faculty
                            it[gpa] = request.gpa
                            it[skills] = request.skills
                            it[createdAt] = now()
                        }
                    }

                    call.respond(HttpStatusCode.OK)
                }

                get {
                    val principal = call.principal<JWTPrincipal>()!!
                    val id = principal.payload.getClaim("id").asInt()

                    val cvs = transaction {
                        CvTable.select { CvTable.userId.eq(id) }.map {
                            Cv(
                                it[CvTable.id].value,
                                it[CvTable.name],
                                it[CvTable.email],
                                it[CvTable.mobile],
                                it[CvTable.github],
                                it[CvTable.linkedln],
                                it[CvTable.description],
                                it[CvTable.jobTitle],
                                it[CvTable.company],
                                it[CvTable.period],
                                it[CvTable.jobDescription],
                                it[CvTable.university],
                                it[CvTable.faculty],
                                it[CvTable.gpa],
                                it[CvTable.skills]
                            )
                        }
                    }

                    call.respond(cvs)
                }

                delete("{id}") {
                    val principal = call.principal<JWTPrincipal>()!!
                    val id = principal.payload.getClaim("id").asInt()
                    val cvId = call.parameters["id"]?.toInt() ?: 0

                    transaction { CvTable.deleteWhere { CvTable.id.eq(cvId) and CvTable.userId.eq(id)  } }

                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}