package com.example.boltalka.backend

import com.example.boltalka.backend.models.ChatsTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.chatRoutes() {
    route("/api/chats") {
        get {
            val chats = transaction {
                ChatsTable.selectAll().orderBy(ChatsTable.lastMessageTime, SortOrder.DESC).map { it.toChatDto() }
            }
            call.respond(chats)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: return@get call.respondText("Invalid chat ID", status = HttpStatusCode.BadRequest)

            val chat = transaction {
                ChatsTable.selectAll().where { ChatsTable.id eq id }.singleOrNull()?.toChatDto()
            } ?: return@get call.respondText("Chat not found", status = HttpStatusCode.NotFound)

            call.respond(chat)
        }

        get("/{id}/messages") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: return@get call.respondText("Invalid chat ID", status = HttpStatusCode.BadRequest)

            val messages = transaction {
                com.example.boltalka.backend.models.MessagesTable.selectAll()
                    .where { com.example.boltalka.backend.models.MessagesTable.chatId eq id }
                    .orderBy(com.example.boltalka.backend.models.MessagesTable.timestamp, SortOrder.ASC)
                    .map { it.toMessageDto() }
            }
            call.respond(messages)
        }

        post {
            val body = call.receive<CreateChatRequest>()
            val id = transaction {
                ChatsTable.insertAndGetId {
                    it[name] = body.name
                    it[phone] = body.phone
                    it[iconColor] = body.iconColor
                    it[isGroup] = body.isGroup
                }
            }
            call.respond(status = HttpStatusCode.Created, mapOf("id" to id.value))
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: return@delete call.respondText("Invalid chat ID", status = HttpStatusCode.BadRequest)

            transaction {
                ChatsTable.deleteWhere { ChatsTable.id eq id }
            }
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
