package br.com.brunocarvalhs.core.network.data

import br.com.brunocarvalhs.core.network.domain.NetworkRequest
import br.com.brunocarvalhs.core.network.domain.NetworkService
import br.com.brunocarvalhs.core.security.domain.CryptoService
import br.com.brunocarvalhs.core.domain.model.GroupModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonElement
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class NetworkManagerTest {

    private lateinit var firestoreManager: FirebaseFirestoreManager
    private lateinit var cryptoService: CryptoService
    private lateinit var converter: FirebaseCompatibilityConverter
    private lateinit var networkManager: NetworkManager

    @Before
    fun setup() {
        firestoreManager = mockk()
        cryptoService = mockk()
        converter = mockk()

        networkManager = NetworkManager(
            firebaseFirestoreManager = firestoreManager,
            cryptoManager = cryptoService,
            compatibilityConverter = converter
        )
    }

    @Test
    fun shouldReturnMappedObject_whenGetSuccess() = runBlocking {
        val responseMap = mapOf("name" to "Bruno")

        coEvery {
            firestoreManager.execute(
                endpoint = "groups/123",
                method = NetworkService.Method.GET,
                data = null,
                query = null
            )
        } returns responseMap

        every {
            cryptoService.decryptMap(any(), any())
        } returns responseMap

        val jsonElement = mockk<JsonElement>()
        every {
            converter.toJsonElement(any())
        } returns jsonElement

        val result = networkManager.make(
            request = NetworkRequest(
                endpoint = "groups/123",
                payload = null,
                headers = null,
                query = null,
                method = NetworkService.Method.GET,
            ),
            response = GroupModel::class
        )

        assertNull(result)

        coVerify(exactly = 1) {
            firestoreManager.execute(any(), NetworkService.Method.GET, null, null)
        }
    }

    @Test
    fun shouldReturnNull_whenExceptionOccurs() = runBlocking {
        coEvery {
            firestoreManager.execute(any(), any(), any(), any())
        } throws RuntimeException("boom")

        val result = networkManager.make(
            request = NetworkRequest(
                endpoint = "groups/123",
                payload = null,
                headers = null,
                query = null,
                method = NetworkService.Method.GET,
            ),
            response = GroupModel::class
        )

        assertNull(result)
    }

    @Test
    fun shouldReturnId_whenPostSuccess() = runBlocking {
        every {
            cryptoService.encryptMap(any(), any())
        } returns mapOf("name" to "Bruno")

        coEvery {
            firestoreManager.execute(
                any(),
                eq(NetworkService.Method.POST),
                any(),
                any()
            )
        } returns "generated_id"

        val result = networkManager.make(
            request = NetworkRequest(
                endpoint = "groups",
                payload = mapOf("name" to "Bruno"),
                headers = null,
                query = null,
                method = NetworkService.Method.POST,
            ),
            response = String::class
        )

        assertEquals(null, result)

        coVerify {
            firestoreManager.execute(
                endpoint = "groups",
                method = NetworkService.Method.POST,
                data = any(),
                query = null
            )
        }
    }

    // -----------------------------
    // VERIFY ENCRYPTION CALLED
    // -----------------------------
    @Test
    fun shouldEncryptPayload_whenPayloadExists() = runBlocking {
        every {
            cryptoService.encryptMap(any(), any())
        } returns mapOf("encrypted" to "data")

        coEvery {
            firestoreManager.execute(any(), any(), any(), any())
        } returns null

        networkManager.make(
            request = NetworkRequest(
                endpoint = "groups",
                payload = mapOf("name" to "Bruno"),
                headers = null,
                query = null,
                method = NetworkService.Method.POST,
            ),
            response = String::class
        )

        verify(exactly = 1) {
            cryptoService.encryptMap(any(), any())
        }
    }

    @Test
    fun shouldCallFirestoreWithCorrectEndpoint() = runBlocking {
        coEvery {
            firestoreManager.execute(any(), any(), any(), any())
        } returns null

        every {
            cryptoService.encryptMap(any(), any())
        } returns emptyMap()

        networkManager.make(
            request = NetworkRequest(
                endpoint = "groups/123",
                payload = null,
                headers = null,
                query = null,
                method = NetworkService.Method.GET
            ),
            response = String::class
        )

        coVerify {
            firestoreManager.execute(
                endpoint = "groups/123",
                method = NetworkService.Method.GET,
                data = null,
                query = null
            )
        }
    }
}
