package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupEditUseCaseTest {

    private lateinit var repository: GroupRepository
    private lateinit var performance: PerformanceManager
    private lateinit var useCase: GroupEditUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        performance = mockk(relaxed = true)

        every { performance.start(any()) } just runs
        every { performance.stop(any()) } just runs

        useCase = GroupEditUseCase(
            groupRepository = repository,
            performance = performance
        )
    }

    @Test
    fun `invoke should update group successfully`() = runBlocking {
        // Arrange
        val group = mockk<GroupEntities>(relaxed = true)
        coEvery { repository.update(group) } just runs

        // Act
        val result = useCase.invoke(group)

        // Assert
        assertTrue(result.isSuccess)
        coVerify { repository.update(group) }
        verify { performance.start(GroupEditUseCase::class.java.simpleName) }
        verify { performance.stop(GroupEditUseCase::class.java.simpleName) }
    }

    @Test
    fun `invoke should handle exception when update fails`() = runBlocking {
        // Arrange
        val group = mockk<GroupEntities>(relaxed = true)
        coEvery { repository.update(group) } throws RuntimeException("Update failed")

        // Act
        val result = useCase.invoke(group)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
        verify { performance.start(GroupEditUseCase::class.java.simpleName) }
        verify { performance.stop(GroupEditUseCase::class.java.simpleName) }
    }
}