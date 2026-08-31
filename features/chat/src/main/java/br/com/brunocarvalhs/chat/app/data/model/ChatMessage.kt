package br.com.brunocarvalhs.chat.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.brunocarvalhs.core.domain.model.MessageModel

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val groupId: String = "",
    val text: String = "",
    val isFromMe: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val senderName: String = "",
    val senderId: String = "",
    val status: MessageModel.MessageStatus = MessageModel.MessageStatus.SENT,
    val reactions: Map<String, String> = emptyMap()
)
