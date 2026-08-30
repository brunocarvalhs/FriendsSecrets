package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.storage.domain.StorageService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IsGroupReminderEnabledUseCaseTest {

    private val storage: StorageService = mockk()
    private lateinit var useCase: IsGroupReminderEnabledUseCase

    @Before
    fun setup() {
        useCase = IsGroupReminderEnabledUseCase(storage)
    }

    @Test
    fun `invoke should return true when stored value is true`() = runTest {
        // Given
        coEvery { storage.load("group_reminder_enabled_group-1", Boolean::class) } returns true

        // When
        val result = useCase("group-1")

        // Then
        assertTrue(result)
    }

    @Test
    fun `invoke should return false when nothing is stored`() = runTest {
        // Given
        coEvery { storage.load("group_reminder_enabled_group-1", Boolean::class) } returns null

        // When
        val result = useCase("group-1")

        // Then
        assertFalse(result)
    }
}
