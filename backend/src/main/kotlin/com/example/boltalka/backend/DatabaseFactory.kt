package com.example.boltalka.backend

import com.example.boltalka.backend.models.ContactsTable
import com.example.boltalka.backend.models.ChatsTable
import com.example.boltalka.backend.models.MessagesTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val url = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/boltalka"
        val user = System.getenv("DB_USER") ?: "postgres"
        val password = System.getenv("DB_PASSWORD") ?: "postgres"

        Database.connect(url, driver = "org.postgresql.Driver", user = user, password = password)

        transaction {
            SchemaUtils.createMissingTablesAndColumns(ChatsTable, MessagesTable, ContactsTable)
        }
    }
}
