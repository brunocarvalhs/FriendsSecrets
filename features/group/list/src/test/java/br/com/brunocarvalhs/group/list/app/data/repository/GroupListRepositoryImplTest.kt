package br.com.brunocarvalhs.group.list.app.data.repository

import br.com.brunocarvalhs.deviceid.DeviceService
import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkRequest
import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkService
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.group.list.app.data.model.GroupListDTO
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupListRepositoryImplTest {

    private val network: NetworkService = mockk()
    private val device: DeviceService = mockk()
    private lateinit var repository: GroupListRepositoryImpl

    @Before
    fun setup() {
        repository = GroupListRepositoryImpl(network, device)
    }

    @Test
    fun `list should return empty list when no tokens are provided`() = runTest {
        val result = repository.list(emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `list should combine admin and token-based groups and remove duplicates`() = runTest {
        // Given
        val tokens = listOf("t1")
        val deviceId = "me"
        val group1 = GroupListDTO(id = "1", token = "t1")
        val group2 = GroupListDTO(id = "2", token = "t2")

        coEvery { device.getDeviceId() } returns deviceId
        coEvery {
            network.make(
                request = NetworkRequest(
                    endpoint = GroupModel.COLLECTION_NAME,
                    query = any(),
                    method = NetworkService.Method.GET
                ),
                response = Array<GroupListDTO>::class
            )
        } returns arrayOf(group1) andThen arrayOf(group1, group2)

        // When
        val result = repository.list(tokens)

        // Then
        assertEquals(2, result.size)
        assertTrue(result.any { it.id == "1" })
        assertTrue(result.any { it.id == "2" })
    }

    @Test
    fun `searchByToken should return first group from network response`() = runTest {
        // Given
        val token = "ABC"
        val group = GroupListDTO(id = "1", token = token)
        coEvery {
            network.make(
                request = NetworkRequest(
                    endpoint = GroupModel.COLLECTION_NAME,
                    query = mapOf(GroupModel.TOKEN to token),
                    method = NetworkService.Method.GET
                ),
                response = Array<GroupListDTO>::class
            )
        } returns arrayOf(group)

        // When
        val result = repository.searchByToken(token)

        // Then
        assertEquals(group, result)
    }

    @Test
    fun `searchByToken should return null when network returns null`() = runTest {
        // Given
        val token = "UNKNOWN"
        coEvery {
            network.make<GroupListDTO>(any(), any())
        } returns null

        // When
        val result = repository.searchByToken(token)

        // Then
        assertEquals(null, result)
    }
}
