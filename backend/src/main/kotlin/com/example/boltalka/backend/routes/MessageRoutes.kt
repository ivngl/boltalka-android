package com.example.boltalka.backend

import com.example.boltalka.backend.models.ChatsTable
import com.example.boltalka.backend.models.MessagesTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun Routing.messageRoutes() {
    route("/api/messages") {
        post {
            val body = call.receive<SendMessageRequest>()
            val now = System.currentTimeMillis()

            val msgId = transaction {
                val insertedId = MessagesTable.insertAndGetId {
                    it[chatId] = body.chatId
                    it[content] = body.content
                    it[timestamp] = now
                    it[isSent] = true
                    it[senderName] = body.senderName
                }

                ChatsTable.update({ ChatsTable.id eq body.chatId }) {
                    it[lastMessage] = body.content
                    it[lastMessageTime] = now
                }

                insertedId.value
            }
            call.respond(status = HttpStatusCode.Created, mapOf("id" to msgId))
        }
    }
}
