package br.com.brunocarvalhs.friendssecrets.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MessageModelTest {

    @Test
    fun shouldCreateMessageModelWithDefaultValues() {
        val message = MessageModel()

        assertEquals("", message.id)
        assertEquals("", message.groupId)
        assertEquals("", message.text)
        assertEquals("", message.senderId)
        assertEquals("", message.senderName)

        assertTrue(message.timestamp > 0)
        assertEquals(MessageModel.MessageStatus.SENT, message.status)
    }

    @Test
    fun shouldCreateMessageModelWithCustomValues() {
        val timestamp = 123456789L

        val message = MessageModel(
            id = "msg1",
            groupId = "group1",
            text = "Hello",
            senderId = "user1",
            senderName = "Bruno",
            timestamp = timestamp,
            status = MessageModel.MessageStatus.SENDING
        )

        assertEquals("msg1", message.id)
        assertEquals("group1", message.groupId)
        assertEquals("Hello", message.text)
        assertEquals("user1", message.senderId)
        assertEquals("Bruno", message.senderName)
        assertEquals(timestamp, message.timestamp)
        assertEquals(MessageModel.MessageStatus.SENDING, message.status)
    }

    @Test
    fun shouldSupportAllMessageStatuses() {
        val sending = MessageModel(status = MessageModel.MessageStatus.SENDING)
        val sent = MessageModel(status = MessageModel.MessageStatus.SENT)
        val error = MessageModel(status = MessageModel.MessageStatus.ERROR)

        assertEquals(MessageModel.MessageStatus.SENDING, sending.status)
        assertEquals(MessageModel.MessageStatus.SENT, sent.status)
        assertEquals(MessageModel.MessageStatus.ERROR, error.status)
    }

    @Test
    fun shouldCopyMessageModelCorrectly() {
        val original = MessageModel(
            id = "1",
            text = "Hello",
            status = MessageModel.MessageStatus.SENDING
        )

        val updated = original.copy(
            status = MessageModel.MessageStatus.SENT
        )

        assertEquals("1", updated.id)
        assertEquals("Hello", updated.text)
        assertEquals(MessageModel.MessageStatus.SENT, updated.status)

        assertEquals(MessageModel.MessageStatus.SENDING, original.status)
    }

    @Test
    fun timestampShouldBeWithinCreationRange() {
        val before = System.currentTimeMillis()
        val message = MessageModel()
        val after = System.currentTimeMillis()

        assertTrue(message.timestamp in before..after)
    }
}
