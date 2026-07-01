package com.example.boltalka.data.repository

import com.example.boltalka.data.local.dao.ContactDao
import com.example.boltalka.data.local.entity.ContactEntity
import com.example.boltalka.data.remote.NetworkDataSource
import com.example.boltalka.data.remote.toEntity
import kotlinx.coroutines.flow.Flow

class ContactRepository(
    private val contactDao: ContactDao,
    private val networkDataSource: NetworkDataSource = NetworkDataSource()
) {
    val allContacts: Flow<List<ContactEntity>> = contactDao.getAllContacts()

    suspend fun refreshContacts() {
        when (val result = networkDataSource.fetchContacts()) {
            is NetworkDataSource.NetworkResult.Success -> {
                val entities = result.data.map { it.toEntity() }
                entities.forEach { contactDao.insertContact(it) }
            }
            is NetworkDataSource.NetworkResult.Error -> { /* offline — keep local cache */ }
        }
    }

    suspend fun addContact(name: String, phone: String, iconColor: Int): Long {
        val remoteResult = networkDataSource.createContact(name, phone, iconColor)
        return when (remoteResult) {
            is NetworkDataSource.NetworkResult.Success -> {
                val remoteId = remoteResult.data
                contactDao.insertContact(ContactEntity(id = remoteId, name = name, phone = phone, iconColor = iconColor))
                remoteId
            }
            is NetworkDataSource.NetworkResult.Error -> {
                contactDao.insertContact(ContactEntity(name = name, phone = phone, iconColor = iconColor))
            }
        }
    }

    suspend fun deleteContact(contact: ContactEntity) {
        networkDataSource.deleteContact(contact.id)
        contactDao.deleteContact(contact)
    }
}
