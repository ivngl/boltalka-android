package com.example.boltalka.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.boltalka.data.local.entity.MessageEntity
import com.example.boltalka.ui.theme.Gray600
import com.example.boltalka.ui.theme.ReceivedBubble
import com.example.boltalka.ui.theme.SentBubble
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessageBubble(
    message: MessageEntity,
    modifier: Modifier = Modifier
) {
    val isSent = message.isSent
    val bubbleColor = if (isSent) SentBubble else ReceivedBubble
    val alignment = if (isSent) Arrangement.End else Arrangement.Start

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = if (isSent) 64.dp else 12.dp,
                end = if (isSent) 12.dp else 64.dp,
                top = 2.dp,
                bottom = 2.dp
            ),
        horizontalArrangement = if (isSent) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isSent) 16.dp else 4.dp,
                        bottomEnd = if (isSent) 4.dp else 16.dp
                    )
                )
                .background(bubbleColor)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = formatTime(message.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = Gray600,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
