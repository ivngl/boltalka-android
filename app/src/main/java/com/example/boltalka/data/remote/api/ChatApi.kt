package com.example.boltalka.data.remote.api

import com.example.boltalka.data.remote.dto.ChatDto
import com.example.boltalka.data.remote.dto.CreateChatRequest
import com.example.boltalka.data.remote.dto.MessageDto
import com.example.boltalka.data.remote.dto.SendMessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {
    @GET("api/chats")
    suspend fun getChats(): List<ChatDto>

    @GET("api/chats/{id}")
    suspend fun getChat(@Path("id") id: Long): ChatDto

    @GET("api/chats/{id}/messages")
    suspend fun getMessages(@Path("id") chatId: Long): List<MessageDto>

    @POST("api/chats")
    suspend fun createChat(@Body request: CreateChatRequest): Map<String, Long>

    @POST("api/messages")
    suspend fun sendMessage(@Body request: SendMessageRequest): Map<String, Long>

    @DELETE("api/chats/{id}")
    suspend fun deleteChat(@Path("id") id: Long): Response<Unit>
}
