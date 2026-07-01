package com.example.boltalka.backend.models

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object ChatsTable : LongIdTable("chats") {
    val name = varchar("name", 255)
    val phone = varchar("phone", 255).default("")
    val lastMessage = varchar("last_message", 255).default("")
    val lastMessageTime = long("last_message_time").default(System.currentTimeMillis())
    val iconColor = integer("icon_color").default(0xFF075E54.toInt())
    val isGroup = bool("is_group").default(false)
}

object MessagesTable : LongIdTable("messages") {
    val chatId = reference("chat_id", ChatsTable, onDelete = ReferenceOption.CASCADE)
    val content = text("content")
    val timestamp = long("timestamp").default(System.currentTimeMillis())
    val isSent = bool("is_sent").default(true)
    val senderName = varchar("sender_name", 255).default("")
}

object ContactsTable : LongIdTable("contacts") {
    val name = varchar("name", 255)
    val phone = varchar("phone", 255)
    val iconColor = integer("icon_color").default(0xFF075E54.toInt())
}
