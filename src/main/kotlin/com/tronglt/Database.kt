package com.tronglt

import com.tronglt.UserTable.nullable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

object UserTable : IntIdTable() {
    val userName = varchar("user_name", 25)
    val fullName = varchar("full_name", 100).nullable()
    val email = varchar("email", 255).nullable()
    val password = varchar("password", 255)
    val isAdmin = bool("is_admin").default(false)
    val createdAt = datetime("created_at")
}

object CvTable : IntIdTable() {
    val userId = integer("user_id")
    val name = varchar("name", 100)
    val email = varchar("email", 255)
    val mobile = varchar("mobile", 15)
    val github = text("github").nullable()
    val linkedln = text("linkedln").nullable()
    val description = text("description").nullable()
    val jobTitle = varchar("job_title", 30).nullable()
    val company = varchar("company", 30).nullable()
    val period = varchar("period", 30).nullable()
    val jobDescription = varchar("job_description", 100).nullable()
    val university = varchar("university", 100).nullable()
    val faculty = varchar("faculty", 100).nullable()
    val gpa = varchar("gpa", 100).nullable()
    val skills = text("skills").nullable()
    val createdAt = datetime("created_at")
}

fun configureDatabase() {
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    Database.connect("jdbc:sqlite:resume-builder-management.db", "org.sqlite.JDBC")

    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.createMissingTablesAndColumns(UserTable, CvTable)

        if (UserTable.select { UserTable.isAdmin.eq(true) and UserTable.userName.eq("admin") }.count() == 0L) {
            UserTable.insert {
                it[userName] = "admin"
                it[fullName] = "admin"
                it[email] = "admin@localhost"
                it[password] = hashPassword("admin".toCharArray())
                it[isAdmin] = true
                it[createdAt] = now()
            }
        }
    }
}