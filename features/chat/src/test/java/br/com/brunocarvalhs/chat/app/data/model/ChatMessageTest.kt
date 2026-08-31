package br.com.brunocarvalhs.chat.app.data.model

import br.com.brunocarvalhs.core.domain.model.MessageModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ChatMessageTest {

    @Test
    fun `constructor should set correct values`() {
        // Given
        val id = "1"
        val groupId = "group_1"
        val text = "Hello"
        val senderId = "user_1"
        val senderName = "Bruno"
        val timestamp = 1000L
        val status = MessageModel.MessageStatus.SENT
        val isFromMe = true

        // When
        val message = ChatMessage(
            id = id,
            groupId = groupId,
            text = text,
            senderId = senderId,
            senderName = senderName,
            timestamp = timestamp,
            status = status,
            isFromMe = isFromMe
        )

        // Then
        assertEquals(id, message.id)
        assertEquals(groupId, message.groupId)
        assertEquals(text, message.text)
        assertEquals(senderId, message.senderId)
        assertEquals(senderName, message.senderName)
        assertEquals(timestamp, message.timestamp)
        assertEquals(status, message.status)
        assertEquals(isFromMe, message.isFromMe)
    }

    @Test
    fun `default values should be correct`() {
        // When
        val message = ChatMessage()

        // Then
        assertEquals("", message.groupId)
        assertEquals("", message.text)
        assertEquals(MessageModel.MessageStatus.SENT, message.status)
        assertEquals(false, message.isFromMe)
        assertEquals(emptyMap<String, String>(), message.reactions)
    }

    @Test
    fun `constructor should set reactions`() {
        // Given
        val reactions = mapOf("device-1" to "👍")

        // When
        val message = ChatMessage(reactions = reactions)

        // Then
        assertEquals(reactions, message.reactions)
    }
}
