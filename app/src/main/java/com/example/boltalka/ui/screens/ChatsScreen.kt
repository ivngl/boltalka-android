package com.example.boltalka.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.boltalka.ui.components.ChatListItem
import com.example.boltalka.ui.theme.AppBarColor
import com.example.boltalka.ui.theme.Gray400
import com.example.boltalka.viewmodel.ChatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    onChatClick: (Long, String) -> Unit,
    viewModel: ChatsViewModel = viewModel()
) {
    val chats by viewModel.chats.collectAsState(initial = emptyList())
    val showDialog by viewModel.showNewChatDialog.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Boltalka") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBarColor,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onNewChatClick() },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New chat",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    ) { padding ->
        if (chats.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No conversations yet",
                    style = MaterialTheme.typography.titleLarge,
                    color = Gray400
                )
                Text(
                    text = "Tap + to start a new chat",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray400,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(chats, key = { it.id }) { chat ->
                    ChatListItem(
                        chat = chat,
                        onClick = { onChatClick(chat.id, chat.name) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        NewChatDialog(
            onDismiss = { viewModel.onDismissNewChatDialog() },
            onCreate = { name, phone ->
                val colors = listOf(
                    0xFF075E54.toInt(),
                    0xFF128C7E.toInt(),
                    0xFF25D366.toInt(),
                    0xFF039BE5.toInt(),
                    0xFFE91E63.toInt(),
                    0xFF9C27B0.toInt(),
                    0xFFFF5722.toInt(),
                    0xFF607D8B.toInt()
                )
                val iconColor = colors[name.length % colors.size]
                viewModel.createChat(name, phone, iconColor)
            }
        )
    }
}

@Composable
fun NewChatDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Chat") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Contact name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onCreate(name.trim(), phone.trim()) },
                enabled = name.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
