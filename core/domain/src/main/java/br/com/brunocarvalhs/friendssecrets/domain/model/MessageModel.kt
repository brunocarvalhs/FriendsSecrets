package br.com.brunocarvalhs.friendssecrets.domain.model

enum class MessageStatus {
    SENDING, SENT, ERROR
}

data class MessageModel(
    val id: String = "",
    val groupId: String = "",
    val text: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: MessageStatus = MessageStatus.SENT
)
