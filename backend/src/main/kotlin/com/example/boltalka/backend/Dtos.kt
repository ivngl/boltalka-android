package com.example.boltalka.backend

import com.example.boltalka.backend.models.ChatsTable
import com.example.boltalka.backend.models.ContactsTable
import com.example.boltalka.backend.models.MessagesTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class ChatDto(
    val id: Long,
    val name: String,
    val phone: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Long = System.currentTimeMillis(),
    val iconColor: Int = 0xFF075E54.toInt(),
    val isGroup: Boolean = false
)

@Serializable
data class CreateChatRequest(
    val name: String,
    val phone: String = "",
    val iconColor: Int = 0xFF075E54.toInt(),
    val isGroup: Boolean = false
)

@Serializable
data class MessageDto(
    val id: Long = 0,
    val chatId: Long,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSent: Boolean = true,
    val senderName: String = ""
)

@Serializable
data class SendMessageRequest(
    val chatId: Long,
    val content: String,
    val senderName: String = ""
)

@Serializable
data class ContactDto(
    val id: Long = 0,
    val name: String,
    val phone: String,
    val iconColor: Int = 0xFF075E54.toInt()
)

@Serializable
data class CreateContactRequest(
    val name: String,
    val phone: String,
    val iconColor: Int = 0xFF075E54.toInt()
)

fun ResultRow.toChatDto(): ChatDto = ChatDto(
    id = this[ChatsTable.id].value,
    name = this[ChatsTable.name],
    phone = this[ChatsTable.phone],
    lastMessage = this[ChatsTable.lastMessage],
    lastMessageTime = this[ChatsTable.lastMessageTime],
    iconColor = this[ChatsTable.iconColor],
    isGroup = this[ChatsTable.isGroup]
)

fun ResultRow.toMessageDto(): MessageDto = MessageDto(
    id = this[MessagesTable.id].value,
    chatId = this[MessagesTable.chatId].value,
    content = this[MessagesTable.content],
    timestamp = this[MessagesTable.timestamp],
    isSent = this[MessagesTable.isSent],
    senderName = this[MessagesTable.senderName]
)

fun ResultRow.toContactDto(): ContactDto = ContactDto(
    id = this[ContactsTable.id].value,
    name = this[ContactsTable.name],
    phone = this[ContactsTable.phone],
    iconColor = this[ContactsTable.iconColor]
)
