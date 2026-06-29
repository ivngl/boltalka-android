package com.example.boltalka.data.repository

import com.example.boltalka.data.local.dao.ChatDao
import com.example.boltalka.data.local.dao.MessageDao
import com.example.boltalka.data.local.entity.ChatEntity
import com.example.boltalka.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

class ChatRepository(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao
) {
    val allChats: Flow<List<ChatEntity>> = chatDao.getAllChats()

    fun getChatById(chatId: Long): Flow<ChatEntity?> = chatDao.getChatByIdFlow(chatId)

    fun getMessagesByChatId(chatId: Long): Flow<List<MessageEntity>> =
        messageDao.getMessagesByChatId(chatId)

    suspend fun createChat(name: String, phone: String, iconColor: Int): Long {
        return chatDao.insertChat(
            ChatEntity(
                name = name,
                phone = phone,
                iconColor = iconColor
            )
        )
    }

    suspend fun sendMessage(chatId: Long, content: String) {
        val now = System.currentTimeMillis()
        messageDao.insertMessage(
            MessageEntity(
                chatId = chatId,
                content = content,
                timestamp = now,
                isSent = true
            )
        )
        chatDao.updateLastMessage(chatId, content, now)
    }

    suspend fun deleteChat(chat: ChatEntity) {
        chatDao.deleteChat(chat)
    }
}
