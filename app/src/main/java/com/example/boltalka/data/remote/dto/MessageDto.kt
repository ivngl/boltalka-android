package com.example.boltalka.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: Long = 0,
    @SerialName("chatId")
    val chatId: Long,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    @SerialName("isSent")
    val isSent: Boolean = true,
    @SerialName("senderName")
    val senderName: String = ""
)

@Serializable
data class SendMessageRequest(
    @SerialName("chatId")
    val chatId: Long,
    val content: String,
    @SerialName("senderName")
    val senderName: String = ""
)
