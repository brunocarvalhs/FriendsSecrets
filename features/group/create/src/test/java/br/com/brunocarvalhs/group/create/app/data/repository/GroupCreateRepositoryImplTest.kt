package br.com.brunocarvalhs.group.create.app.data.repository

import br.com.brunocarvalhs.core.network.domain.NetworkService
import br.com.brunocarvalhs.core.domain.model.GroupModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupCreateRepositoryImplTest {

    private val network: NetworkService = mockk()
    private lateinit var repository: GroupCreateRepositoryImpl

    @Before
    fun setup() {
        repository = GroupCreateRepositoryImpl(network)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `create should return success when network call succeeds`() = runTest {
        // Given
        val group = GroupModel(id = "1", name = "Test")

        coEvery {
            network.make(
                request = match {
                    it.endpoint == "groups" &&
                            it.method == NetworkService.Method.POST
                },
                response = String::class
            )
        } returns "generated-id"

        // When
        val result = repository.create(group)
        advanceUntilIdle()

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `create should return failure when network returns null`() = runTest {
        // Given
        val group = GroupModel(id = "1", name = "Test")

        coEvery {
            network.make(
                request = match {
                    it.endpoint == "groups" &&
                            it.method == NetworkService.Method.POST
                },
                response = String::class
            )
        } returns null

        // When
        val result = repository.create(group)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `update should return success when network call succeeds`() = runTest {
        // Given
        val group = GroupModel(id = "1", name = "Test")

        coEvery {
            network.make(
                request = match {
                    it.endpoint == "groups/1" &&
                            it.method == NetworkService.Method.PUT
                },
                response = Boolean::class
            )
        } returns true

        // When
        val result = repository.update(group)

        // Then
        assertTrue(result.isSuccess)
    }
}
