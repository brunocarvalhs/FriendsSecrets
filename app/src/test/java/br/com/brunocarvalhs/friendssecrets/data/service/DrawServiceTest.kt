package br.com.brunocarvalhs.friendssecrets.data.service

import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.data.exceptions.MinimumsMembersOfDrawException
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class DrawServiceTest {

    private val drawService = spyk(DrawService(), recordPrivateCalls = true)

    @Test
    fun `test drawMembers method`() {
        val group = mockk<GroupEntities>() {
            every { members } returns mapOf(
                "Alice" to "123",
                "Bob" to "456",
                "Charlie" to "789"
            )
        }

        every { drawService.drawMembers(any()) } answers {
            mapOf(
                "Alice" to "Bob",
                "Bob" to "Charlie",
                "Charlie" to "Alice"
            )
        }

        val secretSantaMap = drawService.drawMembers(group)

        assertEquals(3, secretSantaMap.size)
        assertEquals("Bob", secretSantaMap["Alice"])
    }

    @Test
    fun `test draw members with less than three participants`() {
        mockkObject(CustomApplication.Companion)

        every {
            CustomApplication.getInstance().getString(any())
        } returns "Minimums members of draw"

        val participants = mapOf(
            "user1" to "User One",
            "user2" to "User Two"
        )
        val group = mockk<GroupEntities>()
        every { group.members } returns participants

        assertThrows(MinimumsMembersOfDrawException::class.java) {
            drawService.drawMembers(group)
        }
    }

    @Test
    fun `test draw members with exactly three participants`() {
        val participants = mapOf(
            "user1" to "User One",
            "user2" to "User Two",
            "user3" to "User Three"
        )
        val group = mockk<GroupEntities>()
        every { group.members } returns participants

        val result = drawService.drawMembers(group)

        // Assert that the size of the resulting map is equal to the size of the participants
        assertEquals(participants.size, result.size)

        // Assert that no participant draws themselves
        participants.keys.forEach { participant ->
            assert(result[participant] != participant)
        }
    }

    @Test
    fun `test draw members ensures no one draws themselves`() {
        val participants = mapOf(
            "user1" to "User One",
            "user2" to "User Two",
            "user3" to "User Three",
            "user4" to "User Four"
        )
        val group = mockk<GroupEntities>()
        every { group.members } returns participants

        // Try to draw multiple times to ensure randomness
        val results = mutableSetOf<Map<String, String>>()

        repeat(10) {
            results.add(drawService.drawMembers(group))
        }

        // Assert that all results are unique and do not contain self-draws
        results.forEach { result ->
            participants.keys.forEach { participant ->
                assert(result[participant] != participant)
            }
        }
    }
}