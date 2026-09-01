package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.deviceid.DeviceService
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddMemberAdjectiveUseCaseTest {

    private val repository: GroupDetailsRepository = mockk()
    private val deviceService: DeviceService = mockk()
    private lateinit var useCase: AddMemberAdjectiveUseCase

    @Before
    fun setup() {
        useCase = AddMemberAdjectiveUseCase(repository, deviceService)
    }

    @Test
    fun `invoke should add an adjective from the current device to the target member`() = runTest {
        // Given
        val target = UserModel(id = "device-2", name = "Isabella")
        val group = GroupModel(id = "group-1", members = listOf(target))

        coEvery { deviceService.getDeviceId() } returns "device-1"
        coEvery { repository.update(any()) } answers { firstArg() }

        // When
        val result = useCase(group, "device-2", " Engraçada ")

        // Then
        assertTrue(result.isSuccess)
        val updatedMember = result.getOrThrow().members.first()
        assertEquals(listOf("Engraçada"), updatedMember.adjectives["device-1"])
        coVerify { repository.update(any()) }
    }

    @Test
    fun `invoke should keep contributions from other devices and append to the same contributor`() =
        runTest {
            // Given
            val target = UserModel(
                id = "device-2",
                name = "Isabella",
                adjectives = mapOf("device-1" to listOf("Gentil"), "device-3" to listOf("Divertida"))
            )
            val group = GroupModel(id = "group-1", members = listOf(target))

            coEvery { deviceService.getDeviceId() } returns "device-1"
            coEvery { repository.update(any()) } answers { firstArg() }

            // When
            val result = useCase(group, "device-2", "Criativa")

            // Then
            assertTrue(result.isSuccess)
            val updatedMember = result.getOrThrow().members.first()
            assertEquals(listOf("Gentil", "Criativa"), updatedMember.adjectives["device-1"])
            assertEquals(listOf("Divertida"), updatedMember.adjectives["device-3"])
        }

    @Test
    fun `invoke should not duplicate the same adjective from the same contributor`() = runTest {
        // Given
        val target = UserModel(
            id = "device-2",
            name = "Isabella",
            adjectives = mapOf("device-1" to listOf("Gentil"))
        )
        val group = GroupModel(id = "group-1", members = listOf(target))

        coEvery { deviceService.getDeviceId() } returns "device-1"
        coEvery { repository.update(any()) } answers { firstArg() }

        // When
        val result = useCase(group, "device-2", "gentil")

        // Then
        assertTrue(result.isSuccess)
        assertEquals(listOf("Gentil"), result.getOrThrow().members.first().adjectives["device-1"])
    }

    @Test
    fun `invoke should fail when the adjective is blank`() = runTest {
        // Given
        val target = UserModel(id = "device-2", name = "Isabella")
        val group = GroupModel(id = "group-1", members = listOf(target))

        coEvery { deviceService.getDeviceId() } returns "device-1"

        // When
        val result = useCase(group, "device-2", "   ")

        // Then
        assertTrue(result.isFailure)
        coVerify(inverse = true) { repository.update(any()) }
    }
}
