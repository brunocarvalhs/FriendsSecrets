package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import br.com.brunocarvalhs.storage.domain.StorageService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupExitUseCaseTest {

    private val repository: GroupDetailsRepository = mockk()
    private val storage: StorageService = mockk()
    private lateinit var useCase: GroupExitUseCase

    @Before
    fun setup() {
        useCase = GroupExitUseCase(repository, storage)
    }

    @Test
    fun `invoke should remove tokens and return success`() = runTest {
        // Given
        val group = GroupModel(id = "1", token = "token123")
        coEvery { repository.read("1") } returns group
        coEvery { storage.load(GroupModel.COLLECTION_NAME, Array<String>::class) } returns arrayOf("token123")
        coEvery { storage.load(GroupModel.COLLECTION_NAME_ADMIN, Array<String>::class) } returns arrayOf("token123")
        coEvery { storage.save(any(), any()) } returns Unit

        // When
        val result = useCase.invoke(group)

        // Then
        assertTrue(result.isSuccess)
        coVerify { storage.save(GroupModel.COLLECTION_NAME, emptyArray<String>()) }
        coVerify { storage.save(GroupModel.COLLECTION_NAME_ADMIN, emptyArray<String>()) }
    }

    @Test
    fun `invoke should return failure when repository fails`() = runTest {
        // Given
        val group = GroupModel(id = "1")
        coEvery { repository.read("1") } throws Exception("Error")

        // When
        val result = useCase.invoke(group)

        // Then
        assertTrue(result.isFailure)
    }
}
