package com.example.boltalka.data.repository

import com.example.boltalka.data.local.dao.ContactDao
import com.example.boltalka.data.local.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

class ContactRepository(
    private val contactDao: ContactDao
) {
    val allContacts: Flow<List<ContactEntity>> = contactDao.getAllContacts()

    suspend fun addContact(name: String, phone: String, iconColor: Int): Long {
        return contactDao.insertContact(
            ContactEntity(
                name = name,
                phone = phone,
                iconColor = iconColor
            )
        )
    }

    suspend fun deleteContact(contact: ContactEntity) {
        contactDao.deleteContact(contact)
    }
}
