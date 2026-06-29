package com.example.boltalka.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.boltalka.data.local.entity.ChatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY lastMessageTime DESC")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Query("SELECT COUNT(*) FROM chats")
    suspend fun getChatCount(): Int

    @Query("SELECT * FROM chats WHERE id = :chatId")
    suspend fun getChatById(chatId: Long): ChatEntity?

    @Query("SELECT * FROM chats WHERE id = :chatId")
    fun getChatByIdFlow(chatId: Long): Flow<ChatEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity): Long

    @Update
    suspend fun updateChat(chat: ChatEntity)

    @Delete
    suspend fun deleteChat(chat: ChatEntity)

    @Query("UPDATE chats SET lastMessage = :message, lastMessageTime = :time WHERE id = :chatId")
    suspend fun updateLastMessage(chatId: Long, message: String, time: Long)
}
