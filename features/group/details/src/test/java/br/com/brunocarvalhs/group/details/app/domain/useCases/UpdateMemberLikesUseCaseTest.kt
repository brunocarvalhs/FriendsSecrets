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

class UpdateMemberLikesUseCaseTest {

    private val repository: GroupDetailsRepository = mockk()
    private val deviceService: DeviceService = mockk()
    private lateinit var useCase: UpdateMemberLikesUseCase

    @Before
    fun setup() {
        useCase = UpdateMemberLikesUseCase(repository, deviceService)
    }

    @Test
    fun `invoke should update likes of the current device member`() = runTest {
        // Given
        val self = UserModel(id = "device-1", name = "Bruno", likes = emptyList())
        val other = UserModel(id = "device-2", name = "Isabella", likes = listOf("Music"))
        val group = GroupModel(id = "group-1", members = listOf(self, other))
        val newLikes = listOf("Books", " Games ", "")

        coEvery { deviceService.getDeviceId() } returns "device-1"
        coEvery { repository.update(any()) } answers { firstArg() }

        // When
        val result = useCase(group, newLikes)

        // Then
        assertTrue(result.isSuccess)
        val updatedGroup = result.getOrThrow()
        assertEquals(listOf("Books", "Games"), updatedGroup.members.first { it.id == "device-1" }.likes)
        assertEquals(listOf("Music"), updatedGroup.members.first { it.id == "device-2" }.likes)
        coVerify { repository.update(any()) }
    }

    @Test
    fun `invoke should match member by phoneNumber when id differs from device id`() = runTest {
        // Given
        val self = UserModel(id = "user-uuid", phoneNumber = "device-1", name = "Bruno")
        val group = GroupModel(id = "group-1", members = listOf(self))

        coEvery { deviceService.getDeviceId() } returns "device-1"
        coEvery { repository.update(any()) } answers { firstArg() }

        // When
        val result = useCase(group, listOf("Coffee"))

        // Then
        assertTrue(result.isSuccess)
        assertEquals(listOf("Coffee"), result.getOrThrow().members.first().likes)
    }

    @Test
    fun `invoke should fail when current device is not a member of the group`() = runTest {
        // Given
        val other = UserModel(id = "device-2", name = "Isabella")
        val group = GroupModel(id = "group-1", members = listOf(other))

        coEvery { deviceService.getDeviceId() } returns "device-1"

        // When
        val result = useCase(group, listOf("Books"))

        // Then
        assertTrue(result.isFailure)
        coVerify(inverse = true) { repository.update(any()) }
    }
}
