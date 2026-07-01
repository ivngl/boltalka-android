package com.example.boltalka.backend

import com.example.boltalka.backend.models.ContactsTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.contactRoutes() {
    route("/api/contacts") {
        get {
            val contacts = transaction {
                ContactsTable.selectAll().orderBy(ContactsTable.name).map { it.toContactDto() }
            }
            call.respond(contacts)
        }

        post {
            val body = call.receive<CreateContactRequest>()
            val id = transaction {
                ContactsTable.insertAndGetId {
                    it[name] = body.name
                    it[phone] = body.phone
                    it[iconColor] = body.iconColor
                }
            }
            call.respond(status = HttpStatusCode.Created, mapOf("id" to id.value))
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: return@delete call.respondText("Invalid contact ID", status = HttpStatusCode.BadRequest)

            transaction {
                ContactsTable.deleteWhere { ContactsTable.id eq id }
            }
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
