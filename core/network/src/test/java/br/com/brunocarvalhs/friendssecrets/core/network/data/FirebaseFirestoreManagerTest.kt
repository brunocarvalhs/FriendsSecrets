
import br.com.brunocarvalhs.friendssecrets.core.network.data.FirebaseFirestoreManager
import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkService
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FirebaseFirestoreManagerTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var manager: FirebaseFirestoreManager

    @Before
    fun setup() {
        firestore = mockk(relaxed = true)
        manager = FirebaseFirestoreManager(firestore)
    }

    @Test
    fun shouldGetDocumentById() = runBlocking {
        val collection = mockk<CollectionReference>()
        val document = mockk<DocumentReference>()
        val snapshot = mockk<DocumentSnapshot>()

        every { firestore.collection("users") } returns collection
        every { collection.document("123") } returns document
        coEvery { document.get().await() } returns snapshot

        every { snapshot.data } returns mutableMapOf("name" to "Bruno")
        every { snapshot.id } returns "123"

        val result = manager.execute(
            endpoint = "users/123",
            method = NetworkService.Method.GET
        ) as Map<*, *>

        assertEquals("Bruno", result["name"])
        assertEquals("123", result["id"])
    }

    @Test
    fun shouldGetCollectionWithFilter() = runBlocking {
        val collection = mockk<CollectionReference>()
        val query = mockk<Query>()
        val snapshot = mockk<QuerySnapshot>()
        val doc = mockk<DocumentSnapshot>()

        every { firestore.collection("users") } returns collection
        every { collection.whereEqualTo("age", 20) } returns query
        coEvery { query.get().await() } returns snapshot

        every { snapshot.documents } returns listOf(doc)
        every { doc.data } returns mutableMapOf("name" to "Ana")
        every { doc.id } returns "1"

        val result = manager.execute(
            endpoint = "users",
            method = NetworkService.Method.GET,
            query = mapOf("age" to 20)
        ) as List<*>

        val item = result.first() as Map<*, *>

        assertEquals("Ana", item["name"])
        assertEquals("1", item["id"])
    }

    @Test
    fun shouldPostDataAndReturnId() = runBlocking {
        val collection = mockk<CollectionReference>()
        val docRef = mockk<DocumentReference>()

        every { firestore.collection("users") } returns collection
        coEvery { collection.add(any()).await() } returns docRef
        every { docRef.id } returns "generatedId"

        val result = manager.execute(
            endpoint = "users",
            method = NetworkService.Method.POST,
            data = mapOf("name" to "Bruno")
        )

        assertEquals("generatedId", result)
    }

    @Test
    fun shouldUpdateWithoutIdAndNulls() = runBlocking {
        val collection = mockk<CollectionReference>()
        val document = mockk<DocumentReference>()

        every { firestore.collection("users") } returns collection
        every { collection.document("123") } returns document
        coEvery { document.update(any()).await() } returns null
        manager.execute(
            endpoint = "users/123",
            method = NetworkService.Method.PUT,
            data = mapOf(
                "id" to "123",
                "name" to "Bruno",
                "age" to null
            )
        )

        coVerify {
            document.update(match {
                it.containsKey("name") &&
                        !it.containsKey("id") &&
                        !it.containsKey("age")
            })
        }
    }

    @Test
    fun shouldDeleteDocument() = runBlocking {
        val collection = mockk<CollectionReference>()
        val document = mockk<DocumentReference>()

        every { firestore.collection("users") } returns collection
        every { collection.document("123") } returns document
        coEvery { document.delete().await() } returns null

        val result = manager.execute(
            endpoint = "users/123",
            method = NetworkService.Method.DELETE
        )

        assertEquals(true, result)
    }

    @Test
    fun shouldSplitWhereInIntoChunks() = runBlocking {
        val collection = mockk<CollectionReference>()
        val query = mockk<Query>()
        val snapshot = mockk<QuerySnapshot>()

        every { firestore.collection("users") } returns collection
        every {
            collection.whereIn(any<String>(), any())
        } returns query
        coEvery { query.get().await() } returns snapshot
        every { snapshot.documents } returns emptyList()

        val ids = (1..25).map { it.toString() }

        manager.execute(
            endpoint = "users",
            method = NetworkService.Method.GET,
            query = mapOf("id" to ids)
        )

        verify(atLeast = 3) {
            collection.whereIn("id", any())
        }
    }
}