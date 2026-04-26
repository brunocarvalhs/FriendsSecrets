package br.com.brunocarvalhs.group.draw.app.data.repository

import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkService
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.draw.app.data.model.GroupDrawDTO
import br.com.brunocarvalhs.group.draw.app.data.services.DrawManager
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DrawRepositoryImplTest {

    private val network: NetworkService = mockk()
    private val drawManager: DrawManager = mockk()
    private lateinit var repository: DrawRepositoryImpl

    @Before
    fun setup() {
        repository = DrawRepositoryImpl(network, drawManager)
    }

    @Test
    fun `drawMembers should shuffle members and call network PUT`() = runTest {
        // Given
        val members = listOf(
            UserModel(name = "Bruno"),
            UserModel(name = "Alice"),
            UserModel(name = "Bob")
        )

        val group = GroupModel(id = "1", members = members)

        val expectedDraw = mapOf(
            "Bruno" to "Alice",
            "Alice" to "Bob",
            "Bob" to "Bruno"
        )

        val memberNames = members.map { it.name }.toMutableList()

        every { drawManager.draw(memberNames) } returns expectedDraw

        coEvery {
            network.make(
                request = match {
                    it.endpoint == "groups/1" &&
                            it.method == NetworkService.Method.PUT &&
                            it.payload is Map<*, *>
                },
                response = GroupDrawDTO::class
            )
        } returns mockk()

        // When
        val result = repository.drawMembers(group)

        // Then
        assertEquals(expectedDraw, result)
    }
}
