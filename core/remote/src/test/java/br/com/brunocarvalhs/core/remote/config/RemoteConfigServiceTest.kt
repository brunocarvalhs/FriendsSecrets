package br.com.brunocarvalhs.core.remote.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RemoteConfigServiceTest {

    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var service: RemoteConfigService

    @Before
    fun setup() {
        remoteConfig = mockk(relaxed = true)
        service = RemoteConfigService(remoteConfig)
    }

    @Test
    fun `should return value from remote config when not blank`() {
        every { remoteConfig.getString("key") } returns "value"

        val result = service.getString("key", "default")

        assertEquals("value", result)
    }

    @Test
    fun `should return default when value is blank`() {
        every { remoteConfig.getString("key") } returns ""

        val result = service.getString("key", "default")

        assertEquals("default", result)
    }

    @Test
    fun `should return default when value is whitespace`() {
        every { remoteConfig.getString("key") } returns "   "

        val result = service.getString("key", "default")

        assertEquals("default", result)
    }

    @Test
    fun `should return value when contains spaces but not blank`() {
        every { remoteConfig.getString("key") } returns "hello world"

        val result = service.getString("key", "default")

        assertEquals("hello world", result)
    }

    @Test
    fun `should return default when key does not exist`() {
        every { remoteConfig.getString("key") } returns ""

        val result = service.getString("key", "default")

        assertEquals("default", result)
    }
}
