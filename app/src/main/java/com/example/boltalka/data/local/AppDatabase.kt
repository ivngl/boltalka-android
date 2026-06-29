package com.example.boltalka.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.boltalka.data.local.dao.ChatDao
import com.example.boltalka.data.local.dao.ContactDao
import com.example.boltalka.data.local.dao.MessageDao
import com.example.boltalka.data.local.entity.ChatEntity
import com.example.boltalka.data.local.entity.ContactEntity
import com.example.boltalka.data.local.entity.MessageEntity

@Database(
    entities = [ChatEntity::class, MessageEntity::class, ContactEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "boltalka_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
