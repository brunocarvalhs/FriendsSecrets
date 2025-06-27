package br.com.brunocarvalhs.friendssecrets.common.remote

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class RemoteProviderTest {

    private lateinit var remoteEvent: RemoteProvider.RemoteEvent
    private lateinit var provider: RemoteProvider

    @Before
    fun setUp() {
        remoteEvent = mock(RemoteProvider.RemoteEvent::class.java)
        provider = RemoteProvider(remoteEvent)
    }

    @Test(expected = IllegalStateException::class)
    fun `getInstance throws if not initialized`() {
        // Clear instance for test
        val field = RemoteProvider::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        RemoteProvider.getInstance()
    }

    @Test
    fun `initialize creates instance only once`() {
        RemoteProvider.initialize(remoteEvent) // second call, should log warning but no crash
        val instance1 = RemoteProvider.getInstance()
        val instance2 = RemoteProvider.getInstance()
        assertEquals(instance1, instance2)
    }

    @Test
    fun `should return long value from remote event`() {
        `when`(remoteEvent.getValue("test_long", 0L)).thenReturn(123L)

        val result = provider.getLong("test_long")

        assertEquals(123L, result)
    }

    @Test
    fun `should return boolean value from remote event`() {
        `when`(remoteEvent.getValue("test_bool", false)).thenReturn(true)

        val result = provider.getBoolean("test_bool")

        assertTrue(result)
    }

    @Test
    fun `should return string value from remote event`() {
        `when`(remoteEvent.getValue("test_string", "")).thenReturn("value")

        val result = provider.getString("test_string")

        assertEquals("value", result)
    }

    @Test
    fun `should return double value from remote event`() {
        `when`(remoteEvent.getValue("test_double", 0.0)).thenReturn(3.14)

        val result = provider.getDouble("test_double")

        assertEquals(3.14, result, 0.0)
    }

    @Test
    fun `should fetch and activate`() {
        provider.fetchAndActivate()

        verify(remoteEvent).fetchAndActivate()
    }

    @Test
    fun `should set default values`() {
        val defaults = mapOf("key" to "value")

        provider.default(defaults)

        verify(remoteEvent).defaultValues(defaults)
    }

    @Test
    fun `should return async string value from remote event`() = runBlocking {
        `when`(remoteEvent.getValueAsync("test_async", "")).thenReturn("async_value")

        val result = provider.getAsyncString("test_async")

        assertEquals("async_value", result)
    }
}
