package com.example.boltalka.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val id: Long,
    val name: String,
    val phone: String = "",
    @SerialName("lastMessage")
    val lastMessage: String = "",
    @SerialName("lastMessageTime")
    val lastMessageTime: Long = System.currentTimeMillis(),
    @SerialName("iconColor")
    val iconColor: Int = 0xFF075E54.toInt(),
    @SerialName("isGroup")
    val isGroup: Boolean = false
)

@Serializable
data class CreateChatRequest(
    val name: String,
    val phone: String = "",
    @SerialName("iconColor")
    val iconColor: Int = 0xFF075E54.toInt(),
    @SerialName("isGroup")
    val isGroup: Boolean = false
)
