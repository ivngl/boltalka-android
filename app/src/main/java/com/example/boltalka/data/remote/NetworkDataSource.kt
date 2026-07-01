package com.example.boltalka.data.remote

import com.example.boltalka.data.local.entity.ChatEntity
import com.example.boltalka.data.local.entity.ContactEntity
import com.example.boltalka.data.local.entity.MessageEntity
import com.example.boltalka.data.remote.api.ChatApi
import com.example.boltalka.data.remote.api.ContactApi
import com.example.boltalka.data.remote.dto.ChatDto
import com.example.boltalka.data.remote.dto.ContactDto
import com.example.boltalka.data.remote.dto.CreateChatRequest
import com.example.boltalka.data.remote.dto.CreateContactRequest
import com.example.boltalka.data.remote.dto.MessageDto
import com.example.boltalka.data.remote.dto.SendMessageRequest

class NetworkDataSource(
    private val chatApi: ChatApi = ApiClient.chatApi,
    private val contactApi: ContactApi = ApiClient.contactApi
) {
    sealed class NetworkResult<out T> {
        data class Success<T>(val data: T) : NetworkResult<T>()
        data class Error(val message: String) : NetworkResult<Nothing>()
    }

    suspend fun fetchChats(): NetworkResult<List<ChatDto>> = runCatching {
        NetworkResult.Success(chatApi.getChats())
    }.getOrElse { NetworkResult.Error(it.message ?: "Unknown error") }

    suspend fun fetchChat(chatId: Long): NetworkResult<ChatDto> = runCatching {
        NetworkResult.Success(chatApi.getChat(chatId))
    }.getOrElse { NetworkResult.Error(it.message ?: "Unknown error") }

    suspend fun fetchMessages(chatId: Long): NetworkResult<List<MessageDto>> = runCatching {
        NetworkResult.Success(chatApi.getMessages(chatId))
    }.getOrElse { NetworkResult.Error(it.message ?: "Unknown error") }

    suspend fun createChat(name: String, phone: String, iconColor: Int, isGroup: Boolean = false): NetworkResult<Long> = runCatching {
        val response = chatApi.createChat(CreateChatRequest(name, phone, iconColor, isGroup))
        NetworkResult.Success(response["id"] ?: error("Missing id in response"))
    }.getOrElse { NetworkResult.Error(it.message ?: "Unknown error") }

    suspend fun sendMessage(chatId: Long, content: String, senderName: String = ""): NetworkResult<Long> = runCatching {
        val response = chatApi.sendMessage(SendMessageRequest(chatId, content, senderName))
        NetworkResult.Success(response["id"] ?: error("Missing id in response"))
    }.getOrElse { NetworkResult.Error(it.message ?: "Unknown error") }

    suspend fun deleteChat(chatId: Long): NetworkResult<Unit> = runCatching {
        chatApi.deleteChat(chatId)
        NetworkResult.Success(Unit)
    }.getOrElse { NetworkResult.Error(it.message ?: "Unknown error") }

    suspend fun fetchContacts(): NetworkResult<List<ContactDto>> = runCatching {
        NetworkResult.Success(contactApi.getContacts())
    }.getOrElse { NetworkResult.Error(it.message ?: "Unknown error") }

    suspend fun createContact(name: String, phone: String, iconColor: Int): NetworkResult<Long> = runCatching {
        val response = contactApi.createContact(CreateContactRequest(name, phone, iconColor))
        NetworkResult.Success(response["id"] ?: error("Missing id in response"))
    }.getOrElse { NetworkResult.Error(it.message ?: "Unknown error") }

    suspend fun deleteContact(contactId: Long): NetworkResult<Unit> = runCatching {
        contactApi.deleteContact(contactId)
        NetworkResult.Success(Unit)
    }.getOrElse { NetworkResult.Error(it.message ?: "Unknown error") }
}

fun ChatDto.toEntity(): ChatEntity = ChatEntity(
    id = id,
    name = name,
    phone = phone,
    lastMessage = lastMessage,
    lastMessageTime = lastMessageTime,
    iconColor = iconColor,
    isGroup = isGroup
)

fun MessageDto.toEntity(): MessageEntity = MessageEntity(
    id = id,
    chatId = chatId,
    content = content,
    timestamp = timestamp,
    isSent = isSent,
    senderName = senderName
)

fun ContactDto.toEntity(): ContactEntity = ContactEntity(
    id = id,
    name = name,
    phone = phone,
    iconColor = iconColor
)
