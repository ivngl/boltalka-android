package com.example.boltalka.data.local

import android.content.Context
import com.example.boltalka.data.local.entity.ChatEntity
import com.example.boltalka.data.local.entity.ContactEntity
import com.example.boltalka.data.local.entity.MessageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SeedDataHelper {

    suspend fun seedIfEmpty(context: Context) {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)
            val chatDao = db.chatDao()
            val messageDao = db.messageDao()
            val contactDao = db.contactDao()

            if (chatDao.getChatCount() > 0) {
                return@withContext
            }

            val chatId = chatDao.insertChat(
                ChatEntity(
                    name = "Alex",
                    phone = "+1 555-0100",
                    lastMessage = "Hey, how are you?",
                    lastMessageTime = System.currentTimeMillis() - 3600000,
                    iconColor = 0xFF039BE5.toInt()
                )
            )

            messageDao.insertMessage(
                MessageEntity(
                    chatId = chatId,
                    content = "Hey, how are you?",
                    timestamp = System.currentTimeMillis() - 3600000,
                    isSent = false,
                    senderName = "Alex"
                )
            )
            messageDao.insertMessage(
                MessageEntity(
                    chatId = chatId,
                    content = "I'm good, thanks! Want to grab coffee?",
                    timestamp = System.currentTimeMillis() - 3500000,
                    isSent = true
                )
            )
            messageDao.insertMessage(
                MessageEntity(
                    chatId = chatId,
                    content = "Sure, sounds great!",
                    timestamp = System.currentTimeMillis() - 3400000,
                    isSent = false,
                    senderName = "Alex"
                )
            )

            val chatId2 = chatDao.insertChat(
                ChatEntity(
                    name = "Design Team",
                    phone = "",
                    lastMessage = "Meeting at 3pm tomorrow",
                    lastMessageTime = System.currentTimeMillis() - 7200000,
                    iconColor = 0xFF9C27B0.toInt(),
                    isGroup = true
                )
            )

            messageDao.insertMessage(
                MessageEntity(
                    chatId = chatId2,
                    content = "Meeting at 3pm tomorrow",
                    timestamp = System.currentTimeMillis() - 7200000,
                    isSent = false,
                    senderName = "Maria"
                )
            )

            val chatId3 = chatDao.insertChat(
                ChatEntity(
                    name = "Mom",
                    phone = "+1 555-0200",
                    lastMessage = "Don't forget to call grandma",
                    lastMessageTime = System.currentTimeMillis() - 86400000,
                    iconColor = 0xFFE91E63.toInt()
                )
            )

            messageDao.insertMessage(
                MessageEntity(
                    chatId = chatId3,
                    content = "Don't forget to call grandma",
                    timestamp = System.currentTimeMillis() - 86400000,
                    isSent = false,
                    senderName = "Mom"
                )
            )
            messageDao.insertMessage(
                MessageEntity(
                    chatId = chatId3,
                    content = "I won't, thanks for reminding!",
                    timestamp = System.currentTimeMillis() - 85000000,
                    isSent = true
                )
            )

            contactDao.insertContact(
                ContactEntity(name = "Alex", phone = "+1 555-0100", iconColor = 0xFF039BE5.toInt())
            )
            contactDao.insertContact(
                ContactEntity(name = "Mom", phone = "+1 555-0200", iconColor = 0xFFE91E63.toInt())
            )
            contactDao.insertContact(
                ContactEntity(name = "John", phone = "+1 555-0300", iconColor = 0xFFFF5722.toInt())
            )
            contactDao.insertContact(
                ContactEntity(name = "Sarah", phone = "+1 555-0400", iconColor = 0xFF607D8B.toInt())
            )
        }
    }
}
