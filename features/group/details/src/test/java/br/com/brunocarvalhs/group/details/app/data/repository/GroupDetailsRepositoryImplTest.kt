package br.com.brunocarvalhs.group.details.app.data.repository

import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkService
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.group.details.app.data.model.GroupDetailsDTO
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GroupDetailsRepositoryImplTest {

    private val network: NetworkService = mockk()
    private lateinit var repository: GroupDetailsRepositoryImpl

    @Before
    fun setup() {
        repository = GroupDetailsRepositoryImpl(network)
    }

    @Test
    fun `read should return group when network succeeds`() = runTest {
        // Given
        val groupId = "1"
        val dto = GroupDetailsDTO(id = groupId, name = "Test")
        coEvery {
            network.make(
                endpoint = "groups/$groupId",
                method = NetworkService.Method.GET,
                clazz = GroupDetailsDTO::class
            )
        } returns dto

        // When
        val result = repository.read(groupId)

        // Then
        assertEquals(groupId, result.id)
        assertEquals("Test", result.name)
    }

    @Test(expected = GroupNotFoundException::class)
    fun `read should throw exception when network returns null`() = runTest {
        // Given
        val groupId = "1"
        coEvery {
            network.make(
                endpoint = "groups/$groupId",
                method = NetworkService.Method.GET,
                clazz = GroupDetailsDTO::class
            )
        } returns null

        // When
        repository.read(groupId)
    }

    @Test
    fun `delete should complete when network returns true`() = runTest {
        // Given
        val group = GroupModel(id = "1")
        coEvery {
            network.make(
                endpoint = "groups/1",
                method = NetworkService.Method.DELETE,
                clazz = Boolean::class
            )
        } returns true

        // When
        repository.delete(group)
        
        // Then - Should not throw exception
    }
}
