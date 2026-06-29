package com.example.boltalka.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.boltalka.data.local.AppDatabase
import com.example.boltalka.data.local.entity.ChatEntity
import com.example.boltalka.data.local.entity.MessageEntity
import com.example.boltalka.data.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val chatRepository = ChatRepository(db.chatDao(), db.messageDao())

    private val _chat = MutableStateFlow<ChatEntity?>(null)
    val chat: StateFlow<ChatEntity?> = _chat.asStateFlow()

    private val _messages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val messages: StateFlow<List<MessageEntity>> = _messages.asStateFlow()

    private val _messageText = MutableStateFlow("")
    val messageText: StateFlow<String> = _messageText.asStateFlow()

    private var currentChatId: Long = -1

    fun loadChat(chatId: Long) {
        currentChatId = chatId
        viewModelScope.launch {
            chatRepository.getChatById(chatId).collect { chatEntity ->
                _chat.value = chatEntity
            }
        }
        viewModelScope.launch {
            chatRepository.getMessagesByChatId(chatId).collect { msgList ->
                _messages.value = msgList
            }
        }
    }

    fun onMessageTextChanged(text: String) {
        _messageText.value = text
    }

    fun sendMessage() {
        val text = _messageText.value.trim()
        if (text.isEmpty() || currentChatId == -1L) return

        viewModelScope.launch {
            chatRepository.sendMessage(currentChatId, text)
            _messageText.value = ""
        }
    }
}
