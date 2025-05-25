package br.com.brunocarvalhs.friendssecrets.common.remote.toggle

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.common.remote.RemoteProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyMap
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class ToggleManagerTest {

    private lateinit var context: Context
    private lateinit var remoteProvider: RemoteProvider
    private lateinit var toggleManager: ToggleManager

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        remoteProvider = mock(RemoteProvider::class.java)

        doNothing().`when`(remoteProvider).default(anyMap())
        doNothing().`when`(remoteProvider).fetchAndActivate()

        toggleManager = ToggleManager(context, remoteProvider)
    }

    @Test
    fun `should initialize with default values and fetchAndActivate`() {
        verify(remoteProvider).default(anyMap())
        verify(remoteProvider).fetchAndActivate()
    }

    @Test
    fun `should return feature enabled state from remote provider`() {
        `when`(remoteProvider.getBoolean(ToggleKeys.SETTINGS_IS_ENABLED.name))
            .thenReturn(true)
        `when`(remoteProvider.getBoolean(ToggleKeys.SETTINGS_IS_APPEARANCE_ENABLED.name))
            .thenReturn(false)

        assertTrue(toggleManager.isFeatureEnabled(ToggleKeys.SETTINGS_IS_ENABLED))
        assertFalse(toggleManager.isFeatureEnabled(ToggleKeys.SETTINGS_IS_APPEARANCE_ENABLED))
    }
}
