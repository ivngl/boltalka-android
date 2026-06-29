package com.example.boltalka.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Long = System.currentTimeMillis(),
    val iconColor: Int = 0xFF075E54.toInt(),
    val isGroup: Boolean = false
)
