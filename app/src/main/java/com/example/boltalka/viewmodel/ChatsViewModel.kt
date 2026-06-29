package com.example.boltalka.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.boltalka.data.local.AppDatabase
import com.example.boltalka.data.local.entity.ChatEntity
import com.example.boltalka.data.repository.ChatRepository
import com.example.boltalka.data.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val chatRepository = ChatRepository(db.chatDao(), db.messageDao())
    private val contactRepository = ContactRepository(db.contactDao())

    val chats: Flow<List<ChatEntity>> = chatRepository.allChats
    val contacts = contactRepository.allContacts

    private val _showNewChatDialog = MutableStateFlow(false)
    val showNewChatDialog: StateFlow<Boolean> = _showNewChatDialog.asStateFlow()

    fun onNewChatClick() {
        _showNewChatDialog.value = true
    }

    fun onDismissNewChatDialog() {
        _showNewChatDialog.value = false
    }

    fun createChat(name: String, phone: String, iconColor: Int) {
        viewModelScope.launch {
            chatRepository.createChat(name, phone, iconColor)
            _showNewChatDialog.value = false
        }
    }

    fun deleteChat(chat: ChatEntity) {
        viewModelScope.launch {
            chatRepository.deleteChat(chat)
        }
    }
}
