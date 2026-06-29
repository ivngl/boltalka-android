package com.example.boltalka.ui.navigation

sealed class Screen(val route: String) {
    data object Chats : Screen("chats")
    data object Chat : Screen("chat/{chatId}/{chatName}") {
        fun createRoute(chatId: Long, chatName: String): String =
            "chat/$chatId/$chatName"
    }
}
