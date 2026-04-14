package br.com.brunocarvalhs.group.create.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.services.DeviceService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.group.create.app.domain.repositories.GroupCreateRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupCreateUseCaseTest {

    private val repository: GroupCreateRepository = mockk()
    private val storage: StorageService = mockk()
    private val deviceService: DeviceService = mockk()
    private lateinit var useCase: GroupCreateUseCase

    @Before
    fun setup() {
        useCase = GroupCreateUseCase(repository, storage, deviceService)
    }

    @Test
    fun `invoke should create group and persist tokens`() = runTest {
        // Given
        val group = GroupModel(token = "token123")
        val deviceId = "device123"
        
        coEvery { deviceService.getDeviceId() } returns deviceId
        coEvery { repository.create(any()) } returns Result.success(Unit)
        coEvery { storage.load(GroupModel.COLLECTION_NAME, Array<String>::class) } returns emptyArray<String>()
        coEvery { storage.load(GroupModel.COLLECTION_NAME_ADMIN, Array<String>::class) } returns emptyArray<String>()
        coEvery { storage.save(any<String>(), any<Any>()) } returns Unit

        // When
        val result = useCase(group)

        // Then
        assertTrue(result.isSuccess)
        coVerify { repository.create(match { it.ownerId == deviceId }) }
        coVerify { storage.save(GroupModel.COLLECTION_NAME, arrayOf("token123")) }
        coVerify { storage.save(GroupModel.COLLECTION_NAME_ADMIN, arrayOf("token123")) }
    }
}
