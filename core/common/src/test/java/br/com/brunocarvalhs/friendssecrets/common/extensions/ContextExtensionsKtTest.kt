package br.com.brunocarvalhs.friendssecrets.common.extensions

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings.Secure
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ContextExtensionsTest {

    private lateinit var context: Context
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var packageManager: PackageManager

    @Before
    fun setup() {
        mockkStatic(Uri::class)
        every { Uri.parse(any<String>()) } returns mockk(relaxed = true)

        context = mockk(relaxed = true)
        sharedPrefs = mockk()
        editor = mockk()
        packageManager = mockk()

        every { context.packageName } returns "com.example.app"
        every { context.getSharedPreferences("com.example.app", Context.MODE_PRIVATE) } returns sharedPrefs
        every { sharedPrefs.edit() } returns editor
        every { editor.putBoolean(any(), any()) } returns editor
        every { editor.apply() } just Runs
        every { context.packageManager } returns packageManager

        mockkStatic(Secure::class) // Para mockar método estático getString

        val contentResolver = mockk<android.content.ContentResolver>()
        every { context.contentResolver } returns contentResolver
    }

    @Test
    fun `isFistAppOpen returns true and sets FIRST_APP_OPEN to false when first open`() {
        every { sharedPrefs.getBoolean("FIRST_APP_OPEN", true) } returns true

        val result = context.isFistAppOpen()

        assertTrue(result)
        verify {
            editor.putBoolean("FIRST_APP_OPEN", false)
            editor.apply()
        }
    }

    @Test
    fun `isFistAppOpen returns false when not first open`() {
        every { sharedPrefs.getBoolean("FIRST_APP_OPEN", true) } returns false

        val result = context.isFistAppOpen()

        assertFalse(result)
        verify(exactly = 0) {
            editor.putBoolean(any(), any())
            editor.apply()
        }
    }
}
