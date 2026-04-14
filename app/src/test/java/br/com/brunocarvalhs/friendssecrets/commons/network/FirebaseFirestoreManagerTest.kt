package br.com.brunocarvalhs.friendssecrets.commons.network

import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FirebaseFirestoreManagerTest {

    private val firestore: FirebaseFirestore = mockk()
    private lateinit var manager: FirebaseFirestoreManager

    @Before
    fun setup() {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        manager = FirebaseFirestoreManager(firestore)
    }

    @Test
    fun `execute GET should return document data when endpoint has id`() = runTest {
        // Given
        val endpoint = "groups/123"
        val collectionRef = mockk<CollectionReference>()
        val documentRef = mockk<DocumentReference>()
        val snapshot = mockk<DocumentSnapshot>()
        val data = mapOf("name" to "Secret Santa")

        every { firestore.collection("groups") } returns collectionRef
        every { collectionRef.document("123") } returns documentRef
        val task = mockk<Task<DocumentSnapshot>>()
        every { documentRef.get() } returns task
        coEvery { task.await() } returns snapshot
        every { snapshot.data } returns data
        every { snapshot.id } returns "123"

        // When
        val result = manager.execute(endpoint, NetworkService.Method.GET) as? Map<*, *>

        // Then
        assertEquals("Secret Santa", result?.get("name"))
        assertEquals("123", result?.get("id"))
    }

    @Test
    fun `execute POST should return document id`() = runTest {
        // Given
        val endpoint = "groups"
        val data = mapOf("name" to "New Group")
        val collectionRef = mockk<CollectionReference>()
        val documentRef = mockk<DocumentReference>()
        val task = mockk<Task<DocumentReference>>()

        every { firestore.collection("groups") } returns collectionRef
        every { collectionRef.add(data) } returns task
        coEvery { task.await() } returns documentRef
        every { documentRef.id } returns "new_id"

        // When
        val result = manager.execute(endpoint, NetworkService.Method.POST, data = data)

        // Then
        assertEquals("new_id", result)
    }

    @Test
    fun `execute DELETE should return true`() = runTest {
        // Given
        val endpoint = "groups/123"
        val collectionRef = mockk<CollectionReference>()
        val documentRef = mockk<DocumentReference>()
        val task = mockk<Task<Void>>()

        every { firestore.collection("groups") } returns collectionRef
        every { collectionRef.document("123") } returns documentRef
        every { documentRef.delete() } returns task
        coEvery { task.await() } returns mockk()

        // When
        val result = manager.execute(endpoint, NetworkService.Method.DELETE)

        // Then
        assertTrue(result as Boolean)
    }
}
