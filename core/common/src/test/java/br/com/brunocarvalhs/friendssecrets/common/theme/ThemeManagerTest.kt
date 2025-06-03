package br.com.brunocarvalhs.friendssecrets.common.theme

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ThemeManagerTest {

    private lateinit var context: Context
    private lateinit var resources: Resources
    private lateinit var configuration: Configuration
    private lateinit var storage: StorageManager
    private lateinit var themeManager: ThemeManager

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        context = mockk()
        resources = mockk()
        configuration = Configuration()

        every { context.resources } returns resources
        every { resources.configuration } returns configuration

        storage = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should return system theme when no theme is saved`() {
        every { storage.load<String>("theme_key") } returns null
        every { storage.load<Boolean>("dynamic_theme_key") } returns null

        themeManager = ThemeManager(context, storage)

        assertEquals(ThemeManager.Theme.SYSTEM, themeManager.theme)
    }

    @Test
    fun `should save selected theme`() {
        every { storage.load<String>("theme_key") } returns null
        every { storage.load<Boolean>("dynamic_theme_key") } returns null
        every { storage.save<String>(any(), any()) } just Runs

        themeManager = ThemeManager(context, storage)
        themeManager.theme = ThemeManager.Theme.DARK

        assertEquals(ThemeManager.Theme.DARK, themeManager.theme)
    }

    @Test
    fun `should use system theme logic when theme is set to SYSTEM`() {
        every { storage.load<String>("theme_key") } returns ThemeManager.Theme.SYSTEM.value
        every { storage.load<Boolean>("dynamic_theme_key") } returns false

        configuration.uiMode = Configuration.UI_MODE_NIGHT_YES
        themeManager = ThemeManager(context, storage)

        assertTrue(themeManager.isDarkTheme())

        configuration.uiMode = Configuration.UI_MODE_NIGHT_NO
        assertFalse(themeManager.isDarkTheme())
    }

    @Test
    fun `should correctly report dynamic theme enabled`() {
        every { storage.load<String>("theme_key") } returns null
        every { storage.load<Boolean>("dynamic_theme_key") } returns true

        themeManager = ThemeManager(context, storage)

        assertTrue(themeManager.isDynamicThemeEnabled())
    }

    @Test
    fun `should update dynamic theme flag`() {
        every { storage.load<String>("theme_key") } returns null
        every { storage.load<Boolean>("dynamic_theme_key") } returns false
        every { storage.save<Boolean>(any(), any()) } just Runs

        themeManager = ThemeManager(context, storage)
        themeManager.setDynamicThemeEnabled(true)

        assertTrue(themeManager.isDynamicThemeEnabled())
        verify { storage.save("dynamic_theme_key", true) }
    }
}
