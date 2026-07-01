package com.example.boltalka

import android.app.Application
import com.example.boltalka.data.local.AppDatabase
import com.example.boltalka.data.local.SeedDataHelper
import com.example.boltalka.data.repository.ChatRepository
import com.example.boltalka.data.repository.ContactRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class BoltalkaApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            SeedDataHelper.seedIfEmpty(this@BoltalkaApp)
            syncFromRemote()
        }
    }

    private suspend fun syncFromRemote() {
        val chatRepo = ChatRepository(database.chatDao(), database.messageDao())
        val contactRepo = ContactRepository(database.contactDao())
        chatRepo.refreshChats()
        contactRepo.refreshContacts()
    }
}
