package com.example.boltalka.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContactDto(
    val id: Long = 0,
    val name: String,
    val phone: String,
    @SerialName("iconColor")
    val iconColor: Int = 0xFF075E54.toInt()
)

@Serializable
data class CreateContactRequest(
    val name: String,
    val phone: String,
    @SerialName("iconColor")
    val iconColor: Int = 0xFF075E54.toInt()
)
