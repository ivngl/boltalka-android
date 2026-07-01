package com.example.boltalka.data.repository

import com.example.boltalka.data.local.dao.ChatDao
import com.example.boltalka.data.local.dao.MessageDao
import com.example.boltalka.data.local.entity.ChatEntity
import com.example.boltalka.data.local.entity.MessageEntity
import com.example.boltalka.data.remote.NetworkDataSource
import com.example.boltalka.data.remote.toEntity
import kotlinx.coroutines.flow.Flow

class ChatRepository(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val networkDataSource: NetworkDataSource = NetworkDataSource()
) {
    val allChats: Flow<List<ChatEntity>> = chatDao.getAllChats()

    fun getChatById(chatId: Long): Flow<ChatEntity?> = chatDao.getChatByIdFlow(chatId)

    fun getMessagesByChatId(chatId: Long): Flow<List<MessageEntity>> =
        messageDao.getMessagesByChatId(chatId)

    suspend fun refreshChats() {
        when (val result = networkDataSource.fetchChats()) {
            is NetworkDataSource.NetworkResult.Success -> {
                val entities = result.data.map { it.toEntity() }
                entities.forEach { chatDao.insertChat(it) }
            }
            is NetworkDataSource.NetworkResult.Error -> { /* offline — keep local cache */ }
        }
    }

    suspend fun refreshMessages(chatId: Long) {
        when (val result = networkDataSource.fetchMessages(chatId)) {
            is NetworkDataSource.NetworkResult.Success -> {
                val entities = result.data.map { it.toEntity() }
                entities.forEach { messageDao.insertMessage(it) }
            }
            is NetworkDataSource.NetworkResult.Error -> { /* offline — keep local cache */ }
        }
    }

    suspend fun createChat(name: String, phone: String, iconColor: Int): Long {
        val remoteResult = networkDataSource.createChat(name, phone, iconColor)
        return when (remoteResult) {
            is NetworkDataSource.NetworkResult.Success -> {
                val remoteId = remoteResult.data
                chatDao.insertChat(ChatEntity(id = remoteId, name = name, phone = phone, iconColor = iconColor))
                remoteId
            }
            is NetworkDataSource.NetworkResult.Error -> {
                chatDao.insertChat(ChatEntity(name = name, phone = phone, iconColor = iconColor))
            }
        }
    }

    suspend fun sendMessage(chatId: Long, content: String) {
        val now = System.currentTimeMillis()
        networkDataSource.sendMessage(chatId, content)
        messageDao.insertMessage(
            MessageEntity(chatId = chatId, content = content, timestamp = now, isSent = true)
        )
        chatDao.updateLastMessage(chatId, content, now)
    }

    suspend fun deleteChat(chat: ChatEntity) {
        networkDataSource.deleteChat(chat.id)
        chatDao.deleteChat(chat)
    }
}
