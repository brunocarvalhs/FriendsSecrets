package br.com.brunocarvalhs.group.draw.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.draw.app.domain.repository.DrawRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DrawUseCaseTest {

    private val repository: DrawRepository = mockk()
    private lateinit var useCase: DrawUseCase

    @Before
    fun setup() {
        useCase = DrawUseCase(repository)
    }

    @Test
    fun `invoke should return results when validation passes`() = runTest {
        // Given
        val members = listOf(
            UserModel(id = "1"),
            UserModel(id = "2"),
            UserModel(id = "3")
        )
        val group = GroupModel(members = members, draws = emptyMap())
        val expectedResults = mapOf("1" to "2", "2" to "3", "3" to "1")
        coEvery { repository.drawMembers(group) } returns expectedResults

        // When
        val result = useCase(group)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedResults, result.getOrNull())
    }

    @Test
    fun `invoke should return failure when members size is less than 3`() = runTest {
        // Given
        val members = listOf(UserModel(id = "1"), UserModel(id = "2"))
        val group = GroupModel(members = members, draws = emptyMap())

        // When
        val result = useCase(group)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke should return failure when draws is not empty`() = runTest {
        // Given
        val members = listOf(UserModel(id = "1"), UserModel(id = "2"), UserModel(id = "3"))
        val group = GroupModel(members = members, draws = mapOf("1" to "2"))

        // When
        val result = useCase(group)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }
}
