package com.example.boltalka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.boltalka.ui.navigation.Screen
import com.example.boltalka.ui.screens.ChatScreen
import com.example.boltalka.ui.screens.ChatsScreen
import com.example.boltalka.ui.theme.BoltalkaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BoltalkaTheme {
                BoltActionApp()
            }
        }
    }
}

@Composable
fun BoltActionApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Chats.route
    ) {
        composable(Screen.Chats.route) {
            ChatsScreen(
                onChatClick = { chatId, chatName ->
                    navController.navigate(Screen.Chat.createRoute(chatId, chatName))
                }
            )
        }
        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("chatId") { type = NavType.LongType },
                navArgument("chatName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getLong("chatId") ?: return@composable
            val chatName = backStackEntry.arguments?.getString("chatName") ?: return@composable
            ChatScreen(
                chatId = chatId,
                chatName = chatName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
