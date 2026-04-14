package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupDeleteUseCaseTest {

    private val repository: GroupDetailsRepository = mockk()
    private val storage: StorageService = mockk()
    private lateinit var useCase: GroupDeleteUseCase

    @Before
    fun setup() {
        useCase = GroupDeleteUseCase(repository, storage)
    }

    @Test
    fun `invoke should delete group and remove tokens`() = runTest {
        // Given
        val group = GroupModel(id = "1", token = "token123")
        coEvery { repository.read("1") } returns group
        coEvery { repository.delete(any()) } returns Unit
        coEvery { storage.load(GroupModel.COLLECTION_NAME, Array<String>::class) } returns arrayOf("token123")
        coEvery { storage.load(GroupModel.COLLECTION_NAME_ADMIN, Array<String>::class) } returns arrayOf("token123")
        coEvery { storage.save(any(), any()) } returns Unit

        // When
        val result = useCase.invoke(group)

        // Then
        assertTrue(result.isSuccess)
        coVerify { repository.delete(group) }
        coVerify { storage.save(GroupModel.COLLECTION_NAME, emptyArray<String>()) }
        coVerify { storage.save(GroupModel.COLLECTION_NAME_ADMIN, emptyArray<String>()) }
    }
}
