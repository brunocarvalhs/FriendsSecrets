package br.com.brunocarvalhs.friendssecrets.commons.network

import br.com.brunocarvalhs.friendssecrets.domain.model.MessageModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FirebaseRealtimeManagerTest {

    private val database: FirebaseDatabase = mockk()
    private val chatsReference: DatabaseReference = mockk()
    private val groupReference: DatabaseReference = mockk()
    private lateinit var manager: FirebaseRealtimeManager

    @Before
    fun setup() {
        every { database.getReference("chats") } returns chatsReference
        every { chatsReference.child(any()) } returns groupReference
        manager = FirebaseRealtimeManager(database)
    }

    @Test
    fun `sendMessage should call setValue and return success`() = runTest {
        // Given
        val groupId = "group1"
        val message = MessageModel(id = "msg1", text = "Hello")
        val messageReference = mockk<DatabaseReference>()
        val task = Tasks.forResult<Void>(null)

        every { groupReference.child("msg1") } returns messageReference
        every { messageReference.setValue(any()) } returns task

        // When
        val result = manager.sendMessage(groupId, message)

        // Then
        assertTrue(result.exceptionOrNull()?.toString(), result.isSuccess)
        verify { messageReference.setValue(any()) }
    }

    @Test
    fun `clearMessages should call removeValue and return success`() = runTest {
        // Given
        val groupId = "group1"
        val task = Tasks.forResult<Void>(null)

        every { groupReference.removeValue() } returns task

        // When
        val result = manager.clearMessages(groupId)

        // Then
        assertTrue(result.exceptionOrNull()?.toString(), result.isSuccess)
        verify { groupReference.removeValue() }
    }
}
