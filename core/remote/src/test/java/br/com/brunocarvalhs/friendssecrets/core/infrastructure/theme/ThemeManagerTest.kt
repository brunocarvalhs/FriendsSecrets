package br.com.brunocarvalhs.friendssecrets.core.infrastructure.theme

import br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain.ThemeService
import br.com.brunocarvalhs.storage.domain.StorageService
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ThemeManagerTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var storage: StorageService
    private lateinit var manager: ThemeManager

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        storage = mockk(relaxed = true)

        // defaults seguros
        coEvery { storage.load("theme_key", String::class) } returns "SYSTEM"
        coEvery { storage.load("dynamic_theme_key", Boolean::class) } returns false
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createManager(): ThemeManager {
        return ThemeManager(
            context = mockk(relaxed = true),
            storage = storage
        )
    }

    @Test
    fun `should initialize theme from storage`() = runTest {
        coEvery { storage.load("theme_key", String::class) } returns ThemeService.Theme.DARK.type
        coEvery { storage.load("dynamic_theme_key", Boolean::class) } returns true

        manager = createManager()
        manager.initialize()

        advanceUntilIdle()

        assertEquals(ThemeService.Theme.DARK, manager.theme.value)
        assertTrue(manager.isDynamicThemeEnabled.value)
    }

    @Test
    fun `should update theme and persist`() = runTest {
        coEvery { storage.save(any(), any()) } just Runs

        manager = createManager()

        manager.setTheme(ThemeService.Theme.LIGHT)
        advanceUntilIdle()

        assertEquals(ThemeService.Theme.LIGHT, manager.theme.value)

        coVerify(exactly = 1) {
            storage.save("theme_key", ThemeService.Theme.LIGHT.type)
        }
    }

    @Test
    fun `should enable dynamic theme`() = runTest {
        coEvery { storage.save(any(), any()) } just Runs

        manager = createManager()

        manager.setDynamicThemeEnabled(true)
        advanceUntilIdle()

        assertTrue(manager.isDynamicThemeEnabled.value)

        coVerify(exactly = 1) {
            storage.save("dynamic_theme_key", true)
        }
    }

    @Test
    fun `should fallback to SYSTEM when invalid theme`() = runTest {
        coEvery { storage.load("theme_key", String::class) } returns "INVALID"
        coEvery { storage.load("dynamic_theme_key", Boolean::class) } returns false

        manager = createManager()

        advanceUntilIdle()

        assertEquals(ThemeService.Theme.SYSTEM, manager.theme.value)
    }

    @Test
    fun `should emit dark theme when explicitly set`() = runTest {
        coEvery { storage.load("theme_key", String::class) } returns ThemeService.Theme.DARK.type

        manager = createManager()

        manager.initialize()

        advanceUntilIdle()

        assertEquals(ThemeService.Theme.DARK, manager.theme.value)
    }
}