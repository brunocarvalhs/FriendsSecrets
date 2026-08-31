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

class RemoveMemberUseCaseTest {

    private val repository: GroupDetailsRepository = mockk()
    private lateinit var useCase: RemoveMemberUseCase

    @Before
    fun setup() {
        useCase = RemoveMemberUseCase(repository)
    }

    @Test
    fun `invoke should remove member and persist updated group`() = runTest {
        // Given
        val member1 = UserModel(id = "user-1", name = "Bruno")
        val member2 = UserModel(id = "user-2", name = "Isabella")
        val group = GroupModel(id = "group-1", members = listOf(member1, member2))
        coEvery { repository.update(any()) } answers { firstArg() }

        // When
        val result = useCase(group, "user-1")

        // Then
        assertTrue(result.isSuccess)
        val updatedGroup = result.getOrThrow()
        assertEquals(listOf(member2), updatedGroup.members)
        coVerify { repository.update(match { it.members == listOf(member2) }) }
    }

    @Test
    fun `invoke should fail when member is not part of the group`() = runTest {
        // Given
        val member1 = UserModel(id = "user-1", name = "Bruno")
        val group = GroupModel(id = "group-1", members = listOf(member1))

        // When
        val result = useCase(group, "user-unknown")

        // Then
        assertTrue(result.isFailure)
        coVerify(inverse = true) { repository.update(any()) }
    }

    @Test
    fun `invoke should fail when the draw has already been made`() = runTest {
        // Given
        val member1 = UserModel(id = "user-1", name = "Bruno")
        val member2 = UserModel(id = "user-2", name = "Isabella")
        val group = GroupModel(
            id = "group-1",
            members = listOf(member1, member2),
            draws = mapOf("user-1" to "user-2")
        )

        // When
        val result = useCase(group, "user-1")

        // Then
        assertTrue(result.isFailure)
        coVerify(inverse = true) { repository.update(any()) }
    }
}
