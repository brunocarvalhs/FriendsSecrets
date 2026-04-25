package br.com.brunocarvalhs.group.create.app.data.repository

import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkRequest
import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkService
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.group.create.app.data.model.GroupCreateDTO
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupCreateRepositoryImplTest {

    private val network: NetworkService = mockk()
    private lateinit var repository: GroupCreateRepositoryImpl

    @Before
    fun setup() {
        repository = GroupCreateRepositoryImpl(network)
    }

    @Test
    fun `create should return success when network call succeeds`() = runTest {
        // Given
        val group = GroupModel(id = "1", name = "Test")
        coEvery {
            network.make(
                request = NetworkRequest(
                    endpoint = "groups",
                    payload = any(),
                    method = NetworkService.Method.POST
                ),
                response = GroupCreateDTO::class
            )
        } returns mockk<GroupCreateDTO>()

        // When
        val result = repository.create(group)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `create should return failure when network returns null`() = runTest {
        // Given
        val group = GroupModel(id = "1", name = "Test")
        coEvery {
            network.make(
                request = NetworkRequest(
                    endpoint = "groups",
                    payload = any(),
                    method = NetworkService.Method.POST
                ),
                response = GroupCreateDTO::class
            )
        } returns null

        // When
        val result = repository.create(group)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `update should return success when network call succeeds`() = runTest {
        // Given
        val group = GroupModel(id = "1", name = "Test")
        coEvery {
            network.make(
                request = NetworkRequest(
                    endpoint = "groups/1",
                    payload = any(),
                    method = NetworkService.Method.PUT
                ),
                response = GroupCreateDTO::class
            )
        } returns mockk<GroupCreateDTO>()

        // When
        val result = repository.update(group)

        // Then
        assertTrue(result.isSuccess)
    }
}
