package br.com.brunocarvalhs.core.featureflags.data

import br.com.brunocarvalhs.core.featureflags.domain.AppVersionProvider
import br.com.brunocarvalhs.core.remote.domain.ConfigurationService
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VersionedFeatureFlagsManagerTest {

    private val configurationService: ConfigurationService = mockk()
    private val appVersionProvider: AppVersionProvider = mockk()
    private lateinit var manager: VersionedFeatureFlagsManager

    @Before
    fun setup() {
        manager = VersionedFeatureFlagsManager(configurationService, appVersionProvider)
    }

    @Test
    fun `isEnabled should return the default value when there are no overrides`() {
        every { configurationService.getString("feature_flags_overrides", "[]") } returns "[]"
        every { appVersionProvider.getVersionName() } returns "3.7.0"

        val result = manager.isEnabled("feature_ai_gift_chat", defaultValue = true)

        assertTrue(result)
    }

    @Test
    fun `isEnabled should apply an override that matches key and current version`() {
        every { configurationService.getString("feature_flags_overrides", "[]") } returns """
            [{"key":"feature_ai_gift_chat","enabled":false,"version":"3.7.0"}]
        """.trimIndent()
        every { appVersionProvider.getVersionName() } returns "3.7.0"

        val result = manager.isEnabled("feature_ai_gift_chat", defaultValue = true)

        assertFalse(result)
    }

    @Test
    fun `isEnabled should ignore an override for a different version`() {
        every { configurationService.getString("feature_flags_overrides", "[]") } returns """
            [{"key":"feature_ai_gift_chat","enabled":false,"version":"3.6.0"}]
        """.trimIndent()
        every { appVersionProvider.getVersionName() } returns "3.7.0"

        val result = manager.isEnabled("feature_ai_gift_chat", defaultValue = true)

        assertTrue(result)
    }

    @Test
    fun `isEnabled should ignore an override for a different key`() {
        every { configurationService.getString("feature_flags_overrides", "[]") } returns """
            [{"key":"other_feature","enabled":false,"version":"3.7.0"}]
        """.trimIndent()
        every { appVersionProvider.getVersionName() } returns "3.7.0"

        val result = manager.isEnabled("feature_ai_gift_chat", defaultValue = true)

        assertTrue(result)
    }

    @Test
    fun `isEnabled should fall back to the default value on malformed JSON`() {
        every { configurationService.getString("feature_flags_overrides", "[]") } returns "{not valid json"
        every { appVersionProvider.getVersionName() } returns "3.7.0"

        val result = manager.isEnabled("feature_ai_gift_chat", defaultValue = false)

        assertFalse(result)
    }

    @Test
    fun `isEnabled should pick the matching override among several entries`() {
        every { configurationService.getString("feature_flags_overrides", "[]") } returns """
            [
                {"key":"feature_a","enabled":false,"version":"3.7.0"},
                {"key":"feature_ai_gift_chat","enabled":false,"version":"3.7.0"},
                {"key":"feature_ai_gift_chat","enabled":true,"version":"3.6.0"}
            ]
        """.trimIndent()
        every { appVersionProvider.getVersionName() } returns "3.7.0"

        val result = manager.isEnabled("feature_ai_gift_chat", defaultValue = true)

        assertFalse(result)
    }
}
