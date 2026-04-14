package br.com.brunocarvalhs.chat.app.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.com.brunocarvalhs.chat.app.data.model.ChatMessage
import br.com.brunocarvalhs.friendssecrets.domain.model.MessageStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class ChatDatabaseTest {

    private lateinit var db: ChatDatabase
    private lateinit var dao: ChatMessageDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ChatDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.chatMessageDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun `insert and get messages from specific group`() = runTest {
        // Given
        val groupId = "group_1"
        val message = ChatMessage(
            id = "msg_1",
            groupId = groupId,
            text = "Hello World",
            senderName = "Bruno",
            status = MessageStatus.SENT
        )

        // When
        dao.insertMessage(message)
        val messages = dao.getMessages(groupId).first()

        // Then
        assertEquals(1, messages.size)
        assertEquals(message.text, messages[0].text)
        assertEquals(message.id, messages[0].id)
    }

    @Test
    fun `clear messages from specific group`() = runTest {
        // Given
        val groupId = "group_1"
        dao.insertMessage(ChatMessage(id = "1", groupId = groupId, text = "Msg 1"))
        dao.insertMessage(ChatMessage(id = "2", groupId = groupId, text = "Msg 2"))

        // When
        dao.clearMessages(groupId)
        val messages = dao.getMessages(groupId).first()

        // Then
        assertTrue(messages.isEmpty())
    }

    @Test
    fun `get messages should be ordered by timestamp`() = runTest {
        // Given
        val groupId = "group_1"
        val msg1 = ChatMessage(id = "1", groupId = groupId, text = "Old", timestamp = 1000L)
        val msg2 = ChatMessage(id = "2", groupId = groupId, text = "New", timestamp = 2000L)
        
        dao.insertMessages(listOf(msg2, msg1)) // Inserção fora de ordem

        // When
        val messages = dao.getMessages(groupId).first()

        // Then
        assertEquals(2, messages.size)
        assertEquals("Old", messages[0].text)
        assertEquals("New", messages[1].text)
    }
}
