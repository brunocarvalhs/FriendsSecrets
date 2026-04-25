package br.com.brunocarvalhs.friendssecrets.core.infrastructure.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.ConcurrentHashMap

class FeatureFlagsManagerTest {

    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var manager: FeatureFlagsManager

    @Before
    fun setup() {
        remoteConfig = mockk(relaxed = true)

        every { remoteConfig.getBoolean(any()) } returns false
        every { remoteConfig.all } returns ConcurrentHashMap()

        manager = FeatureFlagsManager(remoteConfig)
    }

    @Test
    fun `should return true when feature flag is enabled`() {
        every { remoteConfig.getBoolean("feature_x") } returns true

        val result = manager.validate("feature_x")

        assertTrue(result)
    }

    @Test
    fun `should return false when feature flag is disabled`() {
        every { remoteConfig.getBoolean("feature_x") } returns false

        val result = manager.validate("feature_x")

        assertFalse(result)
    }

    @Test
    fun `should return value from remote config when key exists`() {
        val mockValue = mockk<FirebaseRemoteConfigValue>()
        every { remoteConfig.all } returns mapOf("feature_x" to mockValue)
        every { remoteConfig.getBoolean("feature_x") } returns true

        val result = manager.validate("feature_x", defaultValue = false)

        assertTrue(result)
    }

    @Test
    fun `should return default value when key does not exist`() {
        every { remoteConfig.all } returns emptyMap()
        every { remoteConfig.getBoolean("feature_x") } returns true

        val result = manager.validate("feature_x", defaultValue = false)

        assertFalse(result)
    }

    @Test
    fun `should return default value even if remote config returns false when key missing`() {
        every { remoteConfig.all } returns emptyMap()
        every { remoteConfig.getBoolean("feature_x") } returns false

        val result = manager.validate("feature_x", defaultValue = true)

        assertTrue(result)
    }

    @Test
    fun `should call fetch on init`() {
        val task = mockk<com.google.android.gms.tasks.Task<Void>>(relaxed = true)

        every { remoteConfig.fetch() } returns task
        every { task.addOnCompleteListener(any()) } returns task

        FeatureFlagsManager(remoteConfig)

        verify { remoteConfig.fetch() }
    }
}