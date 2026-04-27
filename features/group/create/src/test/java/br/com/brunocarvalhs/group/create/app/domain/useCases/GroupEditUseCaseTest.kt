package br.com.brunocarvalhs.group.create.app.domain.useCases

import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.repositories.GroupCreateRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupEditUseCaseTest {

    private val repository: GroupCreateRepository = mockk()
    private lateinit var useCase: GroupEditUseCase

    @Before
    fun setup() {
        useCase = GroupEditUseCase(repository)
    }

    @Test
    fun `invoke should call repository update and return success`() = runTest {
        // Given
        val group = mockk<GroupModel>()
        coEvery { repository.update(group) } returns Result.success(Unit)

        // When
        val result = useCase(group)

        // Then
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { repository.update(group) }
    }

    @Test
    fun `invoke should return failure when repository fails`() = runTest {
        // Given
        val group = mockk<GroupModel>()
        val exception = RuntimeException("Error")
        coEvery { repository.update(group) } throws exception

        // When
        val result = useCase(group)

        // Then
        assertTrue(result.isFailure)
        coVerify(exactly = 1) { repository.update(group) }
    }
}
