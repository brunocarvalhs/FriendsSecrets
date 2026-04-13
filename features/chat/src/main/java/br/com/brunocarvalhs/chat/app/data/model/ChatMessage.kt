package br.com.brunocarvalhs.chat.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val groupId: String = "",
    val text: String = "",
    val isFromMe: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val senderName: String = "",
    val senderId: String = ""
)
