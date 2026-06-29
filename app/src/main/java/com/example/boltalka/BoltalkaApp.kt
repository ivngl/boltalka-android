package com.example.boltalka

import android.app.Application
import com.example.boltalka.data.local.AppDatabase
import com.example.boltalka.data.local.SeedDataHelper
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
        }
    }
}
