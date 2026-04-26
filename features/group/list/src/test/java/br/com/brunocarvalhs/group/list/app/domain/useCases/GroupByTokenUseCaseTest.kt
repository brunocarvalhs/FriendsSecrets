package br.com.brunocarvalhs.group.list.app.domain.useCases

import br.com.brunocarvalhs.deviceid.DeviceService
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.list.app.data.exceptions.GroupAlreadyExistException
import br.com.brunocarvalhs.group.list.app.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.group.list.app.data.model.GroupListDTO
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import br.com.brunocarvalhs.storage.domain.StorageService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupByTokenUseCaseTest {

    private val repository: GroupListRepository = mockk()
    private val storage: StorageService = mockk()
    private val device: DeviceService = mockk()
    private lateinit var useCase: GroupByTokenUseCase

    @Before
    fun setup() {
        useCase = GroupByTokenUseCase(repository, storage, device)
    }

    @Test
    fun `invoke should return group and save token when successful`() = runTest {
        // Given
        val token = "VALID_TOKEN"
        val ownerId = "me"
        val dto = GroupListDTO(id = "1", token = token, ownerId = ownerId)
        
        coEvery { device.getDeviceId() } returns ownerId
        coEvery { repository.searchByToken(token) } returns dto
        coEvery { storage.load(GroupModel.COLLECTION_NAME, Array<String>::class) } returns emptyArray()
        coEvery { storage.save(GroupModel.COLLECTION_NAME, any()) } returns Unit

        // When
        val result = useCase.invoke(token)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(token, result.getOrNull()?.token)
        assertTrue(result.getOrNull()!!.isOwner)
        coVerify { storage.save(GroupModel.COLLECTION_NAME, any()) }
    }

    @Test
    fun `invoke should return failure when token is blank`() = runTest {
        // When
        val result = useCase.invoke("")

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke should return failure when group not found`() = runTest {
        // Given
        val token = "UNKNOWN"
        coEvery { device.getDeviceId() } returns "any"
        coEvery { repository.searchByToken(token) } returns null

        // When
        val result = useCase.invoke(token)

        // Then
        assertTrue(result.toString(), result.isFailure)
        assertTrue(result.exceptionOrNull() is GroupNotFoundException)
    }

    @Test
    fun `invoke should return failure when group already exists in list`() = runTest {
        // Given
        val token = "EXISTING"
        val dto = GroupListDTO(id = "1", token = token)
        coEvery { device.getDeviceId() } returns "any"
        coEvery { repository.searchByToken(token) } returns dto
        coEvery { storage.load(GroupModel.COLLECTION_NAME, Array<String>::class) } returns arrayOf(token)

        // When
        val result = useCase.invoke(token)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is GroupAlreadyExistException)
    }
}
