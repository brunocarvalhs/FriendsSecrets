package br.com.brunocarvalhs.core.domain.model

data class MessageModel(
    val id: String = "",
    val groupId: String = "",
    val text: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: MessageStatus = MessageStatus.SENT,
    val reactions: Map<String, String> = emptyMap()
) {
    enum class MessageStatus {
        SENDING, SENT, ERROR
    }
}
