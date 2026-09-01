package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddMemberLikeUseCaseTest {

    private val repository: GroupDetailsRepository = mockk()
    private lateinit var useCase: AddMemberLikeUseCase

    @Before
    fun setup() {
        useCase = AddMemberLikeUseCase(repository)
    }

    @Test
    fun `invoke should append a like to the target member`() = runTest {
        // Given
        val target = UserModel(id = "device-2", name = "Isabella")
        val group = GroupModel(id = "group-1", members = listOf(target))

        coEvery { repository.update(any()) } answers { firstArg() }

        // When
        val result = useCase(group, "device-2", " TikTok ")

        // Then
        assertTrue(result.isSuccess)
        assertEquals(listOf("TikTok"), result.getOrThrow().members.first().likes)
        coVerify { repository.update(any()) }
    }

    @Test
    fun `invoke should keep existing likes and append the new one`() = runTest {
        // Given
        val target = UserModel(id = "device-2", name = "Isabella", likes = listOf("Livros"))
        val group = GroupModel(id = "group-1", members = listOf(target))

        coEvery { repository.update(any()) } answers { firstArg() }

        // When
        val result = useCase(group, "device-2", "Jogos")

        // Then
        assertTrue(result.isSuccess)
        assertEquals(listOf("Livros", "Jogos"), result.getOrThrow().members.first().likes)
    }

    @Test
    fun `invoke should not duplicate an existing like case-insensitively`() = runTest {
        // Given
        val target = UserModel(id = "device-2", name = "Isabella", likes = listOf("Livros"))
        val group = GroupModel(id = "group-1", members = listOf(target))

        coEvery { repository.update(any()) } answers { firstArg() }

        // When
        val result = useCase(group, "device-2", "livros")

        // Then
        assertTrue(result.isSuccess)
        assertEquals(listOf("Livros"), result.getOrThrow().members.first().likes)
    }

    @Test
    fun `invoke should fail when the like is blank`() = runTest {
        // Given
        val target = UserModel(id = "device-2", name = "Isabella")
        val group = GroupModel(id = "group-1", members = listOf(target))

        // When
        val result = useCase(group, "device-2", "   ")

        // Then
        assertTrue(result.isFailure)
        coVerify(inverse = true) { repository.update(any()) }
    }
}
