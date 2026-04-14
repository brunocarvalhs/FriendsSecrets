package br.com.brunocarvalhs.group.list.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.services.DeviceService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.group.list.app.data.model.GroupListDTO
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupListUseCaseTest {

    private val repository: GroupListRepository = mockk()
    private val storage: StorageService = mockk()
    private val deviceService: DeviceService = mockk()
    private lateinit var useCase: GroupListUseCase

    @Before
    fun setup() {
        useCase = GroupListUseCase(repository, storage, deviceService)
    }

    @Test
    fun `invoke should return empty list when no tokens are found`() = runTest {
        // Given
        coEvery { storage.load(GroupModel.COLLECTION_NAME, Array<String>::class) } returns null

        // When
        val result = useCase.invoke()

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()!!.isEmpty())
    }

    @Test
    fun `invoke should return groups and map owner correctly`() = runTest {
        // Given
        val tokens = arrayOf("token1")
        val ownerId = "me"
        val dto = GroupListDTO(id = "1", owner_id = ownerId)

        coEvery { storage.load(GroupModel.COLLECTION_NAME, Array<String>::class) } returns tokens
        coEvery { deviceService.getDeviceId() } returns ownerId
        coEvery { repository.list(tokens.toList()) } returns listOf(dto)

        // When
        val result = useCase.invoke()

        // Then
        assertTrue(result.isSuccess)
        val list = result.getOrNull()!!
        assertEquals(1, list.size)
        assertTrue(list[0].isOwner)
    }

    @Test
    fun `invoke should return failure when storage fails`() = runTest {
        // Given
        coEvery { storage.load<Array<String>>(any(), any()) } throws Exception("Storage Error")

        // When
        val result = useCase.invoke()

        // Then
        assertTrue(result.isFailure)
    }
}
