package br.com.brunocarvalhs.friendssecrets.core.infrastructure.theme.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ThemeRemoteProviderTest {

    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private lateinit var provider: ThemeRemoteProvider

    @Before
    fun setup() {
        firebaseRemoteConfig = mockk(relaxed = true)
        provider = ThemeRemoteProvider(firebaseRemoteConfig)
    }

    @Test
    fun `should return null when dark theme is blank`() {
        every { firebaseRemoteConfig.getString(ThemeRemoteProvider.THEME_DARK) } returns ""

        val result = provider.getDarkColorScheme()

        assertNull(result)
    }

    @Test
    fun `should parse dark theme successfully`() {
        val json = """
            {
                "primary":"#FFFFFF",
                "onPrimary":"#000000",
                "primaryContainer":"#111111",
                "onPrimaryContainer":"#222222",
                "secondary":"#333333",
                "onSecondary":"#444444",
                "secondaryContainer":"#555555",
                "onSecondaryContainer":"#666666",
                "tertiary":"#777777",
                "onTertiary":"#888888",
                "tertiaryContainer":"#999999",
                "onTertiaryContainer":"#AAAAAA",
                "error":"#BBBBBB",
                "onError":"#CCCCCC",
                "errorContainer":"#DDDDDD",
                "onErrorContainer":"#EEEEEE",
                "background":"#FFFFFF",
                "onBackground":"#000000",
                "surface":"#FFFFFF",
                "onSurface":"#000000",
                "surfaceVariant":"#123456",
                "onSurfaceVariant":"#654321",
                "outline":"#111111",
                "outlineVariant":"#222222",
                "scrim":"#333333",
                "inverseSurface":"#444444",
                "inverseOnSurface":"#555555",
                "inversePrimary":"#666666",
                "surfaceDim":"#777777",
                "surfaceBright":"#888888",
                "surfaceContainerLowest":"#999999",
                "surfaceContainerLow":"#AAAAAA",
                "surfaceContainer":"#BBBBBB",
                "surfaceContainerHigh":"#CCCCCC",
                "surfaceContainerHighest":"#DDDDDD"
            }
        """.trimIndent()

        every { firebaseRemoteConfig.getString(ThemeRemoteProvider.THEME_DARK) } returns json

        val result = provider.getDarkColorScheme()

        assertNotNull(result)
    }
}
