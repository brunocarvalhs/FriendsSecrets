package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.services.DeviceService
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupReadUseCaseTest {

    private val repository: GroupDetailsRepository = mockk()
    private val deviceService: DeviceService = mockk()
    private lateinit var useCase: GroupReadUseCase

    @Before
    fun setup() {
        useCase = GroupReadUseCase(repository, deviceService)
    }

    @Test
    fun `invoke should return group and identify owner correctly`() = runTest {
        // Given
        val groupId = "1"
        val ownerId = "owner_123"
        val group = GroupModel(id = groupId, ownerId = ownerId)
        
        coEvery { deviceService.getDeviceId() } returns ownerId
        coEvery { repository.read(groupId) } returns group

        // When
        val result = useCase(groupId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull()?.isOwner)
    }

    @Test
    fun `invoke should return group and identify non-owner correctly`() = runTest {
        // Given
        val groupId = "1"
        val ownerId = "owner_123"
        val myId = "my_id_456"
        val group = GroupModel(id = groupId, ownerId = ownerId)

        coEvery { deviceService.getDeviceId() } returns myId
        coEvery { repository.read(groupId) } returns group

        // When
        val result = useCase(groupId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(false, result.getOrNull()?.isOwner)
    }
}
