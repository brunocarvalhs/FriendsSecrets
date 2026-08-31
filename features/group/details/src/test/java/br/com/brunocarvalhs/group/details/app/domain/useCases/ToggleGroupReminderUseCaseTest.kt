package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.domain.services.GroupReminderService
import br.com.brunocarvalhs.storage.domain.StorageService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ToggleGroupReminderUseCaseTest {

    private val reminderService: GroupReminderService = mockk()
    private val storage: StorageService = mockk()
    private lateinit var useCase: ToggleGroupReminderUseCase

    @Before
    fun setup() {
        useCase = ToggleGroupReminderUseCase(reminderService, storage)
    }

    @Test
    fun `invoke should schedule and persist enabled state when enabling and date is valid`() = runTest {
        // Given
        val group = GroupModel(id = "group-1", date = "25/12/2099")
        every { reminderService.schedule(group) } returns true
        coEvery { storage.save("group_reminder_enabled_group-1", true) } returns Unit

        // When
        val result = useCase(group, true)

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow())
        coVerify { storage.save("group_reminder_enabled_group-1", true) }
    }

    @Test
    fun `invoke should persist disabled state when scheduling fails`() = runTest {
        // Given
        val group = GroupModel(id = "group-1", date = null)
        every { reminderService.schedule(group) } returns false
        coEvery { storage.save("group_reminder_enabled_group-1", false) } returns Unit

        // When
        val result = useCase(group, true)

        // Then
        assertTrue(result.isSuccess)
        assertTrue(!result.getOrThrow())
        coVerify { storage.save("group_reminder_enabled_group-1", false) }
    }

    @Test
    fun `invoke should cancel and persist disabled state when disabling`() = runTest {
        // Given
        val group = GroupModel(id = "group-1", date = "25/12/2099")
        every { reminderService.cancel(group) } returns Unit
        coEvery { storage.save("group_reminder_enabled_group-1", false) } returns Unit

        // When
        val result = useCase(group, false)

        // Then
        assertTrue(result.isSuccess)
        assertTrue(!result.getOrThrow())
        verify { reminderService.cancel(group) }
        coVerify { storage.save("group_reminder_enabled_group-1", false) }
    }
}
